package core;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class GameSetUp extends SimpleApplication {

	private ModelLoader modelLoader;
	private ObjectsInitializer objectsInitializer;
	private AnimationController animationController;
	private KeysSetup keysSetup;
	private ObjectsMovementHandler objectsMovementHandler;
	private ThrowingHandler throwingHandler;
	private DaleState daleState;
	private GameState gameState;
	private SoundsInitializer soundsInitializer;
	private TerrainCreator terrainCreator;
	private ObjectsHolderDTO objectsHolderDTO = new ObjectsHolderDTO();

	@Override
	public void simpleInitApp() {
		loadModels();
		setupTerrain();
		initializeObjects();
		initializeMusic();
		gameState = new GameState();
		animationController = new AnimationController(daleState,
				objectsHolderDTO);
		throwingHandler = new ThrowingHandler(cam, rootNode, objectsHolderDTO,
				daleState, gameState, animationController);
		setupLight();
		animationController.setUpAnimations(modelLoader);
		setupKeys(daleState);

	}

	private void initializeMusic() {
		soundsInitializer = new SoundsInitializer(rootNode, assetManager);
		soundsInitializer.addMusic();
	}

	private void loadModels() {
		modelLoader = new ModelLoader(objectsHolderDTO);
		modelLoader.loadModels(assetManager);
	}

	private void setupTerrain() {
		terrainCreator = new TerrainCreator(assetManager, getStateManager(),
				rootNode, getCamera(), objectsHolderDTO);
		terrainCreator.setupTerrain();
	}

	private void setupKeys(DaleState daleState) {
		objectsMovementHandler = new ObjectsMovementHandler(animationController,
				cam, daleState, objectsHolderDTO);
		keysSetup = new KeysSetup(daleState, objectsMovementHandler,
				throwingHandler);
		keysSetup.setupKeys(inputManager);
	}

	private void initializeObjects() {
		objectsInitializer = new ObjectsInitializer(assetManager, rootNode,
				objectsHolderDTO, cam);
		daleState = objectsInitializer.initializeObjects(stateManager);
		objectsInitializer.addObjectsToScene(rootNode);
	}

	private void setupLight() {

		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(ColorRGBA.White);
		directionalLight.setDirection(
				new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
		rootNode.addLight(directionalLight);

		AmbientLight ambientLight = new AmbientLight();
		ambientLight.setColor(ColorRGBA.White.mult(0.15f));
		rootNode.addLight(ambientLight);

	}

	@Override
	public void simpleUpdate(float tpf) {
		objectsMovementHandler.handleMovement(tpf);
		throwingHandler.markThrowingDestination();
		throwingHandler.markThrowableObject();
		objectsMovementHandler.moveBoxAboveDale();
	}

}
