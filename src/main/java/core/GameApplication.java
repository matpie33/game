package core;

import com.jme3.app.SimpleApplication;
import core.appState.GameStartAppState;
import dto.DaleStateDTO;
import dto.GameStateDTO;

public class GameApplication extends SimpleApplication {

	private GameApplication() {
		if (Holder.INSTANCE != null) {
			throw new IllegalStateException(
					"Instance of this class already constructed");
		}
	}

	public static GameApplication getInstance() {
		return Holder.INSTANCE;
	}

	@Override
	public void simpleInitApp() {
		GameStateDTO gameStateDTO = new GameStateDTO();
		DaleStateDTO daleStateDTO = new DaleStateDTO();
		gameStateDTO.setDaleStateDTO(daleStateDTO);
		stateManager.attach(new GameStartAppState(gameStateDTO));
	}

	private static class Holder {
		private static final GameApplication INSTANCE = new GameApplication();
	}

}
