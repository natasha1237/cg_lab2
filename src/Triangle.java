package src;

import java.util.ArrayList;

public class Triangle implements ObjectInterface {
    private Point v1;
    private Point v2;
    private Point v3;
    private Normal n1;
    private Normal n2;
    private Normal n3;
    private Vector color;
    private Vector t1;
    private Vector t2;
    private Vector t3;
    private MaterialInterface material;

    public Triangle(Point v1, Point v2, Point v3, Normal n1, Normal n2, Normal n3, ArrayList<Double> t1, ArrayList<Double> t2, ArrayList<Double> t3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
        this.t1 = new Vector(t1.get(0), t1.get(1), 0);
        this.t2 = new Vector(t2.get(0), t2.get(1), 0);
        this.t3 = new Vector(t3.get(0), t3.get(1), 0);
    }
    public void transform(Matrix4x4 m){
        this.v1 = m.multiplyPoint(v1);
        this.v2 = m.multiplyPoint(v2);
        this.v3 = m.multiplyPoint(v3);
        this.n1 = m.multiplyNormal(n1);
        this.n2 = m.multiplyNormal(n2);
        this.n3 = m.multiplyNormal(n3);
    }
    public Double intersectionWith(Ray ray) {
        Point o = ray.getOrigin();
        Vector d = ray.getDirection();

        Vector edge1 = v2.sub(v1);
        Vector edge2 = v3.sub(v1);

        Vector pvec = d.cross(edge2);

        double det = edge1.dot(pvec);

        double eps = 0.00001;
        if (det< eps){
            return null;
        }

        Vector tvec = o.sub(v1);

        double u = tvec.dot(pvec);

        if (u<0.0 || u>det){
            return null;
        }

        Vector qvec = tvec.cross(edge1);
        double v = d.dot(qvec);
        if (v<0.0 || u+v>det){
            return null;
        }

        double t = edge2.dot(qvec);
        double inv_det = 1.0/det;
        t *= inv_det;

        if (t >= 0) {
            return t;
        } else {
            return null;
        }
    }

    @Override
    public Normal getNormalAtPoint(Point p) {
        double ownArea = triangleArea(v1, v2, v3);
        double vArea = triangleArea(v2, v1, p);
        double uArea = triangleArea(v1, v3, p);
        double u = uArea / ownArea;
        double v = vArea / ownArea;

        return this.n2.mult(u).add(this.n3.mult(v).add(this.n1.mult(1-v-u))).toNormal();  // barycentric normal
    }

    private double triangleArea(Point v1, Point v2, Point v3) {
        Vector u = v2.sub(v1).cross(v3.sub(v1));
        return u.len() / 2;
    }

    public Point getCenter() {
        double avgX = (v1.x() + v2.x() + v3.x()) / 3;
        double avgY = (v1.y() + v2.y() + v3.y()) / 3;
        double avgZ = (v1.z() + v2.z() + v3.z()) / 3;

        return new Point(avgX, avgY, avgZ);
    }

    public Vector minBounds() {
        double minX = Math.min(v1.x(), Math.min(v2.x(), v3.x()));
        double minY = Math.min(v1.y(), Math.min(v2.y(), v3.y()));
        double minZ = Math.min(v1.z(), Math.min(v2.z(), v3.z()));

        return new Vector(minX, minY, minZ);
    }

    public Vector maxBounds() {
        double maxX = Math.max(v1.x(), Math.max(v2.x(), v3.x()));
        double maxY = Math.max(v1.y(), Math.max(v2.y(), v3.y()));
        double maxZ = Math.max(v1.z(), Math.max(v2.z(), v3.z()));

        return new Vector(maxX, maxY, maxZ);
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
        if (t1.x() <=1) {
            double ownArea = triangleArea(v1, v2, v3);
            double vArea = triangleArea(v2, v1, p);
            double uArea = triangleArea(v1, v3, p);
            double u = uArea / ownArea;
            double v = vArea / ownArea;
            Vector res = t2.mult(u).add(t3.mult(v).add(t1.mult(1 - v - u)));
            return new Double[]{res.x(), res.y()};
        }else{
            return new Double[]{null, null};
        }
    }
}
