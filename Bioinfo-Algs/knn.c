#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include "knn.h"

double vnorm2(double *x, int n) {
    double norm;
    int i;
    
    for (i = 0; i < n; i++) {
        norm += x[i] * x[i];
    }
    norm = sqrt(norm);
    
    return norm;
}

double distance(double *pt1, double *pt2, int n) {
    double *diff, norm2;
    int i;
    
    diff = malloc(sizeof(double) * n);
    for (i = 0; i < n; i++) {
        diff[i] = pt1[i] - pt2[i];
        
    }
    norm2 = vnorm2(diff, n);
    
    free(diff);
    return norm2;
}

int main(int argc, const char * argv[]) {
    double **data, ***cluster;
    double **means, **means_pl;
    double min_dist, dist;
    int *count;
    int k, m;
    int len, i, j, p, q;
    int min_clus;
    int change;
    int eof;

    scanf("%d %d", &k, &m);
    data = malloc(sizeof(double *) * 2000);
    for (i = 0; i < 2000; i++) {
        data[i] = malloc(sizeof(double) * m);
    }
    len = 0;
    eof = 0;
    for (i = 0; i < 2000; i++) {

        for (j = 0; j < m; j++) {
            if (scanf("%lf", &data[i][j]) != 1) {
                eof = 1;
                break;
            }
        }
        if (eof) {
            len = i;
            break;
        }
    }
    
    means = malloc(sizeof(double *) * k);
    for (i = 0; i < k; i++) {
        means[i] = malloc(sizeof(double) * m);
    }
    for (i = 0; i < k; i++) {
        for (j = 0; j < m; j++) {
            means[i][j] = data[i][j];
        }
    }
    
    
    cluster = malloc(sizeof(double **) * k);
    for (i = 0; i < k; i++) {
        cluster[i] = malloc(sizeof(double *) * 2000);
    }
    count = calloc(k, sizeof(int));
    means_pl = malloc(sizeof(double *) * k);
    for (i = 0; i < k; i++) {
        means_pl[i] = malloc(sizeof(double) * m);
    }
    for (i = 0; i < 10000; i++) {
        //assign data to means
        for (j = 0; j < len; j++) {
            min_clus = 0;
            min_dist = distance(data[j], means[0], m);
            for (p = 1; p < k; p++) {
                dist = distance(data[j], means[p], m);
                
                if (dist < min_dist) {
                    min_clus = p;
                    min_dist = dist;
                }
            }
            
            cluster[min_clus][count[min_clus]++] = data[j];
            
            
        }
        
        //record current means
        for (j = 0; j < k; j++) {
            for (p = 0; p < m; p++) {
                means_pl[j][p] = means[j][p];
            }
            
        }
        
        //update means
        for (j = 0; j < k; j++) {
            for (p = 0; p < m; p++) {
                means[j][p] = 0;
            }
        }
        for (j = 0; j < k; j++) {
            for (p = 0; p < count[j]; p++) {
                for (q = 0; q < m; q++) {
                    means[j][q] += cluster[j][p][q]; 
                }
            }
            for (q = 0; q < m; q++) {
                means[j][q] /= count[j]; 
            }
        }
        
        for (j = 0; j < k; j++) {
            count[j] = 0;
        } 
    }
    
    for (i = 0; i < k; i++) {
        for (j = 0; j < m; j++) {
            printf("%lf ", means[i][j]);
        }
        printf("\n");
    }
    return 0;
}