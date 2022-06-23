package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import student_classes.MinimumSnippet;

public class StudentTests {
	private ArrayList<String> sampleDoc;
	private ArrayList<String> sampleTer;
	
	public StudentTests() {
		sampleDoc = new ArrayList<String>();
		StringBuilder sb = new StringBuilder("K W D A I B D W C D W S D B F A C E S D B C D E S A D B X");
		for (int i = 0; i < sb.length(); i++) {
			sampleDoc.add(sb.substring(i, i + 1));
		}
		sampleTer = new ArrayList<String>();
		sampleTer.add("A");
		sampleTer.add("B");
		sampleTer.add("C");
	}
	
	@Test(expected = IllegalArgumentException.class )
	public void testMinimumSnippet() {
		MinimumSnippet ms = new MinimumSnippet(sampleDoc, new ArrayList<String>());
	}

	@Test
	public void testFoundAllTerms() {
		MinimumSnippet ms = new MinimumSnippet(sampleDoc, sampleTer);
		assertNotNull(ms);
		assertTrue(ms.foundAllTerms());
	}

	@Test
	public void testGetStartingPos() {
		MinimumSnippet ms = new MinimumSnippet(sampleDoc, sampleTer);
		assertNotNull(ms);
		int expectedValue = 26;
		assertEquals(expectedValue, ms.getStartingPos());
	}

	@Test
	public void testGetEndingPos() {
		MinimumSnippet ms = new MinimumSnippet(sampleDoc, sampleTer);
		assertNotNull(ms);
		int expectedValue = 32;
		assertEquals(expectedValue, ms.getEndingPos());
	}

	@Test
	public void testGetLength() {
		MinimumSnippet ms = new MinimumSnippet(sampleDoc, sampleTer);
		assertNotNull(ms);
		int expectedValue = 7;
		assertEquals(expectedValue, ms.getLength());
	}

	@Test
	public void testGetPos() {
		MinimumSnippet ms = new MinimumSnippet(sampleDoc, sampleTer);
		assertNotNull(ms);
		int[] expectedValues = {30, 26, 32};
		assertEquals(expectedValues[0], ms.getPos(0));
		assertEquals(expectedValues[1], ms.getPos(1));
		assertEquals(expectedValues[2], ms.getPos(2));
	}

}
