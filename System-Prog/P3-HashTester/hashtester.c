#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <sysexits.h>
#include "216_project_2.h"
#include "216_project_3.h"

#define MAX_LINE 1024
#define MAX_TOKEN 2

int main(int argc, char *argv[]) {
    Table table;
    FILE *file;
    char line[MAX_LINE];
    char tokens[MAX_TOKEN][MAX_STR_SIZE];
    unsigned long len, i_lin, i_tok, i_buc;
    
    init_table(&table);
    
    if (argc == 1) {
        file = stdin;
    } else if (argc == 2) {
        if (!(file = fopen(argv[1], "r"))) {
            fprintf(stderr, "Error: %s\n", strerror(errno));
            return EX_OSERR;
        }
    } else {
        fprintf(stderr, "Error: %s\n", strerror(errno = 9));
        return EX_USAGE;
    }

    while (fgets(line, MAX_LINE, file)) {
        /* parse over proceeding whitespaces */
        for (len = strlen(line) - 1, i_lin = 0;
             len > 0 && line[i_lin] == ' ';
             len--, i_lin++)
            ;
        
        /* if it's a command and the command is display */
        if (strncmp(line + i_lin, "display", 7) == 0) {
            for (; len > 0 && line[i_lin] != ' ';
                 len--, i_lin++)
                ;
            
            if (len == 0) {
                fprintf(stderr, "Invalid line.\n");
                return EX_DATAERR;
            }
            
            for (; len > 0 && line[i_lin] == ' ';
                 len--, i_lin++)
                ;
            
            if (len == 0) {
                fprintf(stderr, "Invalid line.\n");
                return EX_DATAERR;
            }
            
            if (strncmp(line + i_lin, "key_count", 9) == 0) {
                printf("Key count: %d\n", table.key_ct);
            } else if (strncmp(line + i_lin, "table", 5) == 0) {
                for (i_buc = 0; i_buc < NUM_BUCKETS; i_buc++) {
                    if  (table.buckets[i_buc].state == EMPTY) {
                        printf("Bucket %lu: %s\n", i_buc, "EMPTY");
                    } else if (table.buckets[i_buc].state == FULL) {
                        printf("Bucket %lu: %s (%s => %s)\n",
                               i_buc, "FULL",
                               table.buckets[i_buc].data.key,
                               table.buckets[i_buc].data.value);
                    } else {
                        printf("Bucket %lu: %s\n", i_buc, "DELETED");
                    }
                }
            } else {
                fprintf(stderr, "Invalid line.\n");
                return EX_DATAERR;
            }
            
        /* if it's a command and the command is insert */
        } else if (strncmp(line + i_lin, "insert", 6) == 0) {
            for (; len > 0 && line[i_lin] != ' ';
                 len--, i_lin++)
                ;
            
            if (len == 0) {
                fprintf(stderr, "Invalid line.\n");
                return EX_DATAERR;
            }
            
            for (; len > 0 && line[i_lin] == ' ';
                 len--, i_lin++)
                ;
            
            if (len == 0) {
                fprintf(stderr, "Invalid line.\n");
                return EX_DATAERR;
            }
            
            /* store first argument */
            for (i_tok = 0; len > 0 && line[i_lin] != ' ';
                 len--, i_lin++) {
                tokens[0][i_tok++] = line[i_lin];
            }
            tokens[0][i_tok] = '\0';
            
            if (len == 0) {
                fprintf(stderr, "Invalid line.\n");
                return EX_DATAERR;
            }
            
            for (; len > 0 && line[i_lin] == ' ';
                 len--, i_lin++)
                ;
            
            if (len == 0) {
                fprintf(stderr, "Invalid line.\n");
                return EX_DATAERR;
            }
            
            /* store second argument */
            for (i_tok = 0; len > 0 && line[i_lin] != ' ';
                 len--, i_lin++) {
                tokens[1][i_tok++] = line[i_lin];
            }
            tokens[1][i_tok] = '\0';
            
            if (insert(&table, tokens[0], tokens[1])) {
                printf("Insertion of %s => %s failed.\n", tokens[0], tokens[1]);
            } else {
                printf("Insertion of %s => %s succeeded.\n", tokens[0], tokens[1]);
            }
            
        /* if it's a command and the command is search */
        } else if (strncmp(line + i_lin, "search", 6) == 0) {
            for (; len > 0 && line[i_lin] != ' ';
                 len--, i_lin++)
                ;
            
            if (len == 0) {
                fprintf(stderr, "Invalid line.\n");
                return EX_DATAERR;
            }
            
            for (; len > 0 && line[i_lin] == ' ';
                 len--, i_lin++)
                ;
            
            if (len == 0) {
                fprintf(stderr, "Invalid line.\n");
                return EX_DATAERR;
            }
            
            /* store argument */
            for (i_tok = 0; len > 0 && line[i_lin] != ' ';
                 len--, i_lin++) {
                tokens[0][i_tok++] = line[i_lin];
            }
            tokens[0][i_tok] = '\0';
            
            if (search(&table, tokens[0], tokens[1])) {
                printf("Search for %s failed.\n", tokens[0]);
            } else {
                printf("Search for %s succeeded (%s).\n", tokens[0], tokens[1]);
            }
            
        /* if it's a command and the command is delete */
        } else if (strncmp(line + i_lin, "delete", 6) == 0) {
            for (; len > 0 && line[i_lin] != ' ';
                 len--, i_lin++)
                ;
            
            if (len == 0) {
                fprintf(stderr, "Invalid line.\n");
                return EX_DATAERR;
            }
            
            for (; len > 0 && line[i_lin] == ' ';
                 len--, i_lin++)
                ;
            
            if (len == 0) {
                fprintf(stderr, "Invalid line.\n");
                return EX_DATAERR;
            }
            
            /* store argument */
            for (i_tok = 0; len > 0 && line[i_lin] != ' ';
                 len--, i_lin++) {
                tokens[0][i_tok++] = line[i_lin];
            }
            tokens[0][i_tok] = '\0';
            
            if (delete(&table, tokens[0])) {
                printf("Deletion of %s failed.\n", tokens[0]);
            } else {
                printf("Deletion of %s succeeded.\n", tokens[0]);
            }
            
        /* if it's a command and the command is reset */
        } else if (strncmp(line + i_lin, "reset", 5) == 0) {
            reset_table(&table);
            printf("Table reset.\n");
            
        /* if it's neither a comment nor an empty line */
        } else if (line[i_lin] != '#' && line[i_lin] != '\n') {
            fprintf(stderr, "Invalid line.\n");
            return EX_DATAERR;
        }
    }
    return 0;
}