#include <stdio.h>
#include <stdlib.h>

int main(int argc, const char * argv[]) {
    FILE *file;
    char *label, *dna;
    int *sp, *z, max_z, n;
    int i, j, k, l;
    
    label = malloc(15);
    dna = malloc(200000);
    
    gets(label);
    for (i = 0; i < 200000; i++) {
        if (scanf("%c", dna+i) == EOF) {
            dna[i] = 0;
            n = i - 1;
            break;
        }
        
    }
    
    z = malloc(sizeof(int) * n);
    z[0] = max_z = j = 0;
    for (i = 1; i < n; i++) {
        if (max_z < i) {
            z[i] = l = 0;
            while (dna[i+l] == dna[l]) {
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
                while (dna[i+l] == dna[l]) {
                    z[i]++;
                    l++;
                }
                max_z = i + z[i];
                j = i;
            }
        }
    }
    
    sp = calloc(n, sizeof(int));
    for (i = 1; i < n; i++) {
        if (z[i] != 0) {
            for (j = 0; j < z[i]; j++) {
                if (sp[i+j] < j+1) {
                    sp[i+j] = j+1;
                }
            }
        }
    }
    
    file = fopen("output", "w");
    printf("%s\n", dna);
    for (i = 0; i < n; i++) {
        printf("%d ", sp[i]);
        fprintf(file, "%d ", sp[i]);
    }
    fclose(file);
    
    return 0;
}