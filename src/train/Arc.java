package train;

public class Arc {

	private final Section[] elements;
	private Direction direction;
	private int count;

	public Arc(Section[] elements) {

		direction = null;
		count = 0;

		if (elements == null)
			throw new NullPointerException();

		this.elements = elements;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		System.out.println("Direction was : " + this.direction + ", setting direction " + direction);
		this.direction = direction;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public synchronized void incrementCount() {
		this.count += 1;
	}

	public synchronized void decrementCount() {
		this.count -= 1;
		if (this.count == 0) {
			setDirection(null);
		}
	}

	public synchronized boolean validateDirection(Direction dir) {
		if (getDirection() == dir || getDirection() == null) {
			return true;
		} else {
			return false;
		}
	}

}
