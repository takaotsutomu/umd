package tree;

import java.util.Collection;

/**
 * This class is used to represent the empty search tree: a search tree that
 * contains no entries.
 * 
 * This class is a singleton class: since all empty search trees are the same,
 * there is no need for multiple instances of this class. Instead, a single
 * instance of the class is created and made available through the static field
 * SINGLETON.
 * 
 * The constructor is private, preventing other code from mistakenly creating
 * additional instances of the class.
 *  
 */
 public class EmptyTree<K extends Comparable<K>,V> implements Tree<K,V> {
	/**
	 * This static field references the one and only instance of this class.
	 * We won't declare generic types for this one, so the same singleton
	 * can be used for any kind of EmptyTree.
	 */
	private static EmptyTree SINGLETON = new EmptyTree();


	public static  <K extends Comparable<K>, V> EmptyTree<K,V> getInstance() {
		return SINGLETON;
	}

	/**
	 * Constructor is private to enforce it being a singleton
	 *  
	 */
	private EmptyTree() {
		// Nothing to do
	}
	/**
	 * Returns the result of searching an Empty Tree, which is null.
	 * @return the result of searching an Empty Tree, which is null
	 */
	public V search(K key) {
		return null;
	}
	/**
	 * Create a new NonEmptyTree with the corresponding types for its key and value.
	 * @param key key that maps to value
	 * @param value value that the key maps to
	 * @return updated tree
	 */
	public NonEmptyTree<K, V> insert(K key, V value) {
		return new NonEmptyTree(key, value, this, this);
	}
	/**
	 * The result of deleting from this particular class of tree is itself.
	 * @param key key of the element to delete
	 * @return The result of deleting from this particular class of tree,
	 * which is itself
	 */
	public Tree<K, V> delete(K key) {
		return this;
	}
	/**
	 * An EmptyTree does not contain a maximum element.
	 * @throws TreeIsEmptyException
	 */
	public K max() throws TreeIsEmptyException {
		throw new TreeIsEmptyException();
	}
	/**
	 * An EmptyTree does not contain a minimum element.
	 * @throws TreeIsEmptyException
	 */
	public K min() throws TreeIsEmptyException {
		throw new TreeIsEmptyException();
	}
	/**
	 * Returns the size of an EmptyTree.
	 * @return number of keys that are bound in this tree, which is 0
	 */
	public int size() {
		return 0;
	}
	/**
	 * No-op.
	 */
	public void addKeysToCollection(Collection<K> c) {
		return;
	}
	/**
	 * Returns the subtree of an EmptyTree.
	 * @return the subtree of the tree
	 */
	public Tree<K,V> subTree(K fromKey, K toKey) {
		return this;
	}
	/**
	 * Returns the height of an EmptyTree.
	 * @return the height of the tree, which is 0
	 */
	public int height() {
		return 0;
	}
	/**
	 * No-op.
	 */
	public void inorderTraversal(TraversalTask<K,V> p) {
		return;
	}
	/**
	 * No-op.
	 */
	public void rightRootLeftTraversal(TraversalTask<K,V> p) {
		return;
	}
}