package core.appState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import constants.NodeNames;
import core.animationEventListeners.DaleAnimationListener;
import core.animationEventListeners.DogAnimationListener;
import dto.GameStateDTO;

import java.util.ArrayList;
import java.util.List;

public class AnimationsAppState extends AbstractAppState {

	private DaleAnimationListener daleAnimationListener;
	private List<DogAnimationListener> dogAnimationListeners = new ArrayList<>();
	private GameStateDTO gameStateDTO;

	public AnimationsAppState(GameStateDTO gameStateDTO) {
		this.gameStateDTO = gameStateDTO;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		daleAnimationListener = new DaleAnimationListener(gameStateDTO, app);
		Node rootNode = ((SimpleApplication) app).getRootNode();
		daleAnimationListener.setUpAnimations();
		Node dogs = (Node) rootNode.getChild(NodeNames.getDogs());
		for (Spatial dog : dogs.getChildren()) {
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
