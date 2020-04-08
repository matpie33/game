package core.controllers;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.scene.Spatial;
import constants.PhysicsControls;
import dto.DogDataDTO;
import dto.GameStateDTO;

import java.util.ArrayList;
import java.util.List;

public class ObjectsRemovingController {

	private GameStateDTO gameStateDTO;

	public ObjectsRemovingController(GameStateDTO gameStateDTO) {
		this.gameStateDTO = gameStateDTO;
	}

	public void handleObjectsRemoved() {
		List<DogDataDTO> removedDogs = new ArrayList<>();
		for (DogDataDTO dogDataDTO : gameStateDTO.getDogDataDTOS()) {
			if (!dogDataDTO.isAlive()) {
				Spatial dog = dogDataDTO.getDog();

				CharacterControl characterControl = dog.getControl(PhysicsControls.DOG);
				GhostControl ghostControl = dog.getControl(GhostControl.class);
				characterControl.getPhysicsSpace()
					   .remove(characterControl);
				ghostControl.getPhysicsSpace()
					   .remove(ghostControl);
				for (int i = 0; i < dog.getNumControls(); i++) {
					dog.removeControl(dog.getControl(i));

				}
				dog.removeFromParent();
				removedDogs.add(dogDataDTO);
			}
		}
		removedDogs.forEach(gameStateDTO.getDogDataDTOS()::remove);
	}

}
