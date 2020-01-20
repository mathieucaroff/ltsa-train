package train;

/**
 * Représentation d'un circuit constitué d'éléments de voie ferrée : gare ou
 * section de voie
 * 
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Philippe Tanguy <philippe.tanguy@imt-atlantique.fr>
 */
public class Railway {
	private final Station[] stationArray;
	private final Arc[] arcArray;

	public Railway(Station[] stationArray, Arc[] arcArray) {
		super();
		if (stationArray.length != arcArray.length + 1) {
			throw new RuntimeException();
		}
		this.stationArray = stationArray;
		this.arcArray = arcArray;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Station e : this.stationArray) {
			if (first)
				first = false;
			else
				result.append("--");
			result.append(e);
		}
		result.append(":\n:");
		for (Arc e : this.arcArray) {
			if (first)
				first = false;
			else
				result.append("--");
			result.append(e);
		}
		return result.toString();
	}
}
