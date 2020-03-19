package core;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

import java.util.ArrayList;
import java.util.List;

public class ModelLoader {

	private List<Spatial> trees = new ArrayList<>();
	private static final int NUMBER_OF_TREES = 20;
	private static final String MODELS_DIRECTORY = "models/";
	private static final String MODEL_EXTENSION = ".mesh.xml";

	private Spatial dale;

	public void loadModels(AssetManager assetManager) {
		dale = loadModel(assetManager, "dale");
		for (int i = 0; i < NUMBER_OF_TREES; i++) {
			trees.add(loadModel(assetManager, "tree"));
		}
	}

	private Spatial loadModel(AssetManager assetManager, String modelName) {
		return assetManager.loadModel(
				String.format(MODELS_DIRECTORY + "%s" + MODEL_EXTENSION, modelName));
	}

	public List<Spatial> getTrees() {
		return trees;
	}

	public Spatial getDale() {
		return dale;
	}
}
