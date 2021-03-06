package core.appState;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import constants.NodeNames;
import constants.PhysicsControls;
import core.GameApplication;
import dto.GameStateDTO;
import dto.KeyPressDTO;
import enums.ClimbingState;

import java.util.ArrayList;
import java.util.List;

public class DaleLedgeGrabAppState extends BaseAppState {

	public static final float MIN_DISTANCE = 0.5f;
	private Camera camera;
	private Node rootNode;
	private GameStateDTO gameStateDTO;
	private Vector3f ledgeCollisionPoint;
	private ClimbingState climbingState = ClimbingState.NONE;

	public DaleLedgeGrabAppState(GameStateDTO gameStateDTO) {
		GameApplication instance = GameApplication.getInstance();
		camera = instance.getCamera();
		rootNode = instance.getRootNode();
		this.gameStateDTO = gameStateDTO;
	}

	@Override
	public void update(float tpf) {
		handleKeyPress();
		Spatial dale = getDale();
		if (!dale.getControl(PhysicsControls.DALE)
				 .onGround()) {
			handleLedgeDetecting();
		}
		else {
			climbingState = ClimbingState.NONE;
		}

	}

	private Spatial getDale() {
		return rootNode.getChild(NodeNames.getDale());
	}

	private void handleLedgeDetecting() {
		switch (climbingState) {
		case LET_GO:
			handleLetGoLedge();
			break;
		case MOVE_IN:
			handleMoveInLedge();
			break;
		case NONE:
			handleGrabbing();
			break;
		}

	}

	private void handleKeyPress() {
		KeyPressDTO keyPressDTO = gameStateDTO.getKeyPressDTO();
		if (keyPressDTO.isMoveInLedgePress() && climbingState.equals(
				ClimbingState.GRABBING_LEDGE)) {
			climbingState = ClimbingState.MOVE_IN;
		}
		if (keyPressDTO.isLetGoLedgePress() && climbingState.equals(
				ClimbingState.GRABBING_LEDGE)) {
			climbingState = ClimbingState.LET_GO;
		}
	}

	private void handleLetGoLedge() {
		Spatial dale = getDale();
		CharacterControl control = dale.getControl(PhysicsControls.DALE);
		control.setEnabled(true);
	}

	private void handleGrabbing() {
		Spatial dale = getDale();
		CharacterControl control = dale.getControl(CharacterControl.class);
		Vector3f spatialExtent = ((BoundingBox) dale.getWorldBound()).getExtent(
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
				climbingState = ClimbingState.GRABBING_LEDGE;

			}
		}
		else {
			ledgeCollisionPoint = null;
		}
	}

	public ClimbingState getClimbingState() {
		return climbingState;
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
		CharacterControl control = getDale().getControl(CharacterControl.class);
		Vector3f destinationPoint = getDestinationPointAboveLedge(
				control.getViewDirection());
		if (isMovingInLedgeCompleted(destinationPoint)) {
			enablePhysicsAfterReachingLedge(control);
			climbingState = ClimbingState.NONE;
		}
		else {
			moveIntoLedge(control, getDale().getLocalTranslation(),
					destinationPoint);
		}
	}

	private void enablePhysicsAfterReachingLedge(CharacterControl control) {
		control.setEnabled(true);
		control.setPhysicsLocation(getDale().getWorldTranslation());
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
		getDale().setLocalTranslation(getDale().getLocalTranslation()
											   .add(moveDirection));
	}

	private boolean isMovingInLedgeCompleted(Vector3f finalPoint) {
		float distanceToFinalPoint = getDale().getLocalTranslation()
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
		Vector3f extent = ((BoundingBox) getDale().getWorldBound()).getExtent(
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
		Vector3f rayStart = getDale().getWorldTranslation()
									 .add(extent.mult(
											 control.getViewDirection()));
		Ray ray = new Ray(rayStart, control.getViewDirection());
		CollisionResults collisionResults = new CollisionResults();
		rootNode.collideWith(ray, collisionResults);

		return collisionResults.getClosestCollision();
	}

	@Override
	protected void initialize(Application app) {

	}

	@Override
	protected void cleanup(Application app) {

	}

	@Override
	protected void onEnable() {

	}

	@Override
	protected void onDisable() {

	}
}
