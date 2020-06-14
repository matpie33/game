package core.controls;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import constants.PhysicsControls;
import enums.MovementDirection;

import java.util.Random;

public class DogMovingInsideAreaControl extends AbstractControl {

	public static final int NUMBER_OF_POSSIBLE_DIRECTIONS = 4;
	public static final int DIRECTION_MINIMUM_VALUE = 1;
	public static final float MINIMUM_PIXEL_MOVEMENT_IN_DIRECTION = 10.000000f;
	public static final float MOVEMENT_SPEED = 0.1f;

	private MovementDirection movementDirection;
	private float numberOfPixelsToMoveInGivenDirection;
	private boolean collidingWithObstacle;
	private float positionWhereMovementStarted;
	private int squareWidth;
	private Vector3f startOfSquareWhereTheDogMoves;

	public void setCollidingWithObstacle() {
		collidingWithObstacle = true;
	}


	@Override
	protected void controlUpdate(float tpf) {
		if (!spatial.getControl(PhysicsControls.DOG)
					.onGround()) {
			spatial.getControl(PhysicsControls.DOG)
				   .setWalkDirection(Vector3f.ZERO);
			return;
		}
		if (enemyMovedEnoughInCurrentDirection()) {
			collidingWithObstacle = false;
			CharacterControl control = spatial.getControl(PhysicsControls.DOG);
			control.setWalkDirection(Vector3f.ZERO);
			setNewRandomDirectionAndMaximumPixels();

		}
		else if (collidingWithObstacle) {
			CharacterControl control = spatial.getControl(PhysicsControls.DOG);
			control.setWalkDirection(Vector3f.ZERO);
			MovementDirection newDirection = movementDirection.getCounterDirection();
			setMovementDirection(newDirection);
			numberOfPixelsToMoveInGivenDirection = 2f;

			positionWhereMovementStarted = isXMovement(newDirection) ?
					control.getPhysicsLocation()
						   .getX() :
					control.getPhysicsLocation()
						   .getZ();

		}
		else {
			moveEnemy();
		}
	}


	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}

	private void moveEnemy() {

		boolean isXMovement = isXMovement(movementDirection);
		float movementSpeed =
				MOVEMENT_SPEED * movementDirection.getDirectionModifier();
		float xMovement = isXMovement ? movementSpeed : 0;
		float zMovement = isXMovement ? 0 : movementSpeed;
		CharacterControl control = spatial.getControl(PhysicsControls.DOG);
		Vector3f vec = new Vector3f(xMovement, 0, zMovement);
		control.setWalkDirection(vec);
		control.setViewDirection(vec);
	}

	private void setNewRandomDirectionAndMaximumPixels() {

		int newDirection = generateNewDirection();
		float maximumPixelsToMoveInDirection = calculateMaximumMovementInGivenDirection(
				newDirection);
		generateRandomPixelsToMove(maximumPixelsToMoveInDirection);
		setMovementData();

	}

	private void setMovementData() {
		Vector3f position = spatial.getControl(PhysicsControls.DOG)
								   .getPhysicsLocation();
		positionWhereMovementStarted = isXMovement(movementDirection) ?
				position.getX() :
				position.getZ();
	}

	private void generateRandomPixelsToMove(
			float maximumPixelsToMoveInDirection) {
		int maximumNumberOfSteps = (int) Math.floor(
				maximumPixelsToMoveInDirection);
		int stepsToMove = FastMath.nextRandomInt(
				(int) (MINIMUM_PIXEL_MOVEMENT_IN_DIRECTION),
				maximumNumberOfSteps);
		numberOfPixelsToMoveInGivenDirection = (float) stepsToMove;
	}

	private int generateNewDirection() {
		int oldDirection = movementDirection.getCodeValue();
		int newDirection = oldDirection;
		Random random = new Random();
		while (newDirection == oldDirection) {
			newDirection = random.nextInt(NUMBER_OF_POSSIBLE_DIRECTIONS)
					+ DIRECTION_MINIMUM_VALUE;
		}
		return newDirection;
	}

	private float calculateMaximumMovementInGivenDirection(int newDirection) {
		MovementDirection movementDirection = MovementDirection.fromInt(
				newDirection);
		Vector3f location = spatial.getControl(PhysicsControls.DOG)
								   .getPhysicsLocation();
		float maximumPixelsToMoveInDirection;
		switch (movementDirection) {
		case FORWARD_X:
			maximumPixelsToMoveInDirection =
					startOfSquareWhereTheDogMoves.getX() + squareWidth
							- location.getX();
			break;

		case BACKWARD_X:
			maximumPixelsToMoveInDirection =
					location.getX() - startOfSquareWhereTheDogMoves.getX();
			break;

		case FORWARD_Z:
			maximumPixelsToMoveInDirection =
					startOfSquareWhereTheDogMoves.getZ() + squareWidth
							- location.getZ();
			break;
		case BACKWARD_Z:
			maximumPixelsToMoveInDirection =
					location.getZ() - startOfSquareWhereTheDogMoves.getZ();
			break;
		default:
			throw new IllegalArgumentException(
					"Unknown direction: " + "" + movementDirection);
		}
		if (maximumPixelsToMoveInDirection < 0) {
			maximumPixelsToMoveInDirection = -maximumPixelsToMoveInDirection;
			//TODO temporary fix
		}
		if (maximumPixelsToMoveInDirection
				< MINIMUM_PIXEL_MOVEMENT_IN_DIRECTION) {
			movementDirection = movementDirection.getCounterDirection();
			maximumPixelsToMoveInDirection =
					squareWidth - maximumPixelsToMoveInDirection;
		}
		this.movementDirection = movementDirection;
		return maximumPixelsToMoveInDirection;
	}

	private boolean enemyMovedEnoughInCurrentDirection() {
		Vector3f physicsLocation = spatial.getControl(PhysicsControls.DOG)
										  .getPhysicsLocation();

		float currentPixel = isXMovement(movementDirection) ?
				physicsLocation.getX() :
				physicsLocation.getZ();
		return Math.abs(currentPixel - positionWhereMovementStarted
				+ MOVEMENT_SPEED * movementDirection.getDirectionModifier())
				>= numberOfPixelsToMoveInGivenDirection;
	}

	private boolean isXMovement(MovementDirection movementDirection) {
		return movementDirection.equals(MovementDirection.FORWARD_X)
				|| movementDirection.equals(MovementDirection.BACKWARD_X);
	}

	public void setMovementDirection(MovementDirection movementDirection) {
		this.movementDirection = movementDirection;
	}

	public void setNumberOfPixelsToMoveInGivenDirection(
			int numberOfPixelsToMove) {
		this.numberOfPixelsToMoveInGivenDirection = (float) numberOfPixelsToMove;
	}

	public void setPositionWhereMovementBegan(
			float positionWhereMovementStarted) {
		this.positionWhereMovementStarted = positionWhereMovementStarted;
	}

	public void setSquareWidth(int squareWidth) {
		this.squareWidth = squareWidth;
	}

	public void setStartOfSquareWhereTheDogMoves(
			Vector3f startOfSquareWhereTheDogMoves) {
		this.startOfSquareWhereTheDogMoves = startOfSquareWhereTheDogMoves;
	}
}
