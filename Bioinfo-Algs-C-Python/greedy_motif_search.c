
#include "greedy_motif_search.h"
#include <stdio.h>
#include <stdlib.h>


int most_profible_kmer(char *text, int n, double **profile, int k);
int score(char **motifs, int k, int t);

int main(int argc, const char * argv[]) {
    char **best_motifs, **motifs;
    char **dna, *pnt;
    int k, t;
    int n, ca, cc, cg, ct;
    int score1, score2;
    int mpm_i, i, j, p, q;
    double **profile;
    
    scanf("%d%d\n", &k, &t);
    
    dna = malloc(sizeof(char *) * t);
    for (i = 0; i < t; i++) {
        dna[i] = malloc(1000);
        gets(dna[i]);
    }
    
    pnt = dna[0];
    n = 0;
    while (*pnt++) {
        n++;
    }
    
    best_motifs = malloc(sizeof(char *) * t);
    motifs = malloc(sizeof(char *) * t);
    for (i = 0; i < t; i++) {
        best_motifs[i] = malloc(k);
        motifs[i] = malloc(k);
    }
    
    for (i = 0; i < t; i++) {
        for (j = 0; j < k; j++) {
            best_motifs[i][j] = dna[i][j];
        }
    }
    
    profile = malloc(sizeof(double *) * k);
    for (i = 0; i < k; i++) {
        profile[i] = malloc(sizeof(double) * 4);
    }

    
    for (i = 0; i < n-k+1; i++) {
        for (j = 0; j < k; j++) {
            motifs[0][j] = dna[0][i+j];
        }
        for (j = 1; j < t; j++) { // for 1..t-1 motifs
            for (p = 0; p < k; p++) { // make profile
                ca = cc = cg = ct = 0;
                for (q = 0; q < j; q++) { // based on 0...j-1 motifs
                    switch (motifs[q][p]) {
                        case 'A':
                            ca++;
                            break;
                        
                        case 'C':
                            cc++;
                            break;
                        
                        case 'G':
                            cg++;
                            break;
                            
                        default:
                            ct++;
                            break;
                    }
                }
                
                profile[p][0] = ca;
                profile[p][1] = cc;
                profile[p][2] = cg;
                profile[p][3] = ct;
            }
            
            mpm_i = most_profible_kmer(dna[j], n, profile, k);
            for (p = 0; p < k; p++) {
                motifs[j][p] = dna[j][mpm_i+p];
            }
        }
        score1 = score(best_motifs, k, t);
        score2 = score(motifs, k, t);
        if (score2 < score1) {
            for (p = 0; p < t; p++) {
                for (q = 0; q < k; q++) {
                    best_motifs[p][q] = motifs[p][q];
                }
            }
        }
    }
    
    for (i = 0; i < t; i++) {
        for (j = 0; j < k; j++) {
            printf("%c", best_motifs[i][j]);
        }
        printf("\n");
    }
    
    return 0;
}

int most_profible_kmer(char *text, int n, double **profile, int k) {
    double *prob, p_val, max_pval;
    int max_ind, i, j;
    

    prob = malloc(sizeof(double) * (n-k+1));
    for (i = 0; i < n-k+1; i++) {
        p_val = 1;
        for (j = 0; j < k; j++) {
            switch (text[i+j]) {
                case 'A':
                    p_val *= profile[j][0];
                    break;
                    
                case 'C':
                    p_val *= profile[j][1];
                    break;
                    
                case 'G':
                    p_val *= profile[j][2];
                    break;
                    
                default:
                    p_val *= profile[j][3];
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
    
    free(prob);
    return max_ind;
}

int score(char **motifs, int k, int t) {
    int cnt[4];
    int max_c, score;
    int i, j, p;
    
    score = 0;
    for (i = 0; i < k; i++) {
        for (j = 0; j < 4; j++) {
            cnt[j] = 0;
        }
        for (j = 0; j < t; j++) {
            switch (motifs[j][i]) {
                case 'A':
                    cnt[0]++;
                    break;
                
                case 'C':
                    cnt[1]++;
                    break;
                
                case 'G':
                    cnt[2]++;
                    break;
                    
                default:
                    cnt[3]++;
                    break;
            }
        }
        max_c = cnt[0];
        for (p = 1; p < 4; p++) {
            if (cnt[p] > max_c) {
                max_c = cnt[p];
            }
        }
        score += t - max_c;
    }
    
    return score;
}