package src;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {
	private final Sphere sphere = new Sphere(new Point(0, 0, 0), 4);
	@Test
	void intersectsWith() {
		Ray ray1 = new Ray(new Vector(2, 3, 2).toNormal(), new Point(-10, -3, -5));
		assertNull(sphere.intersectionWith(ray1));
	}
	@Test
	void notIntersectsWith() {
		Ray ray = new Ray(new Vector(2, 3, 2).toNormal(), new Point(-5, -3, -5));
		assertNotNull(sphere.intersectionWith(ray));
	}
	@Test
	void IntersectsInside() {
		Ray ray = new Ray(new Vector(2, 3, 2).toNormal(), new Point(0, 0, 0));
		assertNotNull(sphere.intersectionWith(ray));
	}
	@Test
	void IntersectsStartOutsideSphere() {
		Ray ray = new Ray(new Vector(2, 3, 2).toNormal(), new Point(4, 4, 4));
		assertNotNull(sphere.intersectionWith(ray));
	}
}