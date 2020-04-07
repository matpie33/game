package dto;

import com.jme3.math.Vector3f;
import enums.MovementDirection;

public class DebugData {

	private float positionStart;
	private float positionEnd;

	private MovementDirection movementDirection;
	private float maximumPossibleMovement;
	private float generatedSmallerMovement;
	private Vector3f squarePosition;
	private int squareWidth;

	public Vector3f getSquarePosition() {
		return squarePosition;
	}

	public void setSquarePosition(Vector3f squarePosition) {
		this.squarePosition = squarePosition;
	}

	public int getSquareWidth() {
		return squareWidth;
	}

	public void setSquareWidth(int squareWidth) {
		this.squareWidth = squareWidth;
	}

	private boolean isPositionX;

	public float getPositionStart() {
		return positionStart;
	}

	public void setPositionStart(float positionStart) {
		this.positionStart = positionStart;
	}

	public void setPositionEnd(float positionEnd) {
		this.positionEnd = positionEnd;
	}

	public MovementDirection getMovementDirection() {
		return movementDirection;
	}

	public void setMovementDirection(MovementDirection movementDirection) {
		this.movementDirection = movementDirection;
	}

	public float getMaximumPossibleMovement() {
		return maximumPossibleMovement;
	}

	public void setMaximumPossibleMovement(float maximumPossibleMovement) {
		this.maximumPossibleMovement = maximumPossibleMovement;
	}

	public float getGeneratedSmallerMovement() {
		return generatedSmallerMovement;
	}

	public void setGeneratedSmallerMovement(float generatedSmallerMovement) {
		this.generatedSmallerMovement = generatedSmallerMovement;
	}

	public boolean isPositionX() {
		return isPositionX;
	}

	public void setPositionX(boolean positionX) {
		isPositionX = positionX;
	}

	@Override
	public String toString() {
		return String.format("MaximumMovement: %s, generated smaller "
						+ "movement: %s, isX: %s, "
						+ " direction: %s, position start: %s, " + "position end %s"
						+ "square pos: %s, width: %s \n",
				Float.toString(maximumPossibleMovement),
				Float.toString(generatedSmallerMovement),
				Boolean.toString(isPositionX),

				movementDirection.toString(), Float.toString(positionStart),
				Float.toString(positionEnd), squarePosition.toString(),
				Integer.toString(getSquareWidth()));
	}

}
