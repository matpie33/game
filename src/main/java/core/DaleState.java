package core;

import com.jme3.scene.Geometry;
import dto.ThrowableObjectDTO;

public class DaleState {

	private boolean isMovingForward;
	private boolean isMovingBackward;
	private boolean isMovingLeft;
	private boolean isMovingRight;
	private ThrowableObjectDTO carriedObject = new ThrowableObjectDTO();

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
}
