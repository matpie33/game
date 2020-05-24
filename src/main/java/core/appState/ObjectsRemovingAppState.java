package core.appState;

import com.jme3.app.state.AbstractAppState;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;
import constants.PhysicsControls;
import core.controllers.EffectsController;
import dto.DogStateDTO;
import dto.GameStateDTO;

import java.util.ArrayList;
import java.util.List;

public class ObjectsRemovingAppState extends AbstractAppState {

	private GameStateDTO gameStateDTO;
	private EffectsController effectsController;

	public ObjectsRemovingAppState(GameStateDTO gameStateDTO) {
		this.gameStateDTO = gameStateDTO;
		this.effectsController = new EffectsController();
	}

	@Override
	public void update(float tpf) {
		List<DogStateDTO> removedDogs = new ArrayList<>();
		for (DogStateDTO dogStateDTO : gameStateDTO.getDogStateDTOS()) {
			if (!dogStateDTO.isAlive()) {
				Spatial dog = dogStateDTO.getDog();

				CharacterControl characterControl = dog.getControl(
						PhysicsControls.DOG);
				characterControl.getPhysicsSpace()
								.remove(dog);
				dog.removeFromParent();
				removedDogs.add(dogStateDTO);
			}
		}
		removedDogs.forEach(gameStateDTO.getDogStateDTOS()::remove);
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
