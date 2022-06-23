package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import junit.framework.TestCase;
import tree.PlaceKeysValuesInArrayLists;
import tree.PolymorphicBST;

public class StudentTests extends TestCase {
	@Test
	public void testSearch() {
		PolymorphicBST<Integer,String> ptree = new PolymorphicBST<Integer,String>();
		assertEquals(0, ptree.size());
		assertEquals(0, ptree.height());
		
		assertNull(ptree.get(9));
		
		ptree.put(23, "Twenty Three");
		ptree.put(14, "Fourteen");
		ptree.put(47, "Fourty Seven");
		ptree.put(19, "Nineteen");
		ptree.put(32, "Thirty Two");
		ptree.put(12, "Twelve");
		ptree.put(1, "One");
		ptree.put(43, "Fourty Three");
		assertEquals(8, ptree.size());
		assertEquals(4, ptree.height());
		
		assertEquals("Twelve", ptree.get(12));
		assertEquals("Fourteen", ptree.get(14));
		assertEquals("Fourty Three", ptree.get(43));
		assertNull(ptree.get(15));
		assertNull(ptree.get(36));
	}
	
	@Test
	public void testInsert() {
		PolymorphicBST<Integer,String> ptree = new PolymorphicBST<Integer,String>();
		assertEquals(0, ptree.size());
		assertEquals(0, ptree.height());
		
		ptree.put(23, "Twenty Three");
		ptree.put(14, "Fourteen");
		ptree.put(47, "Fourty Seven");
		ptree.put(19, "Nineteen");
		ptree.put(32, "Thirty Two");
		ptree.put(12, "Twelve");
		ptree.put(1, "One");
		ptree.put(43, "Fourty Three");
		assertEquals(8, ptree.size());
		assertEquals(4, ptree.height());
		
		PlaceKeysValuesInArrayLists<Integer, String> task = new PlaceKeysValuesInArrayLists<Integer, String>();
		ptree.inorderTraversal(task);
		assertEquals(task.getKeys().toString(), "[1, 12, 14, 19, 23, 32, 43, 47]");
		assertEquals(task.getValues().toString(), "[One, Twelve, Fourteen, Nineteen, Twenty Three, Thirty Two, Fourty Three, Fourty Seven]");
	}
	
	@Test
	public void testDelete() {
		PolymorphicBST<Integer,String> ptree = new PolymorphicBST<Integer,String>();
		assertEquals(0, ptree.size());
		assertEquals(0, ptree.height());
		
		ptree.put(23, "Twenty Three");
		assertEquals(1, ptree.size());
		assertEquals(1, ptree.height());
		
		ptree.remove(23);
		
		assertEquals(0, ptree.size());
		assertEquals(0, ptree.height());
		
		ptree.put(23, "Twenty Three");
		ptree.put(14, "Fourteen");
		ptree.put(47, "Fourty Seven");
		ptree.put(19, "Nineteen");
		ptree.put(32, "Thirty Two");
		ptree.put(12, "Twelve");
		ptree.put(1, "One");
		ptree.put(43, "Fourty Three");
		assertEquals(8, ptree.size());
		assertEquals(4, ptree.height());
		
		ptree.remove(47);
		ptree.remove(19);
		ptree.remove(23);
		
		assertEquals(5, ptree.size());
		assertEquals(3, ptree.height());
		
		PlaceKeysValuesInArrayLists<Integer, String> task = new PlaceKeysValuesInArrayLists<Integer, String>();
		ptree.inorderTraversal(task);
		assertEquals(task.getKeys().toString(), "[1, 12, 14, 32, 43]");
		assertEquals(task.getValues().toString(), "[One, Twelve, Fourteen, Thirty Two, Fourty Three]");
	}
	
