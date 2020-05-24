package core.appState;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

import java.util.ArrayList;
import java.util.List;

public class GameStartAppState extends AbstractAppState {

	private List<AbstractAppState> appStates = new ArrayList<>();
	private ObjectsHolderDTO objectsHolderDTO;
	private GameStateDTO gameStateDTO;
	private Application app;

	public GameStartAppState(ObjectsHolderDTO objectsHolderDTO,
			GameStateDTO gameStateDTO) {
		this.objectsHolderDTO = objectsHolderDTO;
		this.gameStateDTO = gameStateDTO;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		this.app = app;
		appStates.add(new GameHotkeysAppState(gameStateDTO));
		appStates.add(new HUDAppState());
		appStates.add(new LightAppState());
		appStates.add(new ObjectsStateAppState(gameStateDTO, objectsHolderDTO));
		appStates.add(new LevelAppState(objectsHolderDTO));
		appStates.add(new AdditionalModelsAppState(objectsHolderDTO));
		appStates.add(new ObjectsAddAppState(objectsHolderDTO, gameStateDTO));
		appStates.add(new AnimationsAppState(gameStateDTO, objectsHolderDTO));
		appStates.add(new IdleTimeCheckAppState());
		appStates.add(new SoundsAppState());
		appStates.add(new ObjectsRemovingAppState(gameStateDTO));
		appStates.add(new TerrainAppState(objectsHolderDTO));
		appStates.forEach(stateManager::attach);
		super.initialize(stateManager, app);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
	}

	@Override
	public void cleanup() {
		appStates.forEach(app.getStateManager()::detach);
		super.cleanup();
	}
}
