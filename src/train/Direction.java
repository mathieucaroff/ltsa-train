package train;

/**
 * Représentation de la direction que peut prendre un train : de gauche à droite
 * ou de droite à gauche.
 *
 * Note: - la direction LR est le sens naturel, le sens à l'aller ("next") - la
 * direction RL est le sens au retour ("previous")
 *
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 * @author Mathieu CAROFF <mathieu.caroff@imt-atlantique.net>
 * @author Sébastien NAL <sebastien.nal@imt-atlantique.net>
 */
public enum Direction {
	LR("right") {
		public Direction opposite() {
			return Direction.RL;
		}
	},
	RL("left") {
		public Direction opposite() {
			return Direction.LR;
		}
	};

	private String name;

	Direction(String name) {
		this.name = name;
	}

	public abstract Direction opposite();

	@Override
	public String toString() {
		return name;
	}
}
