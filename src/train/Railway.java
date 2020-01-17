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

	public Railway(Element[] elements) {
		if (elements == null)
			throw new NullPointerException();

		this.elements = elements;
		for (Element e : elements)
			e.setRailway(this);
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
}
