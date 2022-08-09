package src;

public interface MaterialInterface {
    float isMirror();
    Vector bxdfFunc(Vector wo, Vector wi, ObjectInterface obj, Point p);
    Ray reflectedRay(Vector dir, Normal normalAtPoint, Point intersectionPoint);
}
