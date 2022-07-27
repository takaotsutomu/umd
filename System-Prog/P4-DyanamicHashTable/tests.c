#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "216_project_4.h"


/*****************************************************/
/* In this file you will provide tests to your       */
/* hash table.  Each test should be named test1()    */
/* test2(), etc. Each test must have a description   */
/* of what it is testing (this description is really */
/* important).                                       */
/*                                                   */
/* You can tell whether any test failed if you       */
/* execute on the command line "echo $?" and you get */
/* a value other than 0. "echo $?" prints the status */
/* of the last command.                              */
/*                                                   */
/* main just calls all test1(), test2(), etc.        */
/*****************************************************/
/*
int create_table(Table **table, int table_size, void (*free_value)(void *));
int destroy_table(Table *table);
int put(Table *table, const char *key, void *value);
int get_value(const Table *table, const char *key, void **value);
int get_key_count(const Table *table);
int remove_entry(Table *table, const char *key);
int clear_table(Table *table);
int is_empty(const Table *table);
int get_table_size(const Table *table);
*/

/* This test checks the correctness of create_table,
 * put_table, get_value, get_key_count, get_key_count
 * and destroy_table
 */
static int test1() {
    int table_size = 2, i;
    Table *table_ptr;
    char *company;
    void *value;
    
    create_table(&table_ptr, table_size, free);
    if (table_ptr == NULL) {
        return FAILURE;
    }
    if (table_ptr->table_size != 2) {
        return FAILURE;
    }
    if (table_ptr->key_count) {
        return FAILURE;
    }
    if (table_ptr->free_value != free) {
        return FAILURE;
    }
    if (get_table_size(table_ptr) != 2) {
        return FAILURE;
    }
    put(table_ptr, "somepulp", NULL);
    put(table_ptr, "tropicana", NULL);
    put(table_ptr, "purepremium", NULL);
    put(table_ptr, "100%orange", NULL);
    value = malloc(7);
    strcpy(value, "170cal");
    put(table_ptr, "juice", value);
    put(table_ptr, "juice", NULL);
    get_value(table_ptr, "juice", &value);
    if (value != NULL) {
        FAILURE;
    }
    for (i = 0; i < 9; i++) {
        if (i % 2 != 0) {
            put(table_ptr, "tropicana", NULL);
        } else {
            company = malloc(8);
            strcpy(company, "company");
            put(table_ptr, "tropicana", company);
        }
    }
    get_value(table_ptr, "tropicana", &value);
    if (strcmp(company, value)) {
        return FAILURE;
    }
    if (get_key_count(table_ptr) != 5) {
        return FAILURE;
    }
    destroy_table(table_ptr);
    printf("test1 passed\n");
    return SUCCESS;
}

/* This test checks the correctness of remove_entry,
 * clear_table and is_empty
 */
static int test2() {
    int table_size = 2;
    Table *table_ptr;
    void *value;
    
    create_table(&table_ptr, table_size, NULL);
    put(table_ptr, "somepulp", NULL);
    put(table_ptr, "tropicana", NULL);
    put(table_ptr, "purepremium", NULL);
    put(table_ptr, "100%orange", NULL);
    put(table_ptr, "juice", NULL);
    remove_entry(table_ptr, "somepulp");
    if (get_key_count(table_ptr) != 4) {
        return FAILURE;
    }
    if (!get_value(table_ptr, "somepulp", &value)) {
        return FAILURE;
    }
    clear_table(table_ptr);
    if (!is_empty(table_ptr)) {
        return FAILURE;
    }
    put(table_ptr, "agua", NULL);
    put(table_ptr, "pura", NULL);
    put(table_ptr, "sabor", NULL);
    if (get_key_count(table_ptr) != 3) {
        return FAILURE;
    }
    remove_entry(table_ptr, "agua");
    remove_entry(table_ptr, "sabor");
    if (get_key_count(table_ptr) != 1) {
        return FAILURE;
    }
    destroy_table(table_ptr);
    printf("test2 passed\n");
    return SUCCESS;
}

int main() {
    int result = SUCCESS;
    
    if(test1() == FAILURE) result = FAILURE;
    if(test2() == FAILURE) result = FAILURE;
    
    if(result == FAILURE) {
        exit(EXIT_FAILURE);
    }
    
    return EXIT_SUCCESS;
}

