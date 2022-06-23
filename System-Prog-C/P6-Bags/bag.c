#include "bag.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

/*
CC = gcc
CFLAGS = -ansi -Wall -g -O0 -Wwrite-strings -Wshadow -pedantic-errors -fstack-protector-all

all: public01 public02 public03 public04

public01: public01.o bag.o my_memory_checker_216.o
    $(CC) -o public01 public01.o bag.o my_memory_checker_216.o

public02: public02.o bag.o my_memory_checker_216.o
    $(CC) -o public02 public02.o bag.o my_memory_checker_216.o

public03: public03.o bag.o my_memory_checker_216.o
    $(CC) -o public03 public03.o bag.o my_memory_checker_216.o

public04: public04.o bag.o my_memory_checker_216.o
    $(CC) -o public04 public04.o bag.o my_memory_checker_216.o
 
public01.o: public01.c bag.h 
    $(CC) $(CFLAGS) -c public01.c

public02.o: public02.c bag.h
    $(CC) $(CFLAGS) -c public02.c

public03.o: public03.c bag.h
    $(CC) $(CFLAGS) -c public03.c
 
public04.o: public04.c bag.h
    $(CC) $(CFLAGS) -c public04.c

bag.o: bag.c bag.h
    $(CC) $(CFLAGS) -c bag.c
 
my_memory_checker_216.o: my_memory_checker_216.c my_memory_checker_216.o
    $(CC) $(CFLAGS) -c my_memory_checker_216.c
 
clean:
    rm -f public01 public02 public03 public04 *.o

*/

/*
 * This function initializes the bag whose memory address
 * is passed in as a parameter. It must be called once (and 
 * only once) on a Bag variable, before elements can be 
 * stored in it. If the parameter bag is NULL, the function
 * have no effect. All of the other functions may assume 
 * that init bag() has already been called on any bag 
 * variables that are passed into them.
 */
void init_bag(Bag *bag) {
    if (bag == NULL) {
        return;
    }
    
    bag->elements = NULL;
    bag->size = 0;
}

/*
 * This function adds one occurrence of the element element
 * to the bag that its parameter bag points to. If  element
 * was not previously in the bag at all, it will subsequently
 * be present with one occurrence. If it was in the bag, 
 * it will have one more occurrence than it had before. If
 * either bag or element are NULL, the function should just
 * return without changing anything.
 */
void add_to_bag(Bag *bag, const char *element) {
    Element *curr, *add_ele;
    
    if (bag == NULL || element == NULL) {
        return;
    }
    
    for (curr = bag->elements; curr != NULL; curr = curr->next) {
        if (!strcmp(curr->element, element)) {
            break;
        }
    }
    
    if (curr != NULL) {
        curr->count++;
    } else {
        add_ele = malloc(sizeof(Element));
        add_ele->element = malloc(strlen(element) + 1);
        strcpy(add_ele->element, element);
        add_ele->count = 1;
        add_ele->next = bag->elements;
        bag->elements = add_ele;
        bag->size++;
    }
}

/*
 * This function returns the number of elements currently
 * stored in its parameter bag, ignoring the number of 
 * occurrences of the elements. For example, if called 
 * on a bag storing one occurrence of "koala" and two
 * occurrences of "platypus", 2 IS returned. The parameter
 * bag is not be modified.
 */
size_t size(Bag bag) {
    return bag.size;
}


/*
 * This function returns the number of occurrences of the
 * element element that are currently stored in its parameter 
 * bag. If element is not present in the bag at all, the 
 * function returns −1. If element is NULL, the function also 
 * returns −1. The parameter bag is not modified.
 */
int count(Bag bag, const char *element) {
    Element *curr;
    
    if (element == NULL) {
        return -1;
    }
    
    for (curr = bag.elements; curr != NULL; curr = curr->next) {
        if (!strcmp(curr->element, element)) {
            break;
        }
    }
    
    if (curr == NULL) {
        return -1;
    }
    return curr->count;
}

/*
 * This function removes one occurrence of the element element
 * from the bag that its parameter bag points to. The number 
 * of occurrences of that element subsequently decreases by 1.
 * If element is not present in the bag at all, the function 
 * returns −1. If it is present, an occurrence of it is removed,
 * and the function return the new number of occurrences
 * that it has, except if its last or only occurrence is removed,
 * in which case −1 is returned. If either bag or element are 
 *NULL, the function also returns −1 without changing anything.
 */
