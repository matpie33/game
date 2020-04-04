package core.controllers;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import core.GameApplication;
import core.initialization.*;
import dto.DaleStateDTO;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

public class GameController {

	private ModelLoader modelLoader;
	private ObjectsInitializer objectsInitializer;
	private AnimationController animationController;
	private KeysSetup keysSetup;
	private ObjectsMovementController objectsMovementController;
	private ThrowingController throwingController;
	private DaleStateDTO daleStateDTO;
	private GameStateDTO gameStateDTO;
	private SoundsInitializer soundsInitializer;
	private TerrainCreator terrainCreator;
	private ObjectsHolderDTO objectsHolderDTO = new ObjectsHolderDTO();
	private GameApplication gameApplication;
	private EnemyMovementController enemyMovementController;

	public GameController(GameApplication gameApplication) {
		this.gameApplication = gameApplication;
	}

	public void initialize() {

		gameStateDTO = new GameStateDTO();
		setUpModels();
		setUpTerrain();
		setUpObjects();
		setUpMusic();
		setUpAnimations();
		enemyMovementController = new EnemyMovementController(gameStateDTO);
		throwingController = new ThrowingController(objectsHolderDTO,
				daleStateDTO, gameStateDTO, animationController);
		setUpLight();
		setupKeys(daleStateDTO);
	}

	private void setUpAnimations() {
		animationController = new AnimationController(daleStateDTO,
				objectsHolderDTO);
		animationController.setUpAnimations(modelLoader);
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

	private void setupKeys(DaleStateDTO daleStateDTO) {
		objectsMovementController = new ObjectsMovementController(
				animationController, daleStateDTO, objectsHolderDTO);
		keysSetup = new KeysSetup(daleStateDTO, objectsMovementController,
				throwingController);
		keysSetup.setupKeys();
	}

	private void setUpObjects() {
		objectsInitializer = new ObjectsInitializer(objectsHolderDTO, gameStateDTO);
		daleStateDTO = objectsInitializer.initializeObjects();
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
		objectsMovementController.handleMovement(tpf);
		throwingController.markThrowingDestination();
		throwingController.markThrowableObject();
		objectsMovementController.moveBoxAboveDale();
		enemyMovementController.moveEnemies(tpf);
	}

}
