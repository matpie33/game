package core.appState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Spatial;
import dto.ObjectsHolderDTO;
import dto.SpatialDTO;
import initialization.ModelsLoadAppState;
import saveAndLoad.FileLoad;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class LevelAppState extends AbstractAppState {

	public static final String MODELS_DIRECTORY = "/models/";
	public static final String PATH_TO_LEVELS = "/levels/";
	public static final String LEVEL = "level.txt";

	private ObjectsHolderDTO objectsHolderDTO;
	private List<SpatialDTO> spatialDTOS;

	public LevelAppState(ObjectsHolderDTO objectsHolderDTO) {
		this.objectsHolderDTO = objectsHolderDTO;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		ModelsLoadAppState modelsLoadAppState = new ModelsLoadAppState(app.getAssetManager(),
				app.getCamera(), ((SimpleApplication) app).getRootNode());
		InputStream levelsFileStream = getClass().getResourceAsStream(
				PATH_TO_LEVELS + LEVEL);
		modelsLoadAppState.setPaths(Collections.singletonList(
				System.getProperty("user.dir") + MODELS_DIRECTORY));
		stateManager.attach(new AdditionalModelsAppState(objectsHolderDTO));

		spatialDTOS = new FileLoad().readFile(levelsFileStream);
		spatialDTOS.forEach(dto -> {
			String pathToModel = dto.getPathToModel();
			Spatial spatial = modelsLoadAppState.loadModel(pathToModel);
			dto.setSpatial(spatial);
		});
	}

	public List<SpatialDTO> getSpatialDTOS() {
		return spatialDTOS;
	}
}
