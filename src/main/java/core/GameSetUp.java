package core;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;

public class GameSetUp extends SimpleApplication {

	private ModelLoader modelLoader = new ModelLoader();
	private ObjectPositioner objectPositioner = new ObjectPositioner();
	private KeysSetup keysSetup = new KeysSetup();
	private AnimationController animationController = new AnimationController();

	@Override
	public void simpleInitApp() {
		setSettings();
		modelLoader.loadModels(assetManager);
		objectPositioner.addObjectsToScene(modelLoader, rootNode);
		keysSetup.setupKeys(inputManager, modelLoader);
		addLight();
		animationController.setUpAnimations(modelLoader);

	}

	private void addLight() {
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f).normalizeLocal());
		rootNode.addLight(sun);
	}

	private void setSettings() {
		getFlyByCamera().setMoveSpeed(20);

	}

	@Override
	public void simpleUpdate(float tpf) {
	}

}
