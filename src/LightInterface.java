package src;

public interface LightInterface {
    double calcLighting(Normal normalAtPoint, Point point, BoundingTreeInterface tree);
    Normal getDirection(Point point);
    Vector getColor();
    Vector getIntensivity();
}
