
#ifndef __PR_With_Laplacian_Eigenmaps__main_h
#define __PR_With_Laplacian_Eigenmaps__main_h

#include <gsl/gsl_matrix.h>

typedef struct {
    int row;
    int column;
    double value;
} wts_entry, *weights;

int reverse_int(int num);
void read_MNIST(double *image, int n, FILE *mnist_file);
void count_labels(char *labels, int n, int *count);
double get_distance(double *t1, double *t2, int n);
int get_weights(FILE *t_file, int N, int n, weights W, int M);
double ble_discriminant(FILE *file, char *labels, char c1, char c2, int N, int n);
double ale_discriminant(gsl_matrix *Y, char *labels, char c1, char c2, int N, int n);

#endif
