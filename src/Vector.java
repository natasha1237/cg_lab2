package src;

public class Vector {
	private final double x;
	private final double y;
	private final double z;

	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	public double z() {
		return z;
	}

	public Vector add(Vector v) {
		return new Vector(x + v.x, y + v.y, z + v.z);
	}

	public Vector sub(Vector v) {
		return new Vector(x - v.x, y - v.y, z - v.z);
	}

	public double dot(Vector v) {
		return x * v.x + y * v.y + z * v.z;
	}
	public Vector dot_el(Vector v) {
		return new Vector(x * v.x, y * v.y, z * v.z);
	}
	public Vector mult(double k) {
		return new Vector(x * k, y * k, z * k);
	}

	public Vector cross(Vector v) {
		double cx = y * v.z - z * v.y;
		double cy = z * v.x - x * v.z;
		double cz = x * v.y - y * v.x;

		return new Vector(cx, cy, cz);
	}

	public Vector normalize() {
		double len = Math.sqrt(x * x + y * y + z * z);
		return new Vector(x / len, y / len, z / len);
	}

	public Normal toNormal() {
		return Normal.create(x, y, z);
	}

	public double len() { return Math.sqrt(x * x + y * y + z * z); }

	public Vector reverse() { return new Vector(-x, -y, -z); }
}
