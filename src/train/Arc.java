package train;

public class Arc {

	private final String name;
	private final Section[] sectionArray;
	private Direction direction;
	private int count;

	public Arc(String name, Section[] sectionArray) {
		this.name = name;
		direction = null;
		count = 0;

		if (sectionArray.length == 0) {
			throw new IllegalArgumentException("sectionArray is empty");
		}

		this.sectionArray = sectionArray;

		for (Section section : sectionArray) {
			section.setArc(this);
		}
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		System.out.println(this.name + " change direction " + this.direction + " to direction " + direction);
		this.direction = direction;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public synchronized void enter() {
		this.count += 1;
	}

	public synchronized void leave() {
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

	@Override
	public String toString() {
		return this.name;
	}

	public Section getOtherStation(Element elem) {
		if (elem == this.sectionArray[0]) {
			return this.sectionArray[this.sectionArray.length - 1];
		} else {
			return this.sectionArray[0];
		}
	}

}
