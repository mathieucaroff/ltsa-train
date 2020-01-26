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
 * @author Mathieu CAROFF <mathieu.caroff@imt-atlantique.net>
 * @author Sébastien NAL <sebastien.nal@imt-atlantique.net>
 * @version 0.3
 */
public class Train implements Runnable {
	private final String name;
	private Position pos;
	private final int step_limit;

	public Train(String name, Position pos, int step_limit) throws BadPositionForTrainException {
		if (name == null || pos == null)
			throw new NullPointerException();

		// A train should first be in a station
		if (!(pos.getElem().isStation()))
			throw new BadPositionForTrainException(name);

		this.name = name;
		this.pos = pos;
		this.pos.getElem().enter();
		this.step_limit = step_limit;
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
	 * Tente de déplacer le train vers sa prochaine position
	 * 
	 * @return true si le train s'est déplacé
	 */
	public synchronized boolean move() {
		/**
		 * La stratégie de déplacement du train est de déterminer les verrous
		 * nécessaires, de les acquérir dans l'ordre, et de tenter d'effectuer le
		 * déplacement.
		 * 
		 * Si le déplacement n'est pas possible en raison de la direction de l'arc, ou
		 * bien en raison d'un manque de place sur les rails ou dans une gare, la
		 * procédure est interrompue au plus tôt et la valeur fonction renvoie `false`
		 * pour indiquer l'échec du déplacement.
		 */
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
			synchronized (arc) {
				if (arc.validateDirection(myDirection)) {
					/**
					 * /!\ the below lock, on `stationElement` does not respect the lock order This
					 * is not an issue and will never cause a deadlock.
					 *
					 * The cycle of locks cannot be closed because the synchronisation of a station
					 * element can happen in exactly two situations:
					 * 
					 * 1. a train is turning over on a station
					 * 
					 * -- this only requires one lock, so it cannot participate in a deadlock cycle
					 * 
					 * 2. a train is entering or leaving an arc
					 * 
					 * -- entering or leaving an arc always requires locking the arc first. This
					 * way, the train on the entering or leaving the other station on the other end
					 * of the arc will not be able to lock the arc, and thus, will not be able to
					 * close the lock cycle.
					 */
					synchronized (stationElement) {
						if (stationElement.hasRoom()) {
							boolean success = changePosition(currentPos, nextPos, reachingStation);
							if (success) {
								arc.setDirection(myDirection);
								arc.enter();
								stationElement.enter();
							}
							return success;
						} else {
							System.out.println("Stopped (station room): " + toString());
							return false;
						}
					}
				} else {
					System.out.println("Stopped (direction): " + toString());
					return false;
				}
			}
		}
	}

	/**
	 * Calcule la prochaine position que le train occupera en fonction de sa position
	 * courante.
	 */
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

	/**
	 * Déplace le train d'une position donnée à une autre position. Cette méthode est
	 * appelée par `move()`.
	 * 
	 * @param currentPos
	 * @param nextPos
	 * @param reachingStation
	 * @return `true` si le changement de position a réussi, `false` sinon
	 */
	private boolean changePosition(Position currentPos, Position nextPos, boolean reachingStation) {
		Position posA = currentPos;
		Position posB = nextPos;
		if (currentPos.getDirection() == Direction.RL) {
			posA = nextPos;
			posB = currentPos;
		}
		// placer les verrous
		synchronized (posA.getElem()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
		for (int i = 0; i < this.step_limit; i++) {
			this.move();

			try {
				Thread.sleep(0, 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("DONE (" + toString() + ")");
		System.exit(0);
	}
}
