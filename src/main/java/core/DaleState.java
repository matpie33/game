package core;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.scene.Geometry;
import dto.ThrowableObjectDTO;

public class DaleState {

	private boolean isMovingForward;
	private boolean isMovingBackward;
	private boolean isMovingLeft;
	private boolean isMovingRight;
	private CharacterControl characterControl;
	private ThrowableObjectDTO throwableObjectDTO = new ThrowableObjectDTO();

	public ThrowableObjectDTO getThrowableObjectDTO() {
		return throwableObjectDTO;
	}

	public void setThrowableObjectDTO(ThrowableObjectDTO throwableObjectDTO) {
		this.throwableObjectDTO = throwableObjectDTO;
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

	public boolean isMovingRight() {
		return isMovingRight;
	}

	public void setMovingRight(boolean movingRight) {
		isMovingRight = movingRight;
	}

	public void setCharacterControl(CharacterControl characterControl) {
		this.characterControl = characterControl;
	}

	public CharacterControl getCharacterControl() {
		return characterControl;
	}

	public boolean isCarryingThrowableObject() {
		return throwableObjectDTO.isCarried();
	}

	public void setCarryingThrowableObject(boolean carrying) {
		throwableObjectDTO.setCarried(carrying);
	}

	public void setCarriedObject(Geometry geometry) {
		throwableObjectDTO.setObject(geometry);
	}
}
