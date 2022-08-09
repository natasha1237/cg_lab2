package src;

public interface ObjectInterface {
	Double intersectionWith(Ray ray); // Returns the t value in ray equation (dt + o = p)
	Normal getNormalAtPoint(Point p);
	MaterialInterface getMaterial();
	void setMaterial(MaterialInterface material);
	Double[] getUvBari(Point p);

}
