
#if !defined(BAG_H)
#define BAG_H
#include <stdlib.h>

typedef struct element {
    char *element;
    int count;
    struct element *next;
} Element;

typedef struct bag {
    Element *elements;
    size_t size;
} Bag;

#define CORRECT 1
#define FOUND_BUG 2

void init_bag(Bag *bag);
void add_to_bag(Bag *bag, const char *element);
size_t size(Bag bag);
int count(Bag bag, const char *element);
int remove_occurrence(Bag *bag, const char *element);
int remove_from_bag(Bag *bag, const char *element);
Bag bag_union(Bag bag1, Bag bag2);
int is_sub_bag(Bag bag1, Bag bag2);
void clear_bag(Bag *bag);

#endif