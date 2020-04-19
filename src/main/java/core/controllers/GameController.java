package core.controllers;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import core.GameApplication;
import core.gui.HUDCreator;
import core.initialization.*;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

import java.util.List;

public class GameController {

	private ObjectsInitializer objectsInitializer;
	private KeysSetup keysSetup;
	private ObjectsMovementController objectsMovementController;
	private GameStateDTO gameStateDTO;
	private SoundsInitializer soundsInitializer;
	private TerrainCreator terrainCreator;
	private ObjectsHolderDTO objectsHolderDTO = new ObjectsHolderDTO();
	private GameApplication gameApplication;
	private HUDCreator hudCreator;
	private ObjectsStateController objectsStateController;
	private ObjectsRemovingController objectsRemovingController;
	private EffectsController effectsController;
	private AnimationsController animationsController;
	private IdleTimeChecker idleTimeChecker;
	private LevelInitializer levelInitializer;

	public GameController(GameApplication gameApplication) {
		this.gameApplication = gameApplication;
	}

	public void initialize() {

		setUpMouse();
		effectsController = new EffectsController();
		gameStateDTO = new GameStateDTO();
		objectsRemovingController = new ObjectsRemovingController(gameStateDTO,
				effectsController);
		createGui();

		List<Spatial> spatials = setUpModels();
		setUpTerrain();

		setUpMusic();
		objectsMovementController = new ObjectsMovementController(gameStateDTO,
				objectsHolderDTO);
		setUpObjects(spatials);
		setUpAnimations();
		objectsStateController = new ObjectsStateController(gameStateDTO,
				objectsMovementController, hudCreator, objectsHolderDTO);
		setUpLight();
		setupKeys();

	}

	private void setUpMouse() {
		idleTimeChecker = new IdleTimeChecker();
		idleTimeChecker.setUp();
	}

	private void createGui() {
		hudCreator = new HUDCreator();
		hudCreator.createHUD();
	}

	private void setUpAnimations() {

		animationsController = new AnimationsController(gameStateDTO,
				objectsHolderDTO);
		animationsController.setUp();
	}

	private void setUpMusic() {
		soundsInitializer = new SoundsInitializer();
		soundsInitializer.addMusic();
	}

	private List<Spatial> setUpModels() {
		levelInitializer = new LevelInitializer(objectsHolderDTO,
				gameApplication.getAssetManager());
		return levelInitializer.initializeLevel();
	}

	private void setUpTerrain() {
		terrainCreator = new TerrainCreator(objectsHolderDTO);
		terrainCreator.setupTerrain();
	}

	private void setupKeys() {

		keysSetup = new KeysSetup(gameStateDTO);
		keysSetup.setupKeys();
	}

	private void setUpObjects(List<Spatial> spatials) {
		objectsInitializer = new ObjectsInitializer(objectsHolderDTO,
				gameStateDTO, idleTimeChecker);
		objectsInitializer.addObjectsToScene(spatials);
	}

	private void setUpLight() {

		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(ColorRGBA.White);
		directionalLight.setDirection(
				new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
		Node rootNode = gameApplication.getRootNode();
		rootNode.addLight(directionalLight);

		AmbientLight ambientLight = new AmbientLight();
		ambientLight.setColor(ColorRGBA.White.mult(0.15f));
		rootNode.addLight(ambientLight);

	}

	public void update(float tpf) {
		idleTimeChecker.updateTimePassed(tpf);
		animationsController.handleAnimations();
		objectsRemovingController.handleObjectsRemoved();
		objectsStateController.handleObjectsState(tpf);
	}

}
