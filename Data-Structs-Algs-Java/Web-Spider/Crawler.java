package student_classes;
import java.net.*;
import java.io.*;

public class Crawler {
	
    public static void main(String[] args) {
		
	MyQueue<URL> linkQueue = new MyQueue<URL>();
	MyQueue<URL> picQueue = new MyQueue<URL>();
	MySet<URL> beenThere = new MySet<URL>();
	MySet<URL> doneThat = new MySet<URL>();
		
	final int MAX_NUM_EXTRACTORS = 5;  // Change this to whatever you want
		
	ExtractorThread[] extractors = new ExtractorThread[MAX_NUM_EXTRACTORS];
		
	new SlideShowGUI(picQueue);
	new CrawlerGUI(linkQueue, picQueue, beenThere, doneThat, extractors);
	
	URL url = null;
	int count = 0;	
	while(true) {
	    int index = count % extractors.length;
	    if (extractors[index] == null || !extractors[index].isAlive()) {
	    	synchronized (linkQueue) {
	    		if (linkQueue.size() == 0) {
	    			try {
						linkQueue.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
	    	}
	    	synchronized (extractors) {
	    		try {
	    			while (true) {
	    				url = linkQueue.dequeue();
	    				String contentType = url.openConnection().getContentType();
	    				if (contentType != null && contentType.startsWith("text/html")) {
	    					break;
	    				}
	    			}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		extractors[index] = new ExtractorThread(url, linkQueue, picQueue, beenThere, doneThat);
	    		extractors[index].start();
	    	}
	    }
	    if (count++ % extractors.length == (extractors.length - 1)) {
	    	try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
    }
}
