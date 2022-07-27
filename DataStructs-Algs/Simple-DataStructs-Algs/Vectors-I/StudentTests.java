package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import student_classes.DynArray;
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

	@Test
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
	}

	@Test(expected = RuntimeException.class)
	public void testSet() throws IllegalAccessException {
		DynArray<Integer> numbers = new DynArray<Integer>( 16, false );
		assertNotNull( numbers );
		for( int index=0; index < 100; index++)
			numbers.add( generator.nextInt(100));
		
		numbers.set( 49, 50 );
		assertEquals(numbers.get( 49 ).intValue(), 50 );
		numbers.set( 49, null );
	}

	@Test
	public void testSize() {
		DynArray<Integer> numbers = new DynArray<Integer>( 16, false );
		assertNotNull( numbers );
		for( int index=0; index < 100; index++ )
			numbers.add( generator.nextInt(100));
		
		assertEquals(numbers.size(), 100);
	}

}