int remove_occurrence(Bag *bag, const char *element) {
    Element *curr = bag->elements, *prev = NULL;
    
    if (bag == NULL || element == NULL) {
        return -1;
    }
    
    while (curr != NULL && strcmp(curr->element, element)) {
        prev = curr;
        curr = curr->next;
    }
    
    if (curr == NULL) {
        return -1;
    }
    
    curr->count--;
    if (curr->count == 0) {
        if (prev == NULL) {
            bag->elements = curr->next;
        } else {
            prev->next = curr->next;
            
        }
        bag->size--;
        free(curr->element);
        free(curr);
        return -1;
    }
    return curr->count;
}

/*
 * This function removes all occurrences of element from
 * the bag that its parameter bag points to. The size of
 * the bag subsequently decreases by 1. If element is in
 * the bag, the function removes it and returns 0, otherwise,
 * it just returns −1 without changing anything. If either
 * bag or element are NULL, the function also returns −1 
 * without changing anything.
 */
int remove_from_bag(Bag *bag, const char *element) {
    Element *curr = bag->elements, *prev = NULL;
    
    if (bag == NULL || element == NULL) {
        return -1;
    }
    
    while (curr != NULL && strcmp(curr->element, element)) {
        prev = curr;
        curr = curr->next;
    }
    
    if (curr == NULL) {
        return -1;
    }
    
    if (prev == NULL) {
        bag->elements = curr->next;
    } else {
        prev->next = curr->next;
    }
    bag->size--;
    free(curr->element);
    free(curr);
    return 0;
}

/*
 * This function creates and returns a new bag which 
 * has all of the elements of its two parameter bags
 * bag1 and bag2 combined. If an element is only in
 * one of the parameter bags, it will be in the returned
 * bag with the same number of occurrences as it has 
 * in that parameter bag. If an element is in both 
 * parameter bags, it is in the returned bag with the
 * combined number of occurrences that it has in both
 * parameter bags. The two parameter bags are modified.
 */
Bag bag_union(Bag bag1, Bag bag2) {
    Bag bag_u;
    Element *curr;
    int i;
    
    init_bag(&bag_u);
    
    for (curr = bag1.elements; curr != NULL; curr = curr->next) {
        for (i = 0; i < curr->count; i++) {
            add_to_bag(&bag_u, curr->element);
        }
    }
    
    for (curr = bag2.elements; curr != NULL; curr = curr->next) {
        for (i = 0; i < curr->count; i++) {
            add_to_bag(&bag_u, curr->element);
        }
    }
    return bag_u;
}

/*
 * This function tests whether every element in bag1 is
 * present in bag2 with at least as many occurrences as
 * it has in bag1 (meaning the same or more), and returns
 * 1 if so. If any element is in bag1 but not in bag2, 
 * or if any element is in both bags but it has more 
 * occurrences in bag1 than it does in bag2, the function
 * returns 0. Note that if bag1 has no elements, the
 * function always returns 1, regardless of how many 
 * elements bag2 has. The two parameter bags is modified.
 */
int is_sub_bag(Bag bag1, Bag bag2) {
    Element *curr1, *curr2;
    
    if (bag1.size == 0) {
        return 1;
    }
    
    for (curr1 = bag1.elements; curr1 != NULL; curr1 = curr1->next) {
        for (curr2 = bag2.elements; curr2 != NULL; curr2 = curr2->next) {
            if (!strcmp(curr2->element, curr1->element)) {
                break;
            }
        }
        
        if (curr2 == NULL) {
            return 0;
        }
        
        if (curr2->count < curr1->count) {
            return 0;
        }
    }
    return 1;
}

/*
 * This function frees all dynamically-allocated
 * memory associated with the bag that its parameter 
 * bag points to. If the parameter bag is NULL, it has 
 * no effect.
 */
void clear_bag(Bag *bag) {
    Element *curr, *temp;
    
    if (bag == NULL) {
        return;
    }
    
    for (curr = bag->elements; curr != NULL; curr = temp) {
        free(curr->element);
        temp = curr->next;
        free(curr);
    }
}