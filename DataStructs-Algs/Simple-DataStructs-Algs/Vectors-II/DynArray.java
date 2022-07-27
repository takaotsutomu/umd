package student_classes;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * <h1>Special Instructions</h1>
 * 
 * Copy your existing code <b>into this lab</b> and submit your changes/etc through this Lab only.
 * <ul>
 * <li><b>Do not attempt to submit last week's lab in place of this one.</b></li>
 * <li><b>Submit your changes <em>only</em> by including them in this project/lab and submitting them
 * from within this project/lab.</b></li>
 * </ul>
 * <h1>Engineering Change Order(s)</h1>
 * Since the initial release, the customer has requested that the implementation of this object
 * be refined. Specifically, the customer requests that the code be revised so as to enforce the
 * following invariant(s):
 * <ul>
 * <li>A <code>DynArray</code> that is constructed to <em>disallow</em> <code>null</code> objects should
 * <b>never</b> allow the storage or retrieval of a <code>null</code> object.</li>
 * <li>The object overrides, specifically <code>equals</code> and <code>toString</code> must work
 * transparently with <code>DynArray</code> objects irrespective of whether they allow or 
 * disallow <code>null</code> objects.</li>
 * <li>The <code>set()</code> must allow clients to set any permissible object in the array whose
 * index is valid (within the capacity of the structure), and must ensure that the <b>size</b> of
 * the DynArray reflects these changes. For example; creating a DynArray of capacity N should allow
 * clients to <code>set( i, Object )</code> when i is less than N. Moreover, the size of the underlying
 * DynArray should reflect the total number of object locations used as a result of that operation. Thus,
 * <code>set( 4, Object )</code> inserts the object at the desired location (assuming that N is greater
 * than 4), and ensures that the <code>size()</code> of the underlying array is at least 4 when asked.</i>
 * <li>Exceptions handling is tightened up to require the specified exceptions instead of allowing any
 * exceptions.</li>
 * </ul>
 * <P>
 * <h2>Notes on required exceptions</h2>
 * Note that attempts to store <code>null</code> values in <code>DynArray</code> objects that do not allow such
 * values <em>must</em> result in a <code>NullPointerException</code> being raised. Note also that calling any of
 * the methods that require indexing may result in <em>unchecked</em> <code>ArrayIndexOutOfBounds</code> exceptions
 * being thrown. Additional exceptions may be raised by attempting to use the <code>set()</code> method
 * in such a manner as to either insert a value past this current object's capacity or to insert a null where
 * the object disallows nulls. Please see the documentation for the <code>set()</code> and <code>get()</code> methods
 * for a more thorough treatment.
 * <br>
 * <h2>Notes on equals testing</h2>
 * This revision requires that the <code>equals</code> override not throw exceptions when comparing DynArrays that may contain <code>null</code>s,
 * such as might be the case where the client intends that the structures allow nulls.
 * In addition, your implementation should override the <code>toString()</code> and the <code>equals</code> methods, but <em> need not</em>
 * override the <code>hashCode()</code> method.
 * <P>
 * <h2>Previous Definitions remain in effect</h2>
 * <code>DynArray</code>s are dynamically re-sizable arrays that may contain any kind of first-class
 * <code>Object</code>s. <code>DynArray</code> objects differ from linked-lists in that they are 
 * optimized for array-style access, i.e., accessing elements by indices (ints >= 0).
 * As such, <code>DynArray</code> objects must declare that 
 * they implement the <code>RandomAccess</code> <em>marker interface</em>.
 * </P>
 * <P>Some additional considerations: At least four <code>public</code> constructors are required for this implementation:
 * <ol>
 *  <li><code>DynArray()</code> (the default constructor) which creates a dynamic array whose internal
 *  array is a default size and that allows clients to store <code>null</code> values.</li>
 *  <li><code>DynArray( boolean nullOk )</code> a minimal constructor that allows the client to 
 *  specify whether or not <code>null</code> objects are permitted through the use of the <code>nullOk</code> 
 *  flag.</li>
 *  <li><code>DynArray( int ensureCapacity, boolean nullOk )</code> This constructor creates a <code>DynArray</code>
 *  object that is <em> at least large enough</em> to <code>ensureCapacity</code>; note, the <code>nullOk</code> parameter
 *  is used to delegate calls to <code>DynArray( boolean nullOk )</code>, described above.</li>
 *  <li><code>DynArray( DynArray other )</code> This is a standard copy-constructor that creates a shallow copy
 *  of the underlying storage; it must also preserve all relevant properties. </li>
 * </ol>
 * </P>
 * <H1>Prohibited Constructions/Classes/Utilities, etc</H1>
 * Obviously, you should <b>not</b> use any of Java's collection classes to implement this class. In other
 * words, you cannot use any collection class from the <code>java.util.*</code> library, except for the <code>Iterable</code>
 * interface that you will implement.
 * <P>
 * Note: you may copy the bodies of your previous implementation into the relevant methods below. Please 
 * refrain from copying entire files as this may confuse Eclipse and/or other programs, such as CVS, 
 * that keep track of such things.
 * 
 * The relevant changes/improvements to this version include (but are not limited to)
 * <ul>
 * <li>The <code>get</code> accessor is improved to throw an <code>IllegalStateException</code> in the event that 
 * calling <code>get(i)</code> where the index <code>i</code> references a <code>null</code> and the <code>DynArray</code>
 * has been created to disallow nulls. (See the specific documentation in the reference.)</li>
 * <li>The <code>set</code> accessor is improved to throw several exceptions depending upon circumstances.(See the
 * specifics in the documentation for that method in the attached reference manual.)</li>
 * <li>The <code>equals()</code> method is tested to ensure that it does not prematurely throw <code>NullPointerException</code>s if the
 * either this or the incoming <code>DynArray</code> instances contain <code>null</code>s.
 * </ul>
 * @author CMSC Student
 *
 * @param <T>
 */
