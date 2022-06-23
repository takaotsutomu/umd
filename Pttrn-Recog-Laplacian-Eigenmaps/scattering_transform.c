#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include "main.h"
#include "scattering_transform.h"
#include "laplacian_eigenmaps.h"

static int inum_row, inum_col; // number of rows/columns of an image
static int d_h, d_w; // down sampling factors
static int num_lay; // number of layers
static int num_hipass_per_br; // number of high pass per branch
static char ***hipass_filter, *lopass_filter; // filter file name for each high passes
                                              // and filter file name for low passes
/************************************************************/
void set_param(stparam param) {
    char no_str[10];
    int num_hipass, i, j;
    
    inum_row = param.irow;
    inum_col = param.icol;
    d_h = param.dh;
    d_w = param.dw;
    num_lay = 3;
    num_hipass_per_br = 3;
    
    hipass_filter = malloc(sizeof(char **) * num_lay);
    num_hipass = num_hipass_per_br;
    for (i = 0; i < num_lay; i++) {
        hipass_filter[i] = malloc(sizeof(char *) * num_hipass);
        for (j = 0; j < num_hipass; j++) {
            hipass_filter[i][j] = malloc(24);
            strcpy(hipass_filter[i][j], "./Filters/hp_filter"); //hp_filter1.33+'\0'
            sprintf(no_str, "%d.%d", (i+1), (j+1));
            strcat(hipass_filter[i][j], no_str);
        }
        num_hipass *= num_hipass_per_br;
    }
    lopass_filter = malloc(20);
    strcpy(lopass_filter, "./Filters/lp_filter");
    //set the file names of the filters
}
/************************************************************/

/************************************************************/
void free_space() {
    int num_hipass, i, j;
    
    num_hipass = num_hipass_per_br;
    for (i = 0; i < num_lay; i++) {
        for (j = 0; j < num_hipass; j++) {
            free(hipass_filter[i][j]);
        }
        free(hipass_filter[i]);
        num_hipass *= num_hipass_per_br;
    }
    free(hipass_filter);
    free(lopass_filter);
}
/************************************************************/

/************************************************************/
void linear_filter(Pio x, char *h_file_name, Pio y) {
    FILE *h_file;
    double *a, *b;
    int hnum_row, hnum_col;
    int y_indx, x_indx1, x_indx2;
    int i, j, l, p;
    
    if (!(h_file = fopen(h_file_name, "r"))) {
        fprintf(stderr, "cannot open \"%s\"", h_file_name);
        return;
    }
    
    fscanf(h_file, "%d%d", &hnum_row, &hnum_col);
    a = malloc(sizeof(double) * hnum_row);
    for (i = 0; i < hnum_row; i++) {
        fscanf(h_file, "%lf", a+i);
    }
    b = malloc(sizeof(double) * hnum_col);
    for (i = 0; i < hnum_col; i++) {
        fscanf(h_file, "%lf", b+i);
    }
    
    y_indx = 0;
    for (i = 0; i < x->row; i += d_h) {
        for (j = 0; j < x->col; j += d_w) {
            y->mat[y_indx] = 0;
            for (l = 0; l < hnum_row; l++) {
                for (p = 0 ; p < hnum_col; p++) {
                    x_indx1 = i + l;
                    x_indx2 = j + p;
                    
                    if (x_indx1 >= x->row) {
                        x_indx1 %= x->row;
                        x_indx1 = x->row - 1 - x_indx1;
                    }
                    
                    if (x_indx2 >= x->col) {
                        x_indx2 %= x->col;
                        x_indx2 = x->col - 1 - x_indx2;
                    }
                    
                    x_indx1 = x_indx1 * x->col + x_indx2;
                    y->mat[y_indx] += a[l] * b[p] * x->mat[x_indx1];
                }
            }
            y_indx++;
            
            if (y->row == 0) {
                y->col++;
            }
        }
        y->row++;
    }
    
    fclose(h_file);
}
/************************************************************/

/************************************************************/
void non_linearity(Pio y) {
    int size = y->row * y->col;
    
    for (int i = 0; i < size; i++) {
        y->mat[i] = fabs(y->mat[i]);
    }
}
/************************************************************/

