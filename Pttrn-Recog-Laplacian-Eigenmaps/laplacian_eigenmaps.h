#ifndef __PR_With_Laplacian_Eigenmaps__llaplacian_eigenmaps__
#define __PR_With_Laplacian_Eigenmaps__laplacian_eigenmaps__

#define HAVE_INLINE
#define SY 1
#define NS 0

#include <gsl/gsl_matrix.h>
typedef struct {
    weights W;
    int M;
    int d;
    int n;
    int max_itr;
    double thrshd;
} dimredparam;

void set_dimredparam(weights W, int M, int d, int n, int max_itr, double thrshd);
void wfunct(double *x, int n_x, double *y, int n_y);
void vadd(double *x, double *y, int n);
void vsub(double *x, double *y, int n);
void vsqadd(double *x, double *y, int n);
void vsqsub(double *x, double *y, int n);
void vscale(double *x, int n_x, double c);
double vnorm2(double *x, int n_x);
void vnormalise2(double *x, int n);
void vmemcpy(double *dst, double *src, int n);
double get_eigenvalue(const double *dinvsqrt, double *x, int n);
void laplacian_eigenmaps(gsl_matrix *Y);

#endif
