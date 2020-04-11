package core.controllers;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import constants.NodeNames;
import constants.PhysicsControls;
import core.GameApplication;
import dto.DaleStateDTO;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

public class ThrowingController {


	private GameStateDTO gameStateDTO;
	private ObjectsHolderDTO objectsHolderDTO;
	private GameApplication gameApplication;

	public ThrowingController(ObjectsHolderDTO objectsHolderDTO,
			GameStateDTO gameStateDTO) {
		this.objectsHolderDTO = objectsHolderDTO;
		this.gameStateDTO = gameStateDTO;
		gameApplication = GameApplication.getInstance();
	}

	public void handleThrowingAndPicking() {
		markThrowingDestination();
	}

	private void markThrowingDestination() {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
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







}
