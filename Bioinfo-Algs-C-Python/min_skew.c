#include <stdio.h>
#include <stdlib.h>

int min_skew(char *genome, int n);

int main(int argc, const char * argv[]) {
    FILE *output;
    char *genome, *p;
    int n, ind, i;
    
    
    genome = malloc(100000);
    gets(genome);
    
    p = genome;
    n = 0;
    while (*p++) {
        n++;
    }
    printf("%d\n", n);
    
    output = fopen("output", "w");
    for (i = 0; i <= n; i++) {
        ind = min_skew(genome, i);
        fprintf(output, "%d ", ind);
    }
    return 0;
}

int min_skew(char *genome, int n) {
    int *gc_diff_lst, gc_diff, i;
    int min_skew, min_ind;
    
    gc_diff_lst = malloc(sizeof(int) * n);
    gc_diff = 0;
    for (i = 0; i < n; i++) {
        if (genome[i] == 'G') {
            gc_diff++;
        } else if (genome[i] == 'C') {
            gc_diff--;
        }
        
        gc_diff_lst[i] = gc_diff;
    }
    
    if (n > 0) {
        min_skew = gc_diff_lst[0];
        min_ind = 0;
        for (i = 1; i < n; i++) {
            if (min_skew > gc_diff_lst[i]) {
                min_skew = gc_diff_lst[i];
                min_ind = i;
            }
        }
    } else {
        return 0;
    }
    
    
    return i;
}
