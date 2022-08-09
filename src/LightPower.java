package src;

import java.awt.*;

public class LightPower implements MaterialInterface {
    private Vector color;
    private int[][] texture;
    public LightPower(Vector color, int[][] texture){
        this.color = color;
        this.texture = texture;
    }
    @Override
    public Vector bxdfFunc(Vector wo, Vector wi, ObjectInterface obj, Point p) {
        if (texture == null) {
            return color.mult(1/(2*3.14));//obj.getColor();
        }else{
            Double[] xy = obj.getUvBari(p);
            Color color = new Color(texture[(int) (xy[0]*texture.length)][(int) (xy[1]*texture.length)]);
            return new Vector(color.getRed(),color.getGreen(), color.getBlue());
        }
    }

    @Override
    public float isMirror() {
        return 0;
    }

    @Override
    public Ray reflectedRay(Vector dir, Normal normalAtPoint, Point intersectionPoint) {
        Ray reflect_ray = new Ray(dir.sub(normalAtPoint.mult(dir.dot(normalAtPoint) * 2)).toNormal(), intersectionPoint);
        return reflect_ray;
    }
    }
