package core;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.CameraNode;
import core.controllers.GameController;
import main.LevelEditor;

public class GameApplication extends SimpleApplication {

	private GameController gameController;

	private GameApplication() {
		if (Holder.INSTANCE != null) {
			throw new IllegalStateException(
					"Instance of this class already " + "constructed");
		}
		gameController = new GameController(this);
	}

	public static GameApplication getInstance() {
		return Holder.INSTANCE;
	}

	@Override
	public void simpleInitApp() {
		gameController.initialize();
	}

	@Override
	public void simpleUpdate(float tpf) {
		gameController.update(tpf);
	}

	private static class Holder {
		private static final GameApplication INSTANCE = new GameApplication();
	}

}
