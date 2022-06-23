
#include <stdio.h>
#include <stdlib.h>

int main3(int argc, const char * argv[]) {
    char *pattern, *genome, *p;
    int *z, max_z;
    int n, i, j, k, l;
    
    genome = malloc(11000);
    gets(genome);
    
    p = genome;
    n = 0;
    while (*p++) {
        n++;
    }
    
    z = malloc(sizeof(int) * n);
    z[0] = max_z = j = 0;
    for (i = 1; i < n; i++) {
        if (max_z < i) {
            z[i] = l = 0;
            while (genome[i+l] == genome[l]) {
                z[i]++;
                l++;
            }
            max_z = i + z[i];
            j = i;
        } else {
            k = i - j;
            if (z[k] + i < max_z) {
                z[i] = z[k];
            } else if (z[k] + i > max_z) {
                z[i] = max_z - i;
            } else {
                l = z[i] = max_z - i;
                while (genome[i+l] == genome[l]) {
                    z[i]++;
                    l++;
                }
                max_z = i + z[i];
                j = i;
            }
        }
    }

    
    for (i = 0; i < n; i++) {
        if (z[i] == 9) {
          printf("%d ", i-10);
        }
    }
    printf("\n");
    
    
    return 0;
}