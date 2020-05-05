package dto;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class DaleStateDTO {

	private boolean isMovingForward;
	private boolean isMovingBackward;
	private boolean isMovingLeft;
	private boolean isMovingRight;
	private boolean alive;
	private ThrowingDTO carriedObject = new ThrowingDTO();
	private int hp;
	private boolean isJumping;
	private boolean isThrowingObject;
	private boolean isPickingObject;
	private boolean isPuttingAsideObject;
	private boolean isCollidingWithEnemy;
	private boolean isGrabbingLedge;
	private boolean isLetGoLedge;
	private boolean moveInLedge;
	private Vector3f ledgeCollisionPoint;

	public Vector3f getLedgeCollisionPoint() {
		return ledgeCollisionPoint;
	}

	public void setLedgeCollisionPoint(Vector3f ledgeCollisionPoint) {
		this.ledgeCollisionPoint = ledgeCollisionPoint;
	}

	public boolean isMoveInLedge() {
		return moveInLedge;
	}

	public void setMoveInLedge(boolean moveInLedge) {
		this.moveInLedge = moveInLedge;
	}

	public boolean isLetGoLedge() {
		return isLetGoLedge;
	}

	public void setLetGoLedge(boolean letGoLedge) {
		isLetGoLedge = letGoLedge;
	}

	public boolean isGrabbingLedge() {
		return isGrabbingLedge;
	}

	public void setGrabbingLedge(boolean grabbingLedge) {
		isGrabbingLedge = grabbingLedge;
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

	public boolean isMovingForward() {
		return isMovingForward;
	}

	public void setMovingForward(boolean movingForward) {
		isMovingForward = movingForward;
	}

	public boolean isMovingBackward() {
		return isMovingBackward;
	}

	public void setMovingBackward(boolean movingBackward) {
		isMovingBackward = movingBackward;
	}

	public boolean isMovingLeft() {
		return isMovingLeft;
	}

	public boolean isMovingRight() {
		return isMovingRight;
	}

	public void setMovingLeft(boolean movingLeft) {
		isMovingLeft = movingLeft;
	}

	public void setMovingRight(boolean movingRight) {
		isMovingRight = movingRight;
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

	public boolean isJumping() {
		return isJumping;
	}

	public void setJumping(boolean jumping) {
		isJumping = jumping;
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
