package core.controllers;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import constants.NodeNames;
import constants.PhysicsControls;
import core.GameApplication;
import dto.DaleStateDTO;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

public class ThrowingController {

	public static final int MINIMAL_DISTANCE_TO_PICK_OBJECT = 5;
	private DaleStateDTO daleStateDTO;
	private GameStateDTO gameStateDTO;
	private AnimationController animationController;
	private ObjectsHolderDTO objectsHolderDTO;
	private GameApplication gameApplication;

	public ThrowingController(ObjectsHolderDTO objectsHolderDTO,
			DaleStateDTO daleStateDTO, GameStateDTO gameStateDTO,
			AnimationController animationController) {
		this.objectsHolderDTO = objectsHolderDTO;
		this.daleStateDTO = daleStateDTO;
		this.gameStateDTO = gameStateDTO;
		this.animationController = animationController;
		gameApplication = GameApplication.getInstance();
	}

	public void markThrowingDestination() {
		if (!daleStateDTO.isCarryingThrowableObject()) {
			return;
		}
		Spatial dale = objectsHolderDTO.getDale();
		Vector3f viewDir = dale.getControl(PhysicsControls.DALE)
							   .getViewDirection()
							   .mult(100);
		Ray ray = new Ray(dale.getControl(PhysicsControls.DALE)
							  .getPhysicsLocation(), viewDir);
		CollisionResults collisionResults = new CollisionResults();
		for (Spatial spatial : gameApplication.getRootNode()
											  .getChildren()) {
			if (spatial.equals(dale) || daleStateDTO.getCarriedObject()
													.getObject()
													.getParent() == spatial) {
				continue;
			}
			spatial.collideWith(ray, collisionResults);
		}
		CollisionResult closestCollision = collisionResults.getClosestCollision();
		if (collisionResults.size() > 0) {
			Vector3f contactPoint = closestCollision.getContactPoint();
			objectsHolderDTO.getMark()
							.setLocalTranslation(contactPoint);
		}
	}

	public void markThrowableObject() {
		if (daleStateDTO.isCarryingThrowableObject()) {
			return;
		}
		CollisionResults collisionResults = getDistanceToObjects();
		if (collisionResults == null) {
			return;
		}
		CollisionResult closestCollision = collisionResults.getClosestCollision();
		if (!gameStateDTO.isCursorShowing()
				&& !daleStateDTO.isCarryingThrowableObject()
				&& isCloseEnoughToAnyObject(collisionResults)) {

			calculateArrowPosition(closestCollision);
			gameApplication.getRootNode()
						   .attachChild(objectsHolderDTO.getArrow());
			gameStateDTO.setCursorShowingAt(closestCollision.getGeometry());
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
		gameApplication.getRootNode().detachChild(objectsHolderDTO.getArrow());
	}

	private void calculateArrowPosition(CollisionResult closestCollision) {
		Spatial arrow = objectsHolderDTO.getArrow();
		BoundingVolume collisionObjectBounds = closestCollision.getGeometry()
															   .getWorldBound();
		float yDimensionArrow = ((BoundingBox) arrow.getWorldBound()).getYExtent();
		float yDimensionCollisionObject = ((BoundingBox) collisionObjectBounds).getYExtent();
		float xDimensionCollisionObject = ((BoundingBox) collisionObjectBounds).getXExtent();
		float zDimensionCollisionObject = ((BoundingBox) collisionObjectBounds).getZExtent();
		Vector3f objectPos = closestCollision.getGeometry()
											 .getWorldTranslation();
		arrow.setLocalTranslation(objectPos.getX() + xDimensionCollisionObject,
				objectPos.getY() + yDimensionArrow + yDimensionCollisionObject,
				objectPos.getZ() - zDimensionCollisionObject);
	}

	public boolean isCloseToThrowableObject() {

		CollisionResults collisionResults = getDistanceToObjects();
		return isCloseEnoughToAnyObject(collisionResults);
	}

	public void tryToPickObject() {
		if (daleStateDTO.isCarryingThrowableObject()) {
			putAsideObject();
		}
		else {
			pickupObject();
		}
	}

	private void pickupObject() {
		CollisionResults collisionResults = getDistanceToObjects();
		if (isCloseToThrowableObject()) {
			Geometry geometry = collisionResults.getClosestCollision()
												.getGeometry();
			Object control = geometry.getParent()
									 .getControl(

											 PhysicsControls.BOX);

			PhysicsControls.BOX.cast(control)
							   .clearForces();
			PhysicsControls.BOX.cast(control)
							   .applyCentralForce(new Vector3f(0, 9f, 0));
			daleStateDTO.setCarryingThrowableObject(true);
			daleStateDTO.setCarriedObject(geometry);
			hideCursor();
			animationController.animateHoldingObject();
		}
	}

	private void putAsideObject() {
		daleStateDTO.setCarryingThrowableObject(false);
		daleStateDTO.getCarriedObject()
					.getObject()
					.getParent()
					.getControl(PhysicsControls.BOX)
					.applyCentralForce(gameApplication.getCamera()
													  .getDirection()
													  .mult(3f));
		animationController.animateStanding();
	}

	public void tryToThrowObject() {
		if (daleStateDTO.isCarryingThrowableObject()) {
			daleStateDTO.setCarryingThrowableObject(false);
			Object control = daleStateDTO.getCarriedObject()
										 .getObject()
										 .getParent()
										 .getControl(PhysicsControls.BOX);
			PhysicsControls.BOX.cast(control)
							   .setGravity(new Vector3f(0, -40f, 0));
			PhysicsControls.BOX.cast(control)
							   .setLinearVelocity(new Vector3f(
									   gameApplication.getCamera()
													  .getDirection()
													  .mult(80f)));
			animationController.animateStanding();
		}

	}
}
