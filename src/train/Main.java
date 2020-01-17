package train;

/**
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 */
public class Main {
	public static void main(String[] args) {
		Station A = new Station("GareA", 3);
		Station D = new Station("GareD", 3);
		Section AB = new Section("AB");
		Section BC = new Section("BC");
		Section CD = new Section("CD");
		Railway r = new Railway(new Element[] { A, AB, BC, CD, D });
		System.out.println("The railway is:");
		System.out.println("\t" + r);
		Position p = new Position(A, Direction.LR);
		try {
			Train t1 = new Train("1", p);
			Train t2 = new Train("2", p);
			Train t3 = new Train("3", p);
			System.out.println(t1);
			System.out.println(t2);
			System.out.println(t3);
		} catch (BadPositionForTrainException e) {
			System.out.println("Le train " + e.getMessage());
		}

	}
}
