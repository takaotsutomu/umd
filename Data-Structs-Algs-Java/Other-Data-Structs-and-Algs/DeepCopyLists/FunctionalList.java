package utilities;

/**
 * <P>A \code>FunctionalList</code> object supports linked-list operations but, unlike the
 * "standard" implementation that performs \emph{in situ} modifications, e.g., destructively modifies
 * the original list when adding/removing elements, a "functional" list create copies of the original 
 * object with the desired modifications. This has several ramifications---some good, some not so good.
 * <P>
 * <BR>
 * <P>In this assignment, you will implement a functional linked list that provides a subset of useful
 * operations.
 * @author UMD CS Department
 *
 *
 * @param <T>
 */
public class FunctionalList<T> {
	/**
	 * Internal (hidden) class ... define a Node to keep track of values and links.
	 * @author UMD CS Department
	 *
	 * @param <T>
	 */
	class Node  {
		private T data;
		private Node next;
		
		Node(T data) {
			this(data, null);
		}
		
		Node(T data, Node next) {
			this.data = data;
			this.next = next;
		}
		
		public void setNext(Node next) {
			this.next = next;
		}
		
		public T getData() {
			return data;
		}
		
		public Node getNext() {
			return next;
		}
	}
	
	private Node head;
	
	/**
	 *Constructs an empty list.
	 */
	// define ctor(s) here:
	public FunctionalList() {
		head = null;
	}
	/**
	 * This is a standard copy-constructor that creates a deep copy of the underlying storage; 
	 * 
	 * @param lst the list whose elements are to be copied
	 */
	public FunctionalList(FunctionalList<T> lst) {
		if (lst.head == null) {
			head = null;
		} else {
			head = new Node(lst.head.getData());
			Node current_this = head;
			Node current_lst = lst.head;
			while (current_lst.getNext() != null) {
				current_this.setNext(new Node(current_lst.getNext().getData()));
				current_this = current_this.getNext();
				current_lst = current_lst.getNext();
			}
		}
	}
	/**
	 *Reconstruct list by appending element onto its end.
	 *
	 *@param the element to add
	 *@return the reference to newly constructed list
	 */
	// public interface:
	public FunctionalList<T> add( T element ) {
		FunctionalList<T> functionalList = new FunctionalList<T>();
		functionalList.head = add_helper(element, head);
		
		return functionalList;
	}
	
	private Node add_helper(T element, Node head) {
		if (head == null) {
			//the current node being null means the list is exhausted
			//return a new node
			return new Node(element);
		} else {
			//return a copy of current node with its next setting to the copy of next node
			return new Node(head.getData(), add_helper(element, head.getNext()));
		}
	}
	/**
	 *Creates and return a new list whose elements are the original list with the elements of the FunctionalList parameter
	 *appended in their original order.
	 *
	 *@param other the list to append
	 *@return copied FunctionalList but with elements at the end.
	 */
	public FunctionalList<T> append( FunctionalList<T> other ) {
		FunctionalList<T> functionalList = new FunctionalList<T>();
		functionalList.head = append_helper(other, head); 
		
		return functionalList;
	}
	
