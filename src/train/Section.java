package train;

import java.util.Optional;

/**
 * Représentation d'une section de voie ferrée. C'est une sous-classe de la
 * classe {@link Element}.
 *
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
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

	public void setArc(Arc arc) {
		this.arc = arc;
	}
}
