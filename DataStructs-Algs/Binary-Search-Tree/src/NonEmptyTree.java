package tree;

import java.util.Collection;

/**
 * This class represents a non-empty search tree. An instance of this class
 * should contain:
 * <ul>
 * <li>A key
 * <li>A value (that the key maps to)
 * <li>A reference to a left Tree that contains key:value pairs such that the
 * keys in the left Tree are less than the key stored in this tree node.
 * <li>A reference to a right Tree that contains key:value pairs such that the
 * keys in the right Tree are greater than the key stored in this tree node.
 * </ul>
 *  
 */
 public class NonEmptyTree<K extends Comparable<K>, V> implements Tree<K, V> {
	 private K key;
	 private V value;
	 private Tree<K, V> left, right;
	/**
	 * Only constructor we need.
	 * @param key
	 * @param value
	 * @param left
	 * @param right
	 */
	public NonEmptyTree(K key, V value, Tree<K,V> left, Tree<K,V> right) {
		this.key = key;
		this.value = value;
		this.left = left;
		this.right = right;
	}
	/**
	 * Returns the result of a depth-first search.
	 * @param key key to search for
	 * @return value associated with the key by this Tree, 
	 * or null if the key does not have an association in this tree
	 */
	public V search(K key) {
		if (key.compareTo(this.key) == 0) {
			return value;
		} else if (key.compareTo(this.key) < 0) {
			return left.search(key);
		} else {
			return right.search(key);
		}
	}
	/**
	 * Replaces the value if found, otherwise installs it 
	 * in the proper location in the tree.
	 * @param key key that maps to value
	 * @param value value that the key maps to
	 * @return updated tree
	 */
	public NonEmptyTree<K, V> insert(K key, V value) {
		if (key.compareTo(this.key) == 0) {
			this.value = value;
		} else if (key.compareTo(this.key) < 0) {
			this.left = this.left.insert(key, value);
		} else {
			this.right = this.right.insert(key, value);
		}
		return this;
	}
	/**
	 * If the key is found in this tree, then replace it 
	 * with the min of the max and continue the recursion.
	 * @param key key key of the element to delete
	 * @return updated tree
	 */
	public Tree<K, V> delete(K key) {
		if (key.compareTo(this.key) == 0) {
			if (this.min().compareTo(this.key) == 0 && this.max().compareTo(this.key) == 0) {
				return EmptyTree.getInstance();
			} else if (this.min().compareTo(this.key) != 0) {
				try {
					this.key = this.left.max();
				} catch (TreeIsEmptyException e) {
					System.err.println("This should never happen.");
				}
				this.value = this.left.search(this.key);
				this.left = this.left.delete(this.key);
			} else {
				try {
					this.key = this.right.min();
				} catch (TreeIsEmptyException e) {
					System.err.println("This should never happen.");
				}
				this.value = this.right.search(this.key);
				this.right = this.right.delete(this.key);
			}
		} else if (key.compareTo(this.key) < 0) {
			this.left = this.left.delete(key);
		} else {
			this.right = this.right.delete(key);
		}
		return this;
	}
	/**
	 * Returns the maximum value for this tree.
	 * @return maximum key
	 */
	public K max() {
		try {
			return this.right.max();
		} catch (TreeIsEmptyException ex) {
			return this.key;
		}
	}
	/**
	 * Returns the minimum value for this tree.
	 * @return minimum key
	 */
	public K min() {
		try {
			return this.left.min();
		} catch (TreeIsEmptyException ex) {
			return this.key;
		}
	}
	/**
	 * Recursively computes the number of nodes in this tree.
	 * @return number of keys that are bound in this tree
	 */
	public int size() {
		return this.left.size() + 1 + this.right.size();
	}
	/**
	 * Recursively adds c (the Collection) to nodes in this tree.
	 * @param collection to operate
	 */
	public void addKeysToCollection(Collection<K> c) {
		this.left.addKeysToCollection(c);
		c.add(this.key);
		this.right.addKeysToCollection(c);
	}
	/**
	 * Returns the tree that is located between the fromKey and toKey.
	 * @param fromKey lower bound value for keys in subtree
	 * @param toKey upper bound value for keys in subtree
	 * @return Tree containing all entries between fromKey and toKey, inclusive
	 */
	public Tree<K,V> subTree(K fromKey, K toKey) {
		if (fromKey.compareTo(this.key) > 0) {
			return right.subTree(fromKey, toKey);
		} else if (toKey.compareTo(this.key) < 0) {
			return left.subTree(fromKey, toKey);
		} else {
			return new NonEmptyTree(this.key, this.value, left.subTree(fromKey, toKey), right.subTree(fromKey, toKey));
		}
	}
	/**
	 * Implements the standard algorithm that computes the height of 
	 * a binary search tree.
	 * @return height of the tree
	 */
	public int height() {
		return 1 + Math.max(this.left.height(), this.right.height());
	}
	/**
	 * Perform an in-order traversal of this tree performing the TraversalTask.
	 * @param p object defining task
	 */
	public void inorderTraversal(TraversalTask<K,V> p) {
		this.left.inorderTraversal(p);
		p.performTask(this.key, this.value);
		this.right.inorderTraversal(p);
	}
	/**
	 * Perform a right, root, left traversal of this tree performing the TraversalTask.
	 * @param p object defining task
	 */
	public void rightRootLeftTraversal(TraversalTask<K,V> p) {
		this.right.rightRootLeftTraversal(p);
		p.performTask(this.key, this.value);
		this.left.rightRootLeftTraversal(p);
	}
}