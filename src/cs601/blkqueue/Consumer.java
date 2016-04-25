package cs601.blkqueue;

class Consumer<T> implements Runnable {
	protected MessageQueue<T> queue;
	protected MessageSequence<T> sequence; // used only for testing the sequence
	protected volatile boolean done = false;
    //private boolean debug = false;
	public Consumer(MessageQueue<T> queue, MessageSequence<T> sequence) {
        this.queue = queue;
        this.sequence = sequence;
    }

	@Override
	public void run() {
		try {
			T eof = sequence.eof();
			T o = queue.take();
			T prev = null;
			while ( o != eof ) {
				o = queue.take();
				if ( prev!=null ) {
					if ( !sequence.validSequenceMove(prev, o) ) {
						throw new IllegalStateException("Invalid sequence move from "+prev+" to "+o);
					}
				}
                //if(debug) System.out.println("what's now "+o);
                prev = o;
			}
			done = true;
		}
		catch (InterruptedException ie) {
			ie.printStackTrace(System.err);
		}
	}
	public boolean isDone() { return done; }
}
