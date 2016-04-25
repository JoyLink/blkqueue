package cs601.blkqueue;

class Producer<T> implements Runnable {
	protected MessageQueue<T> queue;
	protected MessageSequence<T> sequence;
    private boolean debug = false;
	Producer(MessageQueue<T> queue, MessageSequence<T> sequence) {
		this.queue = queue;
		this.sequence = sequence;
	}

	@Override
	public void run() {
		try {
			while (sequence.hasNext()) {
				T next = sequence.next();
				queue.put(next);
			    if(debug) System.out.println("next is "+next);
            }
			queue.put(sequence.eof());
		}
		catch (InterruptedException ie) {
			ie.printStackTrace(System.err);
		}
	}
}
