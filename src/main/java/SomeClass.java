import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class SomeClass extends SimpleApplication {
	public void simpleInitApp() {
		setSettings();
		loadModels();
		addLight();
	}

	private void loadModels() {
		Spatial dale = assetManager.loadModel("models/dale.mesh.xml");
		Spatial tree = assetManager.loadModel("models/tree.mesh.xml");
		tree.move(100,0,0);
		tree.clone();
		rootNode.attachChild(dale);
		rootNode.attachChild(tree);
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
