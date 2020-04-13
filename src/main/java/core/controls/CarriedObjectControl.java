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
		RigidBodyControl control = daleStateDTO.getCarriedObject()
											   .getCarriedObject()
											   .getControl(PhysicsControls.BOX);
		control.setKinematicSpatial(false);
		control.setKinematic(false);
		control.applyImpulse(gameApplication.getCamera()
											.getDirection()
											.mult(-20), Vector3f.ZERO);
		daleStateDTO.setCarryingThrowableObject(false);
		daleStateDTO.setPuttingAsideObject(false);
		daleStateDTO.setCarriedObject(null);

	}

	private void handleThrowingObject() {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		if (daleStateDTO.isThrowingObject()) {
			Object control = daleStateDTO.getCarriedObject()
										 .getCarriedObject()
										 .getControl(PhysicsControls.BOX);
			PhysicsControls.BOX.cast(control)
							   .setKinematic(false);
			PhysicsControls.BOX.cast(control)
							   .setKinematicSpatial(false);
			Vector3f force = gameApplication.getCamera()
											.getDirection()
											.mult(250f);
			force.setY(force.getY() + 60f);
			PhysicsControls.BOX.cast(control)
							   .applyImpulse(force, new Vector3f(0, 0.5f, 0));
			daleStateDTO.setCarryingThrowableObject(false);
			daleStateDTO.setCarriedObject(null);
			daleStateDTO.setThrowingObject(false);
		}

	}

	private void handleBeingCarried() {
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
