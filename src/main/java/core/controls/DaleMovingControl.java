package core.controls;

import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import constants.PhysicsControls;
import core.GameApplication;
import dto.GameStateDTO;
import dto.KeyPressDTO;
import dto.NodeNamesDTO;

public class DaleMovingControl extends AbstractControl {

	public static final float START_OF_VIEW = 10f;
	private GameStateDTO gameStateDTO;
	private Vector3f modifiableWalkDirectionVector = new Vector3f(0, 0, 0);
	private GameApplication gameApplication;
	private NodeNamesDTO nodeNamesDTO;

	public DaleMovingControl(GameStateDTO gameStateDTO,
			NodeNamesDTO nodeNamesDTO) {
		this.gameStateDTO = gameStateDTO;
		gameApplication = GameApplication.getInstance();
		this.nodeNamesDTO = nodeNamesDTO;
	}

	@Override
	protected void controlUpdate(float tpf) {
		if (!gameStateDTO.getDaleStateDTO()
						 .isAlive()) {
			handleDeadDale();
			return;

		}
		modifiableWalkDirectionVector.set(0, 0, 0);
		Vector3f camDir = gameApplication.getCamera()
										 .getDirection()
										 .clone()
										 .multLocal(0.5f)
										 .setY(0);
		KeyPressDTO keyPressDTO = gameStateDTO.getKeyPressDTO();
		if (keyPressDTO.isJumpPress()) {
			daleJump();
		}
		else if (spatial.getControl(CharacterControl.class)
						.onGround()) {
			if (keyPressDTO.isMoveForwardPress()) {
				setDaleViewDirectionToCameraDirection();
				modifiableWalkDirectionVector.addLocal(camDir);
			}
			if (keyPressDTO.isMoveBackwardPress()) {
				setDaleViewDirectionToCameraDirection();
				modifiableWalkDirectionVector.addLocal(camDir.negate()
															 .mult(0.5f));
			}
			if (keyPressDTO.isMoveLeftPress()) {
				rotateCharacter(tpf, true);
			}
			if (keyPressDTO.isMoveRightPress()) {
				rotateCharacter(tpf, false);
			}
			setMovementDirection();
			moveFieldOfView();
		}

	}

	private void moveFieldOfView() {
		CharacterControl control = spatial.getControl(PhysicsControls.DALE);
		Spatial fieldOfView = GameApplication.getInstance()
											 .getRootNode()
											 .getChild(
													 nodeNamesDTO.getFieldOfViewNodeName());
		float fieldOfViewRadius = ((SphereCollisionShape) fieldOfView.getControl(
				GhostControl.class)
																	 .getCollisionShape()).getRadius();
		fieldOfView.setLocalTranslation(spatial.getWorldTranslation()
											   .add(control.getViewDirection()
														   .mult(fieldOfViewRadius
																   + START_OF_VIEW)));
	}

	private void setMovementDirection() {
		spatial.getControl(PhysicsControls.DALE)
			   .setWalkDirection(modifiableWalkDirectionVector);
	}

	private void rotateCharacter(float tpf, boolean left) {
		Quaternion quaternion = new Quaternion();
		int multiplier = left ? 1 : -1;
		quaternion.fromAngleAxis(FastMath.DEG_TO_RAD * 90 * multiplier * tpf,
				Vector3f.UNIT_Y);
		spatial.getControl(PhysicsControls.DALE)
			   .setViewDirection(quaternion.mult(
					   spatial.getControl(PhysicsControls.DALE)
							  .getViewDirection()));
	}

	private void setDaleViewDirectionToCameraDirection() {
		Vector3f direction = gameApplication.getCamera()
											.getDirection();
		spatial.getControl(PhysicsControls.DALE)
			   .setViewDirection(
					   new Vector3f(direction.getX(), 0, direction.getZ()));
	}

	public void daleJump() {
		if (spatial.getControl(PhysicsControls.DALE)
				   .onGround()) {
			spatial.getControl(PhysicsControls.DALE)
				   .jump(new Vector3f(0, 30f, 0));
		}
	}

	private void handleDeadDale() {
		CharacterControl control = spatial.getControl(PhysicsControls.DALE);
		control.setWalkDirection(Vector3f.ZERO);
		modifiableWalkDirectionVector.set(0, 0, 0);
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}
}
