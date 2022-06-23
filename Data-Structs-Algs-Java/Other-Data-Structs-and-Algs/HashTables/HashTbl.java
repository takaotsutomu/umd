package student_classes;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
/**
 * This is a truly minimal implementation of the well-known HashTable 
 * class that is still defined in Java (qv). Essentially, a HashTable
 * allows users to associate values with keys in O(1) time (amortized
 * over the life of the running application). 
 * 
 * Note: this implementation throws NullPointerExceptions if <code>put</code>
 * is called with either a null key or a null value.
 * 
 * Moreover, instead of returning Enumerations (old school), this version
 * returns Iterators for its keys and values.
 * 
 * You could theoretically use objects of this class as a hash table, but
 * too much would still need to be done, for real applications.
 * 
 * @author UMD CS Department.
 *
 * @param <E> ///> Keys type
 * @param <V> ///> Values type.
 */
public class HashTbl<E, V> {
	/* define your properties here */
	class Entry {
		E key;
		V value;
		
		public Entry(E key, V value) {
			this.key = key;
			this.value = value;
		}
	}
	private final int defaultSize=41;
	private Object buckets[] = new Object[ defaultSize ];
	/** Only one public constructor is provided ... in reality, we'd
	 * probably like a few more that allowed us to control growth rate,
	 * initial size, etc.
	 */
	public HashTbl() { 
		
	}
	/**
	 * Installs the <code>value</code> on the <code>key</code> in this
	 * table. Note, if either parameter is <code>null</code> a
	 * <code>NullPointerException</code> is signaled. 
	 * @param key
	 * @param value
	 */
	public void put( E key, V value ) {
		if (key == null || value == null) {
			throw new NullPointerException();
		}
		int index = Math.abs(key.hashCode() % defaultSize);
		if (buckets[index] == null) {
			LinkedList<Entry> entryList = new LinkedList<Entry>(); 
			entryList.add(new Entry(key, value));
			buckets[index] = entryList;
		} else {
			((LinkedList<Entry>)buckets[index]).add(new Entry(key, value));
		}
	}
	
	/**
	 * Returns the value associated with <code>key</code>. Because this is a table,
	 * nulls are not allowed, therefore if a <code>null</code> is returned ... we
	 * know that the key wasn't found.
	 * @param key
	 * @return
	 */
	public V get( E key ) {
		int index = Math.abs(key.hashCode() % defaultSize);
		if (buckets[index] == null) {
			return null;
		} 
		LinkedList<Entry> entryList = (LinkedList<Entry>)buckets[index];
		for (Entry entry : entryList) {
			if (key.equals(entry.key)) {
				return entry.value;
			}
		}
		return null;
	}
	/**
	 * Returns an Iterator over the <code>key</code>s in this table; note, no particular
	 * order is specified here.
	 * @return an Iterator over Keys.
	 */
	public Iterator<E> keys() {
		LinkedList<E> keyList = new LinkedList<E>();
		for (int i = 0; i < buckets.length; i++) {
			LinkedList<Entry> entryList = (LinkedList<Entry>)buckets[i];
			for (Entry entry : entryList) {
				keyList.add(entry.key);
			}
		}
		return keyList.iterator();
	}
	/**
	 * Returns an Iterator over the <code>value</code>s in the table; note, no
	 * particular order is assumed.
	 * @return
	 */
	public Iterator<V> values() {
		LinkedList<V> valueList = new LinkedList<V>();
		for (int i = 0; i < buckets.length; i++) {
			LinkedList<Entry> entryList = (LinkedList<Entry>)buckets[i];
			for (Entry entry : entryList) {
				valueList.add(entry.value);
			}
		}
		return valueList.iterator();
	}

}
