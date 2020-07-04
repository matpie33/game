package core.appState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import core.controllers.CollisionDetectionAppState;
import dto.GameStateDTO;
import initialization.ModelsLoadAppState;

import java.util.ArrayList;
import java.util.List;

public class GameStartAppState extends AbstractAppState {

	private List<AppState> appStates = new ArrayList<>();
	private GameStateDTO gameStateDTO;
	private Application app;

	public GameStartAppState(GameStateDTO gameStateDTO) {
		this.gameStateDTO = gameStateDTO;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		this.app = app;
		appStates.add(
				new ModelsLoadAppState(app.getAssetManager(), app.getCamera(),
						((SimpleApplication) app).getRootNode()));
		appStates.add(new GameHotkeysAppState(gameStateDTO));
		appStates.add(new HUDAppState());
		appStates.add(new LightAppState());
		appStates.add(new DaleLedgeGrabAppState(gameStateDTO));
		appStates.add(new CollisionDetectionAppState());
		appStates.add(new DaleHPAppState());
		appStates.add(new LevelAppState());
		appStates.add(new AddPhysicsAppState());
		appStates.add(new CheckpointsAppState(gameStateDTO));
		appStates.add(new DaleMovingAppState(gameStateDTO));
		appStates.add(new DogFollowingDaleAppState());
		appStates.add(new FieldOfViewAppState(gameStateDTO));
		appStates.add(new AnimationsAppState(gameStateDTO));
		appStates.add(new IdleTimeCheckAppState());
		appStates.add(new SoundsAppState());
		appStates.add(new TerrainAppState());
		appStates.add(new MarkThrowableObjectsAppState(gameStateDTO));
		appStates.add(new CarriedObjectAppState(gameStateDTO));
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
