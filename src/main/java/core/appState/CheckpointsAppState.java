package core.appState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import core.controllers.CheckpointsConditionsController;
import core.controllers.GameSaveAndLoadController;
import core.util.CheckPointsFileCreator;
import dto.GameStateDTO;
import dto.SavedGameStateDTO;

public class CheckpointsAppState extends BaseAppState {

	private SimpleApplication simpleApplication;
	private CheckPointsFileCreator checkPointsFileCreator;
	private CheckpointsConditionsController checkpointsConditionsController;
	private AddLevelObjects addLevelObjects;
	private GameSaveAndLoadController gameSaveAndLoadController = new GameSaveAndLoadController();
	private SavedGameStateDTO savedState;

	public CheckpointsAppState(GameStateDTO gameStateDTO) {
		addLevelObjects = new AddLevelObjects(gameStateDTO);
	}

	@Override
	protected void initialize(Application app) {
		simpleApplication = ((SimpleApplication) app);
		checkPointsFileCreator = new CheckPointsFileCreator();
		checkpointsConditionsController = new CheckpointsConditionsController(
				simpleApplication.getRootNode());
		addObjectsToMap(app);
	}

	private void addObjectsToMap(Application app) {
		if (gameSaveAndLoadController.doesCheckpointExist()) {
			savedState = gameSaveAndLoadController.load();
		}
		addLevelObjects.addObjects(app.getStateManager(), app);

		addLevelObjects.initializeCamera(simpleApplication.getRootNode());
	}

	@Override
	protected void cleanup(Application app) {

	}

	@Override
	protected void onEnable() {

	}

	@Override
	protected void onDisable() {

	}

	@Override
	public void update(float tpf) {
		if (checkpointsConditionsController.isNextCheckpointConditionPassedWithUpdateCheckpoint()) {
			saveCheckpoint();
		}
		super.update(tpf);
	}

	private void saveCheckpoint() {
		gameSaveAndLoadController.save(simpleApplication.getRootNode(),
				simpleApplication.getStateManager());
	}

	public SavedGameStateDTO getCheckpoint() {
		return savedState;
	}

	public boolean hasCheckpoint() {
		return savedState != null;
	}
}
