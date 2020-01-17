package train;

/**
 * Représentation de la direction que peut prendre un train : de gauche à droite
 * ou de droite à gauche.
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 */
public enum Direction {
	LR {
		@Override
		public String toString() {
			return "from left to right";
		}
	},
	RL {
		@Override
		public String toString() {
			return "from right to left";
		}
	};
}
