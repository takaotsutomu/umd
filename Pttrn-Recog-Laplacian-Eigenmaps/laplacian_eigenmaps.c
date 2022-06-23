#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <gsl/gsl_matrix.h>
#include <gsl/gsl_rng.h>
#include "main.h"
#include "scattering_transform.h"
#include "laplacian_eigenmaps.h"

static dimredparam drp;

/************************************************************/
/* set the max iteration and the theshold parameters for 
 approximating dominant eigen vectors */
void set_dimredparam(weights W, int M, int d, int n,
                    int max_itr, double thrshd) {
    drp.W = W;
    drp.M = M;
    drp.d = d;
    drp.n = n;
    drp.max_itr = max_itr;
    drp.thrshd = thrshd;
}
/************************************************************/

/************************************************************/
/* y = Wx */
void wfunct(double *x, int n_x, double *y, int n_y) {
    int i, j, k;
    double w;

    if (!x || !y || n_x != drp.n || n_y != drp.n) {
        fprintf(stderr, "x or y is null or has wrong size.\n");
        return;
    }
    
    for (k = 0; k < drp.n; k++) {
        y[k] = 0;
    }
    
    for (k = 0; k < drp.M; k++) {
        i = drp.W[k].row;
        j = drp.W[k].column;
        w = drp.W[k].value;
        y[i] += w * x[j];
        y[j] += w * x[i];
    }
}
/************************************************************/

/************************************************************/
/* x = x + y */
void vadd(double *x, double *y, int n) {
    int i;
    
    for (i = 0; i < n; i++) {
        x[i] += y[i];
    }
}
/************************************************************/

/************************************************************/
/* x = x - y */
void vsub(double *x, double *y, int n) {
    int i;
    
    for (i = 0; i < n; i++) {
        x[i] -= y[i];
    }
}
/************************************************************/

/************************************************************/
/* x = x + y.^2 */
void vsqadd(double *x, double *y, int n) {
    int i;
    
    for (i = 0; i < n; i++) {
        x[i] += y[i] * y[i];
    }
}
/************************************************************/

/* x = x - y.^2 */
void vsqsub(double *x, double *y, int n) {
    int i;
    
    for (i = 0; i < n; i++) {
        x[i] -= y[i] * y[i];
    }
}
/************************************************************/

/************************************************************/
/* scale the vector x with constant c */
void vscale(double *x, int n_x, double c) {
    int i;
    
    for (i = 0; i < n_x ; i++) {
        x[i] *= c;
    }
}
/************************************************************/

/************************************************************/
/* compute the Euclidean norm of vector x */
double vnorm2(double *x, int n_x) {
    double norm;
    int i;
    
    norm = 0;
    for (i = 0; i < n_x; i++) {
        norm += x[i] * x[i];
    }
    norm = sqrt(norm);
    
    return norm;
}
/************************************************************/

/************************************************************/
/* normalise the vector x with the Euclidean norm */
void vnormalise2(double *x, int n_x) {
    double norm2;
    
    norm2 = vnorm2(x, n_x);
    vscale(x, n_x, 1/norm2);
}
/************************************************************/

/************************************************************/
/* dst = src */
void vmemcpy(double *dst, double *src, int n) {
    int i = 0;
    
    for (i = 0; i < n; i++) {
        dst[i] = src[i];
    }
}
/************************************************************/

/************************************************************/
double get_eigenvalue(const double *dinvsqrt, double *x, int n) {
    int j;
    double *z, *y, eigenvalue;
    
    z = malloc(sizeof(double) * n);
    y = malloc(sizeof(double) * n);
    
    for (j = 0; j < n; j++) {
        z[j] = dinvsqrt[j] * x[j];
    }
    wfunct(z, n, y, n);
    for (j = 0; j < n; j++) {
        y[j] *= dinvsqrt[j];
    }
    vadd(y, x, n);
    
    eigenvalue = 0;
    for (j = 0; j < n; j++) {
        eigenvalue += y[j] * x[j];
    }
    
    free(z);
    free(y);
    return eigenvalue;
}
/************************************************************/