	@Test
	public void testMaxMin() {
		PolymorphicBST<Integer,String> ptree = new PolymorphicBST<Integer,String>();
		assertEquals(0, ptree.size());
		assertEquals(0, ptree.height());
		
		ptree.put(23, "Twenty Three");
		ptree.put(14, "Fourteen");
		ptree.put(47, "Fourty Seven");
		ptree.put(19, "Nineteen");
		ptree.put(32, "Thirty Two");
		ptree.put(12, "Twelve");
		ptree.put(1, "One");
		ptree.put(43, "Fourty Three");
		assertEquals(8, ptree.size());
		assertEquals(4, ptree.height());
		
		assertEquals(47, (int)ptree.getMax());
		assertEquals(1, (int)ptree.getMin());
		
		ptree.remove(23);
		ptree.remove(1);
		assertEquals(4, ptree.height());
		
		assertEquals(47, (int)ptree.getMax());
		assertEquals(12, (int)ptree.getMin());
		
		ptree.put(56, "Fifty Six");
		ptree.put(49, "Fourty Nine");
		assertEquals(4, ptree.height());
		
		assertEquals(56, (int)ptree.getMax());
		assertEquals(12, (int)ptree.getMin());
		
		ptree.put(6, "Fifty Six");
		ptree.put(74, "Fourty Nine");
		assertEquals(4, ptree.height());
		
		assertEquals(74, (int)ptree.getMax());
		assertEquals(6, (int)ptree.getMin());
	}
	
	@Test
	public void testSubTree() {
		PolymorphicBST<Integer,String> ptree = new PolymorphicBST<Integer,String>();
		assertEquals(0, ptree.size());
		assertEquals(0, ptree.height());
		
		ptree.put(23, "Twenty Three");
		ptree.put(14, "Fourteen");
		ptree.put(47, "Fourty Seven");
		ptree.put(19, "Nineteen");
		ptree.put(32, "Thirty Two");
		ptree.put(12, "Twelve");
		ptree.put(1, "One");
		ptree.put(43, "Fourty Three");
		assertEquals(8, ptree.size());
		assertEquals(4, ptree.height());
		
		PolymorphicBST<Integer,String> subPtree = ptree.subMap(19, 32);
		assertEquals(3, subPtree.size());
		assertEquals(2, subPtree.height());
		
		PlaceKeysValuesInArrayLists<Integer, String> task = new PlaceKeysValuesInArrayLists<Integer, String>();
		subPtree.inorderTraversal(task);
		assertEquals(task.getKeys().toString(), "[19, 23, 32]");
		assertEquals(task.getValues().toString(), "[Nineteen, Twenty Three, Thirty Two]");
		
		subPtree = ptree.subMap(13, 46);
		assertEquals(5, subPtree.size());
		assertEquals(3, subPtree.height());
		task = new PlaceKeysValuesInArrayLists<Integer, String>();
		subPtree.inorderTraversal(task);
		System.out.println(task.getKeys().toString());
		assertEquals(task.getKeys().toString(), "[14, 19, 23, 32, 43]");
		assertEquals(task.getValues().toString(), "[Fourteen, Nineteen, Twenty Three, Thirty Two, Fourty Three]");
	}
	
	@Test
	public void testRightRootTraversal() {
		PolymorphicBST<Integer,String> ptree = new PolymorphicBST<Integer,String>();
		assertEquals(0, ptree.size());
		assertEquals(0, ptree.height());
		
		ptree.put(23, "Twenty Three");
		ptree.put(14, "Fourteen");
		ptree.put(47, "Fourty Seven");
		ptree.put(19, "Nineteen");
		ptree.put(32, "Thirty Two");
		ptree.put(12, "Twelve");
		ptree.put(1, "One");
		ptree.put(43, "Fourty Three");
		assertEquals(8, ptree.size());
		assertEquals(4, ptree.height());
		
		PlaceKeysValuesInArrayLists<Integer, String> task = new PlaceKeysValuesInArrayLists<Integer, String>();
		ptree.rightRootLeftTraversal(task);
		assertEquals(task.getKeys().toString(), "[47, 43, 32, 23, 19, 14, 12, 1]");
		assertEquals(task.getValues().toString(), "[Fourty Seven, Fourty Three, Thirty Two, Twenty Three, Nineteen, Fourteen, Twelve, One]");
	}
}