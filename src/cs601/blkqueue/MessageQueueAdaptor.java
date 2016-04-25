package cs601.blkqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;

class MessageQueueAdaptor<T> implements MessageQueue<T> {
    private long size;
    private ArrayBlockingQueue<T> queue ;
    MessageQueueAdaptor(int size) {
        queue =  new ArrayBlockingQueue<T>(size);
    }

	@Override
	public  void put(T o) throws InterruptedException {
        queue.put(o);
    }

	@Override
	public  T take() throws InterruptedException {
        return  queue.take();
    }
}
