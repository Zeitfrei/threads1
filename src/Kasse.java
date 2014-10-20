import java.util.LinkedList;
import java.util.Random;

public class Kasse extends Thread {

	private double umsatz = 0;
	private LinkedList<Kunde> warteschlange = new LinkedList<Kunde>();
	private int i = warteschlange.size();
	private int nmr;

	public void run() {
		try {
			Thread.currentThread().sleep(6000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		i = warteschlange.size();

		while (i > 0) {

			try {
				Thread.currentThread().sleep(
						6000 + ((long) (new Random().nextDouble() * 4000))
				);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			umsatz += warteschlange.removeFirst().getSumme();
			i = warteschlange.size();
			if (i > 0) {
				System.out.println("Kunde abgearbeitet, noch " + i
						+ " Kunden an Kasse " + nmr);
			} else {
				System.out.println(
						"Kunde abgearbeitet - keine Kunden mehr, Kasse "
						+ nmr + " schliesst."
				);
			}
		}
	}

	public void add(Kunde kunde, int nmr) {
		warteschlange.addLast(kunde);
		this.nmr = nmr;
	}

	public double getUmsatz() {
		return umsatz;
	}

	public int getNmr() {
		return nmr;
	}

	public int getSize() {
		return warteschlange.size();
	}
}
