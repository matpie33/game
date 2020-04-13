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
	private boolean isAlive;
	private boolean isMoving;

	public DogStateDTO(Spatial dog, Vector3f startOfSquareWhereTheDogMoves,
			int squareWidth) {
		this.dog = dog;
		this.startOfSquareWhereTheDogMoves = startOfSquareWhereTheDogMoves;
		this.squareWidth = squareWidth;
		isAlive = true;
		isMoving = true;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public void setMoving(boolean moving) {
		isMoving = moving;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean alive) {
		isAlive = alive;
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
}