package core.initialization;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import core.GameApplication;
import dto.DaleStateDTO;
import core.controllers.ObjectsMovementController;
import core.controllers.ThrowingController;

public class KeysSetup implements ActionListener {

	public static final String MOVE_LEFT = "moveLeft";
	public static final String MOVE_RIGHT = "moveRight";
	public static final String MOVE_FORWARD = "moveForward";
	public static final String MOVE_BACKWARD = "moveBackward";
	public static final String JUMP = "jump";
	public static final String PICK_THROWABLE_OBJECT = "pickThrowableObject";
	public static final String THROW_OBJECT = "throwObject";

	private DaleStateDTO daleStateDTO;
	private ObjectsMovementController objectsMovementController;
	private ThrowingController throwingController;

	public KeysSetup(DaleStateDTO daleStateDTO,
			ObjectsMovementController objectsMovementController,
			ThrowingController throwingController) {
		this.daleStateDTO = daleStateDTO;
		this.objectsMovementController = objectsMovementController;
		this.throwingController = throwingController;
	}

	public void setupKeys() {
		InputManager inputManager = GameApplication.getInstance()
												   .getInputManager();
		inputManager.addMapping(MOVE_LEFT, new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping(MOVE_RIGHT, new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping(MOVE_FORWARD, new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping(MOVE_BACKWARD, new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping(JUMP, new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addMapping(PICK_THROWABLE_OBJECT,
				new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		inputManager.addMapping(THROW_OBJECT,
				new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addListener(this, MOVE_LEFT, MOVE_RIGHT, MOVE_FORWARD,
				MOVE_BACKWARD, JUMP, PICK_THROWABLE_OBJECT, THROW_OBJECT);
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (MOVE_LEFT.equals(name)) {
			daleStateDTO.setMovingLeft(isPressed);
		}
		if (MOVE_RIGHT.equals(name)) {
			daleStateDTO.setMovingRight(isPressed);
		}
		if (MOVE_FORWARD.equals(name)) {
			daleStateDTO.setMovingForward(isPressed);
		}
		if (MOVE_BACKWARD.equals(name)) {
			daleStateDTO.setMovingBackward(isPressed);
		}
		if (JUMP.equals(name)) {
			objectsMovementController.daleJump();
		}
		if (PICK_THROWABLE_OBJECT.equals(name) && isPressed) {
			throwingController.tryToPickObject();
		}
		if (THROW_OBJECT.equals(name) && isPressed) {
			throwingController.tryToThrowObject();
		}

	}

}