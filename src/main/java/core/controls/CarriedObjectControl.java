package core.controls;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import constants.PhysicsControls;
import core.GameApplication;
import core.controllers.AnimationController;
import dto.DaleStateDTO;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

public class CarriedObjectControl extends AbstractControl {

	private GameStateDTO gameStateDTO;
	private ObjectsHolderDTO objectsHolderDTO;
	private GameApplication gameApplication;
	private AnimationController animationController;

	public CarriedObjectControl(GameStateDTO gameStateDTO,
			ObjectsHolderDTO objectsHolderDTO,
			AnimationController animationController) {
		this.gameStateDTO = gameStateDTO;
		this.objectsHolderDTO = objectsHolderDTO;
		gameApplication = GameApplication.getInstance();
		this.animationController = animationController;
	}

	@Override
	protected void controlUpdate(float tpf) {
		handleBeingCarried();
		handlePuttingAside();
		handleThrowingObject();
	}

	private void handlePuttingAside() {
		if (gameStateDTO.getDaleStateDTO()
						.isPuttingAsideObject()) {
			putAsideObject();
		}
	}

	private void putAsideObject() {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		daleStateDTO.setCarryingThrowableObject(false);
		RigidBodyControl control = daleStateDTO.getCarriedObject()
											   .getObject()
											   .getControl(PhysicsControls.BOX);
		control.setKinematicSpatial(false);
		control.setKinematic(false);
		control.applyCentralForce(gameApplication.getCamera()
												 .getDirection()
												 .mult(-0.5f));

		animationController.animateStanding();
	}

	private void handleThrowingObject() {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		if (daleStateDTO.isThrowingObject()) {
			daleStateDTO.setCarryingThrowableObject(false);
			Object control = daleStateDTO.getCarriedObject()
										 .getObject()
										 .getControl(PhysicsControls.BOX);
			PhysicsControls.BOX.cast(control)
							   .setKinematic(false);
			PhysicsControls.BOX.cast(control)
							   .setKinematicSpatial(false);
			PhysicsControls.BOX.cast(control)
							   .setLinearVelocity(new Vector3f(
									   gameApplication.getCamera()
													  .getDirection()
													  .mult(80f)));
			animationController.animateStanding();
		}

	}

	public void handleBeingCarried() {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		if (!daleStateDTO.isCarryingThrowableObject()) {
			return;
		}
		Spatial carriedObject = daleStateDTO.getCarriedObject()
											.getObject();
		Vector3f dalePosition = objectsHolderDTO.getDale()
												.getLocalTranslation();
		float daleHeight = ((BoundingBox) objectsHolderDTO.getDale()
														  .getWorldBound()).getYExtent();
		float boxHeight = ((BoundingBox) carriedObject.getWorldBound()).getYExtent();
		RigidBodyControl boxControl = carriedObject.getControl(
				PhysicsControls.BOX);
		boxControl.setKinematic(true);
		boxControl.setKinematicSpatial(true);
		carriedObject.setLocalTranslation(new Vector3f(dalePosition.getX(),
				dalePosition.getY() + daleHeight + boxHeight + 1f,
				dalePosition.getZ()));
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}
}
