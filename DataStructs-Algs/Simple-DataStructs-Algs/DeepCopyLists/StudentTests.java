package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import utilities.FunctionalList;
/* You may wish to implement any or none of these. Or, perhaps
 * you wish to design your own?
 */
public class StudentTests {

	@Test
	public void testFunctionalList() {
		FunctionalList<Integer> fl = new FunctionalList<Integer>();
		assertNotNull(fl);
		for (int i = 0; i < 10; i++) {
			fl = fl.add(i);
		}
		assertNotNull(fl);
		FunctionalList<Integer> fl2 = new FunctionalList<Integer>(fl);
		assertNotNull(fl2);
	}

	@Test
	public void testAdd() {
		FunctionalList<Integer> fl = new FunctionalList<Integer>();
		assertNotNull(fl);
		for (int i = 0; i < 10; i++) {
			fl = fl.add(i);
		}
		assertNotNull(fl);
		String expectedValue = "0 1 2 3 4 5 6 7 8 9 ";
		assertTrue(fl.toString().equals(expectedValue));
	}
	
	@Test
	public void testAppend() {
		FunctionalList<Integer> fl = new FunctionalList<Integer>();
		assertNotNull(fl);
		for (int i = 0; i < 10; i++) {
			fl = fl.add(i);
		}
		assertNotNull(fl);
		FunctionalList<Integer> flToAppend = new FunctionalList<Integer>();
		assertNotNull(flToAppend);
		for (int i = 0; i < 3; i++) {
			fl = fl.add(i);
		}
		assertNotNull(flToAppend);
		fl.append(flToAppend);
		String expectedValue = "0 1 2 3 4 5 6 7 8 9 0 1 2 ";
		assertTrue(fl.toString().equals(expectedValue));
	}

	@Test
	public void testRemove() {
		FunctionalList<Integer> fl = new FunctionalList<Integer>();
		assertNotNull(fl);
		for (int i = 0; i < 10; i++) {
			fl = fl.add(i);
		}
		assertNotNull(fl);
		fl = fl.remove(4);
		String expectedValue = "0 1 2 3 5 6 7 8 9 ";
		assertTrue(fl.toString().equals(expectedValue));
	}

	@Test
	public void testReverse() {
		FunctionalList<Integer> fl = new FunctionalList<Integer>();
		assertNotNull(fl);
		for (int i = 0; i < 10; i++) {
			fl = fl.add(i);
		}
		assertNotNull(fl);
		fl = fl.reverse();
		assertNotNull(fl);
		String expectedValue = "9 8 7 6 5 4 3 2 1 0 ";
		assertTrue(fl.toString().equals(expectedValue));
	}

	@Test
	public void testSize() {
		FunctionalList<Integer> fl = new FunctionalList<Integer>();
		assertNotNull(fl);
		for (int i = 0; i < 10; i++) {
			fl = fl.add(i);
		}
		assertNotNull(fl);
		int expectedValue = 10;
		assertEquals(fl.size(), expectedValue);
	}

	@Test
	public void testPositionOf() {
		FunctionalList<Integer> fl = new FunctionalList<Integer>();
		assertNotNull(fl);
		for (int i = 0; i < 10; i++) {
			fl = fl.add(i);
		}
		assertNotNull(fl);
		int expectedValue = 8;
		assertEquals(fl.positionOf(8), expectedValue);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNth() throws IllegalAccessException {
		FunctionalList<Integer> fl = new FunctionalList<Integer>();
		assertNotNull(fl);
		for (int i = 0; i < 10; i++) {
			fl = fl.add(i);
		}
		assertNotNull(fl);
		int expectedValue = 8;
		assertEquals(fl.nth(8).intValue(), expectedValue);
		int element = fl.nth(20);
	}

	@Test
	public void testToString() {
		FunctionalList<Integer> fl = new FunctionalList<Integer>();
		assertNotNull(fl);
		for (int i = 0; i < 10; i++) {
			fl = fl.add(i);
		}
		assertNotNull(fl);
		String expectedValue = "0 1 2 3 4 5 6 7 8 9 ";
		assertTrue(fl.toString().equals(expectedValue));
	}
}
