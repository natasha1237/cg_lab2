package src;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Scene {
	public static final int BACKGROUND_COLOR = Color.magenta.getRGB();
	private final Camera camera;
	private final Screen screen;
	private final LightInterface light;
	private final BoundingTreeInterface tree;
	private final ArrayList<ObjectInterface> obj_list = new ArrayList<>();

	public Scene(Camera camera, Screen screen, LightInterface light, BoundingTreeInterface tree) {
		this.camera = camera;
		this.screen = screen;
		this.light = light;
		this.tree = tree;
	}

	public void add_obj(ObjectInterface obj){
		obj_list.add(obj);
	}


	private boolean lightObstructed(Ray ray, ObjectInterface self) {
		for (ObjectInterface object : tree.getTriangles(ray)) {
			if (object == self) continue;
			Double intersection = object.intersectionWith(ray);

			if (intersection != null) {
				return true;
			}
		}

		return false;
	}

	public int[][] render(OutputInterface output, boolean withShadows) {
		Point origin = camera.getLocation();
		int[][] matrix = new int[screen.getWidth()][screen.getHeight()];

		for (int x = 0; x < screen.getWidth(); x++) {
			for (int y = 0; y < screen.getHeight(); y++) {
				Point dest = screen.getPoint(x, y);
				Vector direction = dest.sub(origin);
				Ray ray = new Ray(direction.toNormal(), origin);
				int color = calcucate_intersection(ray, withShadows, matrix,x,y,0, null);
				matrix[x][y] = color;

			}
		}

		output.display(matrix);
		return matrix;
	}

	private int calcucate_intersection(Ray ray, boolean withShadows, int[][] matrix, int x, int y, int deep, ObjectInterface selfObj){
		double tVal = Double.MAX_VALUE;
		ObjectInterface obj = null;

		List<Triangle> triangles = tree.getTriangles(ray);
				for (ObjectInterface object : triangles) {
			Double ttval = object.intersectionWith(ray);

			if (ttval != null && ttval < tVal && object!=selfObj) {
				tVal = ttval;
				obj = object;
			}
		}
		for (ObjectInterface object : obj_list) {
			Double ttval = object.intersectionWith(ray);
			if (ttval != null && ttval < tVal && object!=selfObj) {
				tVal = ttval;
				obj = object;
			}
		}

		if (obj != null) {
			Point intersectionPoint = ray.getPointAt(tVal);
			Normal normalAtPoint = obj.getNormalAtPoint(intersectionPoint);

			if (withShadows && lightObstructed(new Ray(light.getDirection(intersectionPoint), intersectionPoint.add(normalAtPoint.mult(2))), obj)) {

				return Color.BLACK.getRGB();
			} else {
				double lighting = light.calcLighting(normalAtPoint, intersectionPoint, tree);
				while (deep<3 && obj.getMaterial().isMirror() >0){
					Ray reflect_ray = obj.getMaterial().reflectedRay(ray.getDirection(), normalAtPoint, intersectionPoint);//Ray reflect_ray = new Ray(ray.getDirection().sub(normalAtPoint.mult(ray.getDirection().dot(normalAtPoint)*2)).toNormal(), intersectionPoint);

					return calcucate_intersection(reflect_ray, withShadows, matrix, x,y,deep+1, obj);

				}
				if (obj.getMaterial().isMirror() >0){
					return Color.GREEN.getRGB();
				}

				Vector c = obj.getMaterial().bxdfFunc(light.getDirection(intersectionPoint), ray.getDirection(),
						obj, intersectionPoint).dot_el(light.getColor().dot_el(light.getIntensivity()));

				Vector c_rl = roundColor(c).mult(lighting);
				Vector c_r = roundColor(c_rl);
				return new Color((int) c_r.x(), (int) c_r.y(), (int) c_r.z()).getRGB();
			}
		} else {
			if (deep>0){
				return Color.GREEN.getRGB();
			}
			return BACKGROUND_COLOR;
		}
	}
	private Vector roundColor(Vector v){
		if (v.x()<=0 & v.y()<=0 & v.z()<=0){
			return new Vector(0,0,0);
		}
		if (v.x()<=255 & v.y()<=255 & v.z()<=255){
			return v;
		}
		if (v.x() >= v.y() & v.x() >= v.z()){
			return new Vector(255, 255*(v.y()/v.x()), 255*(v.z()/v.x()) );
		}else if (v.y() >= v.x() & v.y() >= v.z()){
			return new Vector(255*(v.x()/v.y()), 255, 255*(v.z()/v.y()) );
		}else{
			return new Vector(255*(v.x()/v.z()), 255*(v.y()/v.z()), 255 );
		}
	}
}
