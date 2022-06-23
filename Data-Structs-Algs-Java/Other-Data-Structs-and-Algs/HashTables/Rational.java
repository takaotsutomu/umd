package student_classes;
/**
 * A streamlined (simplified) implementation of a class
 * that represents non-negative rational numbers. Recall that 
 * a rational number is an object that contains two integers,
 * call them a and b, such that a/b and b not equal 0.
 * <br>
 * <ul>
 * <li>This implementation needs to support comparability as well
 * as the basic four arithmetic operators, +,-,* and /.</li> 
 * <li>
 * In addition, the <code>Rational</code> class must override
 * the <code>equals</code>, the <code>toString()</code>, and
 * the <code>hashCode</code> methods.</li>
 * </ul>
 * 
 * @author UMD CS Department.
 *
 */
public class Rational implements Comparable<Rational> {
	private int num;
	private int denom;
	/**
	 * The main constructor for objects of this type.
	 */
	public Rational(int num, int denom) {
		if (denom == 0) {
			throw new IllegalArgumentException();
		}
		this.num = num;
		this.denom = denom;
	}
	/**
	 * Standard copy-ctor implementation.
	 */
	public Rational(Rational rational) {
		this.num = rational.num;
		this.denom = rational.denom;
	}
	/**
	 * Implements the Comparable interface for Rational number objects.
	 * @param other other rational
	 * @return -1 if <code>other</code> is greater than <code>this</code>;
	 * 0 if <code>other</code> is equal to <code>this</code>; 1 if 
	 * <code>other</code> is less than <code>this</code>
	 */
	public int compareTo(Rational other) {
		int cThisNum = this.num * other.denom;
		int cOtherNum = other.num * this.denom;
		if (cThisNum < cOtherNum) {
			return -1;
		} else if (cThisNum == cOtherNum) {
			return 0;
		} else {
			return 1;
		}
	}
	/**
	 * Creates a new Rational object that embodies the "difference" 
	 * of this and the other rational. Because this implementation 
	 * only provides non-negative rational numbers, if the difference 
	 * between this Rational and the other Rational number is less 
	 * than zero, then a Rational number whose numerator is zero and 
	 * whose denominator is non-zero is returned; this, in effect, 
	 * is a Rational number representing zero.
	 * @param other other rational
	 * @return a new Rational object that embodies the "difference" 
	 * of this and the other rational
	 */
	public Rational diff(Rational other) {
		if (this.denom == other.denom) {
			if (this.num - other.num < 0) {
				return new Rational(0, this.denom);
			} else {
				return new Rational(this.num - other.num, this.denom);
			}
		} else {
			int cThisNum = this.num * other.denom;
			int cOtherNum = other.num * this.denom;
			int cDenom = other.denom * this.denom;
			if (cThisNum - cOtherNum < 0) {
				return new Rational(0, cDenom);
			} else {
				return new Rational(cThisNum - cOtherNum, cDenom);
			}
		}
	}
	/**
	 * Creates a new Rational object embodies the "quotient" of this 
	 * and the other rational.
	 * @param other other rational
	 * @throws ArithmeticException if division by zero has been at- 
	 * tempted in the event that either operand is zero.
	 * @return a new Rational object embodies the "quotient" of this 
	 * and the other rational
	 */
	public Rational divide(Rational other) {
		if (this.num == 0 || other.num == 0) {
			throw new ArithmeticException();
		}
		return new Rational(this.num * other.denom, this.denom * other.num);
	}
	/**
	 * Returns true only when this rational number equals the other 
	 * by the rules of algebra. For example: 1/2 equals 2/4, or any 
	 * other rational that obeys the algebraic relationship.
	 * @param other other 
	 * @return true only when this rational number equals the other 
	 * by the rules of algebra
	 */
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		} 
		if (other == null) {
			return false;
		}
		if (!(other instanceof Rational)) {
			return false;
		}
		Rational otherRational = (Rational)other;
		if (otherRational.num == 0 && this.num == 0) {
			return true;
		}
		int rOtherNum = otherRational.num / gcd(otherRational.num, otherRational.denom);
		int rOtherDenom = otherRational.denom / gcd(otherRational.num, otherRational.denom);
		int rThisNum = this.num / gcd(this.num, this.denom);
		int rThisDenom = this.denom / gcd(this.num, this.denom);
		if (rOtherNum != rThisNum || rOtherDenom != rThisDenom) {
			return false;
		}
		return true;
	}
	/**
	 * Returns the denominator of this Rational object.
	 * @return the denominator of this Rational object
	 */
	public int getDenominator() {
		return this.denom;
	}
	/**
	 * Returns the numerator of this Rational object.
	 * @return the numerator of this Rational object
	 */
	public int getNumerator() {
		return this.num;
	}
	/**
	 * Overrides the default hashCode method to ensure the equals 
	 * contract for hashes, i.e.,two Rational objects that are 
	 * equal must compute the same hash code.
	 * @return the hashcode of this object
	 */
	public int hashCode() {
		if (this.num == 0) {
			return 0;
		}
		int rThisNum = this.num / gcd(this.num, this.denom);
		int rThisDenom = this.denom / gcd(this.num, this.denom);
		return (37 * rThisNum + rThisDenom) * 37;
	}
	/**
	 * Creates a new Rational object that embodies the "product" 
	 * of this and the other rational.
	 * @return a new Rational object that embodies the "product" 
	 * of this and the other rational
	 */
	public Rational mult(Rational other) {
		return new Rational(this.num * other.num, this.denom * other.denom);
	}
	/**
	 * Creates a new Rational object that embodies the "sum" 
	 * of this and the other rational.
	 * @return a new Rational object that embodies the "sum" 
	 * of this and the other rational
	 */
	public Rational plus(Rational other) {
		if (this.denom == other.denom) {
			return new Rational(this.num + other.num, this.denom);
		} else {
			int cThisNum = this.num * other.denom;
			int cOtherNum = other.num * this.denom;
			int cDenom = other.denom * this.denom;
			return new Rational(cThisNum + cOtherNum, cDenom);
		}
	}
	/**
	 * Returns the multiplicative inverse of this rational.
	 * @return the multiplicative inverse of this rational
	 */
	protected Rational recipricol() {
		if (this.num == 0) {
			return null;
		}
		return new Rational(this.denom, this.num);
	}
	/**
	 * Render the Rational exactly as numerator/denominator.
	 * @return String representation of the object
	 */
	public String toString() {
		return String.valueOf(this.num) + "/" + String.valueOf(this.denom);
	}
	
	private int pmod(int a, int modulus) {
		if (a < modulus) {
			return a;
		} else {
			return pmod(a - modulus, modulus);
		}
	}
	
	private int gcd(int m, int n) {
		if (m == 0) {
			return n;
		} else if (n == 0) {
			return m;
		} else {
			return gcd(n, pmod(m, n));
		}
	}
}
