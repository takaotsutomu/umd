package student_classes;
/**
 * Common "base class" for any and all directed di-graphs (or, what we will
 * be creating in this class). This class is extended by the Binary Search Tree
 * class (qv), and therefore relies upon some of its properties (indirectly) and
 * methods. Note that all of the methods belonging to this class address
 * the <emph>structure</emph> of the tree a di-graph, i.e., independently of what
 * values might be stored at these locations. In other words: the operations defined
 * by this class are common to all directed di-graphs, which means that they are
 * appropriate to the BST (binary search tree) that you will be required to define
 * using this as a base class. 
 * <br>
 * <P>Observe that the signatures for many of the methods on this class return
 * <emph>subtree</emph>s and <b>never</b> <code>BTNode</code>s. This ensures that
 * clients never know anything about the underlying representation of these objects
 * or any of their descendants. </P>
 * @author UMD CS Dept.
 *
 * @param <T>
 */
public class BinaryTree<T> {
	
	class BTNode {
		T	value;
		BTNode left, right;
		
		BTNode( T val, BTNode l, BTNode r ) {
			this.value = val;
			this.left = l;
			this.right = r;
		}
		
		BTNode( T val ) {
			this( val, null, null );
		}
		
	} // closes class BTNode
	/*
	 * You may choose to rename this private and use the public accessors, 
	 * or you may leave this protected and use a mixture of public accessors
	 * and/or direct field references.
	 */
	protected BTNode root=null;
	
	/**
	 * Adds a value to the binary tree.
	 * @param value the value to add
	 */
	public void add(T value) {
		root = addHelper(value, root);
	}
	
	private BTNode addHelper(T value, BTNode node) {
		if (node == null) {
			return new BTNode(value);
		} else if (node.left == null) {
			node.left = addHelper(value, node.left);
		} else {
			node.right = addHelper(value, node.right);
		} 
		return node;
	}
	
	/**
	 * Overrides the Object equals method to return true 
	 * only when this and the other tree match node by node, 
	 * value by value.
	 * @param other other binary tree
	 * @return true only when this and the other tree match node by node
	 */
	public boolean equals (Object other) {
		if (this == other) {
			return true;
		} else if (other == null) {
			return false;
		} else if (!(other instanceof BinaryTree)) {
			return false;
		} else {
			BinaryTree<T> otherBinaryTree = (BinaryTree<T>)other;
			if (otherBinaryTree.getSize() != this.getSize()) {
				return false;
			}
			return equalsHelper(otherBinaryTree.root, this.root);
		}
	}
	
	private boolean equalsHelper(BTNode node2, BTNode node1) {
		if (node2 == null && node1 == null) {
			return true;
		} else if (node2 == null && node1 != null || 
				node2 != null && node1 == null) {
			return false;
		} else {
			if (!node2.value.equals(node1.value)) {
				return false;
			}
			return equalsHelper(node2.left, node1.left) && 
					equalsHelper(node2.right, node2.right);
		} 
	}
	/**
	 * Throws an unspecified exception if this tree is empty; 
	 * otherwise, returns the left subtree attached to this tree. 
	 * Note, the left subtree may be null.
	 * @throws IllegalStateException
	 * @return the left subtree attached to this tree
	 */
	public BinaryTree<T> getLeft() {
		if (root == null) {
			throw new IllegalStateException();
		} else if (root.left == null) {
			return null;
		} else {
			BinaryTree<T> subtree = new BinaryTree<T>();
			subtree.root = root.left;
			return subtree;
		}
	}
	/**
	 * Throws an unspecified exception if this tree is empty; 
	 * otherwise returns the right subtree attached to this tree. 
	 * Note, the right subtree may be null.
	 * @throws IllegalStateException
	 * @return  the right subtree attached to this tree
	 */
	public BinaryTree<T> getRight() {
		if (root == null) {
			throw new IllegalStateException();
		} else if (root.right == null) {
			return null;
		} else {
			BinaryTree<T> subtree = new BinaryTree<T>();
			subtree.root = root.right;
			return subtree;
		}
	}
	/**
	 * Returns the size of any binary tree, which is the number 
	 * of nodes it contains.
	 * @return the size of the binary tree
	 */
	public int getSize() {
		return getSizeHelper(root);
	}
	
	private int getSizeHelper(BTNode node) {
		if (node == null) {
			return 0;
		} else {
			return getSizeHelper(node.left) + 1 + getSizeHelper(node.right);
		}
	}
	/**
	 * Given any non-empty binary tree, returns the value at its root. 
	 * Note: if the tree is empty, then a runtime exception is thrown.
	 * @throws IllegalStateException
	 * @return the value at the root
	 */
	public T getValue() {
		if (root == null) {
			throw new IllegalStateException();
		} else {
			return root.value;
		}
	}
	/**
	 * Returns true only when the left branch contains a non-empty tree.
	 * @return true only when the left branch contains a non-empty tree
	 */
	protected boolean hasLeft() {
		if (root.left == null) {
			return false; 
		} else {
			return true;
		}
	}
	/**
	 * Returns true only when the right branch contains a non-empty tree.
	 * @return true only when the right branch contains a non-empty tree
	 */
	protected boolean hasRight() {
		if (root.right == null) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * The "height" of any binary tree is the number of edges 
	 * in the longest path from its root to its deepest child (leaf) node.
	 * @return the height of the binary tree
	 */
	public int height() {
		return heightHelper(root);
	}
	
	private int heightHelper(BTNode node) {
		if (node == null) {
			return -1;
		} else {
			return 1 + Math.max(heightHelper(node.left), heightHelper(node.right));
		}
	}
	/**
	 * Returns true iff this tree is empty.
	 * @return true iff this tree is empty
	 */
	public boolean isEmpty() {
		if (getSize() != 0) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * Returns true iff this tree (or subtree) is non-empty 
	 * and has neither a left nor a right branch.
	 * @return true iff this tree (or subtree) is non-empty 
	 * and has neither a left nor a right branch
	 */
	public boolean isLeaf() {
		if (root == null) {
			return false;
		} else if (hasLeft() || hasRight()) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * Replaces value of left child with value.
	 * @param value value to replace
	 */
	public void setLeftValue(T value) {
		if (root.left == null) {
			root.left = new BTNode(value);
		} else {
			root.left.value = value;
		}
	}
	/**
	 * Replaces value of right child with value.
	 * @param value value to replace
	 */
	public void setRightValue(T value) {
		if (root.right == null) {
			root.right = new BTNode(value);
		} else {
			root.right.value = value;
		}
	}
	/**
	 * Replaces the "value" stored on this tree (its root) 
	 * with newValue.
	 * @param newValue value to replace
	 */
	public void setValue(T newValue) {
		if (root == null) {
			root = new BTNode(newValue);
		} else {
			root.value = newValue;
		}
	}
}

