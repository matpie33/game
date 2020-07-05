package core.controllers;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;
import constants.PhysicsControls;

public class ObjectsRemovingController {

	private EffectsController effectsController;

	public ObjectsRemovingController() {
		this.effectsController = new EffectsController();
	}

	public void removeDog(Spatial dog) {
		if (dog.getParent() == null) {
			return;
		}

		CharacterControl characterControl = dog.getControl(PhysicsControls.DOG);
		characterControl.getPhysicsSpace()
						.remove(characterControl);
		GhostControl ghostControl = dog.getControl(GhostControl.class);
		ghostControl.getPhysicsSpace()
					.remove(ghostControl);
		dog.removeFromParent();
	}

	public void removeBox(Spatial box) {
		if (box.getParent() == null) {
			return;
		}
		RigidBodyControl control = box.getControl(PhysicsControls.BOX);
		control.getPhysicsSpace()
			   .remove(control);
		box.getParent()
		   .detachChild(box);
		effectsController.createBoxDestroyEffect(box);
	}

}
