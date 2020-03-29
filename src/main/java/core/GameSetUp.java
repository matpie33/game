package core;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;

public class GameSetUp extends SimpleApplication {

	private ModelLoader modelLoader = new ModelLoader();
	private ObjectsInitializer objectsInitializer = new ObjectsInitializer();
	private AnimationController animationController;
	private KeysSetup keysSetup;
	private ObjectsMovementHandler objectsMovementHandler;
	private ThrowingHandler throwingHandler;
	private DaleState daleState;
	private GameState gameState;
	private SoundsInitializer soundsInitializer;

	@Override
	public void simpleInitApp() {
		soundsInitializer = new SoundsInitializer(rootNode, assetManager);
		soundsInitializer.addMusic();
		gameState = new GameState();
		modelLoader.loadModels(assetManager);
		daleState = initializeObjects();
		animationController = new AnimationController(daleState);
		throwingHandler = new ThrowingHandler(cam, rootNode, modelLoader,
				daleState, gameState, animationController);
		addLight();
		animationController.setUpAnimations(modelLoader);
		objectsMovementHandler = new ObjectsMovementHandler(animationController,
				cam, daleState, modelLoader);
		setupKeys(daleState);

	}

	private void setupKeys(DaleState daleState) {
		keysSetup = new KeysSetup(daleState, objectsMovementHandler,
				throwingHandler);
		keysSetup.setupKeys(inputManager);
	}

	private DaleState initializeObjects() {
		DaleState daleState = objectsInitializer.initializeObjects(modelLoader,
				stateManager);
		objectsInitializer.addObjectsToScene(modelLoader, rootNode);
		return daleState;
	}

	private void addLight() {
		AmbientLight sun = new AmbientLight();
		rootNode.addLight(sun);
	}

	@Override
	public void simpleUpdate(float tpf) {
		objectsMovementHandler.handleMovement(tpf);
		throwingHandler.markThrowingDestination();
		throwingHandler.markThrowableObject();
		objectsMovementHandler.moveBoxAboveDale();
	}

}
