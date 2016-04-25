package cs601.blkqueue;

import java.util.Map;

public class TestRig {
	public static final long MONITORING_PERIOD = 5000L; // 500us = 0.5ms
	public static <T> void test(MessageQueue<T> queue,
								MessageSequence<T> sequence,
								int N)
	{
		Producer p = new Producer<T>(queue, sequence);
		Consumer c = new Consumer<T>(queue, sequence);
		Thread producerThread = new Thread(p);
		Thread consumerThread = new Thread(c);
		producerThread.setName("ProducerThread");
		consumerThread.setName("ConsumerThread");
		producerThread.start();
		consumerThread.start();

		ThreadObserver producerObserver = new ThreadObserver(producerThread, MONITORING_PERIOD);
		ThreadObserver consumerObserver = new ThreadObserver(consumerThread, MONITORING_PERIOD);

		long start = System.nanoTime();

		Thread observerThread = new Thread(producerObserver);
		observerThread.setName("ProducerObserver");
		observerThread.start();
		observerThread = new Thread(consumerObserver);
		observerThread.setName("ConsumerObserver");
		observerThread.start();

		while ( !c.isDone() ) { } // wait for consumer to finish
		long stop = System.nanoTime();
		producerObserver.terminate();
		consumerObserver.terminate();

		long t = stop - start;
		double tms = t / 1000.0 / 1000;
		double ts = t / 1000.0 / 1000 / 1000;
		System.out.printf("%.3fms %.1f events / second\n", tms, (N/(float)ts));

		System.out.println("Producer: "+producerObserver);
		System.out.println("Consumer: " +consumerObserver);

		System.out.println("Producer:");
		Map<String, Long> psamples = producerObserver.getMethodSamples();
        dump(psamples);
		System.out.println();
		System.out.println("Consumer:");
		Map<String, Long> csamples = consumerObserver.getMethodSamples();
		dump(csamples);
	}

	public static void dump(Map<String, Long> histogram) {
		for (String sig : histogram.keySet()) {
            System.out.printf("%-7d %s\n", histogram.get(sig), sig+"()");
		}
	}
}
