#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <gsl/gsl_matrix.h>
#include <gsl/gsl_blas.h>
#include "main.h"
#include "scattering_transform.h"
#include "laplacian_eigenmaps.h"

int main(int argc, const char * argv[]) {
    FILE *lmnist_file, *imnist_file, *i_file, *t_file;
    FILE *t_file2, *l_file;
    int count[10];
    int lmagic_num, num_item;
    int imagic_num, num_image;
    int num_row, num_col, s1, s2, M2;
    int in1, in2;
    int i, j;
    char *labels, c1, c2;
    double *image, *transform, s;
    stparam stp;
    weights W;
    gsl_matrix *Y;
    
    double epslon;
    int dh, dw, M, d, max_iter; 
    
    if (argc < 7) {
        fprintf(stderr, "Insufficient number of parameters.\n");
        return 1;
    } else {
        dh = atoi(argv[1]);
        dw = atoi(argv[2]);
        M = atoi(argv[3]);
        d = atoi(argv[4]);
        epslon = atof(argv[5]);
        max_iter = atoi(argv[6]);
    }
    
    if (!(lmnist_file = fopen("./Data/train-labels-idx1-ubyte", "r"))) {
        fprintf(stderr, "Cannot open file \"train-labels-idx1-ubyte\".\n");
        return 1;
    }
    
    fread(&lmagic_num, 4, 1, lmnist_file);
    lmagic_num = reverse_int(lmagic_num);
    fread(&num_item, 4, 1, lmnist_file);
    num_item = reverse_int(num_item);
    
    labels = malloc(num_item);
    fread(labels, 1, num_item, lmnist_file);
        
    fclose(lmnist_file);
    
    if (!(imnist_file = fopen("./Data/train-images-idx3-ubyte", "r"))) {
        fprintf(stderr, "Cannot open file \"train-images-idx3-ubyte\".\n");
        return 1;
    }
    
    fread(&imagic_num, 4, 1, imnist_file);
    imagic_num = reverse_int(imagic_num);
    fread(&num_image, 4, 1, imnist_file);
    num_image = reverse_int(num_image);
    fread(&num_row, 4, 1, imnist_file);
    num_row = reverse_int(num_row);
    fread(&num_col, 4, 1, imnist_file);
    num_col = reverse_int(num_col);
    
    num_image = 1000; 
    
    for (i = 0; i < 10; i++) {
        count[i] = 0;
    }
    
    count_labels(labels, num_image, count);
    
    for (i = 0; i < 10; i++) {
        printf("#class %d: %d   percentage: %lf\n", i, count[i], (double)count[i]/num_image);
    }
    printf("\n");
    
    
    if (!(i_file = fopen("./images", "w+"))) {
        fprintf(stderr, "Cannot open \"images\".\n");
        return 1;
    }
    
    if (!(t_file = fopen("./image-transforms", "w+"))) {
        fprintf(stderr, "Cannot open \"image-transforms\".\n");
        return 1;
    }
        
    stp.irow = num_row;
    stp.icol = num_col;
    stp.dh = dh; 
    stp.dw = dw;
    set_param(stp);
    
    s1 = num_row * num_col;
    image = malloc(sizeof(double) * s1); 
    
    s2 = scattering_transform(image, &transform);
    free(transform);
    
    
    for (i = 0; i < num_image; i++) {
        /* read data and perform transform */
        read_MNIST(image, s1, imnist_file);
        scattering_transform(image, &transform);
        
        /* store the image and transformed immage */
        fwrite(image, sizeof(double), s1, i_file);
        fwrite(transform, sizeof(double), s2, t_file);
        
        free(transform);
    }
    
    free_space();
    
    /* compute the discriminants */
    for (i = 0; i < 9; i++) {
        for (j = i+1; j < 10; j++) {
            fseek(i_file, 0, SEEK_SET);
            fseek(t_file, 0, SEEK_SET);
            s = ble_discriminant(i_file, labels, (char)i, (char)j, num_image, s1);
            printf("Discriminant (Separation) on raw data (%d & %d): %lf\n", i, j, s);
            s = ble_discriminant(t_file, labels, (char)i, (char)j, num_image, s2);
            printf("Discriminant (Separation) on transformed data (%d & %d): %lf\n\n", i, j, s);
        }
    }
    
    fclose(imnist_file);
    fclose(i_file);
    fclose(t_file);
    fclose(t_file2);
    
    /* prepare the weight matrix */
    if (!(t_file = fopen("./image-transforms", "r"))) {
        fprintf(stderr, "Cannot open \"image-transforms\".\n");
        return 1;
    }
    
    //M = // <= N^2 /2 d = N M = ideally 10000 or more
    W = malloc(sizeof(wts_entry) * M);
    M2 = get_weights(t_file, num_image, s2, W, M);
    
    if (M2 > M) {
        fprintf(stderr, "Weight matrix size is not correct.\n");
        return -1;
    } else if (M2 < M) {
        printf("Weight matrix has been overallocated. M: %d, M2: %d\n", M, M2);
        M = M2;
    }
    
    fclose(t_file);
    
    /* Laplacian Eigenmaps */
    Y = gsl_matrix_alloc(d, num_image);
    set_dimredparam(W, M, d, num_image, max_iter, epslon);
    laplacian_eigenmaps(Y);
    
    /* compute the discriminants */
    for (i = 0; i < 9; i++) {
        for (j = i+1; j < 10; j ++) {
            s = ale_discriminant(Y, labels, (char)i, (char)j, num_image, d);
            printf("Discriminant (Separation) on reduced data (%d & %d): %lf\n", i, j, s);
        }
    }
    
    return 0;
}

