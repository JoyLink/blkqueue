package cs601.blkqueue;

import java.util.ArrayList;
import java.util.LinkedList;

public class SynchronizedBlockingQueue<T> implements MessageQueue<T> {
    private int size;
    //private boolean debug = false;
    private LinkedList<T> queue;
    public SynchronizedBlockingQueue(int size) {
        this.size = size;
        this.queue = new LinkedList<T>();
	}

	@Override
	public synchronized void put(T o) throws InterruptedException {
        // wait until there is room to write
        try {
            while ( queue.size()==size) wait();
            //if(debug)System.out.println("put: "+o);
        }
        catch (InterruptedException ie) {
            throw new RuntimeException("woke up", ie);
        }
        // add data to queue
        queue.add(o);
        // have data.  tell any waiting threads to wake up
        notifyAll();
	}

	@Override
	public synchronized T take() throws InterruptedException {
        // wait until there is something to read
        try {
            while (queue.size()==0) wait();
            //if(debug) System.out.println("take: "+queue.get(0));
        }
        catch (InterruptedException ie) {
            System.err.println("heh, who woke me up too soon?");
        }
        // we have the lock and state we're seeking; remove, return element
        T o = queue.get(0);

        queue.remove(0);
        //this.data[] = null; // kill the old data
        notifyAll();
        return o;
	}
}
