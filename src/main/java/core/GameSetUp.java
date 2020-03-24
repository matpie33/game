package core;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;

public class GameSetUp extends SimpleApplication {

	private ModelLoader modelLoader = new ModelLoader();
	private ObjectInitialPositionSetter objectInitialPositionSetter = new ObjectInitialPositionSetter();
	private AnimationController animationController = new AnimationController();
	private DaleState daleState = new DaleState();
	private KeysSetup keysSetup = new KeysSetup(animationController, daleState);
	private ObjectsMovementHandler objectsMovementHandler;

	@Override
	public void simpleInitApp() {
		modelLoader.loadModels(assetManager);
		objectInitialPositionSetter.initializeObjects(modelLoader, stateManager);
		objectInitialPositionSetter.addObjectsToScene(modelLoader, rootNode);
		keysSetup.setupKeys(inputManager, modelLoader, cam);
		addLight();
		animationController.setUpAnimations(modelLoader);
		objectsMovementHandler = new ObjectsMovementHandler(animationController,
				cam, daleState, modelLoader);

	}

	private void addLight() {
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f).normalizeLocal());
		rootNode.addLight(sun);
	}


	@Override
	public void simpleUpdate(float tpf) {
		objectsMovementHandler.handleMovement();
	}

}
