package cs601.blkqueue;

public interface MessageQueue<T> {
	void put(T o) throws InterruptedException;
	T take() throws InterruptedException;
}
