
#include <stdio.h>
#include <string.h>
#include "hashtable.h"

/*
 * Initialize the table to be empty. Do nothing if table is NULL.
 */
void init_table(Table *table) {
    reset_table(table);
}

/*
 * Reset the table to an empty state, meaning that all buckets 
 * are in the empty state.  Do nothing if table is NULL.
 */
void reset_table(Table *table) {
    int i;
    
    if (table == NULL) {
        return;
    }
    
    for (i = 0; i < NUM_BUCKETS; i++) {
        table->buckets[i].state = EMPTY;
    }
    table->key_ct = 0;
}

/*
 * Find the index of the bucket in which the key is located.
 * Return the index if the key is present, -1 otherwise.
 */
static unsigned long get_index(Table *table, const char *key) {
    unsigned long start_i, i;
    
    i = start_i = hash_code(key) % NUM_BUCKETS;
    if (table->buckets[i].state == EMPTY) {
        return FAILURE;
    }
    if (table->buckets[i].state != FULL ||
        strcmp(table->buckets[i].data.key, key) != 0) {
        for (i = (i + 1) % NUM_BUCKETS; i != start_i;
             i = (i + 1) % NUM_BUCKETS) {
            if (table->buckets[i].state == EMPTY) {
                break;
            }
            if (table->buckets[i].state == FULL &&
                strcmp(table->buckets[i].data.key, key) == 0) {
                break;
            }
        }
        if (table->buckets[i].state == EMPTY ||
            i == start_i) {
            return FAILURE;
        }
    }
    return i;
    /*
    for (i = start_i; (i + 1) % NUM_BUCKETS != start_i;
         i = (i + 1) % NUM_BUCKETS) {
        if (table->buckets[i].state == EMPTY) {
            break;
        }
        if (table->buckets[i].state == FULL &&
            strcmp(table->buckets[i].data.key, key) == 0) {
            break;
        }
    }
    if (table->buckets[i].state != FULL ||
        strcmp(table->buckets[i].data.key, key) != 0) {
        return -1;
    }
    return i;
     */
}
/*
 * Insert a key/value pair into the table. Insertion fails if:
 * a. either key or val is NULL; b. either the key or value string
 * is longer than the MAX_STR_SIZE defined in hashtable.h; c. table
 * is NULL; or d. the table is full. If the key is already in the
 * table, its corresponding value in the table should be overwritten
 * with the new value. In this case, even if the table is full the 
 * insert succeeds. Return 0 if insertion is successful, -1 otherwise.
 */
int insert(Table *table, const char *key, const char *val) {
    unsigned long start_i, i;
    
    if (table == NULL || key == NULL || val == NULL ||
        strlen(key) > MAX_STR_SIZE || strlen(val) > MAX_STR_SIZE) {
        return FAILURE;
    }
   
    if ((i = get_index(table, key)) != -1) {
        strcpy(table->buckets[i].data.value, val);
    } else {
        i = start_i = hash_code(key) % NUM_BUCKETS;
        if (table->buckets[i].state == FULL) {
            for (i = (i + 1) % NUM_BUCKETS; i != start_i;
                 i = (i + 1) % NUM_BUCKETS) {
                if (table->buckets[i].state != FULL) {
                    break;
                }
            }
            if (i == start_i) {
                return FAILURE;
            }
        }
        
        strcpy(table->buckets[i].data.key, key);
        strcpy(table->buckets[i].data.value, val);
        table->buckets[i].state = FULL;
        table->key_ct++;
    }
    return SUCCESS;
}

/*
 * Search for a key in the table. If the key is present and the 
 * parameter val is not NULL, the value that is paired with the 
 * key in the table should be copied into the buffer val points 
 * to (you may assume the buffer is large enough to hold the value). 
 * If either table or key is NULL, the search is defined as failing. 
 * If the key is present and the parameter val is NULL, the function 
 * will not copy the string and return 0. Return 0 if the key is in
 * the table, -1 if the search fails.
 */
int search(Table *table, const char *key, char *val) {
    unsigned long i;
    
    if (table == NULL || key == NULL) {
        return FAILURE;
    }
    
    if ((i = get_index(table, key)) == -1) {
        return FAILURE;
    }
    
    if (val != NULL) {
      strcpy(val, table->buckets[i].data.value);
    }
    return SUCCESS;
}

/*
 * Attempt to remove the key from the table. If either table
 * or key is NULL, or if the key does not exist in the table,
 * the attempt fails. Return 0 if the deletion succeeds, -1
 * otherwise.
 */
int delete(Table *table, const char *key) {
    unsigned long i;
    
    if (table == NULL || key == NULL) {
        return FAILURE;
    }
    
    if ((i = get_index(table, key)) == -1) {
        return FAILURE;
    }

    table->buckets[i].state = DELETED;
    table->key_ct--;
    return SUCCESS;
}

/*
 * Return the number of keys present in the table, or -1 if
 * table is NULL.
 */
int key_count(Table *table) {
    if (table == NULL) {
        return FAILURE;
    }
    
    return table->key_ct;
}

/*
 * Return the number of buckets the table has; return -1 if
 * table is NULL.
 */
int bucket_count(Table *table) {
    if (table == NULL) {
        return FAILURE;
    }
    
    return NUM_BUCKETS;
}

/*
 * Return the hash code of the string, as defined by the 
 * algorithm in Section 2.2.2. If str is NULL, return 0.
 */
unsigned long hash_code(const char *str) {
    unsigned long hashcode;
    
    if (str == NULL) {
        return 0;
    }
    
    hashcode = 0;
    for (; *str != '\0'; str++) {
        hashcode = hashcode * 65599 + *str;
    }
    return hashcode;
}