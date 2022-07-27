#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "hashtable.h"

/*
 CC = gcc
 CFLAGS = -ansi -Wall -g -O0 -Wwrite-strings -Wshadow -pedantic-errors -fstack-protector-all
 PROGS = public01 public02 public03
 
 all: $(PROGS)
 
 public01: public01.o htable.o my_memory_checker_216.o
    $(CC) -o public01 public01.o htable.o my_memory_checker_216.o
 
 public02: public02.o htable.o my_memory_checker_216.o
    $(CC) -o public02 public02.o htable.o my_memory_checker_216.o
 
 public03: public03.o htable.o my_memory_checker_216.o
    $(CC) -o public03 public03.o htable.o my_memory_checker_216.o
 
 student_tests: student_tests.o htable.o
    $(CC) -o student_test student_test.o htable.o
 
 public01.o: public01.c htable.h my_memory_checker_216.h
    $(CC) $(CFLAGS) -c public01.c
 
 public02.o: public02.c htable.h my_memory_checker_216.h
    $(CC) $(CFLAGS) -c public02.c
 
 public03.o: public03.c htable.h my_memory_checker_216.h
    $(CC) $(CFLAGS) -c public03.c
 
 student_tests.o: student_tests.c htable.h my_memory_checker_216.h
    $(CC) $(CFLAGS) -c student_tests.c
 
 htable.o: htable.c htable.h
    $(CC) $(CFLAGS) -c htable.c
 
 my_memory_checker_216.o: my_memory_checker_216.c my_memory_checker_216.h
    $(CC) $(CFLAGS) -c my_memory_checker_216.c
 
 clean: 
    @rm -f *.o $(PROGS)
 */

unsigned int hash_code(const char *);

/*
 * This function allocates a Table structure and initializes 
 * the buckets field with an array (of size table size) of 
 * pointers to Bucket structures. Each array entry is 
 * initialized to NULL. The function will initialize other
 * table fields based on parameter values. This function 
 * returns SUCCESS (value defined in htable.h) if the table
 * can be created. The function will fail and return FAILURE 
 * if table is NULL or table size is 0.
 */
int create_table(Table **table, int table_size, void (*free_value)(void *)) {
    int i;
    
    if (table == NULL || table_size == 0) {
        return FAILURE;
    }
    
    if (!(*table = malloc(sizeof(Table)))) {
        return FAILURE;
    }
    if (!((*table)->buckets = malloc(sizeof(Bucket *) * table_size))) {
        return FAILURE;
    }
    for (i = 0; i < table_size; i++) {
        (*table)->buckets[i] = NULL;
    }
    (*table)->free_value = free_value;
    (*table)->table_size = table_size;
    (*table)->key_count = 0;
    return SUCCESS;
}

/*
 * Frees ALL memory associated with the given table, including
 * all nodes of the hash chains. All remaining values are freed 
 * by invoking the free value function on each. This function
 * returns SUCCESS if the table can be destroyed successfully. 
 * The function will fail and return FAILURE if table is NULL. 
 */
int destroy_table(Table *table) {
    int i;
    Bucket *curr, *temp;
    
    if (table == NULL) {
        return FAILURE;
    }
    
    for (i = 0; i < table->table_size; i++) {
        for (curr = table->buckets[i]; curr != NULL; curr = temp) {
            if (curr->value != NULL && table->free_value != NULL) {
                table->free_value(curr->value);
            }
            free(curr->key);
            temp = curr->next;
            free(curr);
        }
    }
    free(table->buckets);
    free(table);
    return SUCCESS;
}

/*
 * Attempts to insert a key/value pair into the table. The 
 * function will make a copy of the key. The value is “kept” 
 * by the table. If the key does not exist, it will be added 
 * to the table by making a copy of the key. If the key is 
 * already in the table, no additional copy must be made, and
 * the corresponding value in the table should be updated with
 * the value parameter. The previous value should be freed by
 * free value, if both are not NULL.
 */
