#ifndef __PR_With_Laplacian_Eigenmaps__cattering_transform__
#define __PR_With_Laplacian_Eigenmaps__scattering_transform__

typedef struct {
    int irow, icol;
    int dh, dw;
} stparam;

typedef struct {
    double *mat;
    int row;
    int col;
} io, *Pio;

void set_param(stparam param);
void free_space();
void linear_filter(Pio x, char *h_file_name, Pio y);
void non_linearity(Pio y);
int scattering_transform(double *image, double **transform);

#endif
