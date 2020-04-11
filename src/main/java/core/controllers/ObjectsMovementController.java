package core.controllers;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import constants.PhysicsControls;
import core.GameApplication;
import core.animationEventListeners.DaleAnimationListener;
import dto.DaleStateDTO;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

public class ObjectsMovementController {

	private GameStateDTO gameStateDTO;
	private ObjectsHolderDTO objectsHolderDTO;
	private Camera camera;

	public ObjectsMovementController(DaleAnimationListener daleAnimationListener,
			GameStateDTO gameStateDTO, ObjectsHolderDTO objectsHolderDTO) {
		this.gameStateDTO = gameStateDTO;
		this.objectsHolderDTO = objectsHolderDTO;
		GameApplication gameApplication = GameApplication.getInstance();
		camera = gameApplication.getCamera();
	}

	public void handleMovement(float tpf) {
		if (gameStateDTO.getDaleStateDTO()
						.isMovingLeft()) {
			rotateCharacterAndCamera(tpf, true);
		}
		if (gameStateDTO.getDaleStateDTO()
						.isMovingRight()) {
			rotateCharacterAndCamera(tpf, false);
		}
		handleCameraMovement();

	}

	public void moveDaleBack() {
		if (!gameStateDTO.getDaleStateDTO()
						 .isAlive()) {
			return;
		}
		CharacterControl control = objectsHolderDTO.getDale()
												   .getControl(
														   PhysicsControls.DALE);
		Vector3f direction = camera.getDirection();
		Vector3f multiplied = direction.mult(new Vector3f(30f, 1, 30f));
		control.setPhysicsLocation(control.getPhysicsLocation()
										  .subtract(multiplied));
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
