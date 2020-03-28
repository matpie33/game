package core;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

import java.util.ArrayList;
import java.util.List;

public class ModelLoader {

	private List<Spatial> trees = new ArrayList<>();
	private static final int NUMBER_OF_TREES = 20;
	private static final String MODELS_DIRECTORY = "models/";
	private static final String SCENE_DIRECTORY = "scene/";
	private static final String MODEL_EXTENSION = ".mesh.xml";
	private static final String SCENE_EXTENSION = ".scene";

	private Spatial dale;
	private Spatial scene;
	private Spatial mark;
	private Spatial box;
	private Spatial arrow;

	public void loadModels(AssetManager assetManager) {
		dale = loadModel(assetManager, "dalev4");
		arrow = loadModel(assetManager, "arrow");
		box = loadModel(assetManager, "box");
		scene = loadScene(assetManager, "scene");
		mark = loadModel(assetManager, "mark");
		for (int i = 0; i < NUMBER_OF_TREES; i++) {
			trees.add(loadModel(assetManager, "tree"));
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

	public List<Spatial> getTrees() {
		return trees;
	}

	public Spatial getDale() {
		return dale;
	}

	public Spatial getScene() {
		return scene;
	}

	public Spatial getBox() {
		return box;
	}

	public Spatial getMark() {
		return mark;
	}
}
