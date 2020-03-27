package core;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class ThrowingHandler {

	private Camera camera;
	private Node rootNode;
	private ModelLoader modelLoader;
	private DaleState daleState;
	private GameState gameState;

	public ThrowingHandler(Camera camera, Node rootNode,
			ModelLoader modelLoader, DaleState daleState, GameState gameState) {
		this.camera = camera;
		this.rootNode = rootNode;
		this.modelLoader = modelLoader;
		this.daleState = daleState;
		this.gameState = gameState;
	}

	public void markThrowingDestination() {
		if (!daleState.isCarryingThrowableObject()) {
			return;
		}
		Ray ray = new Ray(camera.getLocation(), camera.getDirection());
		CollisionResults collisionResults = new CollisionResults();
		for (Spatial spatial : rootNode.getChildren()) {
			if (spatial.equals(modelLoader.getDale())) {
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
				|| closestCollision.getDistance() > 5)) {

			hideCursor();
		}

	}

	private boolean isCloseEnoughToAnyObject(
			CollisionResults collisionResults) {
		if (collisionResults == null)
			return false;
		CollisionResult closestCollision = collisionResults.getClosestCollision();
		return collisionResults.size() > 0
				&& closestCollision.getDistance() < 5;

	}

	private CollisionResults getDistanceToObjects() {
		Ray ray = new Ray(daleState.getCharacterControl()
								   .getPhysicsLocation(),
				daleState.getCharacterControl()
						 .getViewDirection());
		CollisionResults collisionResults = new CollisionResults();
		for (Spatial spatial : rootNode.getChildren()) {
			if (spatial.equals(modelLoader.getDale()) || spatial.equals(
					modelLoader.getScene())) {
				continue;
			}
			spatial.collideWith(ray, collisionResults);
		}
		if (collisionResults.size() == 0) {
			return null;
		}
		return collisionResults;
	}

	public void hideCursor() {
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
}
