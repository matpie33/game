package core.appState;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import core.animationEventListeners.DaleAnimationListener;
import core.animationEventListeners.DogAnimationListener;
import dto.DogStateDTO;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

import java.util.ArrayList;
import java.util.List;

public class AnimationsAppState extends AbstractAppState {

	private DaleAnimationListener daleAnimationListener;
	private List<DogAnimationListener> dogAnimationListeners = new ArrayList<>();
	private GameStateDTO gameStateDTO;
	private ObjectsHolderDTO objectsHolderDTO;

	public AnimationsAppState(GameStateDTO gameStateDTO,
			ObjectsHolderDTO objectsHolderDTO) {
		this.gameStateDTO = gameStateDTO;
		this.objectsHolderDTO = objectsHolderDTO;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		daleAnimationListener = new DaleAnimationListener(gameStateDTO,
				objectsHolderDTO, app);
		daleAnimationListener.setUpAnimations();
		for (DogStateDTO dog : gameStateDTO.getDogStateDTOS()) {
			DogAnimationListener dogAnimationListener = new DogAnimationListener(
					dog);
			dogAnimationListeners.add(dogAnimationListener);
			dogAnimationListener.setUpAnimations();
		}
	}

	@Override
	public void update(float tpf) {
		daleAnimationListener.handleAnimation();
	}

}
