#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/wait.h>
#include <sysexits.h>
#include "excutor.h"

/*
CC = gcc
CFLAGS = -ansi -Wall -g -O0 -Wwrite-strings -Wshadow -fstack-protector-all

all: d8sh

d8sh: d8sh.o lexer.o parser.tab.o executor.o
    $(CC) -o d8sh d8sh.o lexer.o parser.tab.o executor.o

d8sh.o: d8sh.c executor.h lexer.h
    $(CC) $(CFLAGS) -c d8sh.c
 
lexer.o: lexer.c parser.tab.h
    $(CC) $(CFLAGS) -c lexer.c

parser.tab.o: parser.tab.c command.h 
    $(CC) $(CFLAGS) -c parser.tab.c

executor.o: executor.c command.h executor.h
    $(CC) $(CFLAGS) -c executor.c

clean: 
    rm -f d8sh *.o 
*/


struct tree {
    enum { NONE = 0, AND, OR, SEMI, PIPE } conjunction;
    struct tree *left, *right;
    char **argv;
    char *input;
    char *output;
};

static const char *conjb[] __attribute__((unused)) = { "err", "&&", "||", ";", "|" };

static void print_tree(struct tree *t);

int execute(struct tree *t) {
    if (t->conjunction == NONE) {
        int status;
        
        if (strcmp(t->argv[0], "exit") == 0) {
            exit(1);
        }
        
        if (strcmp(t->argv[0], "cd") == 0) {
            if (t->argv[1] != NULL) {
                if (chdir(t->argv[1]) < 0) {
                    perror("chdir");
                }
            } else {
                if (chdir(getenv("HOME")) < 0) {
                    perror("chdir");
                }
            }
            return 0;
        } else {
            int fid_in, fid_out;
            pid_t child_id;
            
            if ((child_id = fork()) < 0) {
                perror("fork");
            }
            
            if (child_id == 0) {
                if (t->input != NULL) {
                    if ((fid_in = open(t->input, O_RDONLY)) < 0) {
                        perror("open");
                    }
                    
                    if (dup2(fid_in, STDIN_FILENO) < 0) {
                        perror("dup2");
                    }
                    close(fid_in);
                }
                
                if (t->output != NULL) {
                    if ((fid_out = open(t->output, O_WRONLY | O_CREAT, 0664)) < 0) {
                        perror("open");
                    }
                    
                    if (dup2(fid_out, STDOUT_FILENO) < 0) {
                        perror("dup2");
                    }
                    close(fid_out);
                }
                
                execvp(t->argv[0], t->argv);
                printf("Failed to execute %s\n", t->argv[0]);
                exit(EX_OSERR);
            } else {
                wait(&status);
            }
            return WEXITSTATUS(status);
        }
    } else if (t->conjunction == AND) {
        int fid_in, fid_out, ex_stat, status;
        pid_t child_id;
        
        if ((child_id = fork()) < 0) {
            perror("fork");
        }
        
        if (child_id == 0) {
            if (t->input != NULL) {
                if ((fid_in = open(t->input, O_RDONLY)) < 0) {
                    perror("open");
                }
                
                if (dup2(fid_in, STDIN_FILENO) < 0) {
                    perror("open");
                }
                close(fid_in);
            }
            
            if (t->output != NULL) {
                if ((fid_out = open(t->output, O_WRONLY | O_CREAT, 0664)) < 0) {
                    perror("open");
                }
                    
                if (dup2(fid_out, STDOUT_FILENO) < 0) {
                    perror("open");
                }
                close(fid_out);
            }
            
            if ((ex_stat = execute(t->left)) == 0) {
                ex_stat = execute(t->right);
            }
            exit(ex_stat);
        } else {
            wait(&status);
        }
        return WEXITSTATUS(status);
    } else if (t->conjunction == PIPE) {
        int fid_in, fid_out, pip_fd[2];
        pid_t child_id, child_id_one, child_id_two;
        
        if ((child_id = fork()) < 0) {
            perror("fork()");
        }
        
        if (child_id == 0) {
            if (t->input != NULL) {
                if ((fid_in = open(t->input, O_RDONLY)) < 0) {
                    perror("open");
                }
                
                if (dup2(fid_in, STDIN_FILENO) < 0) {
                    perror("dup2");
                }
                close(fid_in);
            }
            
            if (t->output != NULL) {
                if ((fid_out = open(t->output, O_WRONLY | O_CREAT, 0664)) < 0) {
                    perror("open");
                }
                
                if (dup2(fid_out, STDOUT_FILENO) < 0) {
                    perror("dup2");
                }
                close(fid_out);
            }
            
            if (pipe(pip_fd) < 0) {
                perror("pipe");
            }
            
            if ((child_id_one = fork()) < 0) {
                perror("fork");
            }
            
            if (child_id_one == 0) {
                close(pip_fd[0]);
                
                if (dup2(pip_fd[1], STDOUT_FILENO) < 0) {
                    perror("dup2");
                }
                
                close(pip_fd[1]);
                exit(execute(t->left));
            } else {
                if ((child_id_two = fork()) < 0) {
                    perror("fork");
                }
                
                if (child_id_two == 0) {
                    close(pip_fd[1]);
                    
                    if (dup2(pip_fd[0], STDIN_FILENO) < 0) {
                        perror("fork");
                    }
                    
                    close(pip_fd[0]);
                    exit(execute(t->right));
                } else {
                    close(pip_fd[0]);
                    close(pip_fd[1]);
                    wait(NULL);
                    wait(NULL);
                }
            }
            exit(0);
        } else {
            wait(NULL);
        }
        return 0;
    }
    return 0;
}

static void print_tree(struct tree *t) {
    if (t != NULL) {
        print_tree(t->left);
        
        if (t->conjunction == NONE) {
            printf("NONE: %s, ", t->argv[0]);
        } else {
            printf("%s, ", conjb[t->conjunction]);
        }
        printf("IR: %s, ", t->input);
        printf("OR: %s\n", t->output);
        
        print_tree(t->right);
    }
}