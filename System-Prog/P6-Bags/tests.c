#include <stdio.h>
#include <stdlib.h>
#include "bag.h"

/* write your testing program in this file; the tests below are two simple
 * examples
 */

# define SZ 4

static void test1(void);
static void test2(void);
static void test3(void);
static void test4(void);

/* tests that size() returns the right value for a bag with several
 * elements
 */
static void test1(void) {
    Bag bag;
    char element[3];
    int i;
    
    init_bag(&bag);
    
    for (i= 1; i <= 10; i++) {
        sprintf(element, "%d", i);
        add_to_bag(&bag, element);
    }
    
    if (size(bag) != 10) {
        printf("Buggy bag- size() is wrong!\n");
        exit(FOUND_BUG);
    }
}

/* tests that count() returns the right value for an element that has
 * several occurrences in a bag
 */
static void test2(void) {
    Bag bag;
    int i;
    
    init_bag(&bag);
    
    for (i= 1; i <= 10; i++)
        add_to_bag(&bag, "I love CMSC 216!");
    
    if (count(bag, "I love CMSC 216!") != 10) {
        printf("Buggy bag- count() is wrong!\n");
        exit(FOUND_BUG);
    }
}

/* tests add_to_bag(), remove_occurrence() and remove_from_bag()
 */
static void test3(void) {
    Bag bag;
    char element[3];
    int i, j;
    
    init_bag(&bag);
    
    for (i = 1; i <= 10; i++) {
        sprintf(element, "%d", i);
        for (j = 1; j <= i * 2 ; j++) {
            add_to_bag(&bag, element);
        }
    }
    
    if (size(bag) != 10) {
        printf("Buggy bag- size() is wrong!\n");
        exit(FOUND_BUG);
    }
    
    for (i = 1; i <= 10; i++) {
        sprintf(element, "%d", i);
        if (count(bag, element) != i * 2) {
            printf("Buggy bag- count() is wrong!\n");
            exit(FOUND_BUG);
        }
    }
    
    for (i = 1; i <= 10; i++) {
        sprintf(element, "%d", i);
        remove_occurrence(&bag, element);
        remove_occurrence(&bag, element);
    }
    
    if (size(bag) != 9) {
        printf("Buggy bag- size() is wrong!\n");
        exit(FOUND_BUG);
    }
    
    for (i = 1; i <= 10; i++) {
        sprintf(element, "%d", i);
        if (i == 1 && count(bag, element) != -1) {
            printf("Buggy bag- count() is wrong!\n");
            exit(FOUND_BUG);
        }
        
        if (i != 1 && count(bag, element) != i * 2 - 2) {
            printf("Buggy bag- count() is wrong!\n");
            exit(FOUND_BUG);
        }
    }
    
    remove_from_bag(&bag, element);
    
    if (remove_from_bag(&bag, element) != -1) {
        printf("Buggy bag- remove_from_bag() is wrong!\n");
        exit(FOUND_BUG);
    }
    
    if (remove_occurrence(&bag, element) != -1) {
        printf("Buggy bag- remove_occurence() is wrong!\n");
        exit(FOUND_BUG);
        
    }
    
    if (count(bag, element) != -1) {
        printf("Buggy bag- count() is wrong!\n");
        exit(FOUND_BUG);
    }
    
    sprintf(element, "%d", 5);
    remove_from_bag(&bag, element);
    
    if (remove_from_bag(&bag, element) != -1) {
        printf("Buggy bag- remove_from_bag() is wrong!\n");
        exit(FOUND_BUG);
    }
    
    if (remove_occurrence(&bag, element) != -1) {
        printf("Buggy bag- remove_occurence() is wrong!\n");
        exit(FOUND_BUG);
        
    }
    
    if (count(bag, element) != -1) {
        printf("Buggy bag- count() is wrong!\n");
        exit(FOUND_BUG);
    }
    
    if (size(bag) != 7) {
        printf("Buggy bag- size() is wrong!\n");
        exit(FOUND_BUG);
    }
}

/* tests bag_union() and is_sub_bag()
 */
