package train;

/**
 * Initialise et démarre la simulation du système ferrovière. Ceci inclut le
 * chaînage des éléments
 *
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 * @author Mathieu CAROFF <mathieu.caroff@imt-atlantique.net>
 * @author Sébastien NAL <sebastien.nal@imt-atlantique.net>
 */
public class Main {

	final static int NUMBER_OF_TRAINS = 4;
	final static int NUMBER_OF_STEPS = 600;

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Position pShort = shortRoad();
		Position pLong = longRoad();
		Position pIntermediate = intermediateStationRoad();

		Position p = pIntermediate;

		System.out.println("Railway:");
		System.out.println(describe(p));
		System.out.println();

		try {
			for (int k = 1; k <= NUMBER_OF_TRAINS; k++) {
				Train tt = new Train("" + k, p, NUMBER_OF_STEPS);
				System.out.println(tt);
				Thread tth = new Thread(tt);
				tth.start();
			}
		} catch (BadPositionForTrainException e) {
			System.out.println("Le train " + e.getMessage());
		}

	}

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

	private static Position shortRoad() {
		Station C = new Station("GareC", NUMBER_OF_TRAINS);
		Station D = new Station("GareD", NUMBER_OF_TRAINS);
		Section CD = new Section("CD");
		new Arc("CD", new Section[] { CD });

		linkElementArray(new Element[] { C, CD, D });
		Position p = new Position(C, Direction.LR);

		return p;
	}

	private static Position longRoad() {
		Station C = new Station("GareA", NUMBER_OF_TRAINS);
		Station D = new Station("GareG", NUMBER_OF_TRAINS);
		Section CD = new Section("CD");
		new Arc("CD", new Section[] { CD });

		linkElementArray(new Element[] { C, CD, D });
		Position p = new Position(C, Direction.LR);
		return p;
	}

	private static Position intermediateStationRoad() {
		Station A = new Station("GareA", NUMBER_OF_TRAINS);
		Station C = new Station("GareC", 2);
		Station D = new Station("GareD", NUMBER_OF_TRAINS);
		Section AB = new Section("AB");
		Section BC = new Section("BC");
		Section CD = new Section("CD");
		new Arc("ABC", new Section[] { AB, BC });
		new Arc("CD", new Section[] { CD });

		linkElementArray(new Element[] { A, AB, BC, C, CD, D });
		Position p = new Position(A, Direction.LR);

		return p;
	}

	public static String describe(Position p) {
		Element q = p.getElem();
		Element last_q = null;

		StringBuilder s = new StringBuilder();
		while (true) {
			s.append("" + q);
			if (q.isStation()) {
				s.append("(");
				s.append(q.getSize());
				s.append(")");
			}
			last_q = q;
			q = q.getNextElement();
			if (q == last_q) {
				break;
			}
			s.append("--");
		}

		return s.toString();
	}

}