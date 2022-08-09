package src;

public class DirectedLightSource implements LightInterface {
	private final Normal direction;
	private final Vector color;
	private final Vector intens;
	public DirectedLightSource(Normal direction, Vector color, float[] intens) {
		this.color = color;
		this.direction = direction.reverse().toNormal();
		this.intens = new Vector(intens[0], intens[1], intens[2]);
	}

	public Normal getDirection(Point point) {
		return direction;
	}
	public Vector getColor() { return color; }
	public Vector getIntensivity() {return intens; }

	@Override
	public double calcLighting(Normal normalAtPoint, Point point, BoundingTreeInterface tree) {
		double dotProduct = this.getDirection(point).dot(normalAtPoint);

		if (dotProduct < 0) {
			return 0;
		} else {
			return dotProduct;
		}
	}
}
