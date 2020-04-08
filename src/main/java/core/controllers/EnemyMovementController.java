package core.controllers;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import constants.PhysicsControls;
import dto.DogDataDTO;
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
		List<DogDataDTO> dogDataDTOS = gameStateDTO.getDogDataDTOS();
		for (DogDataDTO dogDataDTO : dogDataDTOS) {
			if (!dogDataDTO.getDog()
						   .getControl(PhysicsControls.DOG)
						   .onGround()) {
				dogDataDTO.getDog()
						  .getControl(PhysicsControls.DOG)
						  .setWalkDirection(Vector3f.ZERO);
				continue;
			}
			if (enemyMovedEnoughInCurrentDirection(dogDataDTO, tpf)) {
				CharacterControl control = dogDataDTO.getDog()
													 .getControl(
																 PhysicsControls.DOG);
				control.setWalkDirection(Vector3f.ZERO);
				setNewRandomDirectionAndMaximumPixels(dogDataDTO);

			}
			else {
				moveEnemy(dogDataDTO, tpf);
			}
		}
	}

	private void moveEnemy(DogDataDTO dogDataDTO, float tpf) {
		Spatial dog = dogDataDTO.getDog();
		boolean isXMovement = isXMovement(
				dogDataDTO.getMovementDirection());
		float movementSpeed =
				MOVEMENT_SPEED * dogDataDTO.getMovementDirection()
										   .getDirectionModifier();
		float xMovement = isXMovement ? movementSpeed : 0;
		float zMovement = isXMovement ? 0 : movementSpeed;
		CharacterControl control = dog.getControl(PhysicsControls.DOG);
		Vector3f vec = new Vector3f(xMovement, 0, zMovement);
		control.setWalkDirection(vec);
		control.setViewDirection(vec);
	}

	private void setNewRandomDirectionAndMaximumPixels(
			DogDataDTO dogDataDTO) {

		int newDirection = generateNewDirection(dogDataDTO);
		float maximumPixelsToMoveInDirection = calculateMaximumMovementInGivenDirection(
				dogDataDTO, newDirection);
		generateRandomPixelsToMove(dogDataDTO,
				maximumPixelsToMoveInDirection);
		setMovementData(dogDataDTO);

	}

	private void setMovementData(DogDataDTO dogDataDTO) {
		Vector3f position = dogDataDTO.getDog()
									  .getControl(PhysicsControls.DOG)
									  .getPhysicsLocation();
		float positionStart = isXMovement(
				dogDataDTO.getMovementDirection()) ?
				position.getX() :
				position.getZ();
		dogDataDTO.setPositionWhereMovementBegan(positionStart);
	}

	private void generateRandomPixelsToMove(DogDataDTO dogDataDTO,
			float maximumPixelsToMoveInDirection) {
		int maximumNumberOfSteps = (int) Math.floor(
				maximumPixelsToMoveInDirection / MOVEMENT_SPEED);
		int stepsToMove = FastMath.nextRandomInt(
				(int) (MINIMUM_PIXEL_MOVEMENT_IN_DIRECTION * MOVEMENT_SPEED),
				maximumNumberOfSteps);
		float pixelsToMove = stepsToMove * MOVEMENT_SPEED;
		dogDataDTO.setNumberOfPixelsToMoveInGivenDirection(pixelsToMove);
	}

	private int generateNewDirection(DogDataDTO dogDataDTO) {
		int oldDirection = dogDataDTO.getMovementDirection()
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
			DogDataDTO dogDataDTO, int newDirection) {
		MovementDirection movementDirection = MovementDirection.fromInt(
				newDirection);
		Vector3f location = dogDataDTO.getDog()
									  .getControl(PhysicsControls.DOG)
									  .getPhysicsLocation();
		float maximumPixelsToMoveInDirection;
		switch (movementDirection) {
		case FORWARD_X:
			maximumPixelsToMoveInDirection =
					dogDataDTO.getStartOfSquareWhereTheDogMoves()
							  .getX() + dogDataDTO.getSquareWidth()
							- location.getX();
			break;

		case BACKWARD_X:
			maximumPixelsToMoveInDirection = location.getX()
					- dogDataDTO.getStartOfSquareWhereTheDogMoves()
								.getX();
			break;

		case FORWARD_Z:
			maximumPixelsToMoveInDirection =
					dogDataDTO.getStartOfSquareWhereTheDogMoves()
							  .getZ() + dogDataDTO.getSquareWidth()
							- location.getZ();
			break;
		case BACKWARD_Z:
			maximumPixelsToMoveInDirection = location.getZ()
					- dogDataDTO.getStartOfSquareWhereTheDogMoves()
								.getZ();
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
			maximumPixelsToMoveInDirection = dogDataDTO.getSquareWidth()
					- maximumPixelsToMoveInDirection;
		}
		dogDataDTO.setMovementDirection(movementDirection);
		return maximumPixelsToMoveInDirection;
	}

	private boolean enemyMovedEnoughInCurrentDirection(
			DogDataDTO dogDataDTO, float tpf) {
		Spatial dog = dogDataDTO.getDog();
		Vector3f physicsLocation = dog.getControl(PhysicsControls.DOG)
									  .getPhysicsLocation();
		MovementDirection movementDirection = dogDataDTO.getMovementDirection();
		float maximumPixelsToMove = dogDataDTO.getNumberOfPixelsToMoveInGivenDirection();
		float startPixel = dogDataDTO.getPositionWhereMovementBegan();
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
