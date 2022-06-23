package student_classes;

import java.util.Stack;

/*
 * You must implement the following methods using Java's Stack object to 
 * replace the iteration/recursion in the original functions with a series
 * of stack operations, including push, pop, peek and isEmpty. Use no other
 * special library functions or classes; in other words, your code should use
 * standard arithmetic operators and in the case of the reverse function, only
 * push and pop and the default constructor for whichever Java collection
 * class you choose to represent lists.
 */

public class CommonFunctions {
	/**
	 * Computes the factorial function (using a stack to remove the recursion): 
	 * that is, the factorial(n) is n ∗ (n - 1)(n-2)∗... 1.
	 * @param n 
	 * @return n factorial
	 */
	public static int factorial( int n ) {
		Stack<Integer> stack = new Stack<Integer>();
		for (int i = n; i > 0; i--) {
			stack.push(i);
		}
		int product = 1;
		while (!stack.isEmpty()) {
			product *= stack.pop();
		}
		
		return product;
	}
	/**
	 * Computes the fibonacci function that you implemented in a previous 
	 * lab (Basic Recurrences) by using a stack to remove the recursion.
	 * @param n
	 * @return nth fibonacci number
	 */
	public static int fibonacci( int n ) {
		Stack<Integer> stack = new Stack<Integer>();
		for (int i = n; i > 0; i--) {
			stack.push(i);
		}
		int odd = 0, even = 0;
		while (!stack.isEmpty()) {
			int ord = stack.pop();
			if (ord == 1) {
				odd = 1;
			} else if (ord == 2) {
				even = 1;
			} else if (ord % 2 != 0){
				odd += even;
			} else {
				even += odd;
			}
		}
		if (n % 2 != 0) {
			return odd;
		} else {
			return even;
		}
		
		/*
		int targetFib = 0;
		Stack<Integer> stack = new Stack<Integer>();
		stack.push(n);
		while (!stack.isEmpty()) {
			int curFib = stack.pop();
			if (curFib == 1) {
				targetFib++;
			} else if (curFib == 2) {
				targetFib++;
			} else {
				stack.push(curFib - 1);
				stack.push(curFib - 2);
			}
		}
		
		return targetFib;
		*/
		
	}
	/**
	 * Given a non-empty stack, returns the smallest item contained within. 
	 * @param theStack
	 * @return the smallest item contained within the stack
	 */
	public static <T extends Comparable< T> > T min( Stack< T > theStack ) {
		Stack<T> temp = new Stack<T>();
		
		T min = theStack.pop();
		temp.push(min);
		while (!theStack.isEmpty()) {
			T ele = theStack.pop();
			if (ele.compareTo(min) < 0) {
				min = ele;
			}
			temp.push(ele);
		}
		while (!temp.isEmpty()) {
			theStack.push(temp.pop());
		}
		
		return min;
	}
	/**
	 * Given an infix expression containing only the basic arithmetic operators, 
	 * symbols, and open and closed parentheses, use a stack-based algorithm that 
	 * returns true only when the parentheses in the expression "balance," 
	 * i.e., when each left parenthesis is paired with a right parenthesis.
	 * @param text
	 * @return if the expression is balanced
	 */
	public static boolean isBalanced( String text ) {
		Stack<Character> stack = new Stack<Character>();
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == '(') {
				stack.push('(');
			} else if (text.charAt(i) == ')') {
				if (stack.isEmpty()) {
					return false;
				} else {
					stack.pop();
				}
			}
		}
		if (stack.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Determine if a string is palindrome.
	 * @param str
	 * @return if the string is palindrome
	 */
	public static boolean isPalindrome( String str ) {
		StringBuilder sb = new StringBuilder();
		String[] split = str.split(" ");
		for (int i = 0; i < split.length; i++) {
			sb.append(split[i]);
		}
		
		Stack<Character> stack = new Stack<Character>();
		for (int i = 0; i < sb.length()/2; i++) {
			stack.push(sb.charAt(i));
		}
		if (sb.length() % 2 != 0) {
			int i = sb.length()/2 + 1;
			while (!stack.isEmpty()) {
				if (sb.charAt(i++) != stack.pop()) {
					return false;
				}
			}
			return true;
		} else {
			int i = sb.length()/2;
			while (!stack.isEmpty()) {
				if (sb.charAt(i++) != stack.pop()) {
					return false;
				}
			}
			return true;
		}
	}
}
