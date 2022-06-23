package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import student_classes.DynArray;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random; // used to support random integer generation
								 // for tests in this file.


/** 
 * Reserved for you to implement your own tests. Note: you may
 * need to add JUnit4 to your build path to use these. Ask 
 * your TA for help, or let Eclipse lead you through
 * the process.
 * 
 * @author CMSC Student
 *
 */
public class StudentTests {
	static Random generator = new Random();
	// uses these as you wish: they are "samples" of what kinds
	// of things are good test candidates.
	@Test
	public void testDynArrayBoolean() { //ctor
		DynArray<Integer> numbers1 = new DynArray<Integer>(true);
		DynArray<Integer> numbers2 = new DynArray<Integer>(false);
		assertNotNull(numbers1);
		assertNotNull(numbers2);
	}

	@Test
	public void testDynArray() { //ctor
		DynArray<Integer> numbers = new DynArray<Integer>();
		assertNotNull(numbers);
	}

	@Test
	public void testDynArrayIntBoolean() {
		DynArray<Integer> numbers1 = new DynArray<Integer>(4, true);
		DynArray<Integer> numbers2 = new DynArray<Integer>(4, false);
		assertNotNull(numbers1);
		assertNotNull(numbers2);
	}

	@Test(expected = RuntimeException.class)
	public void testAdd() {
		DynArray<Integer> numbers = new DynArray<Integer>(4, false);
		assertNotNull(numbers);
		numbers.add(null);
	}
	/**
	 * Uncomment the following test case taken verbatim from the real Public Tests 
	 * for this project after you have implemented all of the methods for the 
	 * DynArray class. We have provided this method as an example of how one
	 * might write a unit-test for a particular functionality---copy construction,
	 * in this case.
	 * @throws IllegalAccessException 
	 */
	@Test
	public void testDynArrayCopyCtor() {
		DynArray< Integer > numbers = new DynArray< Integer >( false );
		/* ensure that the object was created in the first place. */
		assertNotNull( numbers );
		/* Invokes the copy-ctor ... */
		DynArray< Integer > otherArray = 
				new DynArray< Integer >( numbers );
		/* Note: verify that the copy-ctor did something ... */
		assertNotNull( otherArray );
		/* populate the original array with some typical input ... */
		for( int index=0; index < 10; index++ )
			numbers.add( index );
		/* This next line attempts to verify that the copy-constructor did NOT allow the
		 * sharing of structure.
		 */
		assertNotEquals( numbers.size(), otherArray.size() );
		/*
		 * this next piece of logic ensures that ALL of the properties were
		 * copied from the original to the copy .... including whether or not
		 * the original object permitted NULLs.
		 */
		try {
			otherArray.add( null );
		} catch( RuntimeException re ) { }
	}

	@Test( expected = ArrayIndexOutOfBoundsException.class )
	public void testRemove() {
		DynArray<Integer> numbers = new DynArray<Integer>( false );
		assertNotNull( numbers );
		for ( int i = 0; i < 10; i++ ) 
			numbers.add(i);
		
		numbers.remove( 3 );
		assertEquals( 9, numbers.size() );
		numbers.remove( 20 );
	}

	@Test (expected = RuntimeException.class)
	public void testGet() {
		/* In order to use this logic, you need to (1) ensure that you've implemented
		 * the Dynamic Array's add() and size() methods, that your class declares that
		 * it implements the RandomAccess interface; and, (2) you will need to 
		 * use the java.util.Random class to define a static instance variable named
		 * "generator" --which has a number of methods, but "nextInt( int )" is the
		 * one that is being called in the code below.
		 */
		DynArray<Integer> numbers=new DynArray<Integer>( 16, true );
		assertTrue( numbers instanceof java.util.RandomAccess );
		assertNotNull( numbers );
		for( int index=0; index < 100; index++ )
			numbers.add( generator.nextInt(100));
		
		assertEquals( numbers.size(), 100 );
		numbers.add(null);
		assertEquals( numbers.get( 100 ), null );
		
		DynArray<Integer> numbers_2=new DynArray<Integer>( 16, false );
		assertNotNull( numbers_2 );
		for( int index=1; index < 9; index++ )
			numbers_2.add(index);
		
		numbers_2.get(11);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testSet() {
		DynArray<Integer> numbers1 = new DynArray<Integer>( 16, false );
		assertNotNull( numbers1 );
		for( int index=0; index < 100; index++)
			numbers1.add( generator.nextInt(100));
		
		numbers1.set( 49, 50 );
		assertEquals(numbers1.get( 49 ).intValue(), 50 );
		
		DynArray<Integer> numbers2=new DynArray<Integer>( 16, false );
		assertNotNull( numbers2 );
		for( int index=1; index < 9; index++ )
			numbers2.add(index);
		
		assertEquals( numbers2.size(), 8 );
		numbers2.set(11, 12);
	}

	@Test
	public void testSize() {
		DynArray<Integer> numbers = new DynArray<Integer>( 16, false );
		assertNotNull( numbers );
		for( int index=0; index < 100; index++ )
			numbers.add( generator.nextInt(100));
		
		assertEquals(numbers.size(), 100);
	}
	
	@Test (expected = NoSuchElementException.class)
	public void testIterator() {
		DynArray<Integer> numbers = new DynArray<Integer>( 16, false );
		for ( int index = 0; index < 10; index ++ )
			numbers.add(index);
		
		Iterator<Integer> itr = numbers.iterator();
		int count = 0;
		while (itr.hasNext()) {
			itr.next();
			count++;
		}
		assertEquals(count, 10);
		assertEquals(itr.hasNext(), false);
		int next = itr.next();
	}
	
	@Test
	public void testEqual() {
		DynArray<Integer> dy1 = new DynArray<Integer>(16, false);
		DynArray<Integer> dy2 = new DynArray<Integer>(16, true);
		for (int i = 0; i < 10; i++) {
			dy1.add(i);
			dy2.add(i);
		}
		class hey {
			
		}
		assertFalse(dy1.equals(new hey()));
		assertFalse(dy1.equals(dy2));
		
		dy2.set(4, null);
		assertFalse(dy1.equals(dy2));
		
		DynArray<Integer> dy3 = new DynArray<Integer>(16, false);
		for (int i = 0; i < 10; i++) {
			dy3.add(i);
		}
		dy3.set(4, 5);
		assertFalse(dy1.equals(dy3));
		
		dy3.add(1);
		assertFalse(dy1.equals(dy3));
		
		DynArray<Integer> dy4 = new DynArray<Integer>(16, true);
		DynArray<Integer> dy5 = new DynArray<Integer>(16, true);
		for (int i = 0; i < 10; i++) {
			dy4.add(i);
			dy5.add(i);
		}
		assertTrue(dy4.equals(dy5));
		
		dy4.set(5, null);
		dy5.set(5, null);
		assertTrue(dy4.equals(dy5));
		
		dy4.set(2, null);
		dy5.set(6, null);
		assertFalse(dy4.equals(dy5));
	}
}
