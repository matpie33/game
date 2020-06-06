package core;

import com.jme3.app.SimpleApplication;
import core.appState.GameStartAppState;
import dto.GameStateDTO;
import dto.NodeNamesDTO;

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
		NodeNamesDTO nodeNamesDTO = new NodeNamesDTO();
		stateManager.attach(
				new GameStartAppState(nodeNamesDTO, gameStateDTO));
	}

	private static class Holder {
		private static final GameApplication INSTANCE = new GameApplication();
	}

}
