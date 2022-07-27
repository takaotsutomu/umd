package student_classes;
public class MySet<T> {
	private java.util.HashSet<T> set;
	
	public MySet() {
		set = new java.util.HashSet<T>();
	}
	public synchronized int size() {
		return set.size();
	}
	
	public synchronized void clear() {
		set.clear();
	}
	
	public synchronized boolean remove(T o) {
		return set.remove(o);
	}
	
	public synchronized boolean add(T o) {
		return set.add(o);
	}
	
	public synchronized boolean contain(T o) {
		return set.contains(o);
	}
}