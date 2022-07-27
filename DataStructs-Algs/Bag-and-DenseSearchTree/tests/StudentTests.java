package tests;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import student_classes.Bag;
import student_classes.DenseSearchTree;

public class StudentTests {
	// a place to design your own tests.
	@Test
	public void testBag() { 
		Bag<Integer> bag = new Bag<Integer>();
		assertNotNull(bag);
		
		for (int i = 0; i < 3; i++) {
			bag.add(8);
			bag.add(9);
			bag.add(4);
		}
		assertEquals(9, bag.size());
		assertEquals(3, bag.count(4));
		assertEquals(3, bag.count(8));
		assertEquals(3, bag.count(9));
		
		bag.remove(9);
		bag.remove(9);
		bag.remove(4);
		bag.remove(4);
		bag.remove(4);
		bag.remove(8);
		
		assertEquals(3, bag.size());
		assertEquals(0, bag.count(4));
		assertEquals(2, bag.count(8));
		assertEquals(1, bag.count(9));
		
		bag.add(10);
		bag.add(12);
		assertEquals(5, bag.size());
		assertTrue(bag.contains(8));
		assertTrue(bag.contains(9));
		assertTrue(bag.contains(10));
		assertTrue(bag.contains(12));
		assertFalse(bag.contains(4));
		
		java.util.Set<Integer> bagAsSet = bag.asSet();
		assertEquals(4 ,bagAsSet.size());
		assertTrue(bagAsSet.contains(8));
		assertTrue(bagAsSet.contains(9));
		assertTrue(bagAsSet.contains(10));
		assertTrue(bagAsSet.contains(12));
		assertFalse(bagAsSet.contains(4));
		
		Iterator<Integer> itr = bag.iterator();
		int count = 0;
		while (itr.hasNext()) {
			itr.next();
			count++;
		}
		assertEquals(5, count);
		assertEquals(itr.hasNext(), false);
	}
	
	@Test
	public void testDenseSearchTree() { 
		DenseSearchTree<Integer> dst = new DenseSearchTree<Integer>();
		assertNotNull(dst);
		
		for (int i = 0; i < 3; i++) {
			dst.add(6);
			dst.add(9);
			dst.add(3);
		}
		assertEquals(9, dst.size());
		assertEquals(3, dst.count(3));
		assertEquals(3, dst.count(6));
		assertEquals(3, dst.count(9));
		assertEquals(3, dst.getMin().intValue());
		assertEquals(9, dst.getMax().intValue());
		assertEquals("3 3 3 6 6 6 9 9 9 ", dst.toString());
		
		dst.remove(9);
		dst.remove(9);
		dst.remove(3);
		dst.remove(3);
		dst.remove(3);
		dst.remove(6);
		
		assertEquals(3, dst.size());
		assertEquals(0, dst.count(3));
		assertEquals(2, dst.count(6));
		assertEquals(1, dst.count(9));
		assertEquals(6, dst.getMin().intValue());
		assertEquals(9, dst.getMax().intValue());
		assertEquals("6 6 9 ", dst.toString());
		
		dst.add(4);
		dst.add(8);
		assertEquals(5, dst.size());
		assertTrue(dst.contains(4));
		assertTrue(dst.contains(6));
		assertTrue(dst.contains(8));
		assertTrue(dst.contains(9));
		assertFalse(dst.contains(3));
		assertEquals(4, dst.getMin().intValue());
		assertEquals(9, dst.getMax().intValue());
		assertEquals("4 6 6 8 9 ", dst.toString());
		
		java.util.Set<Integer> denseSearchTreeAsSet = dst.asSet();
		assertEquals(4 ,denseSearchTreeAsSet.size());
		assertTrue(denseSearchTreeAsSet.contains(4));
		assertTrue(denseSearchTreeAsSet.contains(6));
		assertTrue(denseSearchTreeAsSet.contains(8));
		assertTrue(denseSearchTreeAsSet.contains(9));
		assertFalse(denseSearchTreeAsSet.contains(3));
		
		Iterator<Integer> itr = dst.iterator();
		String denseSearchTreeString = "";
		while (itr.hasNext()) {
			denseSearchTreeString += itr.next() + " ";
		}
		assertEquals("4 6 6 8 9 ", denseSearchTreeString);
		assertEquals(itr.hasNext(), false);
	}
}
