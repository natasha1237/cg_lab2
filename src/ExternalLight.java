package src;

import java.util.Random;

public class ExternalLight implements LightInterface {
    private final Vector color;
    private final Vector intens;
    public ExternalLight(Vector color, float[] intens) {
        this.color = color;
        this.intens = new Vector(intens[0], intens[1], intens[2]);
    }
    @Override
    public double calcLighting(Normal normalAtPoint, Point point, BoundingTreeInterface tree) {
        double dotProduct = 0;
        int ray_count = 5;
        for (int i=0; i<ray_count; i++){
            Ray r = new Ray(directGen(normalAtPoint).toNormal(), point.add(normalAtPoint.mult(2)));
            int isLight = 1;
            for (ObjectInterface object : tree.getTriangles(r)) {
                Double intersection = object.intersectionWith(r);

                if (intersection != null) {
                    isLight =0;
                    break;
                }
            }
            dotProduct += r.getDirection().toNormal().dot(normalAtPoint)*isLight;


        }

        return dotProduct/ray_count;

    }

    @Override
    public Normal getDirection(Point point) {
        return Normal.create(0,0,0);
    }

    @Override
    public Vector getColor() {
        return color;
    }

    @Override
    public Vector getIntensivity() {
        return intens;
    }

    private Vector directGen(Normal normalAtPoint){
        Random random = new Random();
        double dot = 0;
        Vector dir = null;
        while (dot <=0){
            double u = random.nextDouble();
            double v = random.nextDouble();
            double theta = 2*Math.PI*u;
            double phi = 1/Math.cos(2*v-1);
            double x = Math.sqrt(1-u*u)*Math.cos(theta);
            double y = Math.sqrt(1-u*u)*Math.sin(theta);
            double z = Math.cos(phi);
            dir = new Vector(x, y, z);
            dot = dir.dot(normalAtPoint);
        }
        return dir;
    }

}
