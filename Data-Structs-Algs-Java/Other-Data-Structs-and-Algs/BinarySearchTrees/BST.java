package student_classes;

import java.util.Iterator;

/**
 * <P>This version of the classic Binary Search Tree is similar
 * to the commonly found textbook implementations. My purpose
 * in creating the lab is to have you practice implementing
 * many commonly-referenced "binary tree" and "binary search tree" algorithms.
 * Note the distinction: not every Binary Tree is a Binary Search Tree. 
 * Hence. some operations belong to <b>all</b> trees (i.e., are shared
 * by all directed di-graphs), such as counting the number of nodes contained
 * in the tree, obtaining a left or right subtree, determining if a tree is a
 * leaf, etc. Other operations, however, only make sense in the setting 
 * of a Binary Search Tree, such as insertion, deletion, and searching because
 * in the case of BSTs, some ordering relation has been specified and this guides
 * the structure of the tree itself.
 *</P>
 *<br>
 *<h1>Differences/Exceptional Requirements</h1>
 *<br>
 * You will first implement a generic <code>BinaryTree</code> class that is
 * parameterized on general types (objects). This class must provide a handful
 * of "structural" operations, such as retrieving left and right branches, retrieving
 * values from the root (topmost node of a tree), and a handful of predicates.
 * <br>
 * Your BST must <em>extend</em> the <code>BinaryTree</code> class, and
 * must, in addition, restrict the values that may reside within the BST to
 * homogeneous Comparable types. implementation must accept any homogeneous, 
 * <code>Comparable</code> objects as values. The
 * construction algorithm should treat place values
 * that are less than or equal to the root to the
 * left, and those greater to the right. Duplications
 * will be permitted.
 * <h2>Additional Static Method required</h2>
 * In addition to the "standard" dynamic methods (and those inherited from the
 * <code>BinaryTree</code> class), you must implement a <code>static</code> method
 * called <code>isBST(BinaryTree tree)</code> that returns <code>true</code> iff
 * <code>tree</code> satisfies all of the requirements for a Binary Search Tree (as
 * we've defined it):
 * <ul>
 * <li> It is empty;</li>
 * <li> It is a leaf node (contains no left or right children); or,</li>
 * <li> It is a binary tree whose left elements are less than or equal to its root,
 * and its right elements are greater than its root value.</li>
 * </ul>
 * 
 * <P>
 * Finally, your implementation should provide an
 * <code>Iterator</code> that allows users to iterate
 * through the nodes of a BST <em>in sort order</em>, i.e., 
 * the order in which nodes values' should be presented
 * by the Iterator must correspond to their <em>natural ordering</em>
 * as determined by the <code>compareTo</code> method.
 * @author UMD CS Department
 *
 * @param <T>
 */
public class BST <T extends Comparable<T>> extends BinaryTree<T> implements Iterable<T> {
	public BST() {
		
	}
	/**
	 * Inserts value in its correct position in the tree, 
	 * as determined by its natural ordering.
	 * @param value the value to insert
	 */
	public void add(T value) {
		root = addHelper(value, root);
	}
	
	private BTNode addHelper(T value, BTNode node) {
		if (node == null) {
			return new BTNode(value);
		} else if (value.compareTo(node.value) <= 0) {
			node.left = addHelper(value, node.left);
		} else {
			node.right = addHelper(value, node.right);
		}
		return node;
	}
	/**
	 * This method returns true only when ele appears somewhere 
	 * in the tree.
	 * @param ele
	 * @return true only when ele appears somewhere in the tree
	 */
	public boolean contains(T ele) {
		return containsHelper(ele, root);
	}
	
	private boolean containsHelper(T ele, BTNode node) {
		if (node == null) {
			return false;
		} else if (ele.compareTo(node.value) == 0) {
			return true;
		} else if (ele.compareTo(node.value) < 0) {
			return containsHelper(ele, node.left);
		} else {
			return containsHelper(ele, node.right);
		}
	}
	/**
	 * Accepts any Binary Tree and returns true iff it satisfies 
	 * the definition of a binary search tree: (1) it is empty; 
	 * (2) it is a leaf; or (3) its left and right subtrees (left 
	 * and right values) are less than or equal or greater than the 
	 * value at the immediate root (the immediate) parent, respectively.
	 * @param tree 
	 * @return true iff it satisfies 
	 * the definition of a binary search tree: (1) it is empty; 
	 * (2) it is a leaf; or (3) its left and right subtrees (left 
	 * and right values) are less than or equal or greater than the 
	 * value at the immediate root (the immediate) parent, respectively.
	 */
	public static <T extends Comparable<T>> boolean isBST (BinaryTree<T> tree) {
		if (tree.isEmpty()) {
			return true;
		} else if (tree.isLeaf()) {
			return true;
		} else if (tree.getLeft().getValue().compareTo(tree.getValue()) > 0 ||
					tree.getRight().getValue().compareTo(tree.getValue()) <= 0) {
			return false;
		} else {
			return isBST(tree.getLeft()) && isBST(tree.getRight());
		}
	}
	
	/**
	 * Returns an Iterator<T>, that enumerates the values in the 
	 * underlying tree in order.
	 * @return an Iterator<T>, that enumerates the values in the 
	 * underlying tree in order.
	 */
	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		class BSTIterator implements Iterator<T> {
			Iterator<T> itr;
			
			public BSTIterator() {
				java.util.ArrayList<T> list = new java.util.ArrayList<T>();
				addToListHelper(list, root);
				itr = list.iterator();
			}
			
			private void addToListHelper(java.util.ArrayList<T> list, BTNode node) {
				if (node == null) {
					return;
				} else {
					addToListHelper(list, node.left);
					list.add(node.value);
					addToListHelper(list, node.right);
				}
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
		return new BSTIterator();
	}
	/**
	 * Prints the tree in pre-order.
	 */
	public void printPreOrder() {
		printPreOrderHelper(root);
	}
	
	private void printPreOrderHelper(BTNode node) {
		if (node == null) {
			return;
		} else {
			System.out.println(node.value);
			printPreOrderHelper(node.left);
			printPreOrderHelper(node.right);
		}
	}
	/**
	 * Remove one (the first in sort order) occurrence of value 
	 * from the tree.
	 * @param value value to remove
	 * @return updated tree
	 */
	public BST<T> remove(T value) {
		root = removeHelper(value, root);
		return this;
	}
	
	private BTNode removeHelper(T value, BTNode node) {
		if (node == null) {
			return null;
		} else if (value.compareTo(node.value) == 0) {
			if (node.left == null && node.right == null) {
				return null;
			} else if (node.left != null) {
				if (node.left.value.compareTo(node.value) == 0) {
					node.left = removeHelper(value, node.left);
				} else {
					node.value = getMaxHelper(node.left);
					node.left = removeHelper(node.value, node.left);
				}
			} else {
				node.value = getMinHelper(node.right);
				node.right = removeHelper(node.value, node.right);
			}
		} else if (value.compareTo(node.value) < 0) {
			root.left = removeHelper(value, node.left);
		} else if (value.compareTo(node.value) > 0) {
			root.right = removeHelper(value, node.left);
		}
		return node;
	}
	
	private T getMinHelper(BTNode node) {
		if (node == null) {
			return null;
		} else if (node.left == null) {
			return node.value;
		} else {
			return getMinHelper(node.left);
		}
	}
	private T getMaxHelper(BTNode node) {
		if (node == null) {
			return null;
		} else if (node.right == null){
			return node.value;
		} else {
			return getMaxHelper(node.right);
		}
	}
}

