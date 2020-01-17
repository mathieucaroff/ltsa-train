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

	public void move() {
		this.getPos().getElem().decrementCount();
		pos = railway.getNextPosition(this.pos);
		this.getPos().getElem().incrementCount();
		System.out.println(toString());
	}

	public Position getPos() {
		return pos;
	}

	@Override
	public void run() {
		while (true) {
			this.move();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
