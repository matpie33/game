package core;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;

public class GameSetUp extends SimpleApplication {

	private ModelLoader modelLoader = new ModelLoader();
	private ObjectsInitializer objectsInitializer = new ObjectsInitializer();
	private AnimationController animationController = new AnimationController();
	private KeysSetup keysSetup;
	private ObjectsMovementHandler objectsMovementHandler;
	private ThrowingHandler throwingHandler;
	private DaleState daleState;
	private GameState gameState;

	@Override
	public void simpleInitApp() {
		gameState = new GameState();
		modelLoader.loadModels(assetManager);
		daleState = initializeObjects();
		throwingHandler = new ThrowingHandler(cam, rootNode, modelLoader,
				daleState, gameState);
		addLight();
		animationController.setUpAnimations(modelLoader);
		objectsMovementHandler = new ObjectsMovementHandler(animationController,
				cam, daleState, modelLoader);
		setupKeys(daleState);

	}

	private void setupKeys(DaleState daleState) {
		keysSetup = new KeysSetup(daleState, objectsMovementHandler, throwingHandler);
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
