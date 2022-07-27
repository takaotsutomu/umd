package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import student_classes.BST;

public class StudentTests {

	@Test
	public void testBST() {
		BST bst = new BST();
		assertNotNull(bst);
	}

}
