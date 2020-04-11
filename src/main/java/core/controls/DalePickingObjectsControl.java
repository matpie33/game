package core.controls;

import com.jme3.bounding.BoundingBox;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import constants.NodeNames;
import constants.PhysicsControls;
import core.GameApplication;
import core.controllers.AnimationController;
import core.util.CoordinatesUtil;
import dto.DaleStateDTO;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

public class DalePickingObjectsControl extends AbstractControl {

	private GameStateDTO gameStateDTO;
	private GameApplication gameApplication;
	private ObjectsHolderDTO objectsHolderDTO;
	private AnimationController animationController;
	public static final int MINIMAL_DISTANCE_TO_PICK_OBJECT = 5;

	public DalePickingObjectsControl(GameStateDTO gameStateDTO,
			ObjectsHolderDTO objectsHolderDTO,
			AnimationController animationController) {
		this.gameStateDTO = gameStateDTO;
		this.gameApplication = GameApplication.getInstance();
		this.objectsHolderDTO = objectsHolderDTO;
		this.animationController = animationController;
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
		if (daleStateDTO.isCarryingThrowableObject()) {
			return;
		}
		CollisionResults collisionResults = getDistanceToObjects();
		if (collisionResults == null) {
			return;
		}
		CollisionResult closestCollision = collisionResults.getClosestCollision();

		if (isCloseEnoughToAnyObject(collisionResults)) {
			if (!gameStateDTO.isCursorShowing()
					&& !daleStateDTO.isCarryingThrowableObject()) {
				calculateArrowPosition(closestCollision);
				gameApplication.getRootNode()
							   .attachChild(objectsHolderDTO.getArrow());
				gameStateDTO.setCursorShowingAt(closestCollision.getGeometry());
			}
			if (daleStateDTO.isPickingObject()) {
				daleStateDTO.setCarryingThrowableObject(true);
				daleStateDTO.setCarriedObject(closestCollision.getGeometry()
															  .getParent());
				hideCursor();
				animationController.animateHoldingObject();
			}

		}

		if (gameStateDTO.isCursorShowing() && (closestCollision.getGeometry()
				!= gameStateDTO.getSpatialOnWhichCursorIsShowing()
				|| closestCollision.getDistance()
				> MINIMAL_DISTANCE_TO_PICK_OBJECT)) {

			hideCursor();
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
		if (collisionResults.size() == 0) {
			return null;
		}
		return collisionResults;
	}

	private void hideCursor() {
		gameStateDTO.setCursorNotShowing();
		gameApplication.getRootNode()
					   .detachChild(objectsHolderDTO.getArrow());
	}

	private void calculateArrowPosition(CollisionResult closestCollision) {
		Spatial arrow = objectsHolderDTO.getArrow();
		BoundingBox closestObjectSize = CoordinatesUtil.getSizeOfSpatial(
				closestCollision.getGeometry());
		float yDimensionArrow = CoordinatesUtil.getSizeOfSpatial(arrow)
											   .getYExtent();
		float xDimensionCollisionObject = closestObjectSize.getXExtent();
		float yDimensionCollisionObject = closestObjectSize.getYExtent();
		float zDimensionCollisionObject = closestObjectSize.getZExtent();
		Vector3f objectPosition = closestCollision.getGeometry()
												  .getWorldTranslation();
		arrow.setLocalTranslation(
				objectPosition.getX() + xDimensionCollisionObject,
				objectPosition.getY() + yDimensionArrow
						+ yDimensionCollisionObject,
				objectPosition.getZ() - zDimensionCollisionObject);
	}

}
