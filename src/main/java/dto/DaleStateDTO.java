package dto;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import enums.ClimbingState;

public class DaleStateDTO {

	private boolean alive;
	private ThrowingDTO carriedObject = new ThrowingDTO();
	private int hp;
	private boolean isThrowingObject;
	private boolean isPickingObject;
	private boolean isPuttingAsideObject;
	private boolean isCollidingWithEnemy;
	private Vector3f ledgeCollisionPoint;
	private ClimbingState climbingState = ClimbingState.NOT_STARTED;

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

	public boolean isPickingObject() {
		return isPickingObject;
	}

	public void setPickingObject(boolean pickingObject) {
		isPickingObject = pickingObject;
	}

	public boolean isPuttingAsideObject() {
		return isPuttingAsideObject;
	}

	public void setPuttingAsideObject(boolean puttingAsideObject) {
		isPuttingAsideObject = puttingAsideObject;
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

	public boolean isCarryingThrowableObject() {
		return carriedObject.isCarried();
	}

	public void setCarryingThrowableObject(boolean carrying) {
		carriedObject.setCarried(carrying);
	}

	public void setCarriedObject(Spatial geometry) {
		carriedObject.setCarriedObject(geometry);
	}

	public void setThrowingObject(boolean throwingObject) {
		this.isThrowingObject = throwingObject;
	}

	public boolean isThrowingObject() {
		return isThrowingObject;
	}

	public void setThrowingDestination(Spatial throwingDestination) {
		carriedObject.setThrowingDestination(throwingDestination);
	}

	public Spatial getThrowingDestination() {
		return carriedObject.getThrowingDestination();
	}

	public boolean hasThrowingDestination() {
		return isCarryingThrowableObject()
				&& carriedObject.getThrowingDestination() != null;
	}

	public void clearThrowingDestination() {
		carriedObject.setThrowingDestination(null);
	}
}
