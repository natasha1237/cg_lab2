package src;

public class Ray {
	private final Normal direction;
	private final Point origin;

	public Ray(Normal direction, Point origin) {
		this.direction = direction;
		this.origin = origin;
	}

	public Point getOrigin() {
		return origin;
	}

	public Vector getDirection() {
		return direction;
	}

	public Point getPointAt(double t) {
		Vector dist = direction.mult(t);
		return origin.add(dist);
	}
}
