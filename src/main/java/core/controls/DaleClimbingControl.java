package core.controls;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import constants.PhysicsControls;
import core.GameApplication;
import dto.DaleStateDTO;
import dto.GameStateDTO;

import java.util.ArrayList;
import java.util.List;

public class DaleClimbingControl extends AbstractControl {

	private Camera camera;
	private Node rootNode;
	private GameStateDTO gameStateDTO;

	public DaleClimbingControl(GameStateDTO gameStateDTO) {
		GameApplication instance = GameApplication.getInstance();
		camera = instance.getCamera();
		rootNode = instance.getRootNode();
		this.gameStateDTO = gameStateDTO;
	}

	@Override
	protected void controlUpdate(float tpf) {
		handleGrabbing();
		handleLetGoLedge();
		handleMoveInLedge();

	}

	private void handleLetGoLedge() {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		if (spatial.getControl(PhysicsControls.DALE)
				   .onGround()) {
			gameStateDTO.getDaleStateDTO()
						.setLetGoLedge(false);
		}
		if (daleStateDTO.isGrabbingLedge() && daleStateDTO.isLetGoLedge()) {
			CharacterControl control = spatial.getControl(PhysicsControls.DALE);
			control.setEnabled(true);
			daleStateDTO.setGrabbingLedge(false);

		}
	}

	private void handleGrabbing() {
		CharacterControl control = spatial.getControl(CharacterControl.class);
		Vector3f extent = ((BoundingBox) spatial.getWorldBound()).getExtent(
				new Vector3f());
		CollisionResult closestCollision = getClosestObjectFromCharacterHeadForward(
				control, extent);
		boolean isVeryCloseToObstacle = isVeryCloseToObstacle(control, extent,
				closestCollision);

		if (!gameStateDTO.getDaleStateDTO()
						 .isLetGoLedge() && !gameStateDTO.getDaleStateDTO()
														 .isMoveInLedge()
				&& isVeryCloseToObstacle) {
			boolean enoughSpaceToWalkOnIt = isEnoughSpaceToWalkOnLedge(
					closestCollision.getContactPoint(),
					control.getViewDirection(), extent);
			if (enoughSpaceToWalkOnIt) {
				control.setEnabled(false);
				gameStateDTO.getDaleStateDTO()
							.setLedgeCollisionPoint(
									closestCollision.getContactPoint());
				gameStateDTO.getDaleStateDTO()
							.setGrabbingLedge(true);

			}
		}
	}

	private void handleMoveInLedge() {
		if (gameStateDTO.getDaleStateDTO()
						.isMoveInLedge()) {
			gameStateDTO.getDaleStateDTO()
						.setGrabbingLedge(false);
			Vector3f extent = ((BoundingBox) spatial.getWorldBound()).getExtent(
					new Vector3f());
			CharacterControl control = spatial.getControl(
					CharacterControl.class);
			Vector3f moveDirection;
			Vector3f finalPoint = gameStateDTO.getDaleStateDTO()
											  .getLedgeCollisionPoint()
											  .add(extent.mult(
													  control.getViewDirection()))
											  .add(extent.mult(
													  Vector3f.UNIT_Y));
			float distanceToFinalPoint = spatial.getLocalTranslation()
												.distance(finalPoint);
			Vector3f physicsLocation = spatial.getLocalTranslation();
			if (distanceToFinalPoint < 0.5f) {
				gameStateDTO.getDaleStateDTO()
							.setMoveInLedge(false);
				control.setEnabled(true);
				control.setPhysicsLocation(spatial.getWorldTranslation());
				return;
			}
			float distanceToFinalPointDirectionY = finalPoint.mult(
					Vector3f.UNIT_Y)
															 .distance(
																	 physicsLocation.mult(
																			 Vector3f.UNIT_Y));
			if (distanceToFinalPointDirectionY < 0.1f) {
				moveDirection = control.getViewDirection();
			}
			else {
				moveDirection = Vector3f.UNIT_Y;
			}
			moveDirection = moveDirection.clone()
										 .mult(0.01f);
			spatial.setLocalTranslation(spatial.getLocalTranslation()
											   .add(moveDirection));
		}
	}

	private boolean isEnoughSpaceToWalkOnLedge(Vector3f contactPoint,
			Vector3f viewDirection, Vector3f spatialExtent) {
		List<Vector3f> points = getPointsToCheckEnoughHeightToWalk(contactPoint,
				viewDirection, spatialExtent);

		for (Vector3f toCheck : points) {
			if (!isEnoughHeightToStandInPoint(spatialExtent, toCheck)) {
				return false;
			}
		}
		return true;
	}

	private List<Vector3f> getPointsToCheckEnoughHeightToWalk(
			Vector3f contactPoint, Vector3f viewDirection,
			Vector3f extentOfCharacter) {
		Vector3f mainPoint = contactPoint.add(
				extentOfCharacter.mult(viewDirection));
		Vector3f leftFromMainPoint = mainPoint.add(camera.getLeft()
														 .mult(extentOfCharacter));
		Vector3f rightFromMainPoint = mainPoint.add(camera.getLeft()
														  .negate()
														  .mult(extentOfCharacter));
		List<Vector3f> points = new ArrayList<>();
		points.add(mainPoint);
		points.add(leftFromMainPoint);
		points.add(rightFromMainPoint);
		return points;
	}

	private boolean isEnoughHeightToStandInPoint(Vector3f spatialExtent,
			Vector3f pointToCheck) {
		Vector3f sizeOfSpatialDirectionY = Vector3f.UNIT_Y.mult(spatialExtent);
		Vector3f rayStart = pointToCheck.add(sizeOfSpatialDirectionY);
		Ray ray = new Ray(rayStart, Vector3f.UNIT_Y.negate());
		CollisionResults collisionResults = new CollisionResults();
		rootNode.collideWith(ray, collisionResults);
		CollisionResult closestCollision = collisionResults.getClosestCollision();
		return closestCollision.getDistance() - sizeOfSpatialDirectionY.length()
				< 0.5f;
	}

	private boolean isVeryCloseToObstacle(CharacterControl control,
			Vector3f spatialExtent, CollisionResult closestCollision) {
		return closestCollision != null &&
				closestCollision.getDistance() - spatialExtent.mult(
						control.getViewDirection())
															  .length() < 0.5f
				&& !control.onGround();
	}

	private CollisionResult getClosestObjectFromCharacterHeadForward(
			CharacterControl control, Vector3f extent) {
		Ray ray = new Ray(spatial.getWorldTranslation()
								 .add(Vector3f.UNIT_Y.mult(extent)),
				control.getViewDirection());
		CollisionResults collisionResults = new CollisionResults();
		rootNode.collideWith(ray, collisionResults);
		return collisionResults.getClosestCollision();
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}
}
