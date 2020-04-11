package core.controllers;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.MatParam;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import constants.PhysicsControls;
import core.GameApplication;
import dto.DaleStateDTO;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

public class ThrowingController {

	public static final String DIFFUSE_PARAM = "Diffuse";
	private GameStateDTO gameStateDTO;
	private ObjectsHolderDTO objectsHolderDTO;
	private GameApplication gameApplication;
	private Geometry previouslyMarkedAsThrowingDestinationSpatial;
	private ColorRGBA previousColor;

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
		Ray ray = new Ray(dale.getControl(PhysicsControls.DALE)
							  .getPhysicsLocation(), gameApplication.getCamera()
																	.getDirection()
																	.mult(100f));
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
		changePreviouslyMarkedTargetToItsColor(collisionResults);
		if (collisionResults.size() > 0) {
			Vector3f contactPoint = closestCollision.getContactPoint();

			Node parent = closestCollision.getGeometry()
										  .getParent();
			if (objectsHolderDTO.getDogs()
								.contains(parent)) {
				previouslyMarkedAsThrowingDestinationSpatial = closestCollision.getGeometry();
				MatParam previousColor = previouslyMarkedAsThrowingDestinationSpatial.getMaterial()
																					 .getParam(
																							 "Diffuse");
				this.previousColor = (ColorRGBA) previousColor.getValue();
				closestCollision.getGeometry()
								.getMaterial()
								.setColor(DIFFUSE_PARAM, ColorRGBA.Red);

			}
			objectsHolderDTO.getMark()
							.setLocalTranslation(contactPoint);
		}
	}

	private void changePreviouslyMarkedTargetToItsColor(
			CollisionResults collisionResults) {
		boolean hasPreviouslyMarkedDestination =
				previouslyMarkedAsThrowingDestinationSpatial != null;
		if ((collisionResults.size() == 0 && hasPreviouslyMarkedDestination)
				|| hasPreviouslyMarkedDestination) {
			previouslyMarkedAsThrowingDestinationSpatial.getMaterial()
														.setColor("Diffuse",
																previousColor);
		}
	}

}
