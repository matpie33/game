package core.appState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import constants.PhysicsControls;
import core.controls.DogMovingInsideAreaControl;
import dto.GameStateDTO;
import dto.NodeNamesDTO;

public class DogFollowingDaleAppState extends AbstractAppState {

	private GameStateDTO gameStateDTO;
	private NodeNamesDTO nodeNamesDTO;
	private SimpleApplication app;

	public DogFollowingDaleAppState(NodeNamesDTO nodeNamesDTO,
			GameStateDTO gameStateDTO) {
		this.nodeNamesDTO = nodeNamesDTO;
		this.gameStateDTO = gameStateDTO;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		this.app = ((SimpleApplication) app);
		super.initialize(stateManager, app);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		moveEnemies(tpf);
	}

	public void moveEnemies(float tpf) {
		Node rootNode = app.getRootNode();
		Spatial dale = rootNode.getChild(nodeNamesDTO.getDaleNodeName());
		gameStateDTO.getDogStateDTOS()
					.forEach(state -> {
						Spatial dog = state.getDog();
						if (state.isSeeingDale()
								&& gameStateDTO.getDaleStateDTO()
											   .isAlive()) {
							CharacterControl control = dog.getControl(
									PhysicsControls.DOG);
							dog.getControl(DogMovingInsideAreaControl.class)
							   .setEnabled(false);
							control.setWalkDirection(control.getViewDirection()
															.normalize()
															.mult(0.2f));

							control.setViewDirection(dale.getLocalTranslation()
														 .subtract(
																 control.getPhysicsLocation()));
						}
						if (!state.isSeeingDale()) {
							dog.getControl(DogMovingInsideAreaControl.class)
							   .setEnabled(true);
						}
					});

	}

}
