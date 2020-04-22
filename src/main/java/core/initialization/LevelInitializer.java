package core.initialization;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import core.GameApplication;
import dto.ObjectsHolderDTO;
import initialization.ModelsLoader;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class LevelInitializer {

	public static final String MODELS_DIRECTORY = "/models/";
	private AdditionalModelsLoader additionalModelsLoader;
	public static final String PATH_TO_LEVELS = "/levels/";
	public static final String LEVEL = "level.txt";

	private ObjectsHolderDTO objectsHolderDTO;
	private AssetManager assetManager;

	public LevelInitializer(ObjectsHolderDTO objectsHolderDTO,
			AssetManager assetManager) {
		this.objectsHolderDTO = objectsHolderDTO;
		this.assetManager = assetManager;
	}

	public List<Spatial> initializeLevel() {
		ModelsLoader modelsLoader = new ModelsLoader(assetManager,
				GameApplication.getInstance()
							   .getCamera(), GameApplication.getInstance()
															.getRootNode());
		InputStream in = getClass().getResourceAsStream(PATH_TO_LEVELS + LEVEL);
		modelsLoader.setPaths(Collections.singletonList(
				System.getProperty("user.dir") + MODELS_DIRECTORY));
		additionalModelsLoader = new AdditionalModelsLoader(objectsHolderDTO);
		additionalModelsLoader.loadModels();
		return modelsLoader.loadModelsFromLevelFile(in);
	}

}
