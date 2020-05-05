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
import enums.State;

import java.util.ArrayList;
import java.util.List;

public class DaleClimbingControl extends AbstractControl {

	public static final float MIN_DISTANCE = 1.5f;
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
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		checkCorrectStates(daleStateDTO);
		if (!spatial.getControl(PhysicsControls.DALE)
					.onGround()) {
			if (!daleStateDTO.isLetGoLedge()
							 .inProgress() && !daleStateDTO.isMoveInLedge()
														   .inProgress()) {
				handleGrabbing();
			}
			handleLetGoLedge();
			handleMoveInLedge();
		}
		else {
			daleStateDTO.setMoveInLedge(State.NOT_RUNNING);
			daleStateDTO.setLetGoLedge(State.NOT_RUNNING);
			daleStateDTO.setGrabbingLedge(State.NOT_RUNNING);
		}

	}

	private void checkCorrectStates(DaleStateDTO daleStateDTO) {
		if (daleStateDTO.isMoveInLedge()
						.requested()) {

			daleStateDTO.setMoveInLedge(daleStateDTO.isGrabbingLedge()
													.inProgress() ?
					State.IN_PROGRESS :
					State.NOT_RUNNING);
		}
		if (daleStateDTO.isLetGoLedge()
						.requested()) {
			daleStateDTO.setLetGoLedge(daleStateDTO.isGrabbingLedge()
												   .inProgress() ?
					State.IN_PROGRESS :
					State.NOT_RUNNING);
		}
	}

	private void handleLetGoLedge() {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		if (daleStateDTO.isLetGoLedge()
						.inProgress()) {
			CharacterControl control = spatial.getControl(PhysicsControls.DALE);
			control.setEnabled(true);
			daleStateDTO.setGrabbingLedge(State.NOT_RUNNING);

		}
	}

	private void handleGrabbing() {
		CharacterControl control = spatial.getControl(CharacterControl.class);
		Vector3f spatialExtent = ((BoundingBox) spatial.getWorldBound()).getExtent(
				new Vector3f());
		CollisionResult closestCollision = getClosestObjectFromCharacterHeadForward(
				control, spatialExtent);
		boolean isVeryCloseToObstacle = isVeryCloseToObstacle(control,
				spatialExtent, closestCollision);

		if (isVeryCloseToObstacle) {
			boolean enoughSpaceToWalkOnIt = isEnoughSpaceToWalkOnLedge(
					closestCollision.getContactPoint(),
					control.getViewDirection(), spatialExtent);
			if (enoughSpaceToWalkOnIt) {
				control.setEnabled(false);
				gameStateDTO.getDaleStateDTO()
							.setLedgeCollisionPoint(
									closestCollision.getContactPoint());
				gameStateDTO.getDaleStateDTO()
							.setGrabbingLedge(State.IN_PROGRESS);

			}
		}
	}

	private void handleMoveInLedge() {
		if (gameStateDTO.getDaleStateDTO()
						.isMoveInLedge()
						.inProgress()) {
			gameStateDTO.getDaleStateDTO()
						.setGrabbingLedge(State.NOT_RUNNING);
			CharacterControl control = spatial.getControl(
					CharacterControl.class);
			Vector3f destinationPoint = calculateDestinationPointForMovingInLedge(
					control);
			if (isMovingInLedgeCompleted(destinationPoint)) {
				enablePhysicsAfterReachingLedge(control);
			}
			else {
				moveIntoLedge(control, spatial.getLocalTranslation(),
						destinationPoint);
			}
		}
	}

	private void enablePhysicsAfterReachingLedge(CharacterControl control) {
		gameStateDTO.getDaleStateDTO()
					.setMoveInLedge(State.NOT_RUNNING);
		control.setEnabled(true);
		control.setPhysicsLocation(spatial.getWorldTranslation());
	}

	private void moveIntoLedge(CharacterControl control,
			Vector3f localTranslation, Vector3f destinationPoint) {
		float distanceToDestinationPointDirectionY = destinationPoint.mult(
				Vector3f.UNIT_Y)
																	 .distance(
																			 localTranslation.mult(
																					 Vector3f.UNIT_Y));
		Vector3f moveDirection;
		if (distanceToDestinationPointDirectionY < 0.1f) {
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

	private Vector3f calculateDestinationPointForMovingInLedge(
			CharacterControl control) {
		Vector3f spatialExtent = ((BoundingBox) spatial.getWorldBound()).getExtent(
				new Vector3f());
		return gameStateDTO.getDaleStateDTO()
						   .getLedgeCollisionPoint()
						   .add(spatialExtent.mult(control.getViewDirection()))
						   .add(spatialExtent.mult(Vector3f.UNIT_Y));
	}

	private boolean isMovingInLedgeCompleted(Vector3f finalPoint) {
		float distanceToFinalPoint = spatial.getLocalTranslation()
											.distance(finalPoint);
		return distanceToFinalPoint < 0.5f;
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
															  .length()
						< MIN_DISTANCE;

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
