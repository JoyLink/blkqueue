package cs601.blkqueue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;
/** A runnable class that attaches to another thread and wakes up
 *  at regular intervals to determine that thread's state. The goal
 *  is to figure out how much time that thread is blocked, waiting,
 *  or sleeping.
 */
class ThreadObserver implements Runnable {
    protected final Map<String, Long> histogram = new HashMap<String, Long>();
    protected int numEvents = 0;
    protected int blocked = 0;
    protected int waiting = 0;
    protected int sleeping = 0;
    protected  Thread threadtomonitor;
    public long l;
    public boolean go = true;
    public String keyk;
    public StackTraceElement[] se = new StackTraceElement[1];
    public ThreadObserver(Thread threadToMonitor, long periodInNanoSeconds) {
	    threadtomonitor = threadToMonitor;
        l = periodInNanoSeconds;
    }

	@Override
	public synchronized void  run() {

        while (go) {
            numEvents++;
            switch ( threadtomonitor.getState() ) {
                case BLOCKED: blocked++; break;
                case WAITING: waiting++; break;
                case TIMED_WAITING: sleeping++; break;
            }
            //This sentence cost 3/4 of the total time
            se = threadtomonitor.getStackTrace();
            if(se.length != 0) {
                keyk = se[0].getClassName() + "." + se[0].getMethodName();
                if (histogram.containsKey(keyk)) {
                    histogram.put(keyk, histogram.get(keyk)+1);
                } else {
                    histogram.put(keyk, (long) 1);
                }
            }
            LockSupport.parkNanos(l);
        }
	}

	public Map<String, Long> getMethodSamples() {
        return histogram;
    }

	public void terminate() {
	    go = false;
    }

	public String toString() {
		return String.format("(%d blocked + %d waiting + %d sleeping) / %d samples = %1.2f%% wasted",
							 blocked,
							 waiting,
							 sleeping,
							 numEvents,
							 100.0*(blocked + waiting + sleeping)/numEvents);
	}
}
