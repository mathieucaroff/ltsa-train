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
	private final Railway railway;
	private Position pos;

	public Train(String name, Railway rail, Position p) throws BadPositionForTrainException {
		if (name == null || p == null)
			throw new NullPointerException();

		// A train should be first be in a station
		if (!(p.getElem() instanceof Station))
			throw new BadPositionForTrainException(name);

		this.name = name;
		this.railway = rail;
		this.pos = p.clone();
		this.pos.getElem().incrementCount();
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
		if (currentPos.getElem().toString() == "GareC" && currentPos.getDirection() == Direction.LR) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Position nextPos = railway.getNextPosition(currentPos);

		Direction myDirection = nextPos.getDirection();

		boolean leavingStation = currentPos.getElem().isStation() && !nextPos.getElem().isStation();
		boolean reachingStation = !currentPos.getElem().isStation() && nextPos.getElem().isStation();
		boolean sync = leavingStation || reachingStation;

		if (sync) {
			synchronized (this.railway) {
				if (leavingStation) {
					while (!railway.validateDirection(myDirection)) {
						try {
							railway.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					railway.setDirection(myDirection);
				}
				boolean success = changePosition(currentPos, nextPos);
				if (success) {
					if (leavingStation) {
						railway.incrementCount();
					} else {
						railway.decrementCount();
					}
				}
				railway.notifyAll();
				return success;
			}
		} else {
			return changePosition(currentPos, nextPos);
		}
	}

	private boolean changePosition(Position currentPos, Position nextPos) {
		Position posA = currentPos;
		Position posB = nextPos;
		if (currentPos.getDirection() == Direction.RL) {
			posA = nextPos;
			posB = currentPos;
		}
		// placer les verrous
		synchronized (posA.getElem()) {
			synchronized (posB.getElem()) {
				if (nextPos.getElem().hasRoom()) {
					currentPos.getElem().decrementCount();
					nextPos.getElem().incrementCount();
					setPos(nextPos);
					System.out.println(toString());
					return true;
				} else {
					System.out.println("no room left in the next position: " + toString());
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
	}
}
