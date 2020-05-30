package core.appState;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import dto.ObjectsHolderDTO;

public class AdditionalModelsAppState extends AbstractAppState {

	private static final String TEXTURES_DIRECTORY = "textures/";
	private static final String SCENE_DIRECTORY = "scene/";
	private static final String MODEL_EXTENSION = ".mesh.xml";
	private static final String SCENE_EXTENSION = ".scene";

	private ObjectsHolderDTO objectsHolderDTO;
	private AssetManager assetManager;

	public AdditionalModelsAppState(ObjectsHolderDTO objectsHolderDTO) {
		this.objectsHolderDTO = objectsHolderDTO;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		assetManager = app.getAssetManager();
		createModels();
		createSky();
		createTextures();
	}

	private void createTextures() {
		objectsHolderDTO.setRoad(loadTexture("road.jpg"));
		objectsHolderDTO.setHeightMap(loadTexture("heightmap.png"));
	}

	private void createSky() {
		objectsHolderDTO.setSky(
				SkyFactory.createSky(assetManager, "clouds.dds",
						SkyFactory.EnvMapType.CubeMap));
	}

	private void createModels() {

		objectsHolderDTO.setThrowableObjectMarker(loadModel("arrow"));
		objectsHolderDTO.setMark(loadModel("mark"));
	}

	private Spatial loadModel(String modelName) {
		return assetManager.loadModel(modelName + MODEL_EXTENSION);
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
