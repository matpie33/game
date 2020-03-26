package core;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class ThrowingHandler {

	public void markThrowingDestination(Camera camera, Node rootNode,
			ModelLoader modelLoader, DaleState daleState){
		if (!daleState.isCarryingThrowableObject()){
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
		if (collisionResults.size()>0){
			Vector3f contactPoint = closestCollision.getContactPoint();
			modelLoader.getMark().setLocalTranslation(contactPoint);
		}
	}

}
