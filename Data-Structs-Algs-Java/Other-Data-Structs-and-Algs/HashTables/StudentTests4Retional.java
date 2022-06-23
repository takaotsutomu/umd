package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import student_classes.HashTbl;
import student_classes.Rational;

public class StudentTests {

	@Test
	public void testHashTbl() {
		// start here
		Rational r1 = new Rational(50, 400);
		Rational r2 = new Rational(1, 8); 
		System.out.println(r1.hashCode() == r2.hashCode());
	}

}
