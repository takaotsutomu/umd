#include <stdio.h>
#include <stdlib.h>
#include <string.h>

static char *aminos = "ACDEFGHIKLMNPQRSTVWY";

static int BLOSUM62[20][20] = {
    { 4,  0, -2, -1, -2,  0, -2, -1, -1, -1, -1, -2, -1, -1, -1,  1,  0,  0, -3, -2},
    { 0,  9, -3, -4, -2, -3, -3, -1, -3, -1, -1, -3, -3, -3, -3, -1, -1, -1, -2, -2},
    {-2, -3,  6,  2, -3, -1, -1, -3, -1, -4, -3,  1, -1,  0, -2,  0, -1, -3, -4, -3},
    {-1, -4,  2,  5, -3, -2,  0, -3,  1, -3, -2,  0, -1,  2,  0,  0, -1, -2, -3, -2},
    {-2, -2, -3, -3,  6, -3, -1,  0, -3,  0,  0, -3, -4, -3, -3, -2, -2, -1,  1,  3},
    { 0, -3, -1, -2, -3,  6, -2, -4, -2, -4, -3,  0, -2, -2, -2,  0, -2, -3, -2, -3},
    {-2, -3, -1,  0, -1, -2,  8, -3, -1, -3, -2,  1, -2,  0,  0, -1, -2, -3, -2,  2},
    {-1, -1, -3, -3,  0, -4, -3,  4, -3,  2,  1, -3, -3, -3, -3, -2, -1,  3, -3, -1},
    {-1, -3, -1,  1, -3, -2, -1, -3,  5, -2, -1,  0, -1,  1,  2,  0, -1, -2, -3, -2},
    {-1, -1, -4, -3,  0, -4, -3,  2, -2,  4,  2, -3, -3, -2, -2, -2, -1,  1, -2, -1},
    {-1, -1, -3, -2,  0, -3, -2,  1, -1,  2,  5, -2, -2,  0, -1, -1, -1,  1, -1, -1},
    {-2, -3,  1,  0, -3,  0,  1, -3,  0, -3, -2,  6, -2,  0,  0,  1,  0, -3, -4, -2},
    {-1, -3, -1, -1, -4, -2, -2, -3, -1, -3, -2, -2,  7, -1, -2, -1, -1, -2, -4, -3},
    {-1, -3,  0,  2, -3, -2,  0, -3,  1, -2,  0,  0, -1,  5,  1,  0, -1, -2, -2, -1},
    {-1, -3, -2,  0, -3, -2,  0, -3,  2, -2, -1,  0, -2,  1,  5, -1, -1, -3, -3, -2},
    { 1, -1,  0,  0, -2,  0, -1, -2,  0, -2, -1,  1, -1,  0, -1,  4,  1, -2, -3, -2},
    { 0, -1, -1, -1, -2, -2, -2, -1, -1, -1, -1,  0, -1, -1, -1,  1,  5,  0, -2, -2},
    { 0, -1, -3, -2, -1, -3, -3,  3, -2,  1,  1, -3, -2, -2, -3, -2,  0,  4, -3, -1},
    {-3, -2, -4, -3,  1, -2, -2, -3, -3, -2, -1, -4, -4, -2, -3, -3, -2, -3, 11,  2},
    {-2, -2, -3, -2,  3, -3,  2, -1, -2, -1, -1, -2, -3, -1, -2, -2, -2, -1,  2,  7}
};

int amino_index(char a) {
    int index, i;
    
    index = -1;
    for (i = 0; i < 20; i++) {
        if (aminos[i] == a) {
            index = i;
        }
    }
    
    return index;
}

void concat_str(char *z, char *x, char *y) {
    int lenx, leny;
    
    lenx = strlen(x);
    leny = strlen(y);
    z = malloc(lenx+leny);
    
    while ((*z++ = *x++));
    z--;
    while ((*z++ = *y++));
}

int main(int argc, const char * argv[]) {
    char str1[5000], str2[5000];
    char os1[5000] = {0}, os2[5000] = {0};
    int **score, len1, len2;
    int s1, s2, s3, max;
    int indx1, indx2, os1_i, os2_i;
    int i, j;
    
    gets(str1);
    gets(str2);
    
    len1 = strlen(str1);
    len2 = strlen(str2);
    
    score = malloc(sizeof(int *) * (len1+1));
    for (i = 0; i < len1+1; i++) {
        score[i] = malloc(sizeof(int) * (len2+1));
    }
    
    for (i = 0; i < len1+1; i++) {
        score[i][0] = i * -5;
    }
    for (i = 0; i < len2+1; i++) {
        score[0][i] = i * -5;
    }
    
    for (i = 1; i < len1+1; i++) {
        for (j = 1; j < len2+1; j++) {
            s1 = score[i][j-1] - 5;
            s2 = score[i-1][j] - 5;
            
            indx1 = amino_index(str1[i-1]);
            indx2 = amino_index(str2[j-1]);
            s3 = score[i-1][j-1] + BLOSUM62[indx1][indx2];
            
            if (s1 > s2) {
                max = s1;
                
                if (s3 > s1) {
                    max = s3;
                }
            } else {
                max = s2;
                
                if (s3 > s2) {
                    max = s3;
                }
            }
        
            score[i][j] = max;
        }
    }
    
    
    printf("%d\n", score[len1][len2]);
    
    os1_i = 0;
    os2_i = 0;
    i = len1;
    j = len2;
    while (i != 0 && j!= 0) {
        s1 = score[i][j-1] - 5;
        s2 = score[i-1][j] - 5;
        
        indx1 = amino_index(str1[i]);
        indx2 = amino_index(str2[i]);
        s3 = score[i-1][j-1] + BLOSUM62[indx1][indx2];

        if (score[i][j] == s1) {
            os1[os1_i++] = '-';
            os2[os2_i++] = str2[j-1];
            j--;
        } else if (score[i][j] == s2) {
            os1[os1_i++] = str1[i-1];
            os2[os2_i++] = '-';
            i--;
        } else {
            os1[os1_i++] = str1[i-1];
            os2[os2_i++] = str2[j-1];
            i--;
            j--;
        }
    }
    
    if (i != 0) {
        while (i != 0) {
            os1[os1_i++] = str1[i-1];
            os2[os2_i++] = '-';
            i--;
        }
    } else if (j != 0) {
        while (j != 0) {
            os1[os1_i++] = '-';
            os2[os2_i++] = str2[j-1];
            j--;
        }
    }
    
    for (i = os1_i-1; i > -1; i--) {
        printf("%c", os1[i]);
    }
    printf("\n");
    for (i = os2_i-1; i > -1; i--) {
        printf("%c", os2[i]);
    }
    printf("\n");
    
    return 0;
}