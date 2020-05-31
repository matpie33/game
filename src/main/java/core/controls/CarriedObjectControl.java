package core.controls;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
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
import dto.KeyPressDTO;
import dto.ObjectsHolderDTO;
import enums.ThrowingState;

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
		handleKeyPress();
		switch (gameStateDTO.getDaleStateDTO()
							.getThrowingState()) {
		case THROWING:
			handleThrowingObject();
			break;
		case PICKING_OBJECT:
			handleBeingCarried();
			break;
		case PUTTING_ASIDE_OBJECT:
			handlePuttingAside();
			break;
		}
	}

	private void handleKeyPress() {
		KeyPressDTO keyPressDTO = gameStateDTO.getKeyPressDTO();
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		if (keyPressDTO.isThrowObjectPress() && daleStateDTO.getThrowingState()
															.equals(ThrowingState.PICKING_OBJECT)) {
			daleStateDTO.setThrowingState(ThrowingState.THROWING);
		}
		else if (keyPressDTO.isPutAsideObjectPress()
				&& daleStateDTO.getThrowingState()
							   .equals(ThrowingState.PICKING_OBJECT)) {
			daleStateDTO.setThrowingState(ThrowingState.PUTTING_ASIDE_OBJECT);
		}

	}

	private void handlePuttingAside() {
		if (gameStateDTO.getDaleStateDTO()
						.getThrowingState()
						.equals(ThrowingState.PUTTING_ASIDE_OBJECT)) {
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
		daleStateDTO.setThrowingState(ThrowingState.NOT_STARTED);
		daleStateDTO.setCarriedObject(null);

	}

	private void handleThrowingObject() {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		if (daleStateDTO.getThrowingState()
						.equals(ThrowingState.THROWING)) {
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
			daleStateDTO.setThrowingState(ThrowingState.NOT_STARTED);
			daleStateDTO.setCarriedObject(null);
		}

	}

	private void handleBeingCarried() {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
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
		carriedObject.setLocalRotation(
				new Quaternion().fromAngleAxis(0, new Vector3f(1, 1, 1)));
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}
}
