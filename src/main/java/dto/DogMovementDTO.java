package dto;

import com.jme3.scene.Spatial;
import enums.MovementDirection;

public class DogMovementDTO {

	private int maximumPixelMovementInSingleDirection;
	private float positionWhereMovementBegan;
	private MovementDirection movementDirection;
	private final Spatial dog;

	public MovementDirection getMovementDirection() {
		return movementDirection;
	}

	public void setMovementDirection(MovementDirection movementDirection) {
		this.movementDirection = movementDirection;
	}

	public DogMovementDTO(Spatial dog) {
		this.dog = dog;
	}

	public int getMaximumPixelMovementInSingleDirection() {
		return maximumPixelMovementInSingleDirection;
	}

	public void setMaximumPixelMovementInSingleDirection(
			int maximumPixelMovementInSingleDirection) {
		this.maximumPixelMovementInSingleDirection = maximumPixelMovementInSingleDirection;
	}

	public float getPositionWhereMovementBegan() {
		return positionWhereMovementBegan;
	}

	public void setPositionWhereMovementBegan(float
			positionWhereMovementBegan) {
		this.positionWhereMovementBegan = positionWhereMovementBegan;
	}

	public Spatial getDog() {
		return dog;
	}

}
