package core;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

public class KeysSetup {

	public static final String MOVE_LEFT_MAPPING_NAME = "moveForward";

	private AnimationController animationController;
	private ObjectPositionRotationHandler objectPositionRotationHandler = new ObjectPositionRotationHandler();

	public KeysSetup(AnimationController animationController) {
		this.animationController = animationController;
	}

	public void setupKeys(InputManager inputManager, ModelLoader modelLoader) {
		inputManager.addMapping(MOVE_LEFT_MAPPING_NAME,
				new KeyTrigger(KeyInput.KEY_W));
		inputManager.addListener((ActionListener) (name, keyPressed, tpf) -> {
			if (name.equals(MOVE_LEFT_MAPPING_NAME)) {
				if (animationController.isStanding()){
					objectPositionRotationHandler.moveObjectForward(modelLoader.getDale());
				}
				animationController.animateMovingForward();

			}
		}, MOVE_LEFT_MAPPING_NAME);
	}

}
