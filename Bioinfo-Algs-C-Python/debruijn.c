#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include "debruijn.h"

typedef struct adj_lst_entry {
    char *node;
    struct adj_lst_entry *next;
} adj_lst_entry, *adj_lst;

typedef struct db_node {
    char *node;
    adj_lst head;
    int size;
} db_node, *debruijin;

int already_in(char *node, adj_lst head) {
    adj_lst curr;
    
    for (curr = head; curr != NULL; curr = head->next) {
        if (!strcmp(curr->node, node)) {
            return 1;
        }
    }
    
    return 0;
}

int p_already_in(char *node, char **prnted, int n) {
    int i;
    
    for (i = 0; i < n; i++) {
        if (!strcmp(node, prnted[i])) {
            return 1;
        }
    }
    
    return 0;
}

int main(int argc, const char * argv[]) {
    int k;
    char text[5000];
    
    int len, i, j, p;
    char *prefix, *suffix;
    int size;
    char **prnted;
    char *node_temp;
    int size_temp;
    adj_lst temp;
    
    debruijin db;
    
    scanf("%d\n", &k);
    gets(text);
    
    len = strlen(text);
    db = malloc(sizeof(db_node) * (len-(k-1)+1));
    for (i = 0; i < len-(k-1)+1; i++) {
        db[i].node = malloc(k);
        for (j = 0; j < k-1; j++) {
            db[i].node[j] = text[i+j];
        }
        db[i].node[k-1] = '\0';
        db[i].head = NULL;
        db[i].size = 0;
    }
    
    prefix = malloc(k);
    suffix = malloc(k);
    for (i = 0; i < len-k+1; i++) {
        for (j = 0; j < k-1; j++) {
            prefix[j] = text[i+j];
        }
        prefix[k-1] = '\0';
        for (j = 1; j < k ; j++) {
            suffix[j-1] = text[i+j];
        }
        suffix[k-1] = '\0';
        
        for (j = 0; j < len-(k-1); j++) {
            if (!strcmp(db[j].node, prefix)) {
                for (p = 1; p < len-(k-1)+1; p++) {
                    if (!strcmp(db[p].node, suffix) &&
                        !already_in(db[p].node, db[j].head)) {
                        temp = malloc(sizeof(adj_lst_entry));
                        temp->node = malloc(k);
                        strcpy(temp->node, db[p].node);
                        temp->next = db[j].head;
                        db[j].head = temp;
                        db[j].size++;
                    }
                }
            }
        }
    }
    
    
    node_temp = malloc(k);
    for (i = len-(k-1)+1-1; i > 0; i--) {
        for (j = 0; j < i; j++) {
            if (db[j].size > 0 && db[j+1].size > 0 &&
                strcmp(db[j].node, db[j+1].node) > 0) {
                strcpy(node_temp, db[j].node);
                temp = db[j].head;
                size_temp = db[j].size;
                
                strcpy(db[j].node, db[j+1].node);
                db[j].head = db[j+1].head;
                db[j].size = size_temp;
                
                strcpy(db[j+1].node, node_temp);
                db[j+1].head = temp;
                db[j+1].size = size_temp;
                
            }
        }
    }
    
    FILE *output = fopen("output.txt", "w");
    
    prnted = malloc(sizeof(char *) * (len-(k-1)+1));
    for (i = 0; i < len-(k-1)+1; i++) {
        prnted[i] = calloc(k, 1);
    }
    size = 0;
    
    for (i = 0; i < len-(k-1)+1; i++) {
        if (db[i].size > 0 && !p_already_in(db[i].node, prnted, size)) {
            fprintf(output, "%s -> ", db[i].node);
            temp = db[i].head;
            while (temp != NULL) {
                if (temp->next != NULL) {
                    fprintf(output, "%s,", temp->node);
                } else {
                    fprintf(output, "%s\n", temp->node);
                }
                temp = temp->next;
            }
            
            strcpy(prnted[size], db[i].node);
            size++;
        }
    }
    
    return 0;
}