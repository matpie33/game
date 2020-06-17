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
import dto.KeyPressDTO;
import enums.ClimbingState;

import java.util.ArrayList;
import java.util.List;

public class DaleLedgeGrabControl extends AbstractControl {

	public static final float MIN_DISTANCE = 0.5f;
	private Camera camera;
	private Node rootNode;
	private GameStateDTO gameStateDTO;
	private Vector3f ledgeCollisionPoint;

	public DaleLedgeGrabControl(GameStateDTO gameStateDTO) {
		GameApplication instance = GameApplication.getInstance();
		camera = instance.getCamera();
		rootNode = instance.getRootNode();
		this.gameStateDTO = gameStateDTO;
	}

	@Override
	protected void controlUpdate(float tpf) {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		handleKeyPress(daleStateDTO);
		if (!spatial.getControl(PhysicsControls.DALE)
					.onGround()) {
			handleLedgeDetecting(daleStateDTO);
		}
		else {
			daleStateDTO.setClimbingState(ClimbingState.NOT_STARTED);
		}

	}

	private void handleLedgeDetecting(DaleStateDTO daleStateDTO) {
		switch (daleStateDTO.getClimbingState()) {
		case LET_GO:
			handleLetGoLedge();
			break;
		case MOVE_IN:
			handleMoveInLedge();
			break;
		case NOT_STARTED:
			handleGrabbing();
			break;
		}
	}

	private void handleKeyPress(DaleStateDTO daleStateDTO) {
		KeyPressDTO keyPressDTO = gameStateDTO.getKeyPressDTO();
		if (keyPressDTO.isMoveInLedgePress() && daleStateDTO.getClimbingState()
															.equals(ClimbingState.GRABBING_LEDGE)) {
			daleStateDTO.setClimbingState(ClimbingState.MOVE_IN);
		}
		if (keyPressDTO.isLetGoLedgePress() && daleStateDTO.getClimbingState()
														   .equals(ClimbingState.GRABBING_LEDGE)) {
			daleStateDTO.setClimbingState(ClimbingState.LET_GO);
		}
	}

	private void handleLetGoLedge() {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		if (daleStateDTO.getClimbingState()
						.equals(ClimbingState.LET_GO)) {
			CharacterControl control = spatial.getControl(PhysicsControls.DALE);
			control.setEnabled(true);

		}
	}

	private void handleGrabbing() {
		CharacterControl control = spatial.getControl(CharacterControl.class);
		Vector3f spatialExtent = ((BoundingBox) spatial.getWorldBound()).getExtent(
				new Vector3f());
		CollisionResult closestCollision = getClosestObjectFromCharacterHeadForward(
				control, spatialExtent);
		boolean isVeryCloseToObstacle = isVeryCloseToObstacle(closestCollision);

		if (isVeryCloseToObstacle) {
			ledgeCollisionPoint = closestCollision.getContactPoint();
			boolean enoughSpaceToWalkOnIt = isEnoughSpaceToWalkOnLedge(
					control.getViewDirection(), spatialExtent);
			if (enoughSpaceToWalkOnIt) {
				if (isTheRayInsideObject(closestCollision.getContactPoint(),
						spatialExtent, control.getViewDirection())) {
					return;
				}
				control.setEnabled(false);
				control.setLinearVelocity(Vector3f.ZERO);
				gameStateDTO.getDaleStateDTO()
							.setClimbingState(ClimbingState.GRABBING_LEDGE);

			}
		}
		else {
			ledgeCollisionPoint = null;
		}
	}

	private boolean isTheRayInsideObject(Vector3f contactPoint,
			Vector3f spatialExtent, Vector3f viewDirection) {

		Ray ray = new Ray(contactPoint.add(viewDirection.mult(spatialExtent))
									  .add(Vector3f.UNIT_Y.mult(spatialExtent)),
				viewDirection.negate());
		CollisionResults results = new CollisionResults();
		rootNode.collideWith(ray, results);
		CollisionResult closestCollision = results.getClosestCollision();
		return closestCollision != null
				&& closestCollision.getDistance() <= spatialExtent.mult(
				viewDirection)
																  .length();
	}

	private void handleMoveInLedge() {
		CharacterControl control = spatial.getControl(CharacterControl.class);
		Vector3f destinationPoint = getDestinationPointAboveLedge(
				control.getViewDirection());
		if (isMovingInLedgeCompleted(destinationPoint)) {
			enablePhysicsAfterReachingLedge(control);
		}
		else {
			moveIntoLedge(control, spatial.getLocalTranslation(),
					destinationPoint);
		}
	}

	private void enablePhysicsAfterReachingLedge(CharacterControl control) {
		gameStateDTO.getDaleStateDTO()
					.setClimbingState(ClimbingState.NOT_STARTED);
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

	private boolean isMovingInLedgeCompleted(Vector3f finalPoint) {
		float distanceToFinalPoint = spatial.getLocalTranslation()
											.distance(finalPoint);
		return distanceToFinalPoint < 0.5f;
	}

	private boolean isEnoughSpaceToWalkOnLedge(Vector3f viewDirection,
			Vector3f spatialExtent) {
		List<Vector3f> points = getPointsToCheckEnoughHeightToWalk(
				viewDirection, spatialExtent);

		for (Vector3f toCheck : points) {
			if (!isEnoughHeightToStandInPoint(spatialExtent, toCheck)) {
				return false;
			}
		}
		return true;
	}

	private List<Vector3f> getPointsToCheckEnoughHeightToWalk(
			Vector3f viewDirection, Vector3f extentOfCharacter) {
		Vector3f mainPoint = getDestinationPointAboveLedge(viewDirection);
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

	private Vector3f getDestinationPointAboveLedge(Vector3f viewDirection) {
		Vector3f extent = ((BoundingBox) spatial.getWorldBound()).getExtent(
				new Vector3f());
		return ledgeCollisionPoint.add(extent.mult(viewDirection))
								  .add(extent.mult(Vector3f.UNIT_Y.mult(2)));
	}

	private boolean isEnoughHeightToStandInPoint(Vector3f spatialExtent,
			Vector3f pointToCheck) {
		Ray ray = new Ray(pointToCheck, Vector3f.UNIT_Y.negate());
		CollisionResults collisionResults = new CollisionResults();
		rootNode.collideWith(ray, collisionResults);
		CollisionResult closestCollision = collisionResults.getClosestCollision();
		return closestCollision.getDistance() >= spatialExtent.mult(
				Vector3f.UNIT_Y)
															  .length();
	}

	private boolean isVeryCloseToObstacle(CollisionResult closestCollision) {
		return closestCollision != null
				&& closestCollision.getDistance() < MIN_DISTANCE;

	}

	private CollisionResult getClosestObjectFromCharacterHeadForward(
			CharacterControl control, Vector3f extent) {
		Vector3f rayStart = spatial.getWorldTranslation()
								   .add(extent.mult(
										   control.getViewDirection()));
		Ray ray = new Ray(rayStart, control.getViewDirection());
		CollisionResults collisionResults = new CollisionResults();
		rootNode.collideWith(ray, collisionResults);

		return collisionResults.getClosestCollision();
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}
}
