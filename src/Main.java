package src;

import java.util.ArrayList;

class Main {
	public static void main(String[] args) throws Exception {
		// if run without arguments, assign file paths to these variables
		String input_file = null;
		String output_file = null;

		// arguments without space
		for (String a : args) {
			if (a.startsWith("--source=")) {
				input_file = a.split("=")[1];
			} else if (a.startsWith("--output")) {
				output_file = a.split("=")[1];
			}
		}

		Screen screen = new Screen(300, 300, 1, new Point(450, 0, 0));
		Camera camera = new Camera(new Point(950, 0, 0));
		ExternalLight light = new ExternalLight(new Vector(255,255,0), new float[]{5.5f,6f,6f});
		Plane plane = new Plane(new Point(-100,0,-100), Normal.create(0,0,-1));
		ObjectReader reader = new ObjectReader(input_file);
		ArrayList<Triangle> poligons = reader.readfile();
		ArrayList<Triangle> all_poligons = new ArrayList<>();
		ArrayList<ObjectInterface> other_obj = new ArrayList<>();
		System.out.println(poligons.size());

		Matrix4x4 m1 = new Matrix4x4();
		m1.move(0, -100, 40);
		m1.rotateZ(55);
		m1.scale(400, 400, 400);

		MaterialInterface lamb = new LightPower(new Vector(0.5, 1,1), null); // color from 0 to 1

		for (Triangle tr : poligons) {
			tr.transform(m1);
			tr.setMaterial(lamb);
			all_poligons.add(tr);
		}
		Sphere sphere = new Sphere(new Point(0,200,0), 100);
		MaterialInterface mirror = new Mirror();

		sphere.setMaterial(mirror);
		MaterialInterface lamb2 = new LightPower(new Vector(0, 1,1), null);
		plane.setMaterial(lamb2);
		other_obj.add(plane);
		simple_render(output_file, screen, camera, light,all_poligons, other_obj, 10);
		//compare_render( output_file,"without_tree.ppm", "mask.ppm", screen, camera, light,all_poligons);

	}
	public static void simple_render(String outfile, Screen screen, Camera camera,
									 LightInterface light, ArrayList<Triangle> objects, ArrayList<ObjectInterface> simple_obj, int deep){
		FileOutput out = new FileOutput(outfile);
		SAHBiTree tree = SAHBiTree.create(objects, deep);
		Scene scene = new Scene(camera, screen, light, tree);
		for (ObjectInterface o: simple_obj){
			scene.add_obj(o);
		}
		int[][] withtree = scene.render(out, true);
	}
	public static void compare_render(String outfile, String witoutfile, String maskfile, Screen screen, Camera camera,
									  LightInterface light, ArrayList<Triangle> objects){
		FileOutput out = new FileOutput(outfile);
		FileOutput out2 = new FileOutput(witoutfile);//"without_tree.ppm"
		FileOutput out3 = new FileOutput(maskfile);//"mask.ppm"
		double startTime = System.currentTimeMillis();
		SAHBiTree tree = SAHBiTree.create(objects, 10);
		Scene scene = new Scene(camera, screen, light, tree);
		//scene.add_obj(sphere);
		int[][] withtree = scene.render(out, true);
		System.out.println("Time with tree = " +(System.currentTimeMillis()-startTime));

		NoTree noTree = new NoTree(objects);
		Scene scene2 = new Scene(camera, screen, light, noTree);
		double startTime2 = System.currentTimeMillis();
		int[][] withoutTree = scene2.render(out2, true);
		System.out.println("Time without tree = " +(System.currentTimeMillis()-startTime2));

		out3.get_mask( withtree,withoutTree);

	}

}
