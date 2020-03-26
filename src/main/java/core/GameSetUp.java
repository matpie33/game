package core;

import com.jme3.app.SimpleApplication;
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

	@Override
	public void simpleInitApp() {
		throwingHandler = new ThrowingHandler();
		modelLoader.loadModels(assetManager);
		daleState = initializeObjects();
		addLight();
		animationController.setUpAnimations(modelLoader);
		objectsMovementHandler = new ObjectsMovementHandler(animationController,
				cam, daleState, modelLoader);
		setupKeys(daleState);

	}

	private void setupKeys(DaleState daleState) {
		keysSetup = new KeysSetup(daleState, objectsMovementHandler);
		keysSetup.setupKeys(inputManager);
	}

	private DaleState initializeObjects() {
		DaleState daleState = objectsInitializer.initializeObjects(modelLoader,
				stateManager);
		objectsInitializer.addObjectsToScene(modelLoader, rootNode);
		return daleState;
	}

	private void addLight() {
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f).normalizeLocal());
		rootNode.addLight(sun);
	}

	@Override
	public void simpleUpdate(float tpf) {
		objectsMovementHandler.handleMovement(tpf);
		throwingHandler.markThrowingDestination(cam, rootNode, modelLoader, daleState);
	}

}
