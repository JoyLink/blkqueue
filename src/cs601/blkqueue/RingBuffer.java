package cs601.blkqueue;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

public class RingBuffer<T> implements MessageQueue<T> {
	private final AtomicLong w = new AtomicLong(-1);	// just wrote location
	private final AtomicLong r = new AtomicLong(0);		// about to read location
    public  T buffer[] ;
    public  int mod;
    public int size;
    private int cnt;
    public boolean debug = false;
    public boolean stopPut, stopTake;
    private int mw, mr;
    private int nw, nr;
	public RingBuffer(int n) {
        if(!isPowerOfTwo(n)) throw new IllegalArgumentException();
        mod = n - 1;
        buffer = (T[])new Object[n];
        stopPut = false;
        stopTake = true;
        size = n;
        mw = 0;
        mr = 0;
        nw = -1;
        nr = 0;
    }

	// http://graphics.stanford.edu/~seander/bithacks.html#CountBitsSetParallel
	static boolean isPowerOfTwo(int v) {
		if (v<0) return false;
		v = v - ((v >> 1) & 0x55555555);                    // reuse input as temporary
		v = (v & 0x33333333) + ((v >> 2) & 0x33333333);     // temp
		int onbits = ((v + (v >> 4) & 0xF0F0F0F) * 0x1010101) >> 24; // count
		// if number of on bits is 1, it's power of two, except for sign bit
		return onbits==1;
	}

    //This part use less AtomicLong method which will show you a much more quick speed if you like.
    /**
	@Override
	public  void put(T v) throws InterruptedException {
        waitForFreeSlotAt(w.longValue()+mw);
        int l = nw+1 & mod;
        buffer[l] = v;
        //if(debug) System.out.println("put: "+v + " at: "+l + " w is: "+ (w.intValue()+mw));
        mw++;

        if(mw==1000) {
            mw = 0;
            w.getAndAdd(1000);
        }
        nw = w.intValue() + mw;

    }

	@Override
	public  T take() throws InterruptedException {
        waitForDataAt(r.longValue() + mr);
        int l=nr&mod;
        T ans = (T)buffer[l];
        //if(debug) System.out.println("take: "+ans+" at "+l + "r is: " + (r.longValue()+mr));
        mr++;
        if(mr == 1000) {
            mr = 0;
            r.getAndAdd(1000);
        }
        nr = r.intValue() + mr;

        return  ans;
    }

	// spin wait instead of lock for low latency store
	void waitForFreeSlotAt(final long writeIndex) throws InterruptedException {
        // wait until we have at least one spot, meaning w < r
        // since circular buffer though we worry about wrapping. We
        // have to wait if we've got n values in the buffer already.
        //while (writeIndex  >= mod + r.longValue() + mr) {
        while (nw >= mod + nr) {
            LockSupport.parkNanos(1);
        }
    }

	// spin wait instead of lock for low latency pickup
	void waitForDataAt(final long readIndex) throws InterruptedException {
        // wait until w catches up or passes desired read location
        // repeat until just-wrote-index >= about-to-read-index
        //while (readIndex >= w.intValue()+1 + mw) {
        while (nr >= nw+1) {
            LockSupport.parkNanos(1);
        }
    }
*/

     @Override
     public  void put(T v) throws InterruptedException {
     waitForFreeSlotAt(w.longValue());
     //int l = (int)((w.longValue() + 1)& mod );
     int l = (w.intValue() + 1) & mod;
     buffer[l] = v;
     //if(debug) System.out.println("put: "+v + " at: "+l + " w is: "+ w.longValue());
     w.getAndIncrement();
     }

     @Override
     public  T take() throws InterruptedException {
     waitForDataAt(r.longValue());
     //int l = (int) (r.longValue() & mod );
     int l = r.intValue() & mod;
     T ans = (T)buffer[l];
     //if(debug) System.out.println("take: "+ans+" at "+l + "r is: " + r.longValue());
     r.getAndIncrement();
     return  ans;
     }

     // spin wait instead of lock for low latency store
     void waitForFreeSlotAt(final long writeIndex) throws InterruptedException {
     // wait until we have at least one spot, meaning w < r
     // since circular buffer though we worry about wrapping. We
     // have to wait if we've got n values in the buffer already.
     while (writeIndex - r.longValue() >= mod) {
     LockSupport.parkNanos(1);
     }
     }

     // spin wait instead of lock for low latency pickup
     void waitForDataAt(final long readIndex) throws InterruptedException {
     // wait until w catches up or passes desired read location
     // repeat until just-wrote-index >= about-to-read-index
     while (readIndex >= w.longValue()+1) {
     LockSupport.parkNanos(1);
     }
     }

}
