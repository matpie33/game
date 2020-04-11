package core.controllers;

import com.jme3.scene.Spatial;
import core.animationEventListeners.DaleAnimationListener;
import core.animationEventListeners.DogAnimationListener;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

import java.util.ArrayList;
import java.util.List;

public class AnimationsController {

	private DaleAnimationListener daleAnimationListener;
	private List<DogAnimationListener> dogAnimationListeners = new ArrayList<>();
	private GameStateDTO gameStateDTO;
	private ObjectsHolderDTO objectsHolderDTO;

	public AnimationsController(GameStateDTO gameStateDTO,
			ObjectsHolderDTO objectsHolderDTO) {
		this.gameStateDTO = gameStateDTO;
		this.objectsHolderDTO = objectsHolderDTO;
	}

	public void setUp() {
		daleAnimationListener = new DaleAnimationListener(gameStateDTO,
				objectsHolderDTO);
		daleAnimationListener.setUpAnimations();
		for (Spatial dog : objectsHolderDTO.getDogs()) {
			DogAnimationListener dogAnimationListener = new DogAnimationListener(
					dog);
			dogAnimationListeners.add(dogAnimationListener);
			dogAnimationListener.setUpAnimations();
		}
	}

	public DaleAnimationListener getDaleAnimationListener() {
		return daleAnimationListener;
	}
}
