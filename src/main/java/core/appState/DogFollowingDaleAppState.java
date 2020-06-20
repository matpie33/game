package core.appState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import constants.NodeNames;
import constants.PhysicsControls;
import core.controls.DogMovingInsideAreaControl;
import dto.GameStateDTO;

import java.util.HashSet;
import java.util.Set;

public class DogFollowingDaleAppState extends AbstractAppState {

	private SimpleApplication app;
	private Set<Spatial> enemiesSeeingDale = new HashSet<>();


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
		Spatial dale = rootNode.getChild(NodeNames.getDale());
		DaleHPAppState daleHPAppState = app.getStateManager()
										   .getState(DaleHPAppState.class);
		enemiesSeeingDale.forEach(enemy -> {
			if (daleHPAppState.isAlive()) {
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
