package core.initialization;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import dto.ObjectsHolderDTO;

public class ModelLoader {

	private static final int NUMBER_OF_TREES = 20;
	private static final int NUMBER_OF_BOXES = 15;
	private static final String MODELS_DIRECTORY = "models/";
	private static final String TEXTURES_DIRECTORY = "textures/";
	private static final String SCENE_DIRECTORY = "scene/";
	private static final String MODEL_EXTENSION = ".mesh.xml";
	private static final String SCENE_EXTENSION = ".scene";

	private ObjectsHolderDTO objectsHolderDTO;

	public ModelLoader(ObjectsHolderDTO objectsHolderDTO) {
		this.objectsHolderDTO = objectsHolderDTO;
	}

	public void loadModels(AssetManager assetManager) {
		createScene(assetManager);
		createModels(assetManager);
		createSky(assetManager);
		createTextures(assetManager);
	}

	private void createTextures(AssetManager assetManager) {
		objectsHolderDTO.setRoad(loadTexture(assetManager, "road.jpg"));
		objectsHolderDTO.setHeightMap(loadTexture(assetManager, "heightmap.png"));
	}

	private void createSky(AssetManager assetManager) {
		objectsHolderDTO.setSky(
				SkyFactory.createSky(assetManager, "models/clouds" + ".dds",
						SkyFactory.EnvMapType.CubeMap));
	}

	private void createScene(AssetManager assetManager) {
		objectsHolderDTO.setScene(loadScene(assetManager, "scene"));
	}

	private void createModels(AssetManager assetManager) {
		objectsHolderDTO.setDale(loadModel(assetManager, "dalev4"));
		objectsHolderDTO.setArrow(loadModel(assetManager, "arrow"));
		objectsHolderDTO.setMark(loadModel(assetManager, "mark"));
		for (int i = 0; i < NUMBER_OF_TREES; i++) {
			objectsHolderDTO.addTree(loadModel(assetManager, "tree"));
		}
		for (int i = 0; i < NUMBER_OF_BOXES; i++) {
			objectsHolderDTO.addBox(loadModel(assetManager, "box"));
		}
	}


	private Spatial loadModel(AssetManager assetManager, String modelName) {
		return assetManager.loadModel(
				String.format(MODELS_DIRECTORY + "%s" + MODEL_EXTENSION,
						modelName));
	}

	private Texture loadTexture(AssetManager assetManager, String textureName) {
		return assetManager.loadTexture(
				String.format(TEXTURES_DIRECTORY + "%s", textureName));
	}

	private Spatial loadScene(AssetManager assetManager, String sceneName) {
		return assetManager.loadModel(
				String.format(SCENE_DIRECTORY + "%s" + SCENE_EXTENSION,
						sceneName));
	}

}
