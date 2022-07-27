#include <stdio.h>
#include <stdlib.h>

int main(int argc, const char * argv[]) {
    char *text, *pnt;
    int k;
    double **profile, *prob;
    double p_val, max_pval;
    int n, max_ind, i, j;
    
    text = malloc(100000);
    gets(text);
    scanf("%d", &k);
    profile = malloc(sizeof(double *) * 4);
    for (i = 0; i < 4; i++) {
        profile[i] = malloc(sizeof(double) * k);
    }
    for (i = 0; i < 4; i++) {
        for (j = 0; j < k; j++) {
            scanf("%lf", &(profile[i][j]));
        }
    }
    
    pnt = text;
    n = 0;
    while (*pnt++) {
        n++;
    }
    prob = malloc(sizeof(double) * (n-k+1));
    for (i = 0; i < n-k+1; i++) {
        p_val = 1;
        for (j = 0; j < k; j++) {
            switch (text[i+j]) {
                case 'A':
                    p_val *= profile[0][j];
                    break;
                    
                case 'C':
                    p_val *= profile[1][j];
                    break;
                    
                case 'G':
                    p_val *= profile[2][j];
                    break;
                    
                default:
                    p_val *= profile[3][j];
                    break;
            }
        }
        prob[i] = p_val;
    }
    
    max_ind = 0;
    max_pval = prob[0];
    for (i = 1; i < n-k+1; i++) {
        if (prob[i] > max_pval) {
            max_ind = i;
            max_pval = prob[i];
        }
    }
    
    for (j = 0; j < k; j++) {
        printf("%c", text[max_ind+j]);
    }
    printf("\n");
}