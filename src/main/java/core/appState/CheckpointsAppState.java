package core.appState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.scene.Spatial;
import constants.NodeNames;
import core.controllers.CheckpointsConditionsController;
import core.util.CheckPointsFileCreator;
import dto.GameStateDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class CheckpointsAppState extends BaseAppState {

	private BinaryExporter binaryExporter;
	private SimpleApplication simpleApplication;
	private File file;
	private CheckPointsFileCreator checkPointsFileCreator;
	private CheckpointsConditionsController checkpointsConditionsController;
	private AddLevelObjects addLevelObjects;

	public CheckpointsAppState(GameStateDTO gameStateDTO) {
		addLevelObjects = new AddLevelObjects(gameStateDTO);
	}

	@Override
	protected void initialize(Application app) {
		binaryExporter = BinaryExporter.getInstance();
		simpleApplication = ((SimpleApplication) app);
		checkPointsFileCreator = new CheckPointsFileCreator();
		file = checkPointsFileCreator.createFile();
		checkpointsConditionsController = new CheckpointsConditionsController(
				simpleApplication.getRootNode());
		addObjectsToMap(app);
	}

	private void addObjectsToMap(Application app) {
		if (file.length() != 0) {
			loadCheckpoint();
		}
		else {
			addLevelObjects.addObjects(app.getStateManager(), app);
		}
		addLevelObjects.initializeCamera(simpleApplication.getRootNode());
	}

	private void loadCheckpoint() {
		AssetManager assetManager = simpleApplication.getAssetManager();
		assetManager.registerLocator(Paths.get(file.toURI())
										  .getParent()
										  .toString(), FileLocator.class);
		Spatial spatial = assetManager.loadModel(file.getName());

		simpleApplication.getRootNode()
						 .attachChild(spatial);
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
		try {
			binaryExporter.save(simpleApplication.getRootNode().getChild(
					NodeNames.getGameObjects()),	file);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
