package dto;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import enums.MovementDirection;

public class DogStateDTO {

	private float numberOfPixelsToMoveInGivenDirection;
	private float positionWhereMovementBegan;
	private MovementDirection movementDirection;
	private final Spatial dog;
	private final Vector3f startOfSquareWhereTheDogMoves;
	private final int squareWidth;
	private boolean collidedWithObstacle;
	private boolean isMovingAroundObstacle;
	private boolean seeingDale;

	public DogStateDTO(Spatial dog, Vector3f startOfSquareWhereTheDogMoves,
			int squareWidth) {
		this.dog = dog;
		this.startOfSquareWhereTheDogMoves = startOfSquareWhereTheDogMoves;
		this.squareWidth = squareWidth;
	}

	public boolean isMovingAroundObstacle() {
		return isMovingAroundObstacle;
	}

	public void setMovingAroundObstacle(boolean movingAroundObstacle) {
		isMovingAroundObstacle = movingAroundObstacle;
	}

	public boolean isCollidedWithObstacle() {
		return collidedWithObstacle;
	}

	public void setCollidedWithObstacle(boolean collidedWithObstacle) {
		this.collidedWithObstacle = collidedWithObstacle;
	}


	public int getSquareWidth() {
		return squareWidth;
	}

	public MovementDirection getMovementDirection() {
		return movementDirection;
	}

	public void setMovementDirection(MovementDirection movementDirection) {
		this.movementDirection = movementDirection;
	}

	public float getNumberOfPixelsToMoveInGivenDirection() {
		return numberOfPixelsToMoveInGivenDirection;
	}

	public void setNumberOfPixelsToMoveInGivenDirection(
			float numberOfPixelsToMoveInGivenDirection) {
		this.numberOfPixelsToMoveInGivenDirection = numberOfPixelsToMoveInGivenDirection;
	}

	public float getPositionWhereMovementBegan() {
		return positionWhereMovementBegan;
	}

	public void setPositionWhereMovementBegan(
			float positionWhereMovementBegan) {
		this.positionWhereMovementBegan = positionWhereMovementBegan;
	}

	public Spatial getDog() {
		return dog;
	}

	public Vector3f getStartOfSquareWhereTheDogMoves() {
		return startOfSquareWhereTheDogMoves;
	}

	public void setSeeingDale(boolean seeingDale) {
		this.seeingDale = seeingDale;
	}

	public boolean isSeeingDale() {
		return seeingDale;
	}
}
