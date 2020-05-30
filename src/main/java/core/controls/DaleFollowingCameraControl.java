package core.controls;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import core.GameApplication;
import core.appState.IdleTimeCheckAppState;
import dto.DaleStateDTO;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

public class DaleFollowingCameraControl extends AbstractControl {

	private GameStateDTO gameStateDTO;
	private Camera camera;
	private ObjectsHolderDTO objectsHolderDTO;

	public DaleFollowingCameraControl(GameStateDTO gameStateDTO, Camera camera,
			ObjectsHolderDTO objectsHolderDTO) {
		this.gameStateDTO = gameStateDTO;
		this.camera = camera;
		this.objectsHolderDTO = objectsHolderDTO;
	}

	@Override
	protected void controlUpdate(float tpf) {
		handleMovement(tpf);
		handleThrowingDestinationChase(tpf);
	}

	private void handleThrowingDestinationChase(float tpf) {

		IdleTimeCheckAppState idleTimeCheckAppState = GameApplication.getInstance()
																	 .getStateManager()
																	 .getState(
																			 IdleTimeCheckAppState.class);
		if (gameStateDTO.getDaleStateDTO()
						.hasThrowingDestination()
				&& idleTimeCheckAppState.getIdleTime() > 0.5f) {
			Vector3f throwingDestinationLocation = gameStateDTO.getDaleStateDTO()
															   .getThrowingDestination()
															   .getWorldTranslation();
			camera.lookAt(throwingDestinationLocation, Vector3f.UNIT_Y);
			objectsHolderDTO.getDale()
							.lookAt(throwingDestinationLocation,
									Vector3f.UNIT_Y);
		}

	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}

	public void handleMovement(float tpf) {
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
		Vector3f dalePosition = objectsHolderDTO.getDale()
												.getLocalTranslation();
		Vector3f dalePositionMinusViewDirection = calculateCameraPositionBasedOnDaleViewDirection(
				dalePosition);
		adjustCameraYPosition(dalePositionMinusViewDirection);
		camera.setLocation(dalePositionMinusViewDirection);
	}

	private void adjustCameraYPosition(
			Vector3f dalePositionMinusViewDirection) {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		float distanceAboveHead = 3;
		if (daleStateDTO.isCarryingThrowableObject()) {
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
