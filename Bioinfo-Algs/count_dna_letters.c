#include <stdio.h>
#include <stdlib.h>

int main3(int argc, const char * argv[]) {
    char *dna, nt;
    int acount, ccount, gcount, tcount;
    
    dna = malloc(1000);
    gets(dna);
    
    acount = 0;
    ccount = 0;
    gcount = 0;
    tcount = 0;
    
    while ((nt = *dna++)) {
        switch (nt) {
            case 'A':
                acount++;
                break;
            
            case 'C':
                ccount++;
                break;
                
            case 'G':
                gcount++;
                break;
                
            case 'T':
                tcount++;
                break;
                
            default:
                break;
        }
    }
    
    printf("%d %d %d %d\n", acount, ccount, gcount, tcount);
        
    return 0;
}
