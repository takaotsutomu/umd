package student_classes;

import java.util.Iterator;
import java.util.Set;

/**
 * A Binary Search Tree data-structure that maintains a count
 * of repetitive values (as nodes).
 * 
 * @author UMD CS Dept.
 *
 * @param <T>
 */
public class DenseSearchTree<T extends Comparable<T>> implements Iterable<T>{
	private class TNode {
		private T element;
		private Integer count;
		private TNode left;
		private TNode right;
		
		public TNode(T element, Integer count) {
			this.element = element;
			this.count = count;
		}
	}
	private TNode root;
	
	public DenseSearchTree() {
		root = null;
	}
	/**
	 * Adds ele (T) to the tree.
	 * @param ele element to add
	 */
	public void add(T element) {
		root = addHelper(element, root);
	}
	
	private TNode addHelper(T element, TNode node) {
		if (node == null) {
			return new TNode(element, 1);
		} else if (element.compareTo(node.element) == 0) {
			node.count++;
		} else if (element.compareTo(node.element) < 0) {
			node.left = addHelper(element, node.left);
		} else {
			node.right = addHelper(element, node.right);
		}
		return node;
	}
	/**
	 * Returns true if at least instance of target is found in tree.
	 * @param target
	 * @return true if at least instance of target is found in tree
	 */
	public boolean contains(T target) {
		return containsHelper(target, root);
	}
	
	private boolean containsHelper(T target, TNode node) {
		if (node == null) {
			return false;
		} else if (target.compareTo(node.element) == 0) {
			return true;
		} else if (target.compareTo(node.element) < 0) {
			return containsHelper(target, node.left);
		} else {
			return containsHelper(target, node.right);
		}
	}
	/**
	 * Returns an int >= 0, indicating how many occurrences of target 
	 * reside in the tree. Note, this function returns 0 when the item 
	 * is not found in tree.
	 * @param target
	 * @return an int >= 0, indicating how many occurrences of target 
	 * reside in the tree.
	 */
	public int count(T target) {
		return countHelper(target, root);
	}
	
	private int countHelper(T target, TNode node) {
		if (node == null) {
			return 0;
		} else if (target.compareTo(node.element) == 0){
			return node.count;
		} else if (target.compareTo(node.element) < 0){
			return countHelper(target, node.left);
		} else {
			return countHelper(target, node.right);
		}
	}
	/**
	 * Returns "inflated" size of tree, meaning a count of all keys in 
	 * the tree.
	 * @return "inflated" size of tree
	 */
	public int size() {
		return sizeHelper(root);
	}
	
	private int sizeHelper(TNode node) {
		if (node == null) {
			return 0;
		} else {
			return sizeHelper(node.left) + node.count + sizeHelper(node.right);
		}
	}
	/**
	 * Returns the set representation of this Tree. 
	 * @return the set representation of this Tree
	 */
	public java.util.Set<T> asSet() {
		java.util.LinkedHashSet<T> set = new java.util.LinkedHashSet<>();
		asSetHelper(set, root);
		return set;
	}
	
	private void asSetHelper(java.util.Set<T> set, TNode node) {
		if (node == null) {
			return;
		} else {
			asSetHelper(set, node.left);
			set.add(node.element);
			asSetHelper(set, node.right);
		}
	}
	/**
	 * Somewhat streamlined or simplified version of the classic BST 
	 * remove algorithm.
	 */
	public void remove(T target) {
		root = removeHelper(target, root);
	}
	
	private TNode removeHelper(T target, TNode node) {
		if (node == null) {
			return null;
		} else if (target.compareTo(node.element) == 0) {
			if (node.count > 0) {
				node.count--;
			}
			if (node.count == 0) {
				if (node.left == null && node.right == null) {
					return null;
				} else if (node.left != null) {
					node.element = getMaxHelper(node.left);
					node.count = countHelper(node.element, node.left);
					node.left = removeHelper(node.element, node.left);
				} else if (node.right != null) {
					node.element = getMinHelper(node.right);
					node.count = countHelper(node.element, node.right);
					node.right = removeHelper(node.element, node.right);
				}
			}
		} else if (target.compareTo(node.element) < 0) {
			node.left = removeHelper(target, node.left);
		} else {
			node.right = removeHelper(target, node.right);
		}
		return node;
	}
	/**
	 * Returns a value of type T or throws an IllegalStateException if 
	 * this function is called on an empty tree.
	 * @throws IllegalStateException
	 * @return a value of type T
	 */
	public T getMin() {
		if (root == null) {
			throw new IllegalStateException();
		} else {
			return getMinHelper(root);
		}
	}
	
	private T getMinHelper(TNode node) {
		if (node.left == null) {
			return node.element;
		} else {
			return getMinHelper(node.left);
		}
	}
	/**
	 * Returns the max value (of type T), or throws an IllegalStateException if 
	 * this function is called on an empty tree.	 
	 * @throws IllegalStateException
	 * @return the max value (of type T)
	 */
	public T getMax() {
		if (root == null) {
			throw new IllegalStateException();
		} else {
			return getMaxHelper(root);
		}
	}
	
	private T getMaxHelper(TNode node) {
		if (node.right == null) {
			return node.element;
		} else {
			return getMaxHelper(node.right);
		}
	}
	/**
	 * overrode toString()
	 */
	public String toString() {
		return toStringHelper(root);
	}
	
	private String toStringHelper(TNode node) {
		if (node == null) {
			return "";
		} else {
			String elementString = "";
			for (int i = 0; i < node.count; i++) {
				elementString += node.element + " ";
			}
			return toStringHelper(node.left) + elementString + toStringHelper(node.right);
		}
	}
	/**
	 * Returns an iterator over the DenseSearchTree.
	 * @return an iterator over the DenseSearchTree
	 */
	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		class DenseSearchTreeIterator implements java.util.Iterator<T>{
			java.util.Iterator<T> itr;
			
			public DenseSearchTreeIterator() {
				java.util.ArrayList<T> inflatedTree = new java.util.ArrayList<T>();
				java.util.Set<T> elements = asSet();
				for (T element : elements) {
					for (int i = 0; i < count(element); i++) {
						inflatedTree.add(element);
					}
				}
				itr = inflatedTree.iterator();
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
		return new DenseSearchTreeIterator();
	}
}