/************************************************************/
int reverse_int(int num) {
    unsigned char ch1, ch2, ch3, ch4;
    
    ch1 = num & 255;
    ch2 = (num >> 8) & 255;
    ch3 = (num >> 16) & 255;
    ch4 = (num >> 24) & 255;
    
    return ((int)ch1 << 24) + ((int)ch2 << 16) + ((int)ch3 << 8) + ((int)ch4);
}
/************************************************************/

/************************************************************/
void read_MNIST(double *image, int n, FILE *mnist_file) {
    unsigned char *ch;
    int i;
    
    ch = malloc(n);
    fread(ch, 1, n, mnist_file);
    for (i = 0; i < n; i++) {
        image[i] = (double)ch[i];
    }
    
    free(ch);
}
/************************************************************/

/************************************************************/

void count_labels(char *labels, int n, int *count) {
    int i;
    
    for (i = 0; i < n; i++) {
        count[labels[i]]++;
    }
}
/************************************************************/

/************************************************************/
double get_distance(double *t1, double *t2, int n) {
    double *diff, norm2;
    int i;
    
    diff = malloc(sizeof(double) * n);
    for (i = 0; i < n; i++) {
        diff[i] = t1[i] - t2[i];
    }
    norm2 = vnorm2(diff, n);
    
    free(diff);
    return norm2;
}
/************************************************************/

/************************************************************/
int get_weights(FILE *t_file, int N, int n, weights W, int M) {
    double *t1, *t2, d, a;
    int num_entries, i, j, k;
    
    t1 = malloc(sizeof(double) * n);
    t2 = malloc(sizeof(double) * n);
    
    num_entries = 0;
    
    for (i = 0; i < N-1; i++) {
        fseek(t_file, sizeof(double)*n * i, SEEK_SET);
        fread(t1, sizeof(double), n, t_file);
        
        for (j = i+1; j < N; j++) {
            //fseek(t_file, sizeof(double)*n * j, SEEK_SET);
            fread(t2, sizeof(double), n, t_file);
            d = get_distance(t1, t2, n);
            
            if (num_entries < M) {
                k = num_entries - 1;
                while (k >= 0 && W[k].value > d) {
                    W[k+1].row = W[k].row;
                    W[k+1].column = W[k].column;
                    W[k+1].value = W[k].value;
                    k--;
                }
                W[k+1].row = i;
                W[k+1].column = j;
                W[k+1].value = d;
                num_entries++;
            } else if (W[num_entries-1].value > d) {
                k = num_entries - 2;
                while (k >= 0 && W[k].value > d) {
                    W[k+1].row = W[k].row;
                    W[k+1].column = W[k].column;
                    W[k+1].value = W[k].value;
                    k--;
                }
                W[k+1].row = i;
                W[k+1].column = j;
                W[k+1].value = d;
            }
        }
    }
    
    a = 4*(log(10.0))/W[num_entries-1].value; //to make largest weight close to 1 and
                                    //smallest nonzero weight exactly 10^(-4)
    for (i = 0; i < num_entries; i++) {
        d = W[i].value;
        W[i].value = exp(-a * d); //will set the number 'a' latter
    }
    
    free(t1);
    free(t2);
    
    return num_entries;
}
/************************************************************/

