package core.controllers;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;
import constants.PhysicsControls;
import dto.DogDataDTO;
import dto.GameStateDTO;

import java.util.ArrayList;
import java.util.List;

public class ObjectsRemovingController {

	private GameStateDTO gameStateDTO;
	private EffectsController effectsController;

	public ObjectsRemovingController(GameStateDTO gameStateDTO,
			EffectsController effectsController) {
		this.gameStateDTO = gameStateDTO;
		this.effectsController = effectsController;
	}

	public void handleObjectsRemoved() {
		List<DogDataDTO> removedDogs = new ArrayList<>();
		for (DogDataDTO dogDataDTO : gameStateDTO.getDogDataDTOS()) {
			if (!dogDataDTO.isAlive()) {
				Spatial dog = dogDataDTO.getDog();

				CharacterControl characterControl = dog.getControl(
						PhysicsControls.DOG);
				characterControl.getPhysicsSpace()
								.remove(dog);
				dog.removeFromParent();
				removedDogs.add(dogDataDTO);
			}
		}
		removedDogs.forEach(gameStateDTO.getDogDataDTOS()::remove);
		for (Spatial objectToRemove : gameStateDTO.getObjectsToRemove()) {
			if (objectToRemove.getParent() == null) {
				continue;
			}
			RigidBodyControl control = objectToRemove.getControl(
					PhysicsControls.BOX);
			control.getPhysicsSpace()
				   .remove(objectToRemove);
			objectToRemove.getParent()
						  .detachChild(objectToRemove);
			effectsController.createBoxDestroyEffect(objectToRemove);
		}
	}

}
