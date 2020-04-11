package core.controllers;

import core.animationEventListeners.DaleAnimationListener;
import core.animationEventListeners.DogAnimationListener;
import dto.DogStateDTO;
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
		for (DogStateDTO dog : gameStateDTO.getDogStateDTOS()) {
			DogAnimationListener dogAnimationListener = new DogAnimationListener(
					dog);
			dogAnimationListeners.add(dogAnimationListener);
			dogAnimationListener.setUpAnimations();
		}
	}

	public void handleAnimations() {
		daleAnimationListener.handleAnimation();

	}

}
