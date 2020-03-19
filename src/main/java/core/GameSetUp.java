package core;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;

public class GameSetUp extends SimpleApplication {

	private ModelLoader modelLoader = new ModelLoader();
	private ObjectPositioner objectPositioner = new ObjectPositioner();


	public void simpleInitApp() {
		setSettings();
		modelLoader.loadModels(assetManager);
		objectPositioner.addObjectsToScene(modelLoader, rootNode);
		addLight();
	}


	private void addLight() {
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f).normalizeLocal());
		rootNode.addLight(sun);
	}

	private void setSettings() {
		getFlyByCamera().setMoveSpeed(20);
	}
}
