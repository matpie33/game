package core;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

import java.util.ArrayList;
import java.util.List;

public class ModelLoader {

	private static final int NUMBER_OF_TREES = 20;
	private static final int NUMBER_OF_BOXES = 15;
	private static final String MODELS_DIRECTORY = "models/";
	private static final String SCENE_DIRECTORY = "scene/";
	private static final String MODEL_EXTENSION = ".mesh.xml";
	private static final String SCENE_EXTENSION = ".scene";

	private List<Spatial> trees = new ArrayList<>();
	private List<Spatial> boxes = new ArrayList<>();
	private Spatial dale;
	private Spatial scene;
	private Spatial mark;
	private Spatial arrow;
	private Spatial sky;

	public void loadModels(AssetManager assetManager) {
		createScene(assetManager);
		createModels(assetManager);
		createSky(assetManager);
	}

	private void createSky(AssetManager assetManager) {
		sky = SkyFactory.createSky(assetManager,
				"models/clouds.dds",
				SkyFactory.EnvMapType.CubeMap);
	}

	private void createScene(AssetManager assetManager) {
		scene = loadScene(assetManager, "scene");
	}

	private void createModels(AssetManager assetManager) {
		dale = loadModel(assetManager, "dalev4");
		arrow = loadModel(assetManager, "arrow");
		mark = loadModel(assetManager, "mark");
		for (int i = 0; i < NUMBER_OF_TREES; i++) {
			trees.add(loadModel(assetManager, "tree"));
		}
		for (int i = 0; i < NUMBER_OF_BOXES; i++) {
			boxes.add(loadModel(assetManager, "box"));
		}
	}

	public Spatial getArrow() {
		return arrow;
	}

	private Spatial loadModel(AssetManager assetManager, String modelName) {
		return assetManager.loadModel(
				String.format(MODELS_DIRECTORY + "%s" + MODEL_EXTENSION,
						modelName));
	}

	private Spatial loadScene(AssetManager assetManager, String sceneName) {
		return assetManager.loadModel(
				String.format(SCENE_DIRECTORY + "%s" + SCENE_EXTENSION,
						sceneName));
	}

	public Spatial getSky() {
		return sky;
	}

	public List<Spatial> getTrees() {
		return trees;
	}

	public Spatial getDale() {
		return dale;
	}

	public Spatial getScene() {
		return scene;
	}

	public List<Spatial> getBoxes() {
		return boxes;
	}

	public Spatial getMark() {
		return mark;
	}
}
