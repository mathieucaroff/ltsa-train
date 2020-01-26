package train;

import java.util.Optional;

/**
 * Cette classe abstraite est la représentation générique d'un élément de base
 * d'un circuit, elle factorise les fonctionnalités communes des deux
 * sous-classes : l'entrée d'un train, sa sortie et l'appartenance au
 * circuit.<br/>
 * Les deux sous-classes sont :
 * <ol>
 * <li>La représentation d'une gare : classe {@link Station}</li>
 * <li>La représentation d'une section de voie ferrée : classe
 * {@link Section}</li>
 * </ol>
 *
 * Les éléments ont la responsabilité de connaître et de pouvoir désigner
 * les éléments adjacents. Il est prévu que les fins de lignes se désignent
 * elles-même comme élément adjacent.
 *
 * Les éléments ont la responsabilité de connaître le nombre de trains les
 * occupant (count), ainsi que leur capacité maximale (size).
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 * @author Mathieu CAROFF <mathieu.caroff@imt-atlantique.net>
 * @author Sébastien NAL <sebastien.nal@imt-atlantique.net>
 */
public abstract class Element {
	private final String name;
	protected int size;
	protected int count;
	protected Element nextElement;
	protected Element previousElement;

	protected Element(String name) {
		if (name == null)
			throw new NullPointerException();

		this.name = name;
	}

	public Element getNextElement() {
		return nextElement;
	}

	/**
	 * Indique à un élément quel est l'élément qui lui succède
	 */
	public void setNextElement(Element nextElement) {
		this.nextElement = nextElement;
	}

	public Element getPreviousElement() {
		return previousElement;
	}

	/**
	 * Indique à un élément quel est l'élément le précédant
	 */
	public void setPreviousElement(Element previousElement) {
		this.previousElement = previousElement;
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

	public synchronized void enter() {
		while (!hasRoom()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.count += 1;
		notifyAll();
	}

	public synchronized void leave() {
		if (getCount() == 0) {
			throw new RuntimeException("cannot decrement count already reached 0");
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

	public abstract Optional<Arc> getArc();
}
