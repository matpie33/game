package dto;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import enums.ClimbingState;
import enums.ThrowingState;

public class DaleStateDTO {

	private boolean alive;
	private ThrowingDTO carriedObject = new ThrowingDTO();
	private int hp;
	private boolean isCollidingWithEnemy;
	private Vector3f ledgeCollisionPoint;
	private ClimbingState climbingState = ClimbingState.NOT_STARTED;
	private ThrowingState throwingState = ThrowingState.NOT_STARTED;

	public ThrowingState getThrowingState() {
		return throwingState;
	}

	public void setThrowingState(ThrowingState throwingState) {
		this.throwingState = throwingState;
	}

	public ClimbingState getClimbingState() {
		return climbingState;
	}

	public void setClimbingState(ClimbingState climbingState) {
		this.climbingState = climbingState;
	}

	public Vector3f getLedgeCollisionPoint() {
		return ledgeCollisionPoint;
	}

	public void setLedgeCollisionPoint(Vector3f ledgeCollisionPoint) {
		this.ledgeCollisionPoint = ledgeCollisionPoint;
	}

	public boolean isCollidingWithEnemy() {
		return isCollidingWithEnemy;
	}

	public void setCollidingWithEnemy(boolean collidingWithEnemy) {
		isCollidingWithEnemy = collidingWithEnemy;
	}

	public DaleStateDTO() {
		alive = true;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public ThrowingDTO getCarriedObject() {
		return carriedObject;
	}

	public void setCarriedObject(Spatial geometry) {
		carriedObject.setCarriedObject(geometry);
	}

	public void setThrowingDestination(Spatial throwingDestination) {
		carriedObject.setThrowingDestination(throwingDestination);
	}

	public Spatial getThrowingDestination() {
		return carriedObject.getThrowingDestination();
	}

	public boolean hasThrowingDestination() {
		return getThrowingState().equals(ThrowingState.PICKING_OBJECT)
				&& carriedObject.getThrowingDestination() != null;
	}

	public void clearThrowingDestination() {
		carriedObject.setThrowingDestination(null);
	}
}
