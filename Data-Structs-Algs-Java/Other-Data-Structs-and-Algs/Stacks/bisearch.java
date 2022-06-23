package student_classes;

import java.util.Arrays;

public class bisearch {
	public static void main(String[] args) {
		int[] arr = {12, 19, 22, 23, 25, 28, 31, 42, 44, 59, 60, 76, 84, 87, 90};
		for (int i = 0; i < 5; i++) {
			int index = (int)(Math.random()*15);
			System.out.print("find " + arr[index] + "(" + index +"): ");
			System.out.println(bisearch(arr[index], 0, arr.length, arr));
		}
	}
	public static int bisearch(int target, int start, int end,int[] nums) {
		if (end < start) {
			return -1;
		} else {
			int mid = (start + end) / 2;
			if (nums[mid] > target) {
				return bisearch(target, start, mid - 1, nums);
			} else if (nums[mid] < target) {
				return bisearch(target, mid + 1, end, nums);
			} else {
				return mid;
			}
		}
	}
}