static void test4(void) {
    Bag bag1, bag2, bag_u;
    char element[3];
    int i, j;
    
    init_bag(&bag1);
    init_bag(&bag2);
    init_bag(&bag_u);
    
    for (i = 1; i <= 5; i++) {
        sprintf(element, "%d", i);
        for (j = 1; j <= i * 2 ; j++) {
            add_to_bag(&bag1, element);
        }
    }
    
    for (; i <= 10; i++) {
        sprintf(element, "%d", i);
        for (j = 1; j <= i * 2 ; j++) {
            add_to_bag(&bag2, element);
        }

    }
    
    bag_u = bag_union(bag1, bag2);
    
    if (size(bag_u) != 10) {
        printf("Buggy bag- size() is wrong!\n");
        exit(FOUND_BUG);
    }
    
    for (i = 1; i <= 10; i++) {
        sprintf(element, "%d", i);
        if (count(bag_u, element) != i * 2) {
            printf("Buggy bag- count() is wrong!\n");
            exit(FOUND_BUG);
        }
    }
    
    clear_bag(&bag1);
    clear_bag(&bag2);
    clear_bag(&bag_u);
    init_bag(&bag1);
    init_bag(&bag2);
    init_bag(&bag_u);
    
    for (i = 1; i <= 5; i++) {
        sprintf(element, "%d", i);
        for (j = 1; j <= i * 2 ; j++) {
            add_to_bag(&bag1, element);
        }
    }
    
    for (i = 4; i <= 8; i++) {
        sprintf(element, "%d", i);
        for (j = 1; j <= i * 2 ; j++) {
            add_to_bag(&bag2, element);
        }
    }
    
    bag_u = bag_union(bag1, bag2);

    if (size(bag_u) != 8) {
        printf("Buggy bag- size() is wrong!\n");
        exit(FOUND_BUG);
    }
    
    for (i = 1; i <= 8; i++) {
        sprintf(element, "%d", i);
        if ((i == 4 || i == 5) &&
            count(bag_u, element) != i * 4) {
            printf("Buggy bag- count() is wrong!\n");
            exit(FOUND_BUG);
        }
    
        if (!(i == 4 || i == 5) &&
             count(bag_u, element) != i * 2) {
            printf("Buggy bag- count() is wrong!\n");
            exit(FOUND_BUG);
        }
    }
    
    clear_bag(&bag1);
    clear_bag(&bag2);
    init_bag(&bag1);
    init_bag(&bag2);
    
    if (is_sub_bag(bag1, bag2) != 1) {
        printf("Buggy bag- is_sub_bag() is wrong!\n");
        exit(FOUND_BUG);
    }
    
    for (i = 1; i <= 10; i++) {
        sprintf(element, "%d", i);
        for (j = 1; j <= i * 2 ; j++) {
            add_to_bag(&bag1, element);
        }
    }
    
    for (i = 1; i <= 10; i++) {
        sprintf(element, "%d", i);
        for (j = 1; j <= i * 2 ; j++) {
            add_to_bag(&bag2, element);
        }
    }
    
    if (is_sub_bag(bag1, bag2) != 1) {
        printf("Buggy bag- is_sub_bag() is wrong!\n");
        exit(FOUND_BUG);
    }
    
    sprintf(element, "%d", 5);
    add_to_bag(&bag2, element);
    sprintf(element, "%d", 6);
    add_to_bag(&bag2, element);
    
    if (is_sub_bag(bag1, bag2) != 1) {
        printf("Buggy bag- is_sub_bag() is wrong!\n");
        exit(FOUND_BUG);
    }
    
    remove_occurrence(&bag2, element);
    remove_occurrence(&bag2, element);
    
    if (is_sub_bag(bag1, bag2) != 0) {
        printf("Buggy bag- is_sub_bag() is wrong!\n");
        exit(FOUND_BUG);
    }
    
    remove_from_bag(&bag1, element);
    
    if (is_sub_bag(bag1, bag2) != 1) {
        printf("Buggy bag- is_sub_bag() is wrong!\n");
        exit(FOUND_BUG);
    }
    
    sprintf(element, "%d", 5);
    remove_from_bag(&bag2, element);
    
    if (is_sub_bag(bag1, bag2) != 0) {
        printf("Buggy bag- is_sub_bag() is wrong!\n");
        exit(FOUND_BUG);
    }
    
    clear_bag(&bag1);
    init_bag(&bag1);
    
    if (is_sub_bag(bag1, bag2) != 1) {
        printf("Buggy bag- is_sub_bag() is wrong!\n");
        exit(FOUND_BUG);
    }
}

int main() {
    test1();
    test2();
    test3();
    test4();
    
    printf("No errors detected!\n");
    
    return CORRECT;
}
