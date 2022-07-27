#include <stdio.h>
#include <stdlib.h>

int main3(int argc, const char * argv[]) {
    char *pattern, *genome, *p;
    int n1, n2, match, i, j;
    
    pattern = malloc(1000);
    genome = malloc(10000);
    gets(pattern);
    gets(genome);
    
    p = pattern;
    n1 = 0;
    while (*p++) {
        n1++;
    }
    
    p = genome;
    n2 = 0;
    while (*p++) {
        n2++;
    }
    
    for (i = 0; i < n2; i++) {
        match = 1;
        for (j = 0; j < n1; j++) {
            if (pattern[j] != genome[i+j]) {
                match = 0;
                break;
            }
        }
        
        if (match) {
            printf("%d ", i);
        }
    }
    printf("\n\n");

    
    return 0;
}