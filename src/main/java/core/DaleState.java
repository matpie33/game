package core;

import com.jme3.bullet.control.CharacterControl;

public class DaleState {

	private boolean isMovingForward;
	private boolean isMovingBackward;
	private boolean isMovingLeft;
	private boolean isMovingRight;
	private CharacterControl characterControl;

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
}
