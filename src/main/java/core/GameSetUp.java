package core;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.texture.Texture;

import java.awt.*;

public class GameSetUp extends SimpleApplication {

	private ModelLoader modelLoader = new ModelLoader();
	private ObjectsInitializer objectsInitializer;
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
		objectsInitializer = new ObjectsInitializer(modelLoader,
				assetManager, rootNode);
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


		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(ColorRGBA.White);
		directionalLight.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
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
