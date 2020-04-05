package core.initialization;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import core.GameApplication;
import dto.ObjectsHolderDTO;

public class ModelLoader {

	private static final int NUMBER_OF_TREES = 20;
	private static final int NUMBER_OF_BOXES = 15;
	private static final int NUMBER_OF_DOGS = 15;
	private static final String MODELS_DIRECTORY = "models/";
	private static final String TEXTURES_DIRECTORY = "textures/";
	private static final String SCENE_DIRECTORY = "scene/";
	private static final String MODEL_EXTENSION = ".mesh.xml";
	private static final String SCENE_EXTENSION = ".scene";

	private ObjectsHolderDTO objectsHolderDTO;
	private AssetManager assetManager;

	public ModelLoader(ObjectsHolderDTO objectsHolderDTO) {
		this.objectsHolderDTO = objectsHolderDTO;
		assetManager = GameApplication.getInstance()
									  .getAssetManager();
	}

	public void loadModels() {
		createScene();
		createModels();
		createSky();
		createTextures();
	}

	private void createTextures() {
		objectsHolderDTO.setRoad(loadTexture("road.jpg"));
		objectsHolderDTO.setHeightMap(loadTexture("heightmap.png"));
	}

	private void createSky() {
		objectsHolderDTO.setSky(SkyFactory.createSky(assetManager,
				"models/clouds" + "" + ".dds", SkyFactory.EnvMapType.CubeMap));
	}

	private void createScene() {
		objectsHolderDTO.setScene(loadScene("scene"));
	}

	private void createModels() {

		objectsHolderDTO.setDale(loadModel("dalev4"));
		objectsHolderDTO.setArrow(loadModel("arrow"));
		objectsHolderDTO.setMark(loadModel("mark"));
		for (int i = 0; i < NUMBER_OF_DOGS; i++) {
			objectsHolderDTO.addDog(loadModel("dog"));
		}
		for (int i = 0; i < NUMBER_OF_TREES; i++) {
			objectsHolderDTO.addTree(loadModel("tree"));
		}
		for (int i = 0; i < NUMBER_OF_BOXES; i++) {
			objectsHolderDTO.addBox(loadModel("box"));
		}
	}

	private Spatial loadModel(String modelName) {
		return assetManager.loadModel(
				String.format(MODELS_DIRECTORY + "%s" + MODEL_EXTENSION,
						modelName));
	}

	private Texture loadTexture(String textureName) {
		return assetManager.loadTexture(
				String.format(TEXTURES_DIRECTORY + "%s", textureName));
	}

	private Spatial loadScene(String sceneName) {
		return assetManager.loadModel(
				String.format(SCENE_DIRECTORY + "%s" + SCENE_EXTENSION,
						sceneName));
	}

}
