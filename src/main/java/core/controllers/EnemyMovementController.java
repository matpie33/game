package core.controllers;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import constants.PhysicsControls;
import dto.DogMovementDTO;
import dto.GameStateDTO;
import enums.MovementDirection;

import java.util.List;
import java.util.Random;

public class EnemyMovementController {

	public static final int NUMBER_OF_POSSIBLE_DIRECTIONS = 4;
	public static final int MINIMUM_VALUE = 1;
	public static final int MINIMUM_PIXEL_MOVEMENT_IN_DIRECTION = 5;
	public static final int MAXIMUM_PIXEL_MOVEMENT_IN_DIRECTION = 10;
	private GameStateDTO gameStateDTO;

	public EnemyMovementController(GameStateDTO gameStateDTO) {
		this.gameStateDTO = gameStateDTO;
	}

	public void moveEnemies(float tpf) {
		List<DogMovementDTO> dogMovementDTOS = gameStateDTO.getDogMovementDTOS();
		for (DogMovementDTO dogMovementDTO : dogMovementDTOS) {
			if (enemyMovedEnoughInCurrentDirection(dogMovementDTO)) {
				setNewRandomDirectionAndMaximumPixels(dogMovementDTO);
			}
			else {
				moveDog(dogMovementDTO, tpf);
			}
		}
	}

	private void moveDog(DogMovementDTO dogMovementDTO, float tpf) {
		Spatial dog = dogMovementDTO.getDog();
		boolean isXMovement = isXMovement(
				dogMovementDTO.getMovementDirection());
		float movementSpeed = 1 * tpf;
		float xMovement = isXMovement ? movementSpeed : 0;
		float zMovement = isXMovement ? 0 : movementSpeed;
		CharacterControl control = dog.getControl(PhysicsControls.DOG);
		control.setWalkDirection(new Vector3f(xMovement, 0, zMovement));
	}

	private void setNewRandomDirectionAndMaximumPixels(
			DogMovementDTO dogMovementDTO) {
		int oldDirection = dogMovementDTO.getMovementDirection()
										 .getValue();
		Random random = new Random();
		int newDirection = oldDirection;

		while (newDirection == oldDirection) {
			newDirection = random.nextInt(NUMBER_OF_POSSIBLE_DIRECTIONS)
					+ MINIMUM_VALUE;
		}

		int numberOfPixelsToMove = random.nextInt(
				MAXIMUM_PIXEL_MOVEMENT_IN_DIRECTION
						- MINIMUM_PIXEL_MOVEMENT_IN_DIRECTION + 1)
				+ MINIMUM_PIXEL_MOVEMENT_IN_DIRECTION;

		dogMovementDTO.setMaximumPixelMovementInSingleDirection(
				numberOfPixelsToMove);
		dogMovementDTO.setMovementDirection(
				MovementDirection.fromInt(newDirection));
		Vector3f position = dogMovementDTO.getDog()
										  .getLocalTranslation();
		float positionStart = isXMovement(
				dogMovementDTO.getMovementDirection()) ?
				position.getX() :
				position.getZ();
		dogMovementDTO.setPositionWhereMovementBegan(positionStart);

	}

	private boolean enemyMovedEnoughInCurrentDirection(
			DogMovementDTO dogMovementDTO) {
		Spatial dog = dogMovementDTO.getDog();
		MovementDirection movementDirection = dogMovementDTO.getMovementDirection();
		int maximumPixelsToMove = dogMovementDTO.getMaximumPixelMovementInSingleDirection();
		float startPixel = dogMovementDTO.getPositionWhereMovementBegan();
		float currentPixel = isXMovement(movementDirection) ?
				dog.getLocalTranslation()
				   .getX() :
				dog.getLocalTranslation()
				   .getZ();
		return currentPixel - startPixel >= maximumPixelsToMove;
	}

	private boolean isXMovement(MovementDirection movementDirection) {
		return movementDirection.equals(MovementDirection.FORWARD_X)
				|| movementDirection.equals(MovementDirection.BACKWARD_X);
	}
}