/************************************************************/
/* perform the Lapacian Eigenmaps algorithm */
void laplacian_eigenmaps(gsl_matrix *Y) {
    const gsl_rng_type *T;
    gsl_rng *r;
    double *x, *y, *z, *e;
    double *dinvsqrt, *select, norm2, inrprod, f;
    int d, n, max_itr, thrshd;
    int num_eigenv, i, j, k, l;
    
    
    /* check if Y is null */
    if (!Y) {
        return;
    }
    
    d = drp.d;
    n = drp.n;
    
    /* check if d is less than n */
    if (d > n || Y->size1 != d || Y->size2 != n) {
        fprintf(stderr, "Y has wrong size.\n");
        return;
    }
    
    /* allocate space for each vector */
    x = malloc(sizeof(double) * n);
    y = malloc(sizeof(double) * n);
    z = malloc(sizeof(double) * n);
    e = malloc(sizeof(double) * n);
    dinvsqrt = malloc(sizeof(double) * n);
    select = malloc(sizeof(double) * n);
    
    /* prepare the dinvesqrt and the select matrix */
    for (i = 0; i < n; i++) {
        x[i] = 1;
    }
    wfunct(x, n, y, n);
    
    for (i = 0; i < n; i++) {
        if (!y[i]) {
            dinvsqrt[i] = 0;
            select[i] = 0;
        } else {
            dinvsqrt[i] = 1/sqrt(y[i]);
            select[i] = 1;
        }
    }
    
    /* set up the random number generator */
    gsl_rng_env_setup();
    T = gsl_rng_default;
    r = gsl_rng_alloc(T);
    max_itr = drp.max_itr;
    thrshd = drp.thrshd;
    
    /* perform power iteration for the largest eigenvector */
    for (i = 0; i < n; i++) {
        x[i] = gsl_rng_uniform(r);
    }
    vnormalise2(x, n);
    for (i = 0; i < max_itr; i++) {
        /* let y be ((2I-select)+D^(-1/2)WD^(-1/2))x and normalise it */
        for (j = 0; j < n; j++) {
            z[j] = dinvsqrt[j] * x[j];
        }
        wfunct(z, n, y, n);
        for (j = 0; j < n; j++) {
            y[j] *= dinvsqrt[j];
        }
        for (j = 0; j < n; j++) {
            z[j] = (2 - select[j]) * x[j];
        }
        vadd(y, z, n);
        vnormalise2(y, n);
        
        /* let z be the difference of y and x */
        vmemcpy(z, y, n);
        vsub(z, x, n);
        
        /* set y to be the new x */
        vmemcpy(x, y, n);
        
        if ((norm2 = vnorm2(z, n)) < thrshd) {
            break;
        }
    }
    
    /* store the eigenvector in Y(i.e. E) */
    for (i = 0; i < n; i++) {
        gsl_matrix_set(Y, 0, i, x[i]);
    }
    num_eigenv = 1;
    
    /* perform power iteration for the rest of the d+1 eigenvectors */
    for (i = 0; i < d; i++) {
        for (j = 0; j < n; j++) {
            y[j] = gsl_rng_uniform(r);
        }
        
        /* compute the component of y orthogonal to E and normalise it */
        vmemcpy(z, y, n);
        for (j = 0; j < num_eigenv; j++) {
            inrprod = 0;
            for (k = 0; k < n; k++) {
                e[k] = gsl_matrix_get(Y, j, k);
                inrprod += y[k] * e[k];
            }
            vscale(e, n, inrprod);
            vsub(z, e, n);
        }
        vnormalise2(z, n);
        vmemcpy(x, z, n);
        for (j = 0; j < max_itr; j++) {
            /* let y be ((2I-select)+D^(-1/2)WD^(-1/2))x */
            for (k = 0; k < n; k++) {
                z[k] = dinvsqrt[k] * x[k];
            }
            wfunct(z, n, y, n);
            for (k = 0; k < n; k++) {
                y[k] *= dinvsqrt[k];
            }
            for (k = 0; k < n; k++) {
                z[k] = (2 - select[k]) * x[k];
            }
            vadd(y, z, n);
            
            /* compute the component of y orthogonal to E and normalise it */
            vmemcpy(z, y, n);
            for (k = 0; k < num_eigenv; k++) {
                inrprod = 0;
                for (l = 0; l < n; l++) {
                    e[l] = gsl_matrix_get(Y, k, l);
                    inrprod += y[l] * e[l];
                }
                vscale(e, n, inrprod);
                vsub(z, e, n);
            }
            vnormalise2(z, n);
            
            
            /* let y be the difference of z and x */
            vmemcpy(y, z, n);
            vsub(y, x, n);
            
            /* set z to be the new x */
            vmemcpy(x, z, n);
            
            if ((norm2 = vnorm2(y, n)) < thrshd) {
                break;
            }
        }
        
        /* store the eigenvector in Y(i.e. E) */
        if (num_eigenv == d) {
            for (j = 0; j < n; j++) {
                gsl_matrix_set(Y, 0, j, x[j]);
            }
        } else {
            for (j = 0; j < n; j++) {
                gsl_matrix_set(Y, num_eigenv, j, x[j]);
            }
            num_eigenv++;
        }
    }
    
    /* compute Y(Y=ED^(-1/2)) */
    for (i = 0; i < d; i++) {
        for (j = 0; j < n; j++) {
            f = gsl_matrix_get(Y, i, j);
            f *= dinvsqrt[j];
            gsl_matrix_set(Y, i, j, f);
        }
    }
    
    /* free every space allocated */
    gsl_rng_free(r);
    free(x);
    free(y);
    free(z);
    free(e);
    free(dinvsqrt);
    free(select);
}
/************************************************************/