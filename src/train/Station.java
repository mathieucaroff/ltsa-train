package train;

import java.util.Optional;

/**
 * Représentation d'une gare. C'est une sous-classe de la classe
 * {@link Element}. Une gare est caractérisée par un nom et un nombre de quais
 * (donc de trains qu'elle est susceptible d'accueillir à un instant donné).
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 * @author Mathieu CAROFF <mathieu.caroff@imt-atlantique.net>
 * @author Sébastien NAL <sebastien.nal@imt-atlantique.net>
 */
public class Station extends Element {

	public Station(String name, int size) {
		super(name);
		if (name == null || size <= 0)
			throw new NullPointerException();
		this.size = size;
	}

	public boolean isStation() {
		return true;
	}

	public Optional<Arc> getArc() {
		return Optional.empty();
	}
}
