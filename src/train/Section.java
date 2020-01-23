package train;

import java.util.Optional;

/**
 * Représentation d'une section de voie ferrée. C'est une sous-classe de la
 * classe {@link Element}.
 *
 * `Section` ont la résponsabilité de connaître l'arc dont elles font partie,
 * et de l'indiquer à qui le souhaite.
 *
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 * @author Mathieu CAROFF <mathieu.caroff@imt-atlantique.net>
 * @author Sébastien NAL <sebastien.nal@imt-atlantique.net>
 */
public class Section extends Element {
	private Arc arc;

	public Section(String name) {
		super(name);
		this.size = 1;
	}

	public boolean isStation() {
		return false;
	}

	public Optional<Arc> getArc() {
		return Optional.of(this.arc);
	}

	/**
	 * Indique à une section l'arc dont elle fait partie.
	 * Cette méthode n'est présumément apellée qu'une fois, dans le constructeur
	 * de l'arc.
	 */
	public void setArc(Arc arc) {
		this.arc = arc;
	}
}
