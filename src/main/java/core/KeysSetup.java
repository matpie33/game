package core;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

public class KeysSetup {

	public static final String MOVE_LEFT_MAPPING_NAME = "moveLeft";

	public void setupKeys(InputManager inputManager, ModelLoader modelLoader) {
		inputManager.addMapping(MOVE_LEFT_MAPPING_NAME,
				new KeyTrigger(KeyInput.KEY_J));
		inputManager.addListener((ActionListener) (name, keyPressed, tpf) -> {
			if (name.equals(MOVE_LEFT_MAPPING_NAME)) {
				modelLoader.getDale()
						   .move(1, 0, 0);
			}
		}, MOVE_LEFT_MAPPING_NAME);
	}

}