	private Node append_helper(FunctionalList<T> other, Node head) {
		if (head == null) {
			//the current node being null means the list is exhausted
			//return the head of the list to append
			return other.head;
		} else {
			//making a copy of current node with its next setting to the copy of next node
			return new Node(head.getData(), append_helper(other, head.getNext()));
		}
	}
	/**
	 *Returns true if the list is empty; false otherwise.
	 *
	 *@return true if the list is empty; false otherwise.
	 */
	public boolean isEmpty() {
		if (head == null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 *Returns a copy of List eliminating all occurrences of the <code>element</code>.
	 *
	 *@param element element to be removed
	 *@return a copy of the original list with <code>element</code> removed
	 */
	public FunctionalList<T> remove( T element ) {
		FunctionalList<T> functionalList = new FunctionalList<T>();
		functionalList.head = remove_helper(element, head); 
		
		return functionalList;
	}
	
	private Node remove_helper(T element, Node head) {
		if (head == null) {
			//the current node being null means the list is exhausted
			//return null
			return null;
		} else if (head.getData().equals(element)) {
			//if the current node contains the same object as element
			//skip returning a copy of current node
			return remove_helper(element, head.getNext());
		} else {
			//making a copy of current node with its next setting to the next copy of next node
			return new Node(head.getData(), remove_helper(element, head.getNext()));
		}
	}
	/**
	 *constructs a reversed image of the original list.
	 *
	 *@return a copy of the original list with its elements reversed
	 */
	public FunctionalList<T> reverse() {
		FunctionalList<T> functionalList = new FunctionalList<T>();
		functionalList.head = reverse_helper(head, null); 
		
		return functionalList;
	}
	
	private Node reverse_helper(Node head, Node copyOfPrevious) {
		if (head == null) {
			//the current node being null means the list is empty
			//return null
			return null;
		} else if (head.getNext() == null){
			//if current node is the last node in the list
			//create a copy of current node and set its next
			//to the copy of the previous node and return the 
			//copy of current node
			Node copyOfHead = new Node(head.getData());
			copyOfHead.setNext(copyOfPrevious);
			return copyOfHead;
		} else {
			//create a copy of current node and set its next
			//to the copy of the previous node 
			//pass the copy of current node
			//get the copy of the head of the list
			//return the copy of the head of the list
			Node copyOfHead = new Node(head.getData());
			copyOfHead.setNext(copyOfPrevious);
			Node headOfList = reverse_helper(head.getNext(), copyOfHead); 
			return headOfList;
		}
	}
	/**
	 *Returns the number of values stored in list.
	 *
	 *@return the number of values stored in list
	 */
	public int size() {
		return size_helper(head);
	}
	
	private int size_helper(Node head) {
		if (head == null) {
			//the current node being null means the list is exhausted
			//return 0
			return 0;
		} else {
			//count 1 for current node
			return 1 + size_helper(head.getNext());
		}
	}
	/**
	 *Returns -1 iff element is not found in list; returns the 0-based index of element, otherwise.
	 *
	 *@param element element to search for
	 *@return index corresponding to the location of the element, or -1 if not found
	 */
	public int positionOf( T element ) {
		return positionOf_helper(element, 0, head);
	}
	
	private int positionOf_helper(T element, int cur, Node head) {
		if (head == null) {
			//the current node being null means the list is exhausted
			//return -1
			return -1;
		} else if (head.getData().equals(element)) {
			//if current node contains the object that matches the element
			//return current index
			return cur;
		} else {
			//increase current index and see if next node matches the element
			return positionOf_helper(element, cur + 1, head.getNext());
		}
	}
	/**
	 *Returns a copy of List eliminating all occurrences of the element.
	 *
	 *@param element element to be removed
	 *@return a copy of the original list with element removed
	 *@throws IllegalAccessException if it is called on an empty list,
	 *regardless of the value of <code>index</code>
	 *@throws IllegalArguemtnException if it is called with an <code>index</code>
	 *greater than (or equal to) the number of elements in the underlying list
	 */
	public T nth( int index ) throws IllegalAccessException {
	    if (head == null) {
	    	throw new IllegalAccessException();
	    } else if (index >= size()) {
			throw new IllegalArgumentException();
		} else {
			return nth_helper(index, 0, head);
		}
	}
	
	private T nth_helper (int index, int cur, Node head) throws IllegalAccessException {
		if (cur == index) {
			//if current index matches the index
			//return the data in the node
			return head.getData();
		} else {
			//increase current index and see if it matches the index
			return nth_helper(index, cur + 1, head.getNext());
		}
	}
	/**
	 * Returns the string representation of the list.
	 * 
	 * @return string representation of the list
	 */
	// override(s):
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Node current = head;
		while (current != null) {
			sb.append(current.getData().toString() + " ");
			current = current.getNext();
		}
		
		return sb.toString();
	}
	/**
	 * Returns an array whose elements are the elements of the list, in their list-order.
	 * 
	 * @return an array of objects as they appeared in the list
	 */
	public Object[] toArray() {
		Object[] array = new Object[size()];
		int i = 0;
		Node current = head;
		while (current != null) {
			array[i++] = current.getData();
			current = current.getNext();
		}
		
		return array;
	}
	/**
	 * Returns a new Functional List where each item that was equal to 
	 * the <code>key</code> has been replaced with the <code>value</code>.
	 * 
	 * @return the reference to newly constructed list
	 */
	public FunctionalList<T> subst( T key, T value ) {
		FunctionalList<T> functionalList = new FunctionalList<T>();
		functionalList.head = subst_helper(key, value, head);
		
		return functionalList;
	}
	
	private Node subst_helper(T key, T value, Node head) {
		if (head == null) {
			//the current node being null means the list is exhausted
			//return null
			return null;
		} else if (head.getData().equals(key)){
			//if the data contained in current node matches the key
			//return a new node with its data setting to value and its
			//next setting to the copy of next node or a new node with its data setting
			//to value if the data contained in next node matches the key
			return new Node(value, subst_helper(key, value, head.getNext()));
		} else {
			///making a copy of current node with its next setting to 
			//the copy of next node or a new node with its data setting
			//to value if the data contained in next node matches the key
			return new Node(head.getData(), subst_helper(key, value, head.getNext()));
		}
	}

}
