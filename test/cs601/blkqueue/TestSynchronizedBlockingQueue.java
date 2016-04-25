package cs601.blkqueue;

/** Demo msg passing with blocking queue.
 *
 * N = 8000000 MAX_BUFFER_SIZE = 1024 -server
 * from terminal
 6631.869ms 1206296.5 events / second
 Producer: (4706 blocked + 25 waiting + 0 sleeping) / 10052 samples = 47.07% wasted
 Consumer: (3305 blocked + 1492 waiting + 0 sleeping) / 10043 samples = 47.76% wasted
 */
public class TestSynchronizedBlockingQueue {
	public static final int N = 8000000;

	public static void main(String[] args) throws Exception {
		SynchronizedBlockingQueue<Integer> queue = new SynchronizedBlockingQueue<Integer>(1024);
		MessageSequence<Integer> sequence = new IntegerSequence(1,N);
		TestRig.test(queue, sequence, N);
	}
}
