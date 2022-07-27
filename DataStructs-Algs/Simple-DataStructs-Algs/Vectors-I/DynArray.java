package student_classes;

import java.util.Iterator;
import java.util.RandomAccess;

/**
 * Your implementation goes here ...
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
		if (index >= 0 && index <= size) {
			return array[index];
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
		if (index >= 0 && index <= size) {
			if (object != null || allowNulls) {
				array[index] = object;
			} else {
				throw new RuntimeException();
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
	 * @return an iterator over the elements in this list in proper sequence.
	 */
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			int index = 0;
			
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
					throw new ArrayIndexOutOfBoundsException();
				}
				
			}
			
			public void remove() {
				return;
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
			sb.append(array[i].toString() + ", ");
		}
		sb.append(array[size -1].toString() + "]");
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
		DynArray<T> otherDynArray = (DynArray<T>)other;
		if (size != otherDynArray.size) {
			return false;
		} else {
			for (int i = 0; i < otherDynArray.size; i++) {
				if (array[i] != otherDynArray.array[i]) {
					return false;
				}
			}
		}
		return true;
	}

}
