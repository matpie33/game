package dto;

import com.jme3.scene.Geometry;
import dto.ThrowableObjectDTO;

public class DaleStateDTO {

	private boolean isMovingForward;
	private boolean isMovingBackward;
	private boolean isMovingLeft;
	private boolean isMovingRight;
	private boolean alive;
	private ThrowableObjectDTO carriedObject = new ThrowableObjectDTO();
	private int hp;
	private boolean isJumping;

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

	public ThrowableObjectDTO getCarriedObject() {
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

	public void setCarriedObject(Geometry geometry) {
		carriedObject.setObject(geometry);
	}

	public boolean isJumping() {
		return isJumping;
	}

	public void setJumping(boolean jumping) {
		isJumping = jumping;
	}
}
