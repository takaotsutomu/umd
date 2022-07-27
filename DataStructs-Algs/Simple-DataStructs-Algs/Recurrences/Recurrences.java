package student_classes;
/**
 * Please review the documentation for this lab before beginning.
 * 
 * Please make sure that
 * <ul>
 * <li>You document each method: identify its pre- and post-conditions, and include
 * documentation within the method where you identify the base case and explain (briefly)
 * how the "reductive step(s)" move the problem towards the base case.</li>
 * <li><b>Do not use</b> any Java constructions that we haven't reviewed in class, unless
 * explicitly allowed to do so in the method.</li>
 * <li>No Java iteration statements nor any use of any Iterator is required or 
 * permitted in this lab.</li>
 * <li>You should require <b>no imports</b> from Java's <code>Collection</code> package.</li>
 * </ul>
 * @author UMD CS Department.
 *
 */
public class Recurrences {
	/**
	 * Returns <code>true</code> iff <code>n</code> is a prime integer, which
	 * in this case means a positive integer > 1 that is divisible only by itself
	 * and 1.
	 * @param n (a non-negative integer)
	 * @return true if n is prime and false otherwise
	 */
	public static boolean isPrime( int n ) {
		return !checkDivisible(n, n / 2);
	}
	
	private static boolean checkDivisible (int n, int divisor) {
		if (divisor == 1) {
			return false;
		} else if (n % divisor == 0) {
			return true;
		} else {
			return checkDivisible (n, divisor - 1);
		}
	}
	/**
	 * Recursively implements the classic Fibonacci function. Note,
	 * this implementation requires the fibonacci ( 0 ) = 0.
	 * @param n (a non-negative integer)
	 * @return the nth Fibonacci number
	 */
	public static int fibonacci( int n ) {
		if (n == 0) {
			return 0;
		} else if (n == 1) {
			return 1;
		} else {
			return fibonacci(n - 1) + fibonacci (n - 2);
		}
	}
	/**
	 * Implements the basic laws of positive exponents over the integers.
	 * Note: anything to the 0-power is 1.
	 * @param base (a non-negative integer)
	 * @param exponent (a non-negative integer)
	 * @return the power of the base with the exponent
	 */
	public static int power( int base, int exponent ) {
		if (exponent == 0) {
			return 1;
		} else {
			return base * power(base, exponent -1);
		}
	}
	/**
	 * pmod stands for pseudo-mod. The real modulus function
	 * needs to deal with negative integers, but this one assumes
	 * positive integers only. The modulus operator, from our
	 * perspective, looks just like the % (remainder) operator
	 * in Java---except that you're implementing it recursively,
	 * and you will use it to implement the gcd function, which
	 * is defined immediately after this one.
	 * 
	 */
	public static int pmod( int a, int modulus ) {
		if (a < modulus) {
			return a;
		} else {
			return pmod(a - modulus, modulus);
		}
	}
	/**
	 * uses the Euclidean algorithm to recursively compute the greatest
	 * common divisor of two positive integers, m and n. Note, in order
	 * to receive credit for this function, your implementation 
	 * <em>must</em> use the <code>pmod</code> function implemented 
	 * directly above. (Rather than give you this, you'll need to look
	 * it up!)
	 * @param m 
	 * @param n
	 * @return the greatest common divisor of m and n
	 */
	public static int gcd( int m, int n ) {
		if (m == 0) {
			return n;
		} else if (n == 0) {
			return m;
		} else {
			return gcd(n, pmod(m, n));
		}
	}
	/**
	 * Recursively compute (approximate) the square root of a positive
	 * double number by successive averaging. Hint: to do this, define
	 * several private methods:
	 * <ul>
	 *  <li><code>closeEnough(double x, double guess, double tolerance)</code>: returns
	 *  <code>true</code> when the absolute value of the difference of 
	 *  <code>guess * guess</code> and <code> is less than the 
	 *  <code>tolerance</code>. 
	 *  In other words, close enough is true when: \f[ \lvert guess^2 - x \rvert < tolerance\f].</li>
	 *  <li><code>refine(double x, double guess)</code> returns the average of
	 *  the <code>guess + (x/guess)</code>. 
	 *  In other words, refine computes \f[ \dfrac{guess + \dfrac{x}{guess}}{2}\f].</li>
	 *  <li><code>sqrt_aux( double x, double guess, double tolerance )</code>:
	 *  that recursively <code>refine</code>s the initial guess (which is 1.0),
	 *  testing against the original number, <code>x</code>, until <code>closeEnough</code>
	 *  is true. At this point, the refined value is returned.
	 * </ul>
	 * @param x
	 * @param tolerance
	 * @return the approximation of the square root of x with an error within tolerance
	 */
	public static double sqrt( double x, double tolerance ) {
		return sqrt_aux( x, 1.0, tolerance );
	}
	private static double sqrt_aux( double x, double guess, double tolerance ) {
		double refinedValue = refine(x, guess);
		if (closeEnough(x, refinedValue, tolerance)) {
			return refinedValue;
		} else {
			return sqrt_aux(x, refinedValue, tolerance);
		}
	}
	private static boolean closeEnough(double x, double guess, double tolerance) {
		return Math.abs(Math.pow(guess, 2) - x) < tolerance;
	}
	private static double refine(double x, double guess) {
		return (guess + x/guess)/2;
	}
	/**
	 * Uses "backtracking" (through recursion) to 
	 * determine if the "target" (a positive integer) can be computed as
	 * the result of summing any combination of
	 * integers in the <code>arrayOfIntegers</code>.
	 * Note: make no assumptions about what is included in
	 * the array of integer, except that each integer in the array  
	 * may be used only once.
	 * <P>
	 * For example: summable( 10, {1,2,3,4,5}) is true,
	 * but summable( 20, {1,2,3,4,5}) is not.
	 * </P>
	 * 
	 * <P>By way of a hint: you will most certainly want to define
	 * an "auxiliary" method (private static) that manages the array indexing for you.
	 * @param target
	 * @param arrayOfIntegers
	 * @return true if there is at least one combination of the elements in the array
	 * match the target; false otherwise
	 */
	
	public static boolean summable( int target, int[] arrayOfIntegers ) {
		return checkSummable(target, 0, arrayOfIntegers);
	}
	private static boolean checkSummable(int target, int cur, int[] arrayOfIntegers) {
		if (target == 0) {
			return true;
		}
		if (target < 0) {
			return false;
		}
		if (cur == arrayOfIntegers.length) {
			return false;
		}
		if (checkSummable(target - arrayOfIntegers[cur] , cur + 1, arrayOfIntegers)) {
			return true;
		}
		if (checkSummable(target, cur + 1, arrayOfIntegers)) {
			return true;
		}
	
		return false;
	}
	
	private static boolean summable_helper(int target, int start, int[] array) {
		if (start == array.length) {
			return (target == 0);
		} else if (summable_helper(target - array[start], start + 1, array)) {
			return true;
		} else if (summable_helper(target, start + 1, array)) {
			return true;
		} else {
			return false;
		}
	}
}
