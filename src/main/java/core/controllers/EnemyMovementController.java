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
	public static final int DIRECTION_MINIMUM_VALUE = 1;
	public static final float MINIMUM_PIXEL_MOVEMENT_IN_DIRECTION = 10.000000f;
	public static final float MOVEMENT_SPEED = 0.1f;
	private GameStateDTO gameStateDTO;

	public EnemyMovementController(GameStateDTO gameStateDTO) {
		this.gameStateDTO = gameStateDTO;
	}

	public void moveEnemies(float tpf) {
		List<DogMovementDTO> dogMovementDTOS = gameStateDTO.getDogMovementDTOS();
		for (DogMovementDTO dogMovementDTO : dogMovementDTOS) {
			if (!dogMovementDTO.getDog()
							   .getControl(PhysicsControls.DOG)
							   .onGround()) {
				dogMovementDTO.getDog()
							  .getControl(PhysicsControls.DOG)
							  .setWalkDirection(Vector3f.ZERO);
				continue;
			}
			if (enemyMovedEnoughInCurrentDirection(dogMovementDTO, tpf)) {
				setNewRandomDirectionAndMaximumPixels(dogMovementDTO);
			}
			else {
				moveEnemy(dogMovementDTO, tpf);
			}
		}
	}

	private void moveEnemy(DogMovementDTO dogMovementDTO, float tpf) {
		Spatial dog = dogMovementDTO.getDog();
		boolean isXMovement = isXMovement(
				dogMovementDTO.getMovementDirection());
		float movementSpeed =
				MOVEMENT_SPEED * dogMovementDTO.getMovementDirection()
											   .getDirectionModifier();
		float xMovement = isXMovement ? movementSpeed : 0;
		float zMovement = isXMovement ? 0 : movementSpeed;
		CharacterControl control = dog.getControl(PhysicsControls.DOG);
		Vector3f vec = new Vector3f(xMovement, 0, zMovement);
		control.setWalkDirection(vec);
		control.setViewDirection(vec);
	}

	private void setNewRandomDirectionAndMaximumPixels(
			DogMovementDTO dogMovementDTO) {

		int newDirection = generateNewDirection(dogMovementDTO);
		float maximumPixelsToMoveInDirection = calculateMaximumMovementInGivenDirection(
				dogMovementDTO, newDirection);
		generateRandomPixelsToMove(dogMovementDTO,
				maximumPixelsToMoveInDirection);
		setMovementData(dogMovementDTO);

	}

	private void setMovementData(DogMovementDTO dogMovementDTO) {
		Vector3f position = dogMovementDTO.getDog()
										  .getControl(PhysicsControls.DOG)
										  .getPhysicsLocation();
		float positionStart = isXMovement(
				dogMovementDTO.getMovementDirection()) ?
				position.getX() :
				position.getZ();
		dogMovementDTO.setPositionWhereMovementBegan(positionStart);
	}

	private void generateRandomPixelsToMove(DogMovementDTO dogMovementDTO,
			float maximumPixelsToMoveInDirection) {
		Random random = new Random();
		float numberOfPixelsToMove =
				random.nextFloat() * (maximumPixelsToMoveInDirection
						- MINIMUM_PIXEL_MOVEMENT_IN_DIRECTION)
						+ MINIMUM_PIXEL_MOVEMENT_IN_DIRECTION;
		dogMovementDTO.setNumberOfPixelsToMoveInGivenDirection(
				numberOfPixelsToMove);
	}

	private int generateNewDirection(DogMovementDTO dogMovementDTO) {
		int oldDirection = dogMovementDTO.getMovementDirection()
										 .getCodeValue();
		int newDirection = oldDirection;
		Random random = new Random();
		while (newDirection == oldDirection) {
			newDirection = random.nextInt(NUMBER_OF_POSSIBLE_DIRECTIONS)
					+ DIRECTION_MINIMUM_VALUE;
		}
		return newDirection;
	}

	private float calculateMaximumMovementInGivenDirection(
			DogMovementDTO dogMovementDTO, int newDirection) {
		MovementDirection movementDirection = MovementDirection.fromInt(
				newDirection);
		Vector3f location = dogMovementDTO.getDog()
										  .getControl(PhysicsControls.DOG)
										  .getPhysicsLocation();
		float maximumPixelsToMoveInDirection;
		switch (movementDirection) {
		case FORWARD_X:
			maximumPixelsToMoveInDirection =
					dogMovementDTO.getStartOfSquareWhereTheDogMoves()
								  .getX() + dogMovementDTO.getSquareWidth()
							- location.getX();
			break;

		case BACKWARD_X:
			maximumPixelsToMoveInDirection = location.getX()
					- dogMovementDTO.getStartOfSquareWhereTheDogMoves()
									.getX();
			break;

		case FORWARD_Z:
			maximumPixelsToMoveInDirection =
					dogMovementDTO.getStartOfSquareWhereTheDogMoves()
								  .getZ() + dogMovementDTO.getSquareWidth()
							- location.getZ();
			break;
		case BACKWARD_Z:
			maximumPixelsToMoveInDirection = location.getZ()
					- dogMovementDTO.getStartOfSquareWhereTheDogMoves()
									.getZ();
			break;
		default:
			throw new IllegalArgumentException(
					"Unknown direction: " + "" + movementDirection);
		}
		if (maximumPixelsToMoveInDirection < 0) {
			throw new IllegalArgumentException(
					"Negative pixels to move" + maximumPixelsToMoveInDirection);
		}
		if (maximumPixelsToMoveInDirection
				< MINIMUM_PIXEL_MOVEMENT_IN_DIRECTION) {
			movementDirection = movementDirection.getCounterDirection();
			maximumPixelsToMoveInDirection = dogMovementDTO.getSquareWidth()
					- maximumPixelsToMoveInDirection;
		}
		dogMovementDTO.setMovementDirection(movementDirection);
		return maximumPixelsToMoveInDirection;
	}

	private boolean enemyMovedEnoughInCurrentDirection(
			DogMovementDTO dogMovementDTO, float tpf) {
		Spatial dog = dogMovementDTO.getDog();
		Vector3f physicsLocation = dog.getControl(PhysicsControls.DOG)
									  .getPhysicsLocation();
		MovementDirection movementDirection = dogMovementDTO.getMovementDirection();
		float maximumPixelsToMove = dogMovementDTO.getNumberOfPixelsToMoveInGivenDirection();
		float startPixel = dogMovementDTO.getPositionWhereMovementBegan();
		float currentPixel = isXMovement(movementDirection) ?
				physicsLocation.getX() :
				physicsLocation.getZ();
		return Math.abs(currentPixel - startPixel
				+ MOVEMENT_SPEED * movementDirection.getDirectionModifier())
				>= maximumPixelsToMove;
	}

	private boolean isXMovement(MovementDirection movementDirection) {
		return movementDirection.equals(MovementDirection.FORWARD_X)
				|| movementDirection.equals(MovementDirection.BACKWARD_X);
	}
}