/************************************************************/
int scattering_transform(double *image, double **transform) {
    io x, y;
    double ***tensor_lo, ***tensor_hi; //two tensors
    int *num_lopass, *num_hipass, *o_row, *o_col;
    int t_size, t_pos, parent_indx;
    int size, i, j;
        
    /* prepare data and data structures needed */
    tensor_lo = malloc(sizeof(double **) * num_lay);
    tensor_hi = malloc(sizeof(double **) * num_lay);
    num_lopass = malloc(sizeof(int) * num_lay);
    num_hipass = malloc(sizeof(int) * num_lay);
    
    num_lopass[0] = 1;
    num_hipass[0] = num_hipass_per_br;
    for (i = 0; i < num_lay; i++) {
        tensor_lo[i] = malloc(sizeof(double *) * num_lopass[i]);
        for (j = 0; j < num_lopass[i]; j++) {
            tensor_lo[i][j] = malloc(sizeof(double) * (inum_col*inum_row));
        }
        tensor_hi[i] = malloc(sizeof(double *) * num_hipass[i]);
        for (j = 0; j < num_hipass[i]; j++) {
            tensor_hi[i][j] = malloc(sizeof(double) * (inum_col*inum_row));
        }
        
        if (i < num_lay-1) {
            num_lopass[i+1] = num_lopass[i] * num_hipass_per_br;
            num_hipass[i+1] = num_hipass[i] * num_hipass_per_br;
        }
    }
    
    o_row = calloc(num_lay, sizeof(int));
    o_col = calloc(num_lay, sizeof(int));
    
    /* first layer */
    x.mat = image;
    x.row = inum_row;
    x.col = inum_col;
    y.mat = tensor_lo[0][0];
    y.row = 0;
    y.col = 0;
    linear_filter(&x, lopass_filter, &y);
    o_col[0] = y.col;
    o_row[0] = y.row;
    
    for (j = 0; j < num_hipass[0]; j++) { //high pass
        y.mat = tensor_hi[0][j];
        y.row = 0;
        y.col = 0;
        linear_filter(&x, hipass_filter[0][j], &y);
        non_linearity(&y);
    }
    
    /* the rest of the layers */
    for (i = 1; i < num_lay; i++) {
        for (j = 0; j < num_lopass[i]; j++) { // low pass
            parent_indx = j/num_hipass_per_br;
            x.mat = tensor_lo[i-1][parent_indx];
            x.row = o_row[i-1];
            x.col = o_col[i-1];
            y.mat = tensor_lo[i][j];
            y.row = 0;
            y.col = 0;
            linear_filter(&x, lopass_filter, &y);
            
            if (!o_col[i] || !o_row[i]) {
                o_col[i] = y.col;
                o_row[i] = y.row;
            }
        }
        
        for (j = 0; j < num_hipass[i]; j++) { // high pass
            parent_indx = j/num_hipass_per_br;
            x.mat = tensor_hi[i-1][parent_indx];
            x.row = o_row[i-1];
            x.col = o_col[i-1];
            y.mat = tensor_hi[i][j];
            y.row = 0;
            y.col = 0;
            linear_filter(&x, hipass_filter[i][j], &y);
            non_linearity(&y);
        }
    }
    
    /* write the output */
    t_size = 0;
    for (i = 0; i < num_lay; i++) {
        t_size += o_row[i] * o_col[i] * num_lopass[i];
    }
    for (i = num_lay-1; i < num_lay; i++) {
        t_size += o_row[i] * o_col[i] * num_hipass[i];
    }
    
    if (!(*transform = malloc(sizeof(double) * t_size))) {
        fprintf(stderr, "transform memory not allocated");
    }
    
    t_pos = 0;
    for (i = 0; i < num_lay; i++) {
        size = o_row[i] * o_col[i];
        for (j = 0; j < num_lopass[i]; j++) {
            vmemcpy(*transform + t_pos, tensor_lo[i][j], size);
            t_pos += size;
        }
    }
    for (i = num_lay-1; i < num_lay; i++) {
        size = o_row[i] * o_col[i];
        for (j = 0; j < num_hipass[i]; j++) {
            vmemcpy(*transform + t_pos, tensor_hi[i][j], size);
            t_pos += size;
        }
    }

    
    /* free things */
    for (i = 0; i < num_lay; i++) {
        for (j = 0; j < num_lopass[i]; j++) {
            free(tensor_lo[i][j]);
        }
        free(tensor_lo[i]);
        for (j = 0; j < num_hipass[i]; j++) {
            free(tensor_hi[i][j]);
        }
        free(tensor_hi[i]);
    }
    free(tensor_lo);
    free(tensor_hi);
    free(num_lopass);
    free(num_hipass);
    free(o_row);
    free(o_col);
    
    return t_size;
}
/************************************************************/