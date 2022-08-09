package src;

public class Mirror implements MaterialInterface {

    public Mirror(){

    }
    @Override
    public Vector bxdfFunc(Vector wo, Vector wi, ObjectInterface obj, Point p) {
        return new Vector(1,1,1);//null;
    }
    public float isMirror(){
        return 1f;
    }
    @Override
    public Ray reflectedRay(Vector dir, Normal normalAtPoint, Point intersectionPoint) {
        Ray reflect_ray = new Ray(dir.sub(normalAtPoint.mult(dir.dot(normalAtPoint) * 2)).toNormal(), intersectionPoint);
        return reflect_ray;
    }
}
