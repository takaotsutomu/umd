package student_classes;
public class MyQueue<T> {
	private java.util.LinkedList<T> list;
	
	public MyQueue() {
		list = new java.util.LinkedList<T>();
	}
	
	public synchronized int size() {
		return list.size();
	}
	
	public synchronized void clear() {
		list.clear();
	}
	
	public synchronized void enqueue(T o) {
		list.add(o);
		notify();
	}
	
	public T dequeue() {
		synchronized (this) {
			if (list.size() == 0) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return list.remove();
		}
	}
}

