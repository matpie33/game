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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DogFollowingDaleAppState extends AbstractAppState {

	private GameStateDTO gameStateDTO;
	private NodeNamesDTO nodeNamesDTO;
	private SimpleApplication app;
	private Set<Spatial> enemiesSeeingDale = new HashSet<>();

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
		enemiesSeeingDale.forEach(enemy -> {
			if (gameStateDTO.getDaleStateDTO()
							.isAlive()) {
				CharacterControl control = enemy.getControl(
						PhysicsControls.DOG);
				enemy.getControl(DogMovingInsideAreaControl.class)
					 .setEnabled(false);
				control.setWalkDirection(control.getViewDirection()
												.normalize()
												.mult(0.2f));

				control.setViewDirection(dale.getLocalTranslation()
											 .subtract(
													 control.getPhysicsLocation()));
			}
		});

	}

	public void setEnemySeeingDale(boolean isSeeing, Spatial enemy) {
		if (isSeeing) {
			enemiesSeeingDale.add(enemy);
		}
		else {
			enemiesSeeingDale.remove(enemy);
			enemy.getControl(DogMovingInsideAreaControl.class)
				 .setEnabled(true);
		}
	}

}
