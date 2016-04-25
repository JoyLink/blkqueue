package cs601.blkqueue;

import java.util.Iterator;

public interface MessageSequence<T> extends Iterator<T> {
	boolean validSequenceMove(T previous, T current);
	/** Sentinel object indicating that no further messages are available */
	T eof();
}
