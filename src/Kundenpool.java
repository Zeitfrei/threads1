import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Kundenpool extends Thread {

	private int size, tempo, r, count = 0;
	private boolean kassenBool = true; // alle boolean in einer Zeile machten
										// Probleme
	private boolean whileBool = true;
	private boolean stringBool = true;
	private Kasse k1, k2, k3, k4, k5, k6;
	private Kasse[] kArr = { k1, k2, k3, k4, k5, k6 };
	private Double[] compare = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
	private double temp;
	private DecimalFormat zweiNachkomma = new DecimalFormat("#,##0.00");

	@Override
	public void run() {

		for (int i = 0; i < 6; i++) {
			kArr[i] = new Kasse();
		}
		while (whileBool) {
			while (size < 9) {
				r = 0;
				count = 0;

				/**
				 * Sucht eine zufällige offene Kasse, wenn keine Kasse offen ist
				 * wird
				 */
				for (int o = 0; o < 6; o++) {
					if (kArr[o].isAlive()) {
						kassenBool = false;
						do {
							r = (int) (Math.random() * 6); // sucht eine
															// zufällige offene
															// Kasse
						} while (!kArr[r].isAlive());
						break;
					}
				}

				try {
					Thread.currentThread().sleep((long) (Math.random() * 2000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				/**
				 * Hier wird eine neue Kasse aufgemacht wenn in der aktuellen
				 * Kasse 5 Kunden sind (6 nachdem eienr hinzugefügt wurde)
				 */
				if (kArr[r].getSize() == 5) {
					for (int q = 0; q < 6; q++) {
						if (!kArr[q].isAlive()) {
							kArr[q] = new Kasse();
							kArr[q].start();
							System.out.println("Kasse " + r
									+ " hat 6 Kunden - neue Kasse " + q
									+ " aufgemacht");
							break;
						}
					}
				}

				/**
				 * Wenn keine Kasse offen ist wird hier eine neue
				 * aufgemacht(Kunde hinzugefügt und Thread gestartet Wenn eine
				 * Kasse offen ist wird an dieser ein neuer Kunde angehängt
				 */
				if (kassenBool) { // das heißt es wurde keiner gefunden der
									// offen ist und es muss eine geöffnet
									// werden
					if (kArr[0].getState().equals(Thread.State.TERMINATED)) {
						kArr[0] = new Kasse();
						kArr[0].start();
						kArr[0].add(new Kunde(), 0);
						System.out
								.println("Keine Kasse offen, Kasse 0 wird aufgemacht");
					}
					if (kArr[0].getState().equals(Thread.State.NEW)) {
						kArr[0].add(new Kunde(), 0);
						kArr[0].start();
						System.out
								.println("Keine Kasse offen, Kasse 0 wird aufgemacht");
					}
				} else {
					kArr[r].add(new Kunde(), r);
					System.out.println("Kunde an offener Kasse " + r
							+ " angereiht");
				}

				/**
				 * In der for-Schleife wird die Bilanz geschrieben und die
				 * längste Warteschlange als Abbruchkriterium gesucht
				 */
				for (Kasse k : kArr) {
					if (compare[count] < k.getUmsatz()) { // bilanz wird
															// geschrieben
						compare[count] = k.getUmsatz();
					}
					if (k.getSize() > tempo) { // hier wird die größte schlange
												// gesucht als abbruchkriterium
						size = k.getSize();
					}
					tempo = k.getSize();
					count++;
				}
			}

			/**
			 * Hier wird angenommen, dass die erste Kasse zuletzt schliesst
			 * Nachdem sie geschlossen ist, wird die Schleife verlassen und der
			 * Umsatz nicht mehr gezählt
			 */
			if (kArr[0].isAlive()) {
				whileBool = true;
			} else {
				whileBool = false;
			}
			/**
			 *
			 */
			for (int i = 0; i < 6; i++) {
				if (compare[i] < kArr[i].getUmsatz()) {
					compare[i] = kArr[i].getUmsatz();
				}
			}

			if (stringBool) {
				System.out.println("Akquise der Kunden beendet.");
			}

			stringBool = false;
		}

		/**
		 * Array mit der Bilanz wird sortiert
		 */
		for (int i = 0; i < 6; i++) {
			for (int j = 1; j < 6; j++) {
				if (compare[j - 1] < compare[j]) {
					temp = compare[j - 1];
					compare[j - 1] = compare[j];
					compare[j] = temp;
				}
			}
		}
		System.out.println("Geschäftsende");
		System.out.println("Bilanz:");

		/**
		 * Hier wird die Bilanz mit auf 2 Nachkomma stellen gerundet und
		 * ausgegeben
		 */
		for (int i = 0; i < 6; i++) {
			System.out.println("Kasse: " + i + "   Umsatz: "
					+ zweiNachkomma.format(compare[i]) + ".-");
		}
	}
}
