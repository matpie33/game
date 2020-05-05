package core.initialization;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import core.GameApplication;
import dto.DaleStateDTO;
import dto.GameStateDTO;

public class KeysSetup implements ActionListener {

	public static final String MOVE_LEFT = "moveLeft";
	public static final String MOVE_RIGHT = "moveRight";
	public static final String MOVE_FORWARD_OR_MOVE_IN_LEDGE = "moveForward";
	public static final String MOVE_BACKWARD = "moveBackward";
	public static final String JUMP = "jump";
	public static final String PICK_THROWABLE_OBJECT_OR_LET_GO_LEDGE = "pickThrowableObject";
	public static final String THROW_OBJECT = "throwObject";

	private GameStateDTO gameStateDTO;

	public KeysSetup(GameStateDTO gameStateDTO) {
		this.gameStateDTO = gameStateDTO;
	}

	public void setupKeys() {
		InputManager inputManager = GameApplication.getInstance()
												   .getInputManager();
		inputManager.addMapping(MOVE_LEFT, new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping(MOVE_RIGHT, new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping(MOVE_FORWARD_OR_MOVE_IN_LEDGE, new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping(MOVE_BACKWARD, new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping(JUMP, new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addMapping(PICK_THROWABLE_OBJECT_OR_LET_GO_LEDGE,
				new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		inputManager.addMapping(THROW_OBJECT,
				new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addListener(this, MOVE_LEFT, MOVE_RIGHT,
				MOVE_FORWARD_OR_MOVE_IN_LEDGE,
				MOVE_BACKWARD, JUMP, PICK_THROWABLE_OBJECT_OR_LET_GO_LEDGE, THROW_OBJECT);
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		if (MOVE_LEFT.equals(name)) {
			daleStateDTO.setMovingLeft(isPressed);
		}
		if (MOVE_RIGHT.equals(name)) {
			daleStateDTO.setMovingRight(isPressed);
		}
		if (MOVE_FORWARD_OR_MOVE_IN_LEDGE.equals(name)) {
			daleStateDTO.setMovingForward(isPressed);
			if (daleStateDTO.isGrabbingLedge()){
				daleStateDTO.setMoveInLedge(true);

			}
		}
		if (MOVE_BACKWARD.equals(name)) {
			daleStateDTO.setMovingBackward(isPressed);
		}
		if (JUMP.equals(name)) {
			daleStateDTO.setJumping(isPressed);
		}
		if (PICK_THROWABLE_OBJECT_OR_LET_GO_LEDGE.equals(name)) {
			//TODO only set state here, do not check state
			daleStateDTO.setPickingObject(
					isPressed && !daleStateDTO.isCarryingThrowableObject());
			daleStateDTO.setPuttingAsideObject(
					isPressed && daleStateDTO.isCarryingThrowableObject());
			if (isPressed){
				daleStateDTO.setLetGoLedge(isPressed);
			}
		}
		if (THROW_OBJECT.equals(name)) {
			if (daleStateDTO.isCarryingThrowableObject()) {
				daleStateDTO.setThrowingObject(isPressed);
			}
		}

	}

}
