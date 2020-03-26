package core;

import com.jme3.bounding.BoundingBox;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class ThrowingHandler {

	public void markThrowingDestination(Camera camera, Node rootNode,
			ModelLoader modelLoader, DaleState daleState) {
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

	public void markThrowableObject(Camera camera, Node rootNode,
			ModelLoader modelLoader, DaleState daleState) {
		if (daleState.isCarryingThrowableObject()) {
			return;
		}
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
			return;
		}
		CollisionResult closestCollision = collisionResults.getClosestCollision();
		if (collisionResults.size() > 0 && closestCollision.getDistance() < 5) {
			Spatial arrow = modelLoader.getArrow();
			closestCollision.getGeometry()
							.getWorldBound();
			float yDimensionArrow = ((BoundingBox) arrow.getWorldBound()).getYExtent();
			float yDimensionCollisionObject = ((BoundingBox) closestCollision.getGeometry()
																			 .getWorldBound()).getYExtent();
			float xDimensionCollisionObject = ((BoundingBox) closestCollision.getGeometry()
																			 .getWorldBound()).getXExtent();
			float zDimensionCollisionObject = ((BoundingBox) closestCollision.getGeometry()
																			 .getWorldBound()).getZExtent();
			Vector3f objectPos = closestCollision.getGeometry()
												 .getWorldTranslation();
			arrow.setLocalTranslation(
					objectPos.getX() + xDimensionCollisionObject,
					objectPos.getY() + yDimensionArrow
							+ yDimensionCollisionObject,
					objectPos.getZ() - zDimensionCollisionObject);
			rootNode.attachChild(arrow);
		}
	}

}
