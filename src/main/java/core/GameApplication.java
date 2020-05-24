package core;

import com.jme3.app.SimpleApplication;
import core.appState.GameStartAppState;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

public class GameApplication extends SimpleApplication {


	private GameApplication() {
		if (Holder.INSTANCE != null) {
			throw new IllegalStateException(
					"Instance of this class already " + "constructed");
		}
	}

	public static GameApplication getInstance() {
		return Holder.INSTANCE;
	}

	@Override
	public void simpleInitApp() {
		GameStateDTO gameStateDTO = new GameStateDTO();
		ObjectsHolderDTO objectsHolderDTO = new ObjectsHolderDTO();
		stateManager.attach(
				new GameStartAppState(objectsHolderDTO, gameStateDTO));
	}

	private static class Holder {
		private static final GameApplication INSTANCE = new GameApplication();
	}

}
