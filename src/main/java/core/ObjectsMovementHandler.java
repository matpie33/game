package core;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import constants.PhysicsControls;

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
		Spatial dale = modelLoader.getDale();
		dale.getControl(PhysicsControls.DALE)
			.setWalkDirection(modifiableWalkDirectionVector);
	}

	private void handleCameraMovement() {
		Vector3f dalePosition = modelLoader.getDale()
										   .getLocalTranslation();
		Vector3f dalePositionMinusViewDirection = calculateCameraPositionBasedOnDaleViewDirection(
				dalePosition);
		adjustCameraYPosition(dalePositionMinusViewDirection);
		camera.setLocation(dalePositionMinusViewDirection);
	}

	private void adjustCameraYPosition(
			Vector3f dalePositionMinusViewDirection) {
		float distanceAboveHead = 3;
		if (daleState.isCarryingThrowableObject()) {
			distanceAboveHead = 10;
		}
		dalePositionMinusViewDirection.setY(
				dalePositionMinusViewDirection.getY() + distanceAboveHead);
		if (dalePositionMinusViewDirection.getY() < 5) {
			dalePositionMinusViewDirection.setY(5);
		}
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
		if (daleState.isMovingForward()) {
			setDaleViewDirectionToCameraDirection();
			walkDirection.addLocal(camDir);
			animationController.animateMovingForward();
		}
		if (daleState.isMovingBackward()) {
			setDaleViewDirectionToCameraDirection();
			walkDirection.addLocal(camDir.negate()
										 .mult(0.5f));
			animationController.animateMovingBackward();
		}
		if (daleState.isMovingLeft()) {
		}
		setMovementDirection();

	}

	private void setDaleViewDirectionToCameraDirection() {
		Vector3f direction = camera.getDirection();
		modelLoader.getDale()
				   .getControl(PhysicsControls.DALE)
				   .setViewDirection(new Vector3f(direction.getX(), 0,
						   direction.getZ()));
	}

	public void daleJump() {
		if (modelLoader.getDale()
					   .getControl(PhysicsControls.DALE)
					   .onGround()) {
			modelLoader.getDale()
					   .getControl(PhysicsControls.DALE)
					   .jump(new Vector3f(0, 30f, 0));
		}
	}

	public void moveBoxAboveDale() {
		if (!daleState.isCarryingThrowableObject()) {
			return;
		}
		Geometry carriedObject = daleState.getCarriedObject()
										  .getObject();
		Vector3f dalePosition = modelLoader.getDale()
										   .getLocalTranslation();
		float carriedObjectHeight = ((BoundingBox) modelLoader.getDale()
															  .getWorldBound()).getYExtent();
		float boxHeight = ((BoundingBox) carriedObject.getWorldBound()).getYExtent();
		carriedObject.getParent()
					 .getControl(PhysicsControls.BOX)
					 .setPhysicsLocation(new Vector3f(dalePosition.getX(),
							 dalePosition.getY() + carriedObjectHeight
									 + boxHeight + 1f, dalePosition.getZ()));
	}
}