public class DynArray<T> implements Iterable<T>, RandomAccess {
final static int QUANTA = 2;
	
	private T[] array;
	private boolean allowNulls;
	private int size;
	/**
	 * A minimal constructor that allows the client to specify whether or not
	 * <code>null</code> objects are permitted through the use of <code>allowNulls</code> flag.
	 * 
	 * @param allowNulls set to true to allow null objects.
	 */
	public DynArray( boolean allowNulls ) {
		this.array = (T[])new Object[QUANTA];
		this.allowNulls = allowNulls;
		this.size = 0;
	}
	/**
	 *The default constructor which creates a dynamic array whose internal array
	 *is a default size of and that allows clients to store <code>null</code> values.
	 */
	public DynArray() {
		this(true);
	}
	/**
	 * This constructor creates a DynArray object that is at least large enough to ensureCapacity.
	 * 
	 * @param enSureCapacity if provided, then the internal array is at least this size.
	 * @param allow_nulls true if <code>null</code> objects are allowed.
	 */
	public DynArray( int ensureCapacity, boolean allow_nulls ) {
		this.array = (T[])new Object[Math.max(ensureCapacity, QUANTA)];
		this.allowNulls = allow_nulls;
		this.size = 0;
	}
	/**
	 * This is a standard copy-constructor that creates a shallow copy of the underlying storage; 
	 * it preserve all relevant properties.
	 * 
	 * @param otherArray 
	 */
	public DynArray( DynArray<T> otherArray ) {
		this.array = (T[])new Object[otherArray.array.length];
		for (int i = 0; i < otherArray.size; i++) {
			array[i] = otherArray.array[i];
		}
		this.allowNulls = otherArray.allowNulls;
		this.size = otherArray.size;
	}
	/**
	 * Appends the specified element to the end of this array.
	 * 
	 * @param ele  element to be appended to this array. 
	 */
	public void add( T ele ) {
		if (ele != null || allowNulls) {
			if (size < array.length) {
				array[size++] = ele;
			} else {
				T[] temp = (T[])new Object[size + QUANTA];
				for (int i = 0; i < size; i++) {
					temp[i] = array[i];
				}
				array = temp;
				array[size++] = ele;
			}
		} else {
			throw new NullPointerException();
		}
	}
	/**
	 * Removes the element at the specified position in this array.
	 * Shifts any subsequent elements to the left (subtracts one from their indices).
	 * 
	 * @param  the index of the element to be removed.
	 * @return the element that was removed from the array
	 */
	public T remove( int atIndex ) {
		if (atIndex >= 0 && atIndex <= size - 1) {
			T object = array[atIndex];
			T[] temp = (T[])new Object[array.length];
			for (int i = 0; i < atIndex; i++) {
				temp[i] = array[i];
			}
			for (int i = atIndex; i < size - 1; i++) {
				temp[i] = array[i + 1];
			}
			array = temp;
			size--;
			return object;
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}
	/**
	 * Returns the element at the specified position in this array.
	 * 
	 * @param index index of the element to return.
	 * @return the element at the specified position in this array.
	 */
	public T get( int index ) {
		if (index >= 0 && index <= size - 1) {
			if (array[index] != null || allowNulls) {
					return array[index];
			} else {
				throw new IllegalStateException();
			}
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}
	/**
	 * Replaces the element at the specified position in this list with the specified element.
	 * 
	 * @param index index of the element to replace.
	 * @param object element to be stored at the specified position.
	 */
	public void set( int index, T object ) {
		if (index >= 0 && index <= array.length - 1 ) {
			if (allowNulls) {
				if (index < size) {
					array[index] = object;
				} else {
					for (int i = size; i <= index; i++) {
						add(null);
					}
					array[index] = object;
				}
			} else {
				if (object != null) {
					if (index < size) {
						array[index] = object;
					} else {
						throw new ArrayIndexOutOfBoundsException();
					}
				} else {
					throw new IllegalArgumentException();
				}
			}
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}
	/**
	* Returns the number of elements in this list.
	* 
	 * @return the number of elements in this list.
	 */
	public int size() {
		return size;
	}
	/**
	 * Returns an iterator over the elements in this list in proper sequence.
	 * 
	 * @return an iterator over the elements in this list in proper sequence.
	 */
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private int index = 0;
			
			public boolean hasNext() {
				if (index < size) {
					return true;
				} else {
					return false;
				}
			}
			
			public T next() {
				if (hasNext()) {
					return array[index++];
				} else {
					throw new NoSuchElementException();
				}
			}

		};
	}
	/**
	 * Returns the string representation of the array.
	 * 
	 * @return string representation of the array.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < size - 1; i++) {
			if (array[i] != null) {
				sb.append(array[i].toString() + ", ");
			} else {
				sb.append(null + ", ");
			}
		}
		if (array[size - 1] != null) {
			sb.append(array[size - 1].toString() + "]");
		} else {
			sb.append(null + "]");
		}
		return sb.toString();
	}
	/**
	 * checks whether the array have same elements in same positions.
	 * 
	 * @return whether the array have same elements in same positions.
	 */
	public boolean equals( Object other ) {
		if (other == null) { 
			return false;
		}
		if (!(other instanceof DynArray)) {
			return false;
		}
		DynArray<T> otherDynArray = (DynArray<T>)other;
		if (otherDynArray.allowNulls != allowNulls) {
			return false;
		}
		if (size != otherDynArray.size) {
			return false;
		}
		for (int i = 0; i < otherDynArray.size; i++) {
			if (array[i] != null && otherDynArray.array[i] == null ||
					array[i] == null && otherDynArray.array[i] != null) {
				return false;
			}
			if (array[i] != null && otherDynArray.array[i] != null) {
				if (!array[i].equals(otherDynArray.array[i])) { 
					return false;
				}
			}
		}
		return true;
	}
}