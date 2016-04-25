package cs601.blkqueue;

/** Demo Java's BlockingQueue.
 * from terminal
 * jdk 1.7 N = 80,000,000 -server
 22063.847ms 3625841.0 events / second
 Producer: (0 blocked + 2367 waiting + 0 sleeping) / 31160 samples = 7.60% wasted
 Consumer: (0 blocked + 6801 waiting + 0 sleeping) / 31115 samples = 21.86% wasted
 */
public class TestBlockingQueue {
	public static final int N = 80000000;

	public static void main(String[] args) throws Exception {
		MessageQueueAdaptor<Integer> queue = new MessageQueueAdaptor<Integer>(1024);
		MessageSequence<Integer> sequence = new IntegerSequence(1,N);
		TestRig.test(queue, sequence, N);
	}
}
