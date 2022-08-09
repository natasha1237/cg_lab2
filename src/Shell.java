package src;

public class Shell {
	private final Vector min;
	private final Vector max;

	public Shell(Vector minBounds, Vector maxBounds) {
		this.min = minBounds;
		this.max = maxBounds;
	}

	public double area() {
		Vector sides = max.sub(min);
		double xy = sides.x() * sides.y();
		double xz = sides.x() * sides.z();
		double yz = sides.y() * sides.z();

		return (xy + xz + yz) * 2;
	}

	public boolean intersectionWith(Ray ray) {
		Point origin = ray.getOrigin();
		Vector direction = ray.getDirection();

		double tmin = (min.x() - origin.x()) / direction.x();
		double tmax = (max.x() - origin.x()) / direction.x();

		if (tmin > tmax) {
			double temp = tmin;
			tmin = tmax;
			tmax = temp;
		}

		double tymin = (min.y() - origin.y()) / direction.y();
		double tymax = (max.y() - origin.y()) / direction.y();

		if (tymin > tymax) {
			double temp = tymin;
			tymin = tymax;
			tymax = temp;
		}

		if ((tmin > tymax) || (tymin > tmax))
			return false;

		if (tymin > tmin)
			tmin = tymin;

		if (tymax < tmax)
			tmax = tymax;

		double tzmin = (min.z() - origin.z()) / direction.z();
		double tzmax = (max.z() - origin.z()) / direction.z();

		if (tzmin > tzmax) {
			double temp = tzmin;
			tzmin = tzmax;
			tzmax = temp;
		}

		if ((tmin > tzmax) || (tzmin > tmax))
			return false;

		return true;
	}
}