/************************************************************/
double ble_discriminant(FILE *file, char *labels,
                     char c1, char c2, int N, int n) {
    double *u, *v1, *v2, *w1, *w2;
    double *diff_mean, dist, var1, var2, s;
    int nc1, nc2, i;
    
    u = malloc(sizeof(double) * n);
    v1 = calloc(n, sizeof(double));
    v2 = calloc(n, sizeof(double));
    w1 = calloc(n, sizeof(double));
    w2 = calloc(n, sizeof(double));
    nc1 = 0;
    nc2 = 0;
    
    for (i = 0; i < N; i++) {
        fread(u, sizeof(double), n, file);
        
        if (labels[i] == c1) {
            vadd(v1, u, n);
            vsqadd(w1, u, n);
            nc1++;
        } else if (labels[i] == c2) {
            vadd(v2, u, n);
            vsqadd(w2, u, n);
            nc2++;
        }
    }
    
    if (!nc1 || !nc2) {
        s = -1;
        fprintf(stderr, "No label found c1: %c, c2: %c\n", c1+48, c2+48);
    } else {
        diff_mean = malloc(sizeof(double) * n);
        vscale(v1, n, 1/(double)nc1);
        vscale(v2, n, 1/(double)nc2);
        vmemcpy(diff_mean, v1, n);
        vsub(diff_mean, v2, n);
        dist = 0;
        for (i = 0; i < n; i++) {
            dist += diff_mean[i] * diff_mean[i];
        }
        
        vscale(w1, n, 1/(double)nc1);
        vsqsub(w1, v1, n);
        var1 = 0;
        for (i = 0; i < n; i++) {
            var1 += w1[i];
        }
        vscale(w2, n, 1/(double)nc2);
        vsqsub(w2, v2, n);
        var2 = 0;
        for (i = 0; i < n; i++) {
            var2 += w2[i];
        }
        s = dist / (var1 + var2);
        free(diff_mean);
    }
    
    free(v1);
    free(v2);
    free(w1);
    free(w2);
    return s;
}
/************************************************************/

/************************************************************/
double ale_discriminant(gsl_matrix *Y, char *labels,
                      char c1, char c2, int N, int n) {
    double *v1, *v2, *w1, *w2;
    double *diff_mean, y, dist, var1, var2, s;
    int nc1, nc2, i, j;
    
    v1 = calloc(n, sizeof(double));
    v2 = calloc(n, sizeof(double));
    w1 = calloc(n, sizeof(double));
    w2 = calloc(n, sizeof(double));
    nc1 = 0;
    nc2 = 0;
    
    for (i = 0; i < N; i++) {
        if (labels[i] == c1) {
            for (j = 0; j < n; j++) {
                y = gsl_matrix_get(Y, j, i);
                v1[j] += y;
                w1[j] += y * y;
                nc1++;
            }
        } else if (labels[i] == c2) {
            for (j = 0; j < n; j++) {
                y = gsl_matrix_get(Y, j, i);
                v2[j] += y;
                w2[j] += y * y;
                nc2++;
            }
        }
    }
    
    if (!nc1 || !nc2) {
        s = -1;
    } else {
        diff_mean = malloc(sizeof(double) * n);
        vscale(v1, n, 1/(double)nc1);
        vscale(v2, n, 1/(double)nc2);
        vmemcpy(diff_mean, v1, n);
        vsub(diff_mean, v2, n);
        dist = 0;
        for (i = 0; i < n; i++) {
            dist += diff_mean[i] * diff_mean[i];
        }
        
        vscale(w1, n, 1/(double)nc1);
        vsqsub(w1, v1, n);
        var1 = 0;
        for (i = 0; i < n; i++) {
            var1 += w1[i];
        }
        vscale(w2, n, 1/(double)nc2);
        vsqsub(w2, v2, n);
        var2 = 0;
        for (i = 0; i < n; i++) {
            var2 += w2[i];
        }
        s = dist / (var1 + var2);
        free(diff_mean);
    }
    
    free(v1);
    free(v2);
    free(w1);
    free(w2);
    return s;
}
/************************************************************/