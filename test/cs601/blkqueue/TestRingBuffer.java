package cs601.blkqueue;

/*
from terminal not intellij seems faster, more stable.
jdk 7 N = 80,000,000 -server
4187.161ms 19106024.0 events / second
Producer: (0 blocked + 0 waiting + 1911 sleeping) / 6127 samples = 31.19% wasted
Consumer: (0 blocked + 0 waiting + 2358 sleeping) / 6129 samples = 38.47% wasted
 */
public class TestRingBuffer {
	public static final int N = 80000000;

	public static void main(String[] args) throws Exception {
		RingBuffer<Integer> queue = new RingBuffer<Integer>(1024);
		MessageSequence<Integer> sequence = new IntegerSequence(1,N);
		TestRig.test(queue, sequence, N);
	}
}
