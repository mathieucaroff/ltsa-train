package train;

/**
 * @author Fabien Dagnat <fabien.dagnat@imt-atlantique.fr>
 */
public class Main {

	private static void linkElementArray() {

	}

	public static void main(String[] args) {
		Station A = new Station("GareA", 4);
		Station C = new Station("GareC", 2);
		Station D = new Station("GareD", 4);
		Section AB = new Section("AB");
		Section BC = new Section("BC");
		Section CD = new Section("CD");
		Railway r = new Railway(new Element[] { A, AB, BC, C, CD, D });
		System.out.println("The railway is:");
		System.out.println("\t" + r);
		Position p = new Position(A, Direction.LR);
		try {
			Train t1 = new Train("1", r, p);
			Train t2 = new Train("2", r, p);
			Train t3 = new Train("3", r, p);
			// Train t4 = new Train("4", r, p);
			System.out.println(t1);
			System.out.println(t2);
			System.out.println(t3);
			// System.out.println(t4);
			Thread t01 = new Thread(t1);
			Thread t02 = new Thread(t2);
			Thread t03 = new Thread(t3);
			// Thread t04 = new Thread(t4);
			t01.start();
			t02.start();
			t03.start();
			// t04.start();
		} catch (BadPositionForTrainException e) {
			System.out.println("Le train " + e.getMessage());
		}

	}

}