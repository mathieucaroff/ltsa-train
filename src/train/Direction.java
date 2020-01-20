package train;

/**
 * Représentation de la direction que peut prendre un train : de gauche à droite
 * ou de droite à gauche.
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 */
public enum Direction {
	LR("from left to right") {
		public Direction opposite() {
			return Direction.RL;
		}
	},
	RL("from right to left") {
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
