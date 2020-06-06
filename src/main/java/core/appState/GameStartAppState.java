package core.appState;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import core.controllers.FieldOfViewAppState;
import dto.GameStateDTO;
import dto.NodeNamesDTO;

import java.util.ArrayList;
import java.util.List;

public class GameStartAppState extends AbstractAppState {

	private List<AppState> appStates = new ArrayList<>();
	private NodeNamesDTO nodeNamesDTO;
	private GameStateDTO gameStateDTO;
	private Application app;

	public GameStartAppState(NodeNamesDTO nodeNamesDTO,
			GameStateDTO gameStateDTO) {
		this.nodeNamesDTO = nodeNamesDTO;
		this.gameStateDTO = gameStateDTO;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		this.app = app;
		appStates.add(new GameHotkeysAppState(gameStateDTO));
		appStates.add(new HUDAppState());
		appStates.add(new LightAppState());
		appStates.add(new DaleHPAppState(gameStateDTO, nodeNamesDTO));
		appStates.add(new LevelAppState(nodeNamesDTO));
		appStates.add(new FieldOfViewAppState(nodeNamesDTO, gameStateDTO));
		appStates.add(new AddLevelObjectsAppState(nodeNamesDTO, gameStateDTO));
		appStates.add(new AnimationsAppState(gameStateDTO, nodeNamesDTO));
		appStates.add(new IdleTimeCheckAppState());
		appStates.add(new SoundsAppState());
		appStates.add(new TerrainAppState(nodeNamesDTO));
		appStates.add(
				new MarkThrowableObjectsAppState(gameStateDTO, nodeNamesDTO));
		appStates.add(new CarriedObjectAppState(gameStateDTO, nodeNamesDTO));
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
