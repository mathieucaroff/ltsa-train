package train;

/**
 * Représentation d'un circuit constitué d'éléments de voie ferrée : gare ou
 * section de voie
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 */
public class Railway {
	private final Element[] elements;
	private Direction direction;
	private int count;

	public Railway(Element[] elements) {
		direction = null;
		count = 0;

		if (elements == null)
			throw new NullPointerException();

		this.elements = elements;
		for (Element e : elements)
			e.setRailway(this);
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		System.out.println("Setting direction " + direction);
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

	public Position getNextPosition(Position current) {
		Element currentElem = current.getElem();
		Direction currentDirection = current.getDirection();
		int currentIndex = -1;
		for (int k = 0; k < elements.length; k++) {
			if (elements[k] == currentElem) {
				currentIndex = k;
			}
		}
		if (currentIndex == -1) {
			throw new RuntimeException();
		}
		if (currentDirection == Direction.LR) {
			if (currentIndex < elements.length - 1) {
				return new Position(elements[currentIndex + 1], currentDirection);
			} else {
				return new Position(elements[currentIndex], Direction.RL);
			}
		} else {
			if (currentIndex > 0) {
				return new Position(elements[currentIndex - 1], currentDirection);
			} else {
				return new Position(elements[currentIndex], Direction.LR);
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Element e : this.elements) {
			if (first)
				first = false;
			else
				result.append("--");
			result.append(e);
		}
		return result.toString();
	}

	public synchronized boolean validateDirection(Direction dir) {
		if (getDirection() == dir || getDirection() == null) {
			return true;
		} else {
			return false;
		}
	}
}
