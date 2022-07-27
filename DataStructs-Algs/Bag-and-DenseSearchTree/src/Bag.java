package student_classes;

import java.util.Set;

/**
 * A "bag" is a data-structure that implements a <em>multiset</em>, which is
 * a set that allows multiple (duplicates). The standard operators on such
 * a data-structure include:
 * 
 * 	Bag() --creates an empty bag (optionally, perhaps a default size?)
 * 
 * 	add( Type ele ) --adds an element of Type to the bag
 * 
 * 	remove( Type ele ) --removes an element from the bag; may throw IllegalStateException
 * 					   --in the event that ele is NOT in the Bag.
 * 
 * 	contains( Type ele ) --returns True if ele is in bag.
 * 
 * 	count( Type ele ) --returns the number of occurrences of ele in set. 
 *                 This is called the "multiplicity" of the element.
 * 
 * 	asSet() --returns a collection of the elements in this bag as set.
 * 
 *  size() --returns the number of elements in this bag taking into account
 *           their multiplicities. For example: if the Bag contains the following
 *           elements with multiplicities:
 *           
 *           Key    Count
 *           ------------
 *           "A"      2
 *           "B"      3
 *           "C"      1
 *           
 *           size() => 6.
 * 
 *  iterator() --returns an Iterator that also takes into account the multiplicities
 *           of each key. So, an iterator() should iterate over exactly the same number
 *           of elements as size().
 * Important reminder: You will most likely replace references to <code>Object</code>
 * that appear in parameters lists with your generic variable type(s).
 * 
 * @author UMD CS Department.
 *
 * @param
 */
public class Bag<T> implements Iterable<T> { // note you will need to parameterize this class and
	                                   // most likely the implements declaration.
	java.util.HashMap<T, Integer> bag;
	
	public Bag() {
		bag = new java.util.HashMap<T, Integer>();
	}
	/**
	 * Adds ele (T) to the bag. If ele was already in the Bag, then its 
	 * internal count is incremented by 1; otherwise a new entry is made, 
	 * indicating that at least 1 ele exists in this Bag.
	 * @param ele element to add
	 */
	public void add(T ele) {
		if (!bag.containsKey(ele)) {
			bag.put(ele, 1);
		} else {
			bag.put(ele, bag.get(ele) + 1);
		}
	}
	/**
	 * Effectively remove the ele from the bag.
	 * @param ele element to remove
	 */
	public void remove(T ele) {
		if (!bag.containsKey(ele)) {
			throw new IllegalStateException();
		} else if (bag.get(ele) == 1){
			bag.remove(ele);
		} else {
			bag.put(ele, bag.get(ele) - 1);
		}
	}
	/**
	 * Returns true if this Bag contains at least one copy of the key.
	 * @param ele 
	 * @return true if this Bag contains at least one copy of the key
	 */
	public boolean contains(T ele) {
		return bag.containsKey(ele);
	}
	/**
	 * Returns the number of occurrences of ele in the Bag; returns 0 
	 * in the event that ele is not in Bag. 
	 * @param ele 
	 * @return the number of occurrences of ele in the Bag; 0 in the 
	 * event that ele is not in Bag
	 */
	public int count(T ele) {
		if (!bag.containsKey(ele)) {
			return 0;
		} else {
			return bag.get(ele);
		}
	}
	/**
	 * Return the keys as a Set.
	 * @return the keys as a Set
	 */
	public java.util.Set<T> asSet() {
		return bag.keySet();
	}
	/**
	 * The "size" of a bag is the cardinality of the multiset that it 
	 * embodies. 
	 * @return size of the bag
	 */
	public int size() {
		int size = 0;
		java.util.Collection<Integer> values = bag.values();
		for (int value : values) {
			size += value;
		}
		return size;
	}
	/**
	 * Returns an object that iterates (implements the Iterator interface)
	 *  over objects in the Bag.
	 * @return size of the bag
	 */
	@Override
	public java.util.Iterator<T> iterator() {
		// TODO Auto-generated method stub
		class BagIterator implements java.util.Iterator<T>{
			java.util.Iterator<T> itr;
			
			public BagIterator() {
				java.util.ArrayList<T> inflatedBag = new java.util.ArrayList<T>();
				java.util.Set<T> keys = bag.keySet();
				for (T key : keys) {
					for (int i = 0; i < bag.get(key); i++) {
						inflatedBag.add(key);
					}
				}
				itr = inflatedBag.iterator();
			}

			@Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return itr.hasNext();
			}

			@Override
			public T next() {
				// TODO Auto-generated method stub
				return itr.next();
			}
		}
		return new BagIterator();
	}
}
