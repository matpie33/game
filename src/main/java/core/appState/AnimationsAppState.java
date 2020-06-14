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
import dto.NodeNamesDTO;

import java.util.ArrayList;
import java.util.List;

public class AnimationsAppState extends AbstractAppState {

	private DaleAnimationListener daleAnimationListener;
	private List<DogAnimationListener> dogAnimationListeners = new ArrayList<>();
	private GameStateDTO gameStateDTO;
	private NodeNamesDTO nodeNamesDTO;

	public AnimationsAppState(GameStateDTO gameStateDTO,
			NodeNamesDTO nodeNamesDTO) {
		this.gameStateDTO = gameStateDTO;
		this.nodeNamesDTO = nodeNamesDTO;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		daleAnimationListener = new DaleAnimationListener(gameStateDTO,
				nodeNamesDTO, app);
		Node rootNode = ((SimpleApplication) app).getRootNode();
		daleAnimationListener.setUpAnimations();
		Node dogs = (Node) rootNode.getChild(NodeNames.DOGS);
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
