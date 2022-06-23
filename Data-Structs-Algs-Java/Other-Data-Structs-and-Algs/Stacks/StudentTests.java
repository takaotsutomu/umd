package tests;

import static org.junit.Assert.*;

import java.util.Stack;

import org.junit.Test;

import student_classes.CommonFunctions;

public class StudentTests {

	@Test
	public void test() {
		StringBuilder sb1 = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			sb1.append(CommonFunctions.factorial(i) + " ");
		}
		assertEquals(sb1.toString(), "1 1 2 6 24 120 720 5040 40320 362880 ");
		StringBuilder sb2 = new StringBuilder();
		for (int i = 1; i < 11; i++) {
			sb2.append(CommonFunctions.fibonacci(i) + " ");
		}
		System.out.println(sb2.toString());
		assertEquals(sb2.toString(), "1 1 2 3 5 8 13 21 34 55 ");
		Stack<Integer> stack = new Stack<Integer>();
		int[] values = {44, 20, 35, 48, 29, 41, 22, 1, 28, 35};
		for (int i = 0; i < values.length; i++) {
			stack.push(values[i]);
		}
		assertEquals(CommonFunctions.min(stack).intValue(), 1);
		assertTrue(CommonFunctions.isPalindrome("abcba"));
		assertTrue(CommonFunctions.isPalindrome("abba"));
		assertTrue(CommonFunctions.isPalindrome("a man a plan a canal panama"));
		assertTrue(CommonFunctions.isBalanced(""));
		assertTrue(CommonFunctions.isBalanced("()"));
		assertTrue(CommonFunctions.isBalanced("(2+3-(3∗4))"));
		assertFalse(CommonFunctions.isBalanced(")("));
		assertFalse(CommonFunctions.isBalanced("(2+(3+4)"));
		assertFalse(CommonFunctions.isBalanced("( 2 + ( 3 + 4 ) ) )"));
		
		
		
		
	}

}
