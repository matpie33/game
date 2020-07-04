package core.loading;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

import java.util.ArrayList;
import java.util.List;

public class AdditionalModelsLoader {

	private static final String SCENE_DIRECTORY = "scene/";
	private static final String MODEL_EXTENSION = ".mesh.xml";
	private static final String SCENE_EXTENSION = ".scene";

	private AssetManager assetManager;
	private List<Spatial> models = new ArrayList<>();

	public List<Spatial> loadModels(Application app) {
		assetManager = app.getAssetManager();
		createModels();
		return models;
	}

	private void createModels() {

		models.add(loadModel("arrow"));
		models.add(loadModel("mark"));
	}

	private Spatial loadModel(String modelName) {
		return assetManager.loadModel(modelName + MODEL_EXTENSION);
	}

	private Spatial loadScene(String sceneName) {
		return assetManager.loadModel(
				String.format(SCENE_DIRECTORY + "%s" + SCENE_EXTENSION,
						sceneName));
	}

}
