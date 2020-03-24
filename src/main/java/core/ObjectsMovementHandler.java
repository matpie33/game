package core;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

public class ObjectsMovementHandler {

	public static final int DISTANCE_X_FROM_CAMERA_TO_DALE = 20;
	public static final int DISTANCE_Y_FROM_CAMERA_TO_DALE = 18;
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

	public void handleMovement() {
		handleDaleMovement();
		handleCameraMovement();

	}

	private void handleDaleMovement() {
		modifiableWalkDirectionVector.set(0, 0, 0);
		handleMovementByKeys(modifiableWalkDirectionVector);
		setMovementDirection();
	}

	private void setMovementDirection() {
		CharacterControl control = modelLoader.getDale()
											  .getControl(
													  CharacterControl.class);
		control.setViewDirection(camera.getDirection());
		control.setWalkDirection(modifiableWalkDirectionVector);
	}

	private void handleCameraMovement() {
		Vector3f dalePosition = modelLoader.getDale()
										   .getLocalTranslation();

		camera.setLocation(new Vector3f(
				dalePosition.getX() - DISTANCE_X_FROM_CAMERA_TO_DALE,
				dalePosition.getY() + DISTANCE_Y_FROM_CAMERA_TO_DALE,
				dalePosition.getZ()));
	}

	private void handleMovementByKeys(Vector3f walkDirection) {
		Vector3f camDir = camera.getDirection()
								.clone()
								.multLocal(0.1f);
		camDir.y = 0;
		if (daleState.isMovingForward()) {
			walkDirection.addLocal(camDir);
			animationController.animateMovingForward();
		}
		if (daleState.isMovingBackward()) {
			walkDirection.addLocal(camDir.negate());
		}
	}

}
