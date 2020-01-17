package train;

/**
 * Cette classe abstraite est la représentation générique d'un élément de base
 * d'un circuit, elle factorise les fonctionnalitÃ©s communes des deux
 * sous-classes : l'entrée d'un train, sa sortie et l'appartenance au
 * circuit.<br/>
 * Les deux sous-classes sont :
 * <ol>
 * <li>La représentation d'une gare : classe {@link Station}</li>
 * <li>La représentation d'une section de voie ferrée : classe
 * {@link Section}</li>
 * </ol>
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 */
public abstract class Element {
	private final String name;
	protected Railway railway;
	protected int size;
	protected int count;

	protected Element(String name) {
		if (name == null)
			throw new NullPointerException();

		this.name = name;
	}

	public void setRailway(Railway r) {
		if (r == null)
			throw new NullPointerException();

		this.railway = r;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public int getCount() {
		return count;
	}

	/**
	 * this method should only be used at the initialization of the railway
	 */
	public synchronized void setCount(int count) {
		this.count = count;
	}

	public synchronized void incrementCount() {
		while (!hasRoom()) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.count += 1;
		notifyAll();
	}

	public synchronized void decrementCount() {
		if (getCount() == 0) {
			throw new RuntimeException();
		}
		this.count -= 1;
	}

	public int getSize() {
		return size;
	}

	public synchronized boolean hasRoom() {
		if (getCount() < getSize()) {
			return true;
		} else {
			return false;
		}
	}

	public abstract boolean isStation();
}
