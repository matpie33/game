package dto;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import enums.MovementDirection;

import java.util.ArrayList;
import java.util.List;

public class DogMovementDTO {

	private float numberOfPixelsToMoveInGivenDirection;
	private float positionWhereMovementBegan;
	private MovementDirection movementDirection;
	private final Spatial dog;
	private final Vector3f startOfSquareWhereTheDogMoves;
	private final int squareWidth;
	private List<DebugData> debugData = new ArrayList<>();

	public DogMovementDTO(Spatial dog, Vector3f startOfSquareWhereTheDogMoves,
			int squareWidth) {
		this.dog = dog;
		this.startOfSquareWhereTheDogMoves = startOfSquareWhereTheDogMoves;
		this.squareWidth = squareWidth;
	}

	public void addDebugData(DebugData debugData) {
		this.debugData.add(debugData);
	}

	public List<DebugData> getDebugData() {
		return debugData;
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

	public DebugData getLastMovementData() {
		return debugData.isEmpty() ? null : debugData.get(debugData.size() - 1);
	}
}
