package core;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class GameSetUp extends SimpleApplication {

	private ModelLoader modelLoader = new ModelLoader();
	private ObjectInitialPositionSetter objectInitialPositionSetter = new ObjectInitialPositionSetter();
	private AnimationController animationController = new AnimationController();
	private KeysSetup keysSetup = new KeysSetup(animationController);

	@Override
	public void simpleInitApp() {
		setSettings();
		modelLoader.loadModels(assetManager);
		objectInitialPositionSetter.addObjectsToScene(modelLoader, rootNode);
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
		Spatial dale = modelLoader.getDale();
		Vector3f localTranslation = dale.getLocalTranslation();

		cam.setLocation(
				new Vector3f(localTranslation.getX(), localTranslation.getY
						() + 10,
						localTranslation.getZ() - 10));
	}

}
