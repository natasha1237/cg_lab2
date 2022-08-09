package src;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TriangleTest {
    private final Triangle triangle= new Triangle(new Point(0, 0, 0), new Point(0, 15, 0), new Point(0, 0, 15),
            Normal.create(0,0,0),Normal.create(0,0,0),Normal.create(0,0,0), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    @Test
    void intersectsWith() {
        Ray ray1 = new Ray(new Vector(-1, 0, 0).toNormal(), new Point(5, 0, 0));

        assertNotNull(triangle.intersectionWith(ray1));
    }
    @Test
    void notIntersectsWith() {
        Ray ray = new Ray(new Vector(1, 0, 0).toNormal(), new Point(5, 0, 0));
        assertNull(triangle.intersectionWith(ray));
    }
    @Test
    void IntersectInVerticle() {
        Ray ray = new Ray(new Vector(-1, 0, 0).toNormal(), new Point(5, 15, 0));
        assertNotNull(triangle.intersectionWith(ray));
    }
    @Test
    void NotIntersectNearVerticle() {
        Ray ray = new Ray(new Vector(-1, 0, 0).toNormal(), new Point(5, 15.1, 0));
        assertNull(triangle.intersectionWith(ray));
    }
    @Test
    void IntersectInEdge() {
        Ray ray = new Ray(new Vector(-1, 0, 0).toNormal(), new Point(5, 15/2*Math.sqrt(2), 15/2*Math.sqrt(2)/2));
        assertNotNull(triangle.intersectionWith(ray));
    }
    @Test
    void NotIntersectNearEdge() {
        Ray ray = new Ray(new Vector(-1, 0, 0).toNormal(), new Point(5, 15/2*Math.sqrt(2)+0.5, 15/2*Math.sqrt(2)+0.5));
        assertNull(triangle.intersectionWith(ray));
    }
    @Test
    void NotIntersectParallel() {
        Ray ray = new Ray(new Vector(0, -1, -1).toNormal(), new Point(5, 0, 0));
        assertNull(triangle.intersectionWith(ray));
    }
}
