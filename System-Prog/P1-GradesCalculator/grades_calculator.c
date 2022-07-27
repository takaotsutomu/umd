#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define MAX_SIZE_INFO_ASSIGNMENT 200
#define NUM_INFO_ENTRIES 4

/*
 * compare_by_assignments compares the assignments by assignment
 * numbers. It returns 1 if the first assignment's assignment
 * number is less than the second one's and returns 0 otherwise.
 */
static int compare_by_assignments(int *info_1, int *info_2) {
    return *info_1 < *info_2 ? 1 : 0;
}

/*
 * compare_by_grades compares the assignments by assignment
 * grades. It returns 1 if the first assignment's assignment
 * grade is less than the second one's or if the assignment
 * grades are the same but the first assignment's assignment 
 * number is greater than the second one's and returns 0 otherwise.
 */
static int compare_by_grades(int *info_1, int *info_2) {
    return (double)*(info_1 + 1) * *(info_1 + 2) / 100 >
           (double)*(info_2 + 1) * *(info_2 + 2) / 100 ||
          ((double)*(info_1 + 1) * *(info_1 + 2) / 100 ==
           (double)*(info_2 + 1) * *(info_2 + 2) / 100 &&
           !compare_by_assignments(info_1, info_2)) ? 1 : 0;
}

/*
 * swap_info_assignments swaps the information of assignments 
 * in the array storing the information.
 */
static void swap_info_assignemnts(int *info_1, int *info_2) {
    int *start_entry = info_1, temp;
    
    for (;info_1 < start_entry + NUM_INFO_ENTRIES; info_1++, info_2++) {
        temp = *info_1;
        *info_1 = *info_2;
        *info_2 = temp;
    }
}

/*
 * sort_info_assignments sorts the information of assignments
 * in the array storing the information using insertion sort.
 */
static void sort_info_assignments(int *info_assignments,
                                  int size_info_assignemts,
                                  int (*compare_by)(int *info_1, int *info_2)) {
    int *cur_info = info_assignments, *tracked_info;
    
    for (cur_info += 4;
         cur_info < info_assignments + size_info_assignemts;
         cur_info += 4) {
        for (tracked_info = cur_info;
             tracked_info > info_assignments + 3 && (*compare_by)(tracked_info, tracked_info - 4);
             tracked_info -= 4) {
            swap_info_assignemnts(tracked_info, tracked_info - 4);
        }
    }
}

/*
 * check_weight_validity checks if the weight information in 
 * assignment information provided is valid i.e. the sum of
 * the weights is 100.
 */
static int check_weights_validity(int *info_assignments,
                                  int size_info_assignemts) {
    int *cur_info = info_assignments, sum = 0;

    for (cur_info += 2;
         cur_info < info_assignments + size_info_assignemts;
         cur_info += 4) {
        sum += *cur_info;
    }
    return sum == 100 ? 1 : 0;
}

/*
 * compute_numerical_score computes the numerical score as 
 * specified in project1 description and store the result in
 * *numerical_score.
 */
static void compute_numerical_score(double *numerical_score, int num_assignments_to_drop,
                                    int points_penalty_per_day, int *info_assignments,
                                    int size_info_assignments) {
    int *cur_info = info_assignments;
    double grade_weighted, remain_weight = 0;
    
    for (; cur_info < info_assignments + size_info_assignments - 4 * num_assignments_to_drop;
         cur_info += 4) {
        *numerical_score += (grade_weighted = (double)(*(cur_info + 1) -
                             *(cur_info + 3) * points_penalty_per_day)
                             * *(cur_info + 2) / 100) > 0? grade_weighted : 0;
        remain_weight += (double)*(cur_info + 2) / 100;
    }
    *numerical_score /= remain_weight;
}

/*
 * compute_mean_std computes the mean and standard deviation 
 * as specified in project1 description and store the results 
 * in *mean and *std.
 */
static void compute_mean_std(double *mean, double *std, int *info_assignments,
                             int num_assignments, int points_penalty_per_day) {
    int i;
    
    for (i = 0; i < num_assignments; i++) {
        *mean += *(info_assignments + 4 * i + 1) -
                 *(info_assignments + 4 * i + 3)
                 * points_penalty_per_day;
    }
    *mean /= num_assignments;
    for (i = 0; i < num_assignments; i++) {
        *std += pow(*(info_assignments + 4 * i + 1) -
                    *(info_assignments + 4 * i + 3) *
                    points_penalty_per_day - *mean, 2);
    }
    *std = sqrt(*std / num_assignments);
}

int main() {
    int points_penalty_per_day, num_assignments_to_drop;
    int stats_generation, num_assignments;
    int info_assignments[MAX_SIZE_INFO_ASSIGNMENT];
    int size_info_assignments, i;
    double numerical_score = 0, mean = 0, std = 0;
    /* get data */
    scanf("%d%d", &points_penalty_per_day, &num_assignments_to_drop);
    while ((stats_generation = getchar()) == ' ')
        ;
    stats_generation = (stats_generation == 'Y' ||
                        stats_generation == 'y') ? 1 : 0;
    scanf("%d", &num_assignments);
    size_info_assignments = num_assignments * NUM_INFO_ENTRIES;
    for (i = 0; i < size_info_assignments; i++) {
        if (i % 4 != 3) {
            scanf("%d, ", info_assignments + i);
        } else {
            scanf("%d", info_assignments + i);
        }
    }
    if (!check_weights_validity(info_assignments, size_info_assignments)) {
        printf("ERROR: Invalid values provided.");
        return 1;
    }
    /* sort and compute */
    sort_info_assignments(info_assignments, size_info_assignments, compare_by_grades);
    compute_numerical_score(&numerical_score, num_assignments_to_drop, points_penalty_per_day,
                            info_assignments, size_info_assignments);
    sort_info_assignments(info_assignments, size_info_assignments, compare_by_assignments);
    if (stats_generation) {
        compute_mean_std(&mean, &std, info_assignments, num_assignments, points_penalty_per_day);
    }
    /* print results */
    printf("Numeric Score: %5.4f\n", numerical_score);
    printf("Points Penalty Per Day Late: %d\n", points_penalty_per_day);
    printf("Number of Assignments Dropped: %d\n", num_assignments_to_drop);
    printf("Values Provided:\nAssignment, Score, Weight, Days Late\n");
    for (i = 0; i < size_info_assignments; i++) {
        if (i % 4 != 3) {
            printf("%d, ", info_assignments[i]);
        } else {
            printf("%d\n", info_assignments[i]);
        }
    }
    if (stats_generation) {
        printf("Mean: %5.4f, Standard Deviation: %5.4f\n", mean, std);
    }
    return 0;
}