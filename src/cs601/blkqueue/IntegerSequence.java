package cs601.blkqueue;

public class IntegerSequence implements MessageSequence<Integer> {
	protected int a, b;
	protected int i;

	public IntegerSequence(int a, int b) {
		this.a = a;
		this.b = b;
		i = a;
	}

	@Override
	public boolean hasNext() {
		return i<b;
	}

	@Override
	public Integer next() {
		int next = i;
		i++;
		return next;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Integer eof() {
		return -1;
	}

	@Override
	public boolean validSequenceMove(Integer previous, Integer current) {
		return current == previous + 1 || current==eof();
	}
}
