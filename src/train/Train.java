package train;

/**
 * Représentation d'un train. Un train est caractérisé par deux valeurs :
 * <ol>
 * <li>Son nom pour l'affichage.</li>
 * <li>La position qu'il occupe dans le circuit (un élément avec une direction)
 * : classe {@link Position}.</li>
 * </ol>
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Mayte segarra <mt.segarra@imt-atlantique.fr> Test if the first
 *         element of a train is a station
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 * @version 0.3
 */
public class Train implements Runnable {
	private final String name;
	private Position pos;

	public Train(String name, Position pos) throws BadPositionForTrainException {
		if (name == null || pos == null)
			throw new NullPointerException();

		// A train should be first be in a station
		if (!(pos.getElem() instanceof Station))
			throw new BadPositionForTrainException(name);

		this.name = name;
		this.pos = pos;
		this.pos.getElem().enter();
	}

	private void setPos(Position pos) {
		this.pos = pos;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("Train[");
		result.append(this.name);
		result.append("]");
		result.append(" is on ");
		result.append(this.pos);
		return result.toString();
	}

	/**
	 * Try to move to the next position
	 * 
	 * @return true iff the train did move
	 */
	public synchronized boolean move() {
		Position currentPos = getPos();
		Position nextPos = this.computeNextPosition();

		Direction myDirection = nextPos.getDirection();

		boolean leavingStation = currentPos.getElem().isStation() && !nextPos.getElem().isStation();
		boolean reachingStation = !currentPos.getElem().isStation() && nextPos.getElem().isStation();

		if (!reachingStation && !leavingStation) {
			return changePosition(currentPos, nextPos, reachingStation);
		} else if (reachingStation) {
			Arc arc = currentPos.getElem().getArc().get();

			synchronized (arc) {
				boolean success = changePosition(currentPos, nextPos, reachingStation);
				if (success) {
					arc.leave();
				}
				return success;
			}
		} else { // leavingStation == true
			Arc arc = nextPos.getElem().getArc().get();

			Section section = arc.getOtherStation(nextPos.getElem());
			Element stationElement = nextPos.getDirection() == Direction.LR ? section.getNextElement()
					: section.getPreviousElement();

			if (!stationElement.isStation()) {
				throw new RuntimeException();
			}

			synchronized (stationElement) {
				if (stationElement.hasRoom()) {
					synchronized (arc) {
						if (arc.validateDirection(myDirection)) {
							boolean success = changePosition(currentPos, nextPos, reachingStation);
							if (success) {
								arc.setDirection(myDirection);
								arc.enter();
								stationElement.enter();
							}
							return success;
						} else {
							System.out.println("Stopped (direction): " + toString());
							return false;
						}
					}
				} else {
					System.out.println("Stopped (station room): " + toString());
					return false;
				}
			}
		}
	}

	private Position computeNextPosition() {
		Element currentElement = getPos().getElem();
		Direction direction = getPos().getDirection();
		Element nextElement = (direction == Direction.LR) ? currentElement.getNextElement()
				: currentElement.getPreviousElement();
		if (nextElement == currentElement) {
			return new Position(nextElement, direction.opposite());
		} else {
			return new Position(nextElement, direction);
		}
	}

	private boolean changePosition(Position currentPos, Position nextPos, boolean reachingStation) {
		Position posA = currentPos;
		Position posB = nextPos;
		if (currentPos.getDirection() == Direction.RL) {
			posA = nextPos;
			posB = currentPos;
		}
		// placer les verrous
		synchronized (posA.getElem()) {
			synchronized (posB.getElem()) {
				if (reachingStation || nextPos.getElem().hasRoom()) {
					currentPos.getElem().leave();
					if (reachingStation) {
						// enter has been done previously
					} else {
						nextPos.getElem().enter();
					}
					setPos(nextPos);
					System.out.println(toString());
					return true;
				} else {
					System.out.println("Stopped (room): " + toString());
					return false;
				}
			}
		}
	}

	public Position getPos() {
		return pos;
	}

	@Override
	public void run() {
		for (int i = 0; i < 300; i++) {
			this.move();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("DONE (" + toString() + ")");
		System.exit(0);
	}
}
