package core.controls;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import constants.PhysicsControls;
import dto.DogStateDTO;
import dto.GameStateDTO;
import enums.MovementDirection;

import java.util.Random;

public class DogMovementControl extends AbstractControl {

	public static final int NUMBER_OF_POSSIBLE_DIRECTIONS = 4;
	public static final int DIRECTION_MINIMUM_VALUE = 1;
	public static final float MINIMUM_PIXEL_MOVEMENT_IN_DIRECTION = 10.000000f;
	public static final float MOVEMENT_SPEED = 0.1f;
	private DogStateDTO dogStateDTO;

	public DogMovementControl(DogStateDTO dogStateDTO) {
		this.dogStateDTO = dogStateDTO;
	}

	@Override
	protected void controlUpdate(float tpf) {
		moveEnemies();
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}

	public void moveEnemies() {
		if (!dogStateDTO.getDog()
						.getControl(PhysicsControls.DOG)
						.onGround()) {
			dogStateDTO.getDog()
					   .getControl(PhysicsControls.DOG)
					   .setWalkDirection(Vector3f.ZERO);
			return;
		}
		if (enemyMovedEnoughInCurrentDirection(dogStateDTO)) {
			CharacterControl control = dogStateDTO.getDog()
												  .getControl(
														  PhysicsControls.DOG);
			control.setWalkDirection(Vector3f.ZERO);
			setNewRandomDirectionAndMaximumPixels(dogStateDTO);

		}
		else {
			moveEnemy(dogStateDTO);
		}
	}

	private void moveEnemy(DogStateDTO dogStateDTO) {
		Spatial dog = dogStateDTO.getDog();
		boolean isXMovement = isXMovement(dogStateDTO.getMovementDirection());
		float movementSpeed =
				MOVEMENT_SPEED * dogStateDTO.getMovementDirection()
											.getDirectionModifier();
		float xMovement = isXMovement ? movementSpeed : 0;
		float zMovement = isXMovement ? 0 : movementSpeed;
		CharacterControl control = dog.getControl(PhysicsControls.DOG);
		Vector3f vec = new Vector3f(xMovement, 0, zMovement);
		control.setWalkDirection(vec);
		control.setViewDirection(vec);
	}

	private void setNewRandomDirectionAndMaximumPixels(
			DogStateDTO dogStateDTO) {

		int newDirection = generateNewDirection(dogStateDTO);
		float maximumPixelsToMoveInDirection = calculateMaximumMovementInGivenDirection(
				dogStateDTO, newDirection);
		generateRandomPixelsToMove(dogStateDTO, maximumPixelsToMoveInDirection);
		setMovementData(dogStateDTO);

	}

	private void setMovementData(DogStateDTO dogStateDTO) {
		Vector3f position = dogStateDTO.getDog()
									   .getControl(PhysicsControls.DOG)
									   .getPhysicsLocation();
		float positionStart = isXMovement(dogStateDTO.getMovementDirection()) ?
				position.getX() :
				position.getZ();
		dogStateDTO.setPositionWhereMovementBegan(positionStart);
	}

	private void generateRandomPixelsToMove(DogStateDTO dogStateDTO,
			float maximumPixelsToMoveInDirection) {
		int maximumNumberOfSteps = (int) Math.floor(
				maximumPixelsToMoveInDirection / MOVEMENT_SPEED);
		int stepsToMove = FastMath.nextRandomInt(
				(int) (MINIMUM_PIXEL_MOVEMENT_IN_DIRECTION * MOVEMENT_SPEED),
				maximumNumberOfSteps);
		float pixelsToMove = stepsToMove * MOVEMENT_SPEED;
		dogStateDTO.setNumberOfPixelsToMoveInGivenDirection(pixelsToMove);
	}

	private int generateNewDirection(DogStateDTO dogStateDTO) {
		int oldDirection = dogStateDTO.getMovementDirection()
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
			DogStateDTO dogStateDTO, int newDirection) {
		MovementDirection movementDirection = MovementDirection.fromInt(
				newDirection);
		Vector3f location = dogStateDTO.getDog()
									   .getControl(PhysicsControls.DOG)
									   .getPhysicsLocation();
		float maximumPixelsToMoveInDirection;
		switch (movementDirection) {
		case FORWARD_X:
			maximumPixelsToMoveInDirection =
					dogStateDTO.getStartOfSquareWhereTheDogMoves()
							   .getX() + dogStateDTO.getSquareWidth()
							- location.getX();
			break;

		case BACKWARD_X:
			maximumPixelsToMoveInDirection = location.getX()
					- dogStateDTO.getStartOfSquareWhereTheDogMoves()
								 .getX();
			break;

		case FORWARD_Z:
			maximumPixelsToMoveInDirection =
					dogStateDTO.getStartOfSquareWhereTheDogMoves()
							   .getZ() + dogStateDTO.getSquareWidth()
							- location.getZ();
			break;
		case BACKWARD_Z:
			maximumPixelsToMoveInDirection = location.getZ()
					- dogStateDTO.getStartOfSquareWhereTheDogMoves()
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
			maximumPixelsToMoveInDirection = dogStateDTO.getSquareWidth()
					- maximumPixelsToMoveInDirection;
		}
		dogStateDTO.setMovementDirection(movementDirection);
		return maximumPixelsToMoveInDirection;
	}

	private boolean enemyMovedEnoughInCurrentDirection(DogStateDTO dogStateDTO) {
		Spatial dog = dogStateDTO.getDog();
		Vector3f physicsLocation = dog.getControl(PhysicsControls.DOG)
									  .getPhysicsLocation();
		MovementDirection movementDirection = dogStateDTO.getMovementDirection();
		float maximumPixelsToMove = dogStateDTO.getNumberOfPixelsToMoveInGivenDirection();
		float startPixel = dogStateDTO.getPositionWhereMovementBegan();
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
