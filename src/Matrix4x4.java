package src;

public class Matrix4x4 {
	public double[][] matrix = null;

	public void multiplyMatrices(double[][] secondMatrix) {
		double[][] result = new double[this.matrix.length][secondMatrix[0].length];

		for (int row = 0; row < result.length; row++) {
			for (int col = 0; col < result[row].length; col++) {
				double cell = 0;
				for (int i = 0; i < secondMatrix.length; i++) {
					cell += this.matrix[row][i] * secondMatrix[i][col];
				}
				result[row][col] = cell;
			}
		}
		this.matrix = result;
	}

	public Normal multiplyNormal(Normal v) {
		double[] result = new double[this.matrix.length];
		double[] vector4 = new double[]{v.x(), v.y(), v.z(), 1};
		for (int row = 0; row < result.length; row++) {
			double cell = 0;
			for (int i = 0; i < 4; i++) {
				cell += this.matrix[row][i] * vector4[i];
			}
			result[row] = cell;
		}

		return Normal.create(result[0], result[1], result[2]);
	}

	public Point multiplyPoint(Point v) {
		double[] result = new double[this.matrix.length];
		double[] vector4 = new double[]{v.x(), v.y(), v.z(), 1};
		for (int row = 0; row < result.length; row++) {
			double cell = 0;
			for (int i = 0; i < 4; i++) {
				cell += this.matrix[row][i] * vector4[i];
			}
			result[row] = cell;
		}

		return new Point(result[0], result[1], result[2]);
	}

	public void move(double x, double y, double z) {
		double[][] new_move = new double[][]{
			new double[]{1, 0, 0, x},
			new double[]{0, 1, 0, y},
			new double[]{0, 0, 1, z},
			new double[]{0, 0, 0, 1}
		};
		if (this.matrix == null) {
			this.matrix = new_move;
		} else {
			this.multiplyMatrices(new_move);
		}
	}

	public void scale(double x, double y, double z) {
		double[][] new_move = new double[][]{
			new double[]{x, 0, 0, 0},
			new double[]{0, y, 0, 0},
			new double[]{0, 0, z, 0},
			new double[]{0, 0, 0, 1}
		};
		if (this.matrix == null) {
			this.matrix = new_move;
		} else {
			this.multiplyMatrices(new_move);
		}
	}

	public void rotateZ(double xAngle) {
		double rad = Math.toRadians(xAngle);
		double[][] new_move = new double[][]{
			new double[]{Math.cos(rad), -Math.sin(rad), 0, 0},
			new double[]{Math.sin(rad), Math.cos(rad), 0, 0},
			new double[]{0, 0, 1, 0},
			new double[]{0, 0, 0, 1}
		};
		if (this.matrix == null) {
			this.matrix = new_move;
		} else {
			this.multiplyMatrices(new_move);
		}
	}
}
