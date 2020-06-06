package core.appState;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import core.GameApplication;
import dto.GameStateDTO;
import dto.NodeNamesDTO;

public class ThrowingDestinationFollowingCameraAppState extends BaseAppState {
	private GameStateDTO gameStateDTO;
	private NodeNamesDTO nodeNamesDTO;
	private Camera camera;
	private Spatial throwingDestination;

	public ThrowingDestinationFollowingCameraAppState(GameStateDTO gameStateDTO,
			NodeNamesDTO nodeNamesDTO) {
		this.gameStateDTO = gameStateDTO;
		this.nodeNamesDTO = nodeNamesDTO;
	}

	public void setThrowingDestination(Spatial throwingDestination) {
		this.throwingDestination = throwingDestination;
	}

	@Override
	protected void initialize(Application app) {
		camera = app.getCamera();
	}

	private void handleThrowingDestinationChase(float tpf) {

		IdleTimeCheckAppState idleTimeCheckAppState = GameApplication.getInstance()
																	 .getStateManager()
																	 .getState(
																			 IdleTimeCheckAppState.class);
		if (idleTimeCheckAppState.getIdleTime() > 0.5f) {
			Vector3f throwingDestinationLocation = throwingDestination.getWorldTranslation();
			camera.lookAt(throwingDestinationLocation, Vector3f.UNIT_Y);
			GameApplication.getInstance()
						   .getRootNode()
						   .getChild(nodeNamesDTO.getDaleNodeName())
						   .lookAt(throwingDestinationLocation,
								   Vector3f.UNIT_Y);
		}

	}

	@Override
	protected void cleanup(Application app) {

	}

	@Override
	protected void onEnable() {

	}

	@Override
	protected void onDisable() {

	}

	@Override
	public void update(float tpf) {

		handleThrowingDestinationChase(tpf);
		super.update(tpf);
	}
}
