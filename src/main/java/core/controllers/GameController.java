package core.controllers;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import core.GameApplication;
import core.gui.HUDCreator;
import core.initialization.*;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

public class GameController {

	private ModelLoader modelLoader;
	private ObjectsInitializer objectsInitializer;
	private AnimationController animationController;
	private KeysSetup keysSetup;
	private ObjectsMovementController objectsMovementController;
	private ThrowingController throwingController;
	private GameStateDTO gameStateDTO;
	private SoundsInitializer soundsInitializer;
	private TerrainCreator terrainCreator;
	private ObjectsHolderDTO objectsHolderDTO = new ObjectsHolderDTO();
	private GameApplication gameApplication;
	private EnemyMovementController enemyMovementController;
	private HUDCreator hudCreator;
	private ObjectsStateController objectsStateController;
	private ObjectsRemovingController objectsRemovingController;
	private EffectsController effectsController;

	public GameController(GameApplication gameApplication) {
		this.gameApplication = gameApplication;
	}

	public void initialize() {

		effectsController = new EffectsController();
		gameStateDTO = new GameStateDTO();
		objectsRemovingController = new ObjectsRemovingController(gameStateDTO,
				effectsController);
		createGui();

		setUpModels();
		setUpTerrain();

		setUpMusic();
		setUpAnimations();
		objectsMovementController = new ObjectsMovementController(
				animationController, gameStateDTO, objectsHolderDTO);
		setUpObjects();
		enemyMovementController = new EnemyMovementController(gameStateDTO);
		throwingController = new ThrowingController(objectsHolderDTO,
				gameStateDTO);
		objectsStateController = new ObjectsStateController(gameStateDTO,
				objectsMovementController, hudCreator, objectsHolderDTO);
		setUpLight();
		setupKeys();

	}

	private void createGui() {
		hudCreator = new HUDCreator();
		hudCreator.createHUD();
	}

	private void setUpAnimations() {
		animationController = new AnimationController(gameStateDTO,
				objectsHolderDTO);
		animationController.setUpAnimations();
	}

	private void setUpMusic() {
		soundsInitializer = new SoundsInitializer();
		soundsInitializer.addMusic();
	}

	private void setUpModels() {
		modelLoader = new ModelLoader(objectsHolderDTO);
		modelLoader.loadModels();
	}

	private void setUpTerrain() {
		terrainCreator = new TerrainCreator(objectsHolderDTO);
		terrainCreator.setupTerrain();
	}

	private void setupKeys() {

		keysSetup = new KeysSetup(gameStateDTO);
		keysSetup.setupKeys();
	}

	private void setUpObjects() {
		objectsInitializer = new ObjectsInitializer(objectsHolderDTO,
				gameStateDTO, animationController);
		objectsInitializer.initializeObjects();
		objectsInitializer.addObjectsToScene();
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
		objectsRemovingController.handleObjectsRemoved();
		objectsStateController.handleObjectsState(tpf);
		objectsMovementController.handleMovement(tpf);
		throwingController.handleThrowingAndPicking();
		enemyMovementController.moveEnemies(tpf);
	}

}
