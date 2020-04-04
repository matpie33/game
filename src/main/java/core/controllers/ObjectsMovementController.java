package core.controllers;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import constants.PhysicsControls;
import core.GameApplication;
import dto.DaleStateDTO;
import dto.ObjectsHolderDTO;

public class ObjectsMovementController {

	private AnimationController animationController;
	private DaleStateDTO daleStateDTO;
	private Vector3f modifiableWalkDirectionVector = new Vector3f(0, 0, 0);
	private ObjectsHolderDTO objectsHolderDTO;
	private Camera camera;

	public ObjectsMovementController(AnimationController animationController,
			DaleStateDTO daleStateDTO, ObjectsHolderDTO objectsHolderDTO) {
		this.animationController = animationController;
		this.daleStateDTO = daleStateDTO;
		this.objectsHolderDTO = objectsHolderDTO;
		GameApplication gameApplication = GameApplication.getInstance();
		camera = gameApplication.getCamera();
	}

	public void handleMovement(float tpf) {
		handleDaleMovement(tpf);
		handleCameraMovement();

	}

	private void handleDaleMovement(float tpf) {
		modifiableWalkDirectionVector.set(0, 0, 0);
		handleMovementByKeys(modifiableWalkDirectionVector, tpf);

	}

	private void setMovementDirection() {
		Spatial dale = objectsHolderDTO.getDale();
		dale.getControl(PhysicsControls.DALE)
			.setWalkDirection(modifiableWalkDirectionVector);
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

	private void handleMovementByKeys(Vector3f walkDirection, float tpf) {
		Vector3f camDir = camera.getDirection()
								.clone()
								.multLocal(0.5f);
		camDir.y = 0;
		if (daleStateDTO.isMovingForward()) {
			setDaleViewDirectionToCameraDirection();
			walkDirection.addLocal(camDir);
			animationController.animateMovingForward();
		}
		if (daleStateDTO.isMovingBackward()) {
			setDaleViewDirectionToCameraDirection();
			walkDirection.addLocal(camDir.negate()
										 .mult(0.5f));
			animationController.animateMovingBackward();
		}
		if (daleStateDTO.isMovingLeft()) {
		}
		setMovementDirection();

	}

	private void setDaleViewDirectionToCameraDirection() {
		Vector3f direction = camera.getDirection();
		objectsHolderDTO.getDale()
						.getControl(PhysicsControls.DALE)
						.setViewDirection(new Vector3f(direction.getX(), 0,
								direction.getZ()));
	}

	public void daleJump() {
		if (objectsHolderDTO.getDale()
							.getControl(PhysicsControls.DALE)
							.onGround()) {
			objectsHolderDTO.getDale()
							.getControl(PhysicsControls.DALE)
							.jump(new Vector3f(0, 30f, 0));
		}
	}

	public void moveBoxAboveDale() {
		if (!daleStateDTO.isCarryingThrowableObject()) {
			return;
		}
		Geometry carriedObject = daleStateDTO.getCarriedObject()
											 .getObject();
		Vector3f dalePosition = objectsHolderDTO.getDale()
												.getLocalTranslation();
		float carriedObjectHeight = ((BoundingBox) objectsHolderDTO.getDale()
																   .getWorldBound()).getYExtent();
		float boxHeight = ((BoundingBox) carriedObject.getWorldBound()).getYExtent();
		carriedObject.getParent()
					 .getControl(PhysicsControls.BOX)
					 .setPhysicsLocation(new Vector3f(dalePosition.getX(),
							 dalePosition.getY() + carriedObjectHeight
									 + boxHeight + 1f, dalePosition.getZ()));
	}
}
