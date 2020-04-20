package core.initialization;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import core.GameApplication;
import dto.ObjectsHolderDTO;
import initialization.ModelsLoader;

import java.util.List;

public class LevelInitializer {

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
		additionalModelsLoader = new AdditionalModelsLoader(objectsHolderDTO);
		additionalModelsLoader.loadModels();
		ModelsLoader modelsLoader = new ModelsLoader(assetManager,
				GameApplication.getInstance()
							   .getCamera(),
				GameApplication.getInstance().getRootNode());
		String path = getClass().getResource(PATH_TO_LEVELS + LEVEL)
								.getPath();
		return modelsLoader.loadModelsFromFile(path.substring(1));
	}

}
