package core;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;

public class ObjectsMovementHandler {

	private AnimationController animationController;
	private Camera camera;
	private DaleState daleState;
	private ModelLoader modelLoader;
	private Vector3f modifiableWalkDirectionVector = new Vector3f(0, 0, 0);

	public ObjectsMovementHandler(AnimationController animationController,
			Camera camera, DaleState daleState, ModelLoader modelLoader) {
		this.animationController = animationController;
		this.camera = camera;
		this.daleState = daleState;
		this.modelLoader = modelLoader;
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
		CharacterControl control = daleState.getCharacterControl();
		control.setViewDirection(camera.getDirection());
		control.setWalkDirection(modifiableWalkDirectionVector);
	}

	private void handleCameraMovement() {
		Vector3f dalePosition = modelLoader.getDale()
										   .getLocalTranslation();
		CharacterControl control = daleState.getCharacterControl();
		Vector3f dalePositionMinusViewDirection = calculateCameraPositionBasedOnDaleViewDirection(
				dalePosition, control);
		adjustCameraYPosition(dalePositionMinusViewDirection);
		camera.setLocation(dalePositionMinusViewDirection);
	}

	private void adjustCameraYPosition(
			Vector3f dalePositionMinusViewDirection) {
		dalePositionMinusViewDirection.setY(
				dalePositionMinusViewDirection.getY() + 3);
		if (dalePositionMinusViewDirection.getY() < 5) {
			dalePositionMinusViewDirection.setY(5);
		}
	}

	private Vector3f calculateCameraPositionBasedOnDaleViewDirection(
			Vector3f dalePosition, CharacterControl control) {
		Vector3f viewDirection = control.getViewDirection();
		Vector3f viewDirectionScaled = viewDirection.mult(20);
		viewDirectionScaled.setY(viewDirectionScaled.getY());
		return dalePosition.subtract(viewDirectionScaled);
	}

	private void handleMovementByKeys(Vector3f walkDirection, float tpf) {
		Vector3f camDir = camera.getDirection()
								.clone()
								.multLocal(0.5f);
		camDir.y = 0;
		if (daleState.isMovingForward()) {
			walkDirection.addLocal(camDir);
			animationController.animateMovingForward();
		}
		if (daleState.isMovingBackward()) {
			walkDirection.addLocal(camDir.negate());
		}
		if (daleState.isMovingLeft()) {
		}
		setMovementDirection();

	}

	public void daleJump() {
		CharacterControl control = daleState.getCharacterControl();
		if (control.onGround()) {
			control.jump(new Vector3f(0, 10f, 0));
		}
	}

	public void moveBoxAboveDale() {
		if (!daleState.isCarryingThrowableObject()) {
			return;
		}
		Geometry object = daleState.getThrowableObjectDTO()
								   .getObject();
		Vector3f dalePosition = modelLoader.getDale()
										   .getLocalTranslation();
		float height = ((BoundingBox) modelLoader.getDale()
												 .getWorldBound()).getYExtent();
		float boxHeight = ((BoundingBox) modelLoader.getBox()
													.getWorldBound()).getYExtent();
		RigidBodyControl control = modelLoader.getBox()
											  .getControl(
													  RigidBodyControl.class);
		control.setPhysicsLocation(new Vector3f(dalePosition.getX(),
				dalePosition.getY() + height + boxHeight + 3f,
				dalePosition.getZ()));
	}
}
