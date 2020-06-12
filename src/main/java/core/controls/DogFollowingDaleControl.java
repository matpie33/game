package core.controls;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import constants.PhysicsControls;
import core.GameApplication;
import dto.DogStateDTO;
import dto.GameStateDTO;
import dto.NodeNamesDTO;

public class DogFollowingDaleControl extends AbstractControl {

	private DogStateDTO dogState;
	private GameStateDTO gameStateDTO;
	private NodeNamesDTO nodeNamesDTO;

	public DogFollowingDaleControl(DogStateDTO dogStateDTO,
			NodeNamesDTO nodeNamesDTO, GameStateDTO gameStateDTO) {
		this.nodeNamesDTO = nodeNamesDTO;
		this.gameStateDTO = gameStateDTO;
		this.dogState = dogStateDTO;
	}

	@Override
	protected void controlUpdate(float tpf) {
		moveEnemies(tpf);
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}

	public void moveEnemies(float tpf) {

		if (dogState.isSeeingDale() && gameStateDTO.getDaleStateDTO()
												   .isAlive()) {
			CharacterControl control = spatial.getControl(PhysicsControls.DOG);
			spatial.getControl(DogMovingInsideAreaControl.class).setEnabled(false);
			control.setWalkDirection(control.getViewDirection()
											.normalize()
											.mult(0.2f));

			control.setViewDirection(GameApplication.getInstance()
													.getRootNode()
													.getChild(
															nodeNamesDTO.getDaleNodeName())
													.getLocalTranslation()
													.subtract(
															control.getPhysicsLocation()));
		}
		if (!dogState.isSeeingDale()){
			spatial.getControl(DogMovingInsideAreaControl.class).setEnabled(true);
		}

	}

}
