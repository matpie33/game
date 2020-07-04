package core.appState;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Spatial;
import constants.NodeNames;
import core.loading.AdditionalModelsLoader;
import dto.*;
import initialization.ModelsLoadAppState;
import saveAndLoad.FileLoad;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LevelAppState extends AbstractAppState {

	public static final String MODELS_DIRECTORY = "/models/";
	public static final String PATH_TO_LEVELS = "/levels/";
	public static final String LEVEL = "level.txt";

	private List<SpatialDTO> spatialDTOS;
	private List<ObjectSavedStateDTO> additionalModels = new ArrayList<>();
	private SavedGameStateDTO savedGameStateDTO = new SavedGameStateDTO();

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		ModelsLoadAppState modelsLoadAppState = stateManager.getState(
				ModelsLoadAppState.class);
		InputStream levelsFileStream = getClass().getResourceAsStream(
				PATH_TO_LEVELS + LEVEL);
		modelsLoadAppState.setPaths(Collections.singletonList(
				System.getProperty("user.dir") + MODELS_DIRECTORY));

		loadModelsFromLevelFile(modelsLoadAppState, levelsFileStream);
		loadAdditionalInvisibleModels(app);
		convertSpatialDTOsToGameStateDTO();
	}

	private void convertSpatialDTOsToGameStateDTO() {
		savedGameStateDTO = new SavedGameStateDTO();
		for (SpatialDTO spatialDTO : spatialDTOS) {
			String spatialName = spatialDTO.getSpatial()
										   .getName();
			if (spatialName.equals(NodeNames.getDale())) {
				DaleSavedStateDTO daleSavedStateDTO = new DaleSavedStateDTO();
				daleSavedStateDTO.setHp(100);
				fillCommonParameters(daleSavedStateDTO, spatialDTO);
				savedGameStateDTO.setDaleSavedStateDTO(daleSavedStateDTO);
			}
			else if (spatialName.equals(NodeNames.getDog())) {
				DogSavedStateDTO dogSavedStateDTO = new DogSavedStateDTO();
				dogSavedStateDTO.setHp(100);
				fillCommonParameters(dogSavedStateDTO, spatialDTO);
				savedGameStateDTO.getDogsSavedStateDTOs()
								 .add(dogSavedStateDTO);
			}
			else {
				ObjectSavedStateDTO objectSavedStateDTO = new ObjectSavedStateDTO();
				fillCommonParameters(objectSavedStateDTO, spatialDTO);
				savedGameStateDTO.getOtherObjectsStateDTOs()
								 .add(objectSavedStateDTO);
			}
		}
	}

	private void fillCommonParameters(ObjectSavedStateDTO objectSavedStateDTO,
			SpatialDTO spatialDTO) {
		objectSavedStateDTO.setRotation(spatialDTO.getRotation());
		objectSavedStateDTO.setPosition(spatialDTO.getPosition());
		objectSavedStateDTO.setPathToModel(spatialDTO.getPathToModel());
	}

	private void loadAdditionalInvisibleModels(Application app) {
		new AdditionalModelsLoader().loadModels(app)
									.forEach(spatial -> {
										SpatialDTO spatialDTO = new SpatialDTO();
										spatialDTO.setSpatial(spatial);
										spatialDTO.setPathToModel(
												spatial.getKey().getName());
										ObjectSavedStateDTO objectSavedStateDTO = new ObjectSavedStateDTO();
										fillCommonParameters(objectSavedStateDTO, spatialDTO);
										additionalModels.add(objectSavedStateDTO);
									});
	}

	private void loadModelsFromLevelFile(ModelsLoadAppState modelsLoadAppState,
			InputStream levelsFileStream) {
		spatialDTOS = new FileLoad().readFile(levelsFileStream);
		spatialDTOS.forEach(dto -> {
			String pathToModel = dto.getPathToModel();
			Spatial spatial = modelsLoadAppState.loadModel(pathToModel);
			dto.setSpatial(spatial);
		});
	}

	public SavedGameStateDTO getSavedGameDTO() {
		return savedGameStateDTO;
	}

	public List<ObjectSavedStateDTO> getAdditionalModels() {
		return additionalModels;
	}
}
