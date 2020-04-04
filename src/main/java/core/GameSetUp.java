package core;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import core.controllers.AnimationController;
import core.controllers.ObjectsMovementController;
import core.controllers.ThrowingController;
import dto.DaleStateDTO;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;
import core.initialization.*;

public class GameSetUp extends SimpleApplication {

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

	private GameSetUp (){
		if (Holder.INSTANCE != null){
			throw new IllegalStateException("Instance of this class already "
					+ "constructed");
		}
	}

	public static GameSetUp getInstance (){
		return Holder.INSTANCE;
	}

	@Override
	public void simpleInitApp() {
		setUpModels();
		setUpTerrain();
		setUpObjects();
		setUpMusic();
		gameStateDTO = new GameStateDTO();
		animationController = new AnimationController(daleStateDTO,
				objectsHolderDTO);
		throwingController = new ThrowingController(cam, rootNode, objectsHolderDTO,
				daleStateDTO, gameStateDTO, animationController);
		setUpLight();
		animationController.setUpAnimations(modelLoader);
		setupKeys(daleStateDTO);

	}

	private void setUpMusic() {
		soundsInitializer = new SoundsInitializer(rootNode, assetManager);
		soundsInitializer.addMusic();
	}

	private void setUpModels() {
		modelLoader = new ModelLoader(objectsHolderDTO);
		modelLoader.loadModels(assetManager);
	}

	private void setUpTerrain() {
		terrainCreator = new TerrainCreator(assetManager, getStateManager(),
				rootNode, getCamera(), objectsHolderDTO);
		terrainCreator.setupTerrain();
	}

	private void setupKeys(DaleStateDTO daleStateDTO) {
		objectsMovementController = new ObjectsMovementController(animationController,
				cam, daleStateDTO, objectsHolderDTO);
		keysSetup = new KeysSetup(daleStateDTO, objectsMovementController,
				throwingController);
		keysSetup.setupKeys(inputManager);
	}

	private void setUpObjects() {
		objectsInitializer = new ObjectsInitializer(assetManager, rootNode,
				objectsHolderDTO, cam);
		daleStateDTO = objectsInitializer.initializeObjects(stateManager);
		objectsInitializer.addObjectsToScene(rootNode);
	}

	private void setUpLight() {

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
		objectsMovementController.handleMovement(tpf);
		throwingController.markThrowingDestination();
		throwingController.markThrowableObject();
		objectsMovementController.moveBoxAboveDale();
	}

	private static class Holder {
		private static final GameSetUp INSTANCE = new GameSetUp();
	}


}
