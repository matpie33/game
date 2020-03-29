package core;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import constants.NodeNames;
import constants.PhysicsControls;

public class ThrowingHandler {

	public static final int MINIMAL_DISTANCE_TO_PICK_OBJECT = 5;
	private Camera camera;
	private Node rootNode;
	private ModelLoader modelLoader;
	private DaleState daleState;
	private GameState gameState;
	private AnimationController animationController;

	public ThrowingHandler(Camera camera, Node rootNode,
			ModelLoader modelLoader, DaleState daleState, GameState
			gameState, AnimationController animationController) {
		this.camera = camera;
		this.rootNode = rootNode;
		this.modelLoader = modelLoader;
		this.daleState = daleState;
		this.gameState = gameState;
		this.animationController = animationController;
	}

	public void markThrowingDestination() {
		if (!daleState.isCarryingThrowableObject()) {
			return;
		}
		Spatial dale = modelLoader.getDale();
		Vector3f viewDir = dale.getControl(PhysicsControls.DALE)
							   .getViewDirection()
							   .mult(100);
		Ray ray = new Ray(dale.getControl(PhysicsControls.DALE)
							  .getPhysicsLocation(), viewDir);
		CollisionResults collisionResults = new CollisionResults();
		for (Spatial spatial : rootNode.getChildren()) {
			if (spatial.equals(dale) || daleState.getCarriedObject()
												 .getObject()
												 .getParent() == spatial) {
				continue;
			}
			spatial.collideWith(ray, collisionResults);
		}
		CollisionResult closestCollision = collisionResults.getClosestCollision();
		if (collisionResults.size() > 0) {
			Vector3f contactPoint = closestCollision.getContactPoint();
			modelLoader.getMark()
					   .setLocalTranslation(contactPoint);
		}
	}

	public void markThrowableObject() {
		if (daleState.isCarryingThrowableObject()) {
			return;
		}
		CollisionResults collisionResults = getDistanceToObjects();
		if (collisionResults == null) {
			return;
		}
		CollisionResult closestCollision = collisionResults.getClosestCollision();
		if (!gameState.isCursorShowing()
				&& !daleState.isCarryingThrowableObject()
				&& isCloseEnoughToAnyObject(collisionResults)) {

			calculateArrowPosition(closestCollision);
			rootNode.attachChild(modelLoader.getArrow());
			gameState.setCursorShowingAt(closestCollision.getGeometry());
		}

		if (gameState.isCursorShowing() && (closestCollision.getGeometry()
				!= gameState.getSpatialOnWhichCursorIsShowing()
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
		Spatial dale = modelLoader.getDale();
		Ray ray = new Ray(dale.getControl(PhysicsControls.DALE)
							  .getPhysicsLocation(),
				dale.getControl(PhysicsControls.DALE)
					.getViewDirection());
		CollisionResults collisionResults = new CollisionResults();
		Spatial throwables = rootNode.getChild(NodeNames.THROWABLES);
		for (Spatial spatial : ((Node) throwables).getChildren()) {
			spatial.collideWith(ray, collisionResults);
		}
		if (collisionResults.size() == 0) {
			return null;
		}
		return collisionResults;
	}

	private void hideCursor() {
		gameState.setCursorNotShowing();
		rootNode.detachChild(modelLoader.getArrow());
	}

	private void calculateArrowPosition(CollisionResult closestCollision) {
		Spatial arrow = modelLoader.getArrow();
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
		if (daleState.isCarryingThrowableObject()) {
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
			daleState.setCarryingThrowableObject(true);
			daleState.setCarriedObject(geometry);
			hideCursor();
			animationController.animateHoldingObject();
		}
	}

	private void putAsideObject() {
		daleState.setCarryingThrowableObject(false);
		daleState.getCarriedObject()
				 .getObject()
				 .getParent()
				 .getControl(PhysicsControls.BOX)
				 .applyCentralForce(camera.getDirection()
										  .mult(3f));
		animationController.animateStanding();
	}

	public void tryToThrowObject() {
		if (daleState.isCarryingThrowableObject()) {
			daleState.setCarryingThrowableObject(false);
			Object control = daleState.getCarriedObject()
									  .getObject()
									  .getParent()
									  .getControl(PhysicsControls.BOX);
			PhysicsControls.BOX.cast(control)
							   .setGravity(new Vector3f(0, -10f, 0));
			PhysicsControls.BOX.cast(control)
							   .setLinearVelocity(new Vector3f(
									   camera.getDirection()
											 .mult(80f)));
			animationController.animateStanding();
		}

	}
}