int put(Table *table, const char *key, void *value) {
    unsigned int i;
    Bucket *curr;
    
    if (table == NULL || key == NULL) {
        return FAILURE;
    }
    
    i = hash_code(key) % table->table_size;
    for (curr = table->buckets[i]; curr != NULL; curr = curr->next) {
        if (!strcmp(curr->key, key)) {
            break;
        }
    }
    
    if (curr != NULL) {
        if (curr->value != NULL && table->free_value != NULL) {
            table->free_value(curr->value);
        }
        curr->value = value;
    } else {
        if (!(curr = malloc(sizeof(Bucket))) ||
            !(curr->key = malloc(strlen(key) + 1))) {
            return FAILURE;
        } else {
            strcpy(curr->key, key);
            curr->value = value;
            if (table->buckets[i] == NULL) {
                curr->next = NULL;
                table->buckets[i] = curr;
            } else {
                curr->next = table->buckets[i];
                table->buckets[i] = curr;
            }
        }
        table->key_count++;
    }
    return SUCCESS;
}

/*
 * This function will copy the value pointer into the value out
 * parameter if the key is found. If the key is not found in the
 * table, *value will be set to NULL and the function will return
 * FAILURE. This function returns SUCCESS if the key was found in 
 * the table. The function will fail and return FAILURE if table
 * is NULL or key is NULL.
 */
int get_value(const Table *table, const char *key, void **value) {
    int i;
    Bucket *curr;
    
    if (table == NULL || key == NULL) {
        return FAILURE;
    }
    
    i = hash_code(key) % table->table_size;
    for (curr = table->buckets[i]; curr != NULL; curr = curr->next) {
        if (!strcmp(curr->key, key)) {
            break;
        }
    }
    
    if (curr == NULL) {
        *value = NULL;
        return FAILURE;
    }
    
    *value = curr->value;
    return SUCCESS;
}

/*
 * This function returns the number of keys in the table. The 
 * function will fail and return FAILURE if table is NULL.
 */
int get_key_count(const Table *table) {
    if (table == NULL) {
        return FAILURE;
    }
    
    return table->key_count;
}

/*
 * This function will remove the key/value pair from the table if
 * the key is present in the table. It invokes free value on the
 * associated value if the key is present in the table and the 
 * associated value is not NULL. This function returns SUCCESS 
 * if the value was deleted from the table. The function will 
 * return FAIL- URE if table is NULL, key is NULL or if the the
 * key is not found.
 */
int remove_entry(Table *table, const char *key) {
    int i;
    Bucket *curr, *prev;
    
    if (table == NULL || key == NULL) {
        return FAILURE;
    }
    
    i = hash_code(key) % table->table_size;
    curr = table->buckets[i];
    prev = NULL;
    while (curr != NULL && strcmp(curr->key, key)) {
        prev = curr;
        curr = curr->next;
    }
    
    if (curr == NULL) {
        return FAILURE;
    }
    
    if (curr->value != NULL && table->free_value != NULL) {
        table->free_value(curr->value);
    }
    free(curr->key);
    if (prev == NULL) {
        table->buckets[i] = curr->next;
        free(curr);
    } else {
        prev->next = curr->next;
        free(curr);
    }
    table->key_count--;
    return SUCCESS;
}

/*
 * This function removes the list associated with each bucket. 
 * After removing the lists, the buckets will be set to NULL. 
 * The key count will be set to 0 as part of the clearing process. 
 * All values must be freed using free value(); This function 
 * returns SUCCESS if the table is cleared and FAILURE if the
 * table is NULL.
 */
int clear_table(Table *table) {
    int i;
    Bucket *curr, *temp;
    
    if (table == NULL) {
        return FAILURE;
    }
    
    for (i = 0; i < table->table_size; i++) {
        for (curr = table->buckets[i]; curr != NULL; curr = temp) {
            if (curr->value != NULL && table->free_value != NULL) {
                table->free_value(curr->value);
            }
            free(curr->key);
            temp = curr->next;
            free(curr);
        }
        table->buckets[i] = NULL;
    }
    table->key_count = 0;
    return SUCCESS;
}

/*
 * This function returns SUCCESS if there are no keys in the 
 * table or table is NULL, and 0 otherwise. 
 */
int is_empty(const Table *table) {
    if (table != NULL && table->key_count != 0) {
        return 0;
    }
    
    return SUCCESS;
}

/*
 * This function returns the table size. The function returns
 * FAILURE if the table is NULL.
 */
int get_table_size(const Table *table) {
    if (table == NULL) {
        return FAILURE;
    }
    
    return table->table_size;
}
/*
 * Do not modify this hash_code function.
 * Leave this function at the end of the file.
 * Do not add a prototype to the htable.h file
 * for this function.
 *
 */

unsigned int hash_code(const char *str) {
    unsigned int code;
    
    for (code = 0; *str; str++) {
        code ^= ((code << 3) | (code >> 29)) + (*str);
    }
    
    return code;
}