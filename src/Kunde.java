public class Kunde {
	private double einkaufSumme;

	public Kunde() {
		einkaufSumme = Math.random() * 100;
	}

	public double getSumme() {
		return einkaufSumme;
	}
}
