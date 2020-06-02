package core.controllers;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;
import constants.PhysicsControls;
import dto.DogStateDTO;
import dto.GameStateDTO;

public class ObjectsRemovingController {

	private GameStateDTO gameStateDTO;
	private EffectsController effectsController;

	public ObjectsRemovingController(GameStateDTO gameStateDTO) {
		this.gameStateDTO = gameStateDTO;
		this.effectsController = new EffectsController();
	}

	public void removeDog(Spatial dog) {
		if (dog.getParent() == null) {
			return;
		}
		DogStateDTO dogStateDTO = gameStateDTO.getDogStateDTOS()
											  .stream()
											  .filter(dogState -> dogState.getDog()
																		  .equals(dog))
											  .findFirst()
											  .orElseThrow(
													  () -> createExeptionForDogCollision(
															  dog));

		CharacterControl characterControl = dog.getControl(PhysicsControls.DOG);
		characterControl.getPhysicsSpace()
						.remove(characterControl);
		GhostControl ghostControl = dog.getControl(GhostControl.class);
		ghostControl.getPhysicsSpace()
					.remove(ghostControl);
		dog.removeFromParent();
		gameStateDTO.getDogStateDTOS()
					.remove(dogStateDTO);
	}

	private IllegalArgumentException createExeptionForDogCollision(
			Spatial dogNode) {
		return new IllegalArgumentException(
				"Dog collided with obstacle, but dog state not " + "found: "
						+ dogNode);
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
