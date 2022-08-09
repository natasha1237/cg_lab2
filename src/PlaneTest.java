package src;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {
	private final Plane plane = new Plane(new Point(0, 0, 0), Normal.create(2, 3, 2));
	@Test
	void intersectsWith() {
		Ray ray = new Ray(new Vector(2, 3, 2).toNormal(), new Point(-5, -3, -5));
		assertNotNull(plane.intersectionWith(ray));
	}
	@Test
	void intersectsWithOpposite() {
		Ray oppositeRay = new Ray(new Vector(-2, -3, -2).toNormal(), new Point(-5, -3, -5));
		assertNull(plane.intersectionWith(oppositeRay));
	}
	@Test
	void intersectsWithOpposite2() {
		Ray oppositeRay1 = new Ray(new Vector(-2, -3, -2).toNormal(), new Point(5, 3, 5));
		assertNull(plane.intersectionWith(oppositeRay1));
	}
	@Test
	void intersectsWithInside() {
		Ray rayInsidePlane = new Ray(new Vector(1, -1, 0.5).toNormal(), new Point(0, 0, 0));
		assertNull(plane.intersectionWith(rayInsidePlane));
	}
	@Test
	void intersectsWithParallel() {
		Ray parallelRay = new Ray(new Vector(1, -1, 0.5).toNormal(), new Point(5, 3, 5));
		assertNull(plane.intersectionWith(parallelRay));
	}
}