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
import core.util.CoordinatesUtil;
import dto.DaleStateDTO;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

public class CarriedObjectControl extends AbstractControl {

	private GameStateDTO gameStateDTO;
	private ObjectsHolderDTO objectsHolderDTO;
	private GameApplication gameApplication;

	public CarriedObjectControl(GameStateDTO gameStateDTO,
			ObjectsHolderDTO objectsHolderDTO) {
		this.gameStateDTO = gameStateDTO;
		this.objectsHolderDTO = objectsHolderDTO;
		gameApplication = GameApplication.getInstance();
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
											   .getCarriedObject()
											   .getControl(PhysicsControls.BOX);
		control.setKinematicSpatial(false);
		control.setKinematic(false);
		control.applyCentralForce(gameApplication.getCamera()
												 .getDirection()
												 .mult(-0.5f));

	}

	private void handleThrowingObject() {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		if (daleStateDTO.isThrowingObject()) {
			daleStateDTO.setCarryingThrowableObject(false);
			Object control = daleStateDTO.getCarriedObject()
										 .getCarriedObject()
										 .getControl(PhysicsControls.BOX);
			PhysicsControls.BOX.cast(control)
							   .setKinematic(false);
			PhysicsControls.BOX.cast(control)
							   .setKinematicSpatial(false);
			Vector3f force = gameApplication.getCamera()
											.getDirection()
											.mult(150f);
			force.setY(force.getY() + 30f);
			PhysicsControls.BOX.cast(control)
							   .applyForce(force, new Vector3f(0, 0.2f, 0));
		}

	}

	public void handleBeingCarried() {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		if (!daleStateDTO.isCarryingThrowableObject()) {
			return;
		}
		Spatial carriedObject = daleStateDTO.getCarriedObject()
											.getCarriedObject();
		Vector3f dalePosition = objectsHolderDTO.getDale()
												.getLocalTranslation();
		BoundingBox daleSize = CoordinatesUtil.getSizeOfSpatial(
				objectsHolderDTO.getDale());
		BoundingBox carriedObjectSize = CoordinatesUtil.getSizeOfSpatial(
				carriedObject);
		float daleHeight = daleSize.getYExtent();
		float boxHeight = carriedObjectSize.getYExtent();
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