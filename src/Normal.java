package src;

public class Normal extends Vector {

	private Normal(double x, double y, double z) {
		super(x, y, z);
	}

	public static Normal create(double x, double y, double z) {
		double len = Math.sqrt(x * x + y * y + z * z);
		return new Normal(x / len, y / len, z / len);
	}
}
