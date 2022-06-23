package tests;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import student_classes.Heap;

public class StudentTests { // these are merely suggestions ....

	@Test
	public void testHeap() {
		java.util.ArrayList<Integer> list = new java.util.ArrayList<Integer>();
		list.add(11);
		list.add(5);
		list.add(13);
		list.add(6);
		list.add(1);
		
		Heap<Integer> heap = new Heap(list);
		assertEquals("{ 13, 6, 11, 5, 1,  }", heap.toString());
	}

	@Test
	public void testSort() {
		java.util.ArrayList<Integer> list = new java.util.ArrayList<Integer>();
		list.add(24);
		list.add(2);
		list.add(45);
		list.add(20);
		list.add(56);
		list.add(75);
		list.add(2);
		list.add(56);
		list.add(99);
		list.add(53);
		list.add(12);
		
		list = (java.util.ArrayList<Integer>)Heap.sort(list);
		assertEquals("[2, 2, 12, 20, 24, 45, 53, 56, 56, 75, 99]", list.toString());
	}

	@Test
	public void testSize() {
		java.util.ArrayList<Integer> list = new java.util.ArrayList<Integer>();
		list.add(11);
		list.add(5);
		list.add(13);
		list.add(6);
		list.add(1);
		
		Heap<Integer> heap = new Heap(list);
		assertEquals(5, heap.size());
	}
	
	@Test
	public void testAsList() { 
		java.util.ArrayList<Integer> list = new java.util.ArrayList<Integer>();
		list.add(11);
		list.add(5);
		list.add(13);
		list.add(6);
		list.add(1);
		
		Heap<Integer> heap = new Heap(list);
		list = (java.util.ArrayList<Integer>)heap.asList();
		assertEquals("[13, 6, 11, 5, 1]", list.toString());
	}

}
