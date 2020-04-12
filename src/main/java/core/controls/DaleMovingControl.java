package core.controls;

import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;
import constants.PhysicsControls;
import core.GameApplication;
import dto.DaleStateDTO;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

public class DaleMovingControl extends AbstractControl {

	public static final float START_OF_VIEW = 10f;
	private GameStateDTO gameStateDTO;
	private Vector3f modifiableWalkDirectionVector = new Vector3f(0, 0, 0);
	private GameApplication gameApplication;
	private ObjectsHolderDTO objectsHolderDTO;

	public DaleMovingControl(GameStateDTO gameStateDTO,
			ObjectsHolderDTO objectsHolderDTO) {
		this.gameStateDTO = gameStateDTO;
		gameApplication = GameApplication.getInstance();
		this.objectsHolderDTO = objectsHolderDTO;
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
		}
		if (daleStateDTO.isMovingBackward()) {
			setDaleViewDirectionToCameraDirection();
			modifiableWalkDirectionVector.addLocal(camDir.negate()
														 .mult(0.5f));
		}
		if (daleStateDTO.isMovingLeft()) {
			rotateCharacter(tpf, true);
		}
		if (daleStateDTO.isMovingRight()) {
			rotateCharacter(tpf, false);
		}
		setMovementDirection();
		CharacterControl control = spatial.getControl(PhysicsControls.DALE);
		Geometry fieldOfView = objectsHolderDTO.getFieldOfView();
		float fieldOfViewRadius = ((SphereCollisionShape) fieldOfView.getControl(
				GhostControl.class)
																	 .getCollisionShape()).getRadius();
		objectsHolderDTO.getFieldOfView()
						.getControl(GhostControl.class)
						.setPhysicsLocation(spatial.getLocalTranslation()
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
