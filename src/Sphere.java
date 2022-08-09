package src;

public class Sphere implements ObjectInterface {
	private final Point center;
	private final double radius;
	private Vector color;
	private MaterialInterface material;

	public Sphere(Point center, double radius) {
		this.center = center;
		this.radius = radius;
	}

	public Double intersectionWith(Ray ray) {
		Point o = ray.getOrigin();
		Vector k = o.sub(center);
		Vector d = ray.getDirection();

		double d2 = d.dot(d);
		double r2 = radius * radius;
		double k2 = k.dot(k);

		double a = d2;
		double b = 2 * d.dot(k);
		double cc = k2 - r2;

		double D = b * b - 4 * a * cc;

		if (D >= 0) {
			return (-b - Math.sqrt(D)) / 2 * a;
		} else {
			return null;
		}
	}

	@Override
	public Normal getNormalAtPoint(Point p) {
		return p.sub(center).toNormal();
	}
	public Vector getColor(){
		return color;
	}

	public void setColor(Vector c) {
		this.color = c;
	}
	public MaterialInterface getMaterial() { return this.material;}
	public void setMaterial(MaterialInterface material){ this.material = material;}
	public Double[] getUvBari(Point p){
		return null; //not implemented
	}
}
