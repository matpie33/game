package core.controls;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import constants.PhysicsControls;
import core.GameApplication;
import core.controllers.AnimationController;
import dto.DaleStateDTO;
import dto.GameStateDTO;

public class DaleMovingControl extends AbstractControl {

	private GameStateDTO gameStateDTO;
	private Vector3f modifiableWalkDirectionVector = new Vector3f(0, 0, 0);
	private GameApplication gameApplication;
	private AnimationController animationController;

	public DaleMovingControl(GameStateDTO gameStateDTO,
			AnimationController animationController) {
		this.gameStateDTO = gameStateDTO;
		gameApplication = GameApplication.getInstance();
		this.animationController = animationController;
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
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		if (daleStateDTO.isJumping()) {
			daleJump();
		}
		if (daleStateDTO.isMovingForward()) {
			setDaleViewDirectionToCameraDirection();
			modifiableWalkDirectionVector.addLocal(camDir);
			animationController.animateMovingForward();
		}
		if (daleStateDTO.isMovingBackward()) {
			setDaleViewDirectionToCameraDirection();
			modifiableWalkDirectionVector.addLocal(camDir.negate()
														 .mult(0.5f));
			animationController.animateMovingBackward();
		}
		if (daleStateDTO.isMovingLeft()) {
			rotateCharacter(tpf, true);
		}
		if (daleStateDTO.isMovingRight()) {
			rotateCharacter(tpf, false);
		}
		setMovementDirection();
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
