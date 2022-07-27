package tests;

import static org.junit.Assert.*;

import org.junit.Test;
/*
 * Do not change or mess with these imports unless you really know
 * what you're doing.
 */
import static student_collections.SList.*;
import student_collections.*;
import static student_classes.Sorts.*;

public class StudentTests {

	@Test
	public void test() {
		int[] values1 = {24, 2, 45, 20, 56, 75};
		int[] values2 = {2, 56, 99, 53, 12};
		SList<Integer> list1 = getList(values1, 0);
		SList<Integer> list2 = getList(values2, 0);
		SList<Integer> resultList = append(list1, list2);
		resultList = qSort(resultList);
		int[] values = getValues(resultList);
		for (int i = 0; i < values.length; i++) {
			System.out.print(values[i] + " ");
		}
	}
	
	private SList<Integer> getList(int[] values, int cur) {
		if (cur == values.length - 1) {
			return list(values[cur]);
		} else {
			return cons(values[cur], getList(values, cur + 1));
		}
	}
	
	private int[] getValues(SList<Integer> list) {
		int[] values = new int[length(list)];
		for (int i = 0; i < values.length; i++) {
			values[i] = first(list);
			list = rest(list);
		}
		return values;
	}
}
