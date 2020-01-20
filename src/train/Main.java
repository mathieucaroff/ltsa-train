package train;

/**
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 */
public class Main {

	private static void linkElementArray(Element[] elementArray) {
		for (int i = 0; i < elementArray.length - 1; i++) {
			elementArray[i].setNextElement(elementArray[i + 1]);
			elementArray[i + 1].setPreviousElement(elementArray[i]);
		}
		Element beginning = elementArray[0];
		beginning.setPreviousElement(beginning);
		Element end = elementArray[elementArray.length - 1];
		end.setNextElement(end);
	}

	public static void main(String[] args) {
		Station A = new Station("GareA", 4);
		Station C = new Station("GareC", 2);
		Station D = new Station("GareD", 4);
		Section AB = new Section("AB");
		Section BC = new Section("BC");
		Section CD = new Section("CD");
		Arc abcArc = new Arc("ABC", new Section[] { AB, BC });
		Arc CdArc = new Arc("CD", new Section[] { CD });
		Station stationArray[] = { A, C, D };
		Arc arcArray[] = { abcArc, CdArc };

		linkElementArray(new Element[] { A, AB, BC, C, CD, D });

		Railway r = new Railway(stationArray, arcArray);
		System.out.println("The railway is:");
		System.out.println("\n" + r + "\n");
		Position p = new Position(A, Direction.LR);
		try {
			for (int k = 1; k <= 4; k++) {
				Train tt = new Train("" + k, p);
				System.out.println(tt);
				Thread tth = new Thread(tt);
				tth.start();
			}
		} catch (BadPositionForTrainException e) {
			System.out.println("Le train " + e.getMessage());
		}

	}

}