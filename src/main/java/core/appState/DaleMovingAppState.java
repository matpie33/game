package core.appState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import constants.PhysicsControls;
import core.GameApplication;
import dto.GameStateDTO;
import dto.KeyPressDTO;
import dto.NodeNamesDTO;

public class DaleMovingAppState extends AbstractAppState {

	private GameStateDTO gameStateDTO;
	private Vector3f modifiableWalkDirectionVector = new Vector3f(0, 0, 0);
	private GameApplication gameApplication;
	private SimpleApplication app;
	private NodeNamesDTO nodeNamesDTO;

	public DaleMovingAppState(GameStateDTO gameStateDTO,
			NodeNamesDTO nodeNamesDTO) {
		this.gameStateDTO = gameStateDTO;
		gameApplication = GameApplication.getInstance();
		this.nodeNamesDTO = nodeNamesDTO;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		this.app = ((SimpleApplication) app);
		super.initialize(stateManager, app);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		Spatial dale = app.getRootNode()
						  .getChild(nodeNamesDTO.getDaleNodeName());
		DaleHPAppState daleHPAppState = app.getStateManager()
										   .getState(DaleHPAppState.class);
		if (!daleHPAppState.isAlive()) {
			handleDeadDale(dale);
			return;
		}
		modifiableWalkDirectionVector.set(0, 0, 0);
		Vector3f camDir = app.getCamera()
							 .getDirection()
							 .clone()
							 .multLocal(0.5f)
							 .setY(0);
		KeyPressDTO keyPressDTO = gameStateDTO.getKeyPressDTO();
		if (keyPressDTO.isJumpPress()) {
			daleJump(dale);
		}
		else if (dale.getControl(CharacterControl.class)
					 .onGround()) {
			if (keyPressDTO.isMoveForwardPress()) {
				setDaleViewDirectionToCameraDirection(dale);
				modifiableWalkDirectionVector.addLocal(camDir);
			}
			if (keyPressDTO.isMoveBackwardPress()) {
				setDaleViewDirectionToCameraDirection(dale);
				modifiableWalkDirectionVector.addLocal(camDir.negate()
															 .mult(0.5f));
			}
			if (keyPressDTO.isMoveLeftPress()) {
				rotateCharacter(tpf, true, dale);
			}
			if (keyPressDTO.isMoveRightPress()) {
				rotateCharacter(tpf, false, dale);
			}
			setMovementDirection(dale);
		}

	}

	private void setMovementDirection(Spatial dale) {
		dale.getControl(PhysicsControls.DALE)
			.setWalkDirection(modifiableWalkDirectionVector);
	}

	private void rotateCharacter(float tpf, boolean left, Spatial dale) {
		Quaternion quaternion = new Quaternion();
		int multiplier = left ? 1 : -1;
		quaternion.fromAngleAxis(FastMath.DEG_TO_RAD * 90 * multiplier * tpf,
				Vector3f.UNIT_Y);
		dale.getControl(PhysicsControls.DALE)
			.setViewDirection(quaternion.mult(
					dale.getControl(PhysicsControls.DALE)
						.getViewDirection()));
	}

	private void setDaleViewDirectionToCameraDirection(Spatial dale) {
		Vector3f direction = gameApplication.getCamera()
											.getDirection();
		dale.getControl(PhysicsControls.DALE)
			.setViewDirection(
					new Vector3f(direction.getX(), 0, direction.getZ()));
	}

	public void daleJump(Spatial dale) {
		if (dale.getControl(PhysicsControls.DALE)
				.onGround()) {
			dale.getControl(PhysicsControls.DALE)
				.jump(new Vector3f(0, 30f, 0));
		}
	}

	private void handleDeadDale(Spatial dale) {
		CharacterControl control = dale.getControl(PhysicsControls.DALE);
		control.setWalkDirection(Vector3f.ZERO);
		modifiableWalkDirectionVector.set(0, 0, 0);
	}

}
