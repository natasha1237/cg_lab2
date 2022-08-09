package src;

import java.util.ArrayList;
import java.util.function.Function;

public class SAHBiTree implements BoundingTreeInterface {
    private final ArrayList<Triangle> triangles;
    private final Shell boundingBox;
    private SAHBiTree left;
    private SAHBiTree right;
    private boolean hasChildren;

    public static SAHBiTree create(ArrayList<Triangle> triangles, int treeDepth) {
        Vector[] bounds = getBounds(triangles);
        return new SAHBiTree(triangles, new Shell(bounds[0], bounds[1]), 0, treeDepth);
    }

    private static Vector[] getBounds(ArrayList<Triangle> triangles) {
        Vector[] res = new Vector[2];
        double minX = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        double minZ = Double.MAX_VALUE;
        double maxZ = -Double.MAX_VALUE;

        for (Triangle tr : triangles) {
            Vector min = tr.minBounds();
            Vector max = tr.maxBounds();

            if (min.x() < minX)
                minX = min.x();
            if (min.y() < minY)
                minY = min.y();
            if (min.z() < minZ)
                minZ = min.z();

            if (max.x() > maxX)
                maxX = max.x();
            if (max.y() > maxY)
                maxY = max.y();
            if (max.z() > maxZ)
                maxZ = max.z();
        }

        res[0] = new Vector(minX, minY, minZ);
        res[1] = new Vector(maxX, maxY, maxZ);

        return res;
    }

    private void divideTriangles(double middle, Function<Vector, Double> axis, ArrayList<Triangle> leftTs, ArrayList<Triangle> rightTs) {
        for (Triangle tr : triangles) {
            Point c = tr.getCenter();
            Vector center = new Vector(c.x(), c.y(), c.z());
            Vector min = tr.minBounds();
            Vector max = tr.maxBounds();

            if (axis.apply(min) <= middle && axis.apply(max) >= middle) {
                leftTs.add(tr);
                rightTs.add(tr);
            } else if (axis.apply(center) < middle) {
                leftTs.add(tr);
            } else {
                rightTs.add(tr);
            }
        }
    }

    private double SAHPoint(Vector minBox, Vector maxBox, Function<Vector, Double> axis) {
        int NUM_POINTS = 10;
        Vector diff = maxBox.sub(minBox);
        double step = axis.apply(diff) / NUM_POINTS;
        double parentArea = boundingBox.area();
        double[] estimatedTime = new double[NUM_POINTS];

        for (int i = 0; i < NUM_POINTS; i++) {
            ArrayList<Triangle> leftTs = new ArrayList<>();
            ArrayList<Triangle> rightTs = new ArrayList<>();
            divideTriangles(axis.apply(minBox) + step * i, axis, leftTs, rightTs);
            Vector[] leftBounds = getBounds(leftTs);
            Vector[] rightBounds = getBounds(rightTs);
            Shell leftBox = new Shell(leftBounds[0], leftBounds[1]);
            Shell rightBox = new Shell(rightBounds[0], rightBounds[1]);
            double p1 = leftBox.area() / parentArea;
            double p2 = rightBox.area() / parentArea;

            estimatedTime[i] = p1 * leftTs.size() + p2 * rightTs.size();
        }

        int minAt = 0;
        for (int i = 0; i < estimatedTime.length; i++) {
            minAt = estimatedTime[i] < estimatedTime[minAt] ? i : minAt;
        }

        return axis.apply(minBox) + step * minAt;
    }

    private double findDivisionPoint(Vector minBox, Vector maxBox, Function<Vector, Double> axis) {
        return SAHPoint(minBox, maxBox, axis);
    }

    private SAHBiTree(ArrayList<Triangle> triangles, Shell boundingBox, int depth, int treeDepth) {
        this.triangles = triangles;
        this.boundingBox = boundingBox;
        Vector[] bounds = getBounds(triangles);
        Vector minBox = bounds[0];
        Vector maxBox = bounds[1];
        Vector diff = maxBox.sub(minBox);
        double maxDiff = Math.max(diff.x(), Math.max(diff.y(), diff.z()));
        Shell leftBox;
        Shell rightBox;
        ArrayList<Triangle> leftTs = new ArrayList<>();
        ArrayList<Triangle> rightTs = new ArrayList<>();

        if (maxDiff == diff.x()) {
            double middle = findDivisionPoint(minBox, maxBox, Vector::x);
            divideTriangles(middle, Vector::x, leftTs, rightTs);
            leftBox = new Shell(minBox, new Vector(middle, maxBox.y(), maxBox.z()));
            rightBox = new Shell(new Vector(middle, minBox.y(), minBox.z()), maxBox);
        } else if (maxDiff == diff.y()) {
            double middle = findDivisionPoint(minBox, maxBox, Vector::y);
            divideTriangles(middle, Vector::y, leftTs, rightTs);
            leftBox = new Shell(minBox, new Vector(maxBox.x(), middle, maxBox.z()));
            rightBox = new Shell(new Vector(minBox.x(), middle, minBox.z()), maxBox);
        } else {
            double middle = findDivisionPoint(minBox, maxBox, Vector::z);
            divideTriangles(middle, Vector::z, leftTs, rightTs);
            leftBox = new Shell(minBox, new Vector(maxBox.x(), maxBox.y(), middle));
            rightBox = new Shell(new Vector(minBox.x(), minBox.y(), middle), maxBox);
        }

        if (leftTs.size() >= triangles.size() || rightTs.size() >= triangles.size() || depth > treeDepth) {
            return;
        }
        hasChildren = true;
        left = new SAHBiTree(leftTs, leftBox, depth + 1, treeDepth);
        right = new SAHBiTree(rightTs, rightBox, depth + 1, treeDepth);
    }

    public ArrayList<Triangle> getTriangles(Ray ray) {
        boolean leftIntersection = hasChildren && left.boundingBox.intersectionWith(ray);
        boolean rightIntersection = hasChildren && right.boundingBox.intersectionWith(ray);

        if (leftIntersection && rightIntersection) {
            ArrayList<Triangle> res = new ArrayList<>();
            res.addAll(right.getTriangles(ray));
            res.addAll(left.getTriangles(ray));
            return res;
        } else if (leftIntersection) {
            return left.getTriangles(ray);
        } else if (rightIntersection) {
            return right.getTriangles(ray);
        } else if (!hasChildren) {
            return triangles;
        } else {
            return new ArrayList<>();
        }
    }
}