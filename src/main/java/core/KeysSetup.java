package core;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.renderer.Camera;

public class KeysSetup implements ActionListener {

	public static final String MOVE_LEFT = "moveLeft";
	public static final String MOVE_RIGHT = "moveRight";
	public static final String MOVE_FORWARD = "moveForward";
	public static final String MOVE_BACKWARD = "moveBackward";

	private AnimationController animationController;
	private DaleState daleState;

	public KeysSetup(AnimationController animationController, DaleState daleState) {
		this.animationController = animationController;
		this.daleState = daleState;
	}

	public void setupKeys(InputManager inputManager, ModelLoader modelLoader,
			Camera camera) {
		inputManager.addMapping(MOVE_LEFT, new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping(MOVE_RIGHT, new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping(MOVE_FORWARD, new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping(MOVE_BACKWARD, new KeyTrigger(KeyInput.KEY_S));
		inputManager.addListener(this, MOVE_LEFT, MOVE_RIGHT, MOVE_FORWARD,
				MOVE_BACKWARD);
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (MOVE_LEFT.equals(name)){
			daleState.setMovingLeft(isPressed);
		}
		if (MOVE_RIGHT.equals(name)){
			daleState.setMovingRight(isPressed);
		}
		if (MOVE_FORWARD.equals(name)){
			daleState.setMovingForward(isPressed);
		}
		if (MOVE_BACKWARD.equals(name)){
			daleState.setMovingBackward(isPressed);
		}

	}

}
