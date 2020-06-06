package core.appState;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import core.GameApplication;
import dto.DaleStateDTO;
import dto.GameStateDTO;
import dto.NodeNamesDTO;

public class CameraFollowingDaleAppState extends AbstractAppState {

	private GameStateDTO gameStateDTO;
	private NodeNamesDTO nodeNamesDTO;
	private Camera camera;
	private Application app;

	public CameraFollowingDaleAppState(GameStateDTO gameStateDTO,
			NodeNamesDTO nodeNamesDTO) {
		this.gameStateDTO = gameStateDTO;
		this.nodeNamesDTO = nodeNamesDTO;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		camera = app.getCamera();
		this.app = app;
		ThrowingDestinationFollowingCameraAppState appState = new ThrowingDestinationFollowingCameraAppState(
				gameStateDTO, nodeNamesDTO);
		appState.setEnabled(false);
		app.getStateManager()
		   .attach(appState);
		super.initialize(stateManager, app);
	}

	@Override
	public void update(float tpf) {
		handleMovement(tpf);

	}

	private void handleMovement(float tpf) {
		if (gameStateDTO.getKeyPressDTO()
						.isMoveLeftPress()) {
			rotateCharacterAndCamera(tpf, true);
		}
		if (gameStateDTO.getKeyPressDTO()
						.isMoveRightPress()) {
			rotateCharacterAndCamera(tpf, false);
		}
		handleCameraMovement();

	}

	private void handleCameraMovement() {
		Vector3f dalePosition = GameApplication.getInstance()
											   .getRootNode()
											   .getChild(
													   nodeNamesDTO.getDaleNodeName())
											   .getLocalTranslation();
		Vector3f dalePositionMinusViewDirection = calculateCameraPositionBasedOnDaleViewDirection(
				dalePosition);
		adjustCameraYPosition(dalePositionMinusViewDirection);
		camera.setLocation(dalePositionMinusViewDirection);
	}

	private void adjustCameraYPosition(
			Vector3f dalePositionMinusViewDirection) {
		float distanceAboveHead = 3;
		if (app.getStateManager()
			   .getState(CarriedObjectAppState.class)
			   .isEnabled()) {
			distanceAboveHead = 10;
		}
		dalePositionMinusViewDirection.setY(
				dalePositionMinusViewDirection.getY() + distanceAboveHead);
	}

	private Vector3f calculateCameraPositionBasedOnDaleViewDirection(
			Vector3f dalePosition) {
		Vector3f cameraDirection = camera.getDirection();
		Vector3f viewDirectionScaled = cameraDirection.mult(20);
		viewDirectionScaled.setY(viewDirectionScaled.getY());
		return dalePosition.subtract(viewDirectionScaled);
	}

	private void rotateCharacterAndCamera(float tpf, boolean left) {
		Quaternion quaternion = new Quaternion();
		int multiplier = left ? 1 : -1;
		quaternion.fromAngleAxis(FastMath.DEG_TO_RAD * 90 * multiplier * tpf,
				Vector3f.UNIT_Y);
		camera.setRotation(quaternion.mult(camera.getRotation()));
	}

}
