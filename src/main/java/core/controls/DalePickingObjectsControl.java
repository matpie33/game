package core.controls;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import constants.NodeNames;
import constants.PhysicsControls;
import core.GameApplication;
import core.appState.ThrowableObjectInRangeAppState;
import dto.DaleStateDTO;
import dto.GameStateDTO;
import dto.KeyPressDTO;
import dto.ObjectsHolderDTO;
import enums.ThrowingState;

public class DalePickingObjectsControl extends AbstractControl {

	private GameStateDTO gameStateDTO;
	private GameApplication gameApplication;
	private ObjectsHolderDTO objectsHolderDTO;
	public static final int MINIMAL_DISTANCE_TO_PICK_OBJECT = 5;
	private ThrowableObjectInRangeAppState throwableObjectInRangeAppState;

	public DalePickingObjectsControl(GameStateDTO gameStateDTO,
			ObjectsHolderDTO objectsHolderDTO,
			ThrowableObjectInRangeAppState throwableObjectInRangeAppState) {
		this.gameStateDTO = gameStateDTO;
		this.gameApplication = GameApplication.getInstance();
		this.objectsHolderDTO = objectsHolderDTO;
		this.throwableObjectInRangeAppState = throwableObjectInRangeAppState;
	}

	@Override
	protected void controlUpdate(float tpf) {
		markThrowableObject();
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}

	private void markThrowableObject() {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		KeyPressDTO keyPressDTO = gameStateDTO.getKeyPressDTO();
		if (daleStateDTO.getThrowingState().equals(ThrowingState.PICKING_OBJECT)) {
			return;
		}
		CollisionResults collisionResults = getDistanceToObjects();
		CollisionResult closestCollision = collisionResults.getClosestCollision();

		if (isCloseEnoughToAnyObject(collisionResults)) {
			if (keyPressDTO.isPickObjectPress()
					&& daleStateDTO.getThrowingState()
								   .equals(ThrowingState.NOT_STARTED)) {
				daleStateDTO.setCarriedObject(closestCollision.getGeometry()
															  .getParent());
				throwableObjectInRangeAppState.setEnabled(false);
				daleStateDTO.setThrowingState(ThrowingState.PICKING_OBJECT);
			}
			else {
				throwableObjectInRangeAppState.setThrowableObject(
						closestCollision.getGeometry());
				throwableObjectInRangeAppState.setEnabled(true);
			}

		}

		if ((collisionResults.size() == 0 || closestCollision.getDistance()
				> MINIMAL_DISTANCE_TO_PICK_OBJECT)) {
			throwableObjectInRangeAppState.setEnabled(false);
		}

	}

	private boolean isCloseEnoughToAnyObject(
			CollisionResults collisionResults) {
		if (collisionResults == null)
			return false;
		CollisionResult closestCollision = collisionResults.getClosestCollision();
		return collisionResults.size() > 0 && closestCollision.getDistance()
				< MINIMAL_DISTANCE_TO_PICK_OBJECT;

	}

	private CollisionResults getDistanceToObjects() {
		Spatial dale = objectsHolderDTO.getDale();
		Ray ray = new Ray(dale.getControl(PhysicsControls.DALE)
							  .getPhysicsLocation(),
				dale.getControl(PhysicsControls.DALE)
					.getViewDirection());
		CollisionResults collisionResults = new CollisionResults();
		Spatial throwables = gameApplication.getRootNode()
											.getChild(NodeNames.THROWABLES);
		for (Spatial spatial : ((Node) throwables).getChildren()) {
			spatial.collideWith(ray, collisionResults);
		}
		return collisionResults;
	}

}
