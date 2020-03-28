package core;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;

public class KeysSetup implements ActionListener {

	public static final String MOVE_LEFT = "moveLeft";
	public static final String MOVE_RIGHT = "moveRight";
	public static final String MOVE_FORWARD = "moveForward";
	public static final String MOVE_BACKWARD = "moveBackward";
	public static final String JUMP = "jump";
	public static final String PICK_THROWABLE_OBJECT = "pickThrowableObject";

	private DaleState daleState;
	private ObjectsMovementHandler objectsMovementHandler;
	private ThrowingHandler throwingHandler;

	public KeysSetup(DaleState daleState,
			ObjectsMovementHandler objectsMovementHandler,
			ThrowingHandler throwingHandler) {
		this.daleState = daleState;
		this.objectsMovementHandler = objectsMovementHandler;
		this.throwingHandler = throwingHandler;
	}

	public void setupKeys(InputManager inputManager) {
		inputManager.addMapping(MOVE_LEFT, new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping(MOVE_RIGHT, new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping(MOVE_FORWARD, new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping(MOVE_BACKWARD, new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping(JUMP, new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addMapping(PICK_THROWABLE_OBJECT,
				new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		inputManager.addListener(this, MOVE_LEFT, MOVE_RIGHT, MOVE_FORWARD,
				MOVE_BACKWARD, JUMP, PICK_THROWABLE_OBJECT);
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (MOVE_LEFT.equals(name)) {
			daleState.setMovingLeft(isPressed);
		}
		if (MOVE_RIGHT.equals(name)) {
			daleState.setMovingRight(isPressed);
		}
		if (MOVE_FORWARD.equals(name)) {
			daleState.setMovingForward(isPressed);
		}
		if (MOVE_BACKWARD.equals(name)) {
			daleState.setMovingBackward(isPressed);
		}
		if (JUMP.equals(name)) {
			objectsMovementHandler.daleJump();
		}
		if (PICK_THROWABLE_OBJECT.equals(name) && isPressed) {
			throwingHandler.handleRightClickPressed();
		}

	}

}