package dto;

public class KeyPressDTO {
	private boolean moveInLedgePress;
	private boolean letGoLedgePress;
	private boolean moveForwardPress;
	private boolean moveBackwardPress;
	private boolean moveRightPress;
	private boolean moveLeftPress;
	private boolean jumpPress;
	private boolean pickObjectPress;
	private boolean throwObjectPress;
	private boolean putAsideObjectPress;

	public boolean isPutAsideObjectPress() {
		return putAsideObjectPress;
	}

	public void setPutAsideObjectPress(boolean putAsideObjectPress) {
		this.putAsideObjectPress = putAsideObjectPress;
	}

	public boolean isMoveInLedgePress() {
		return moveInLedgePress;
	}

	public void setMoveInLedgePress(boolean moveForwardOrMoveInLedgePress) {
		this.moveInLedgePress = moveForwardOrMoveInLedgePress;
	}

	public boolean isLetGoLedgePress() {
		return letGoLedgePress;
	}

	public void setLetGoLedgePress(boolean letGoLedgePress) {
		this.letGoLedgePress = letGoLedgePress;
	}

	public boolean isMoveForwardPress() {
		return moveForwardPress;
	}

	public void setMoveForwardPress(boolean moveForwardPress) {
		this.moveForwardPress = moveForwardPress;
	}

	public boolean isMoveBackwardPress() {
		return moveBackwardPress;
	}

	public void setMoveBackwardPress(boolean moveBackwardPress) {
		this.moveBackwardPress = moveBackwardPress;
	}

	public boolean isMoveRightPress() {
		return moveRightPress;
	}

	public void setMoveRightPress(boolean moveRightPress) {
		this.moveRightPress = moveRightPress;
	}

	public boolean isMoveLeftPress() {
		return moveLeftPress;
	}

	public void setMoveLeftPress(boolean moveLeftPress) {
		this.moveLeftPress = moveLeftPress;
	}

	public boolean isJumpPress() {
		return jumpPress;
	}

	public void setJumpPress(boolean jumpPress) {
		this.jumpPress = jumpPress;
	}

	public boolean isPickObjectPress() {
		return pickObjectPress;
	}

	public void setPickObjectPress(boolean pickObjectPress) {
		this.pickObjectPress = pickObjectPress;
	}

	public boolean isThrowObjectPress() {
		return throwObjectPress;
	}

	public void setThrowObjectPress(boolean throwObjectPress) {
		this.throwObjectPress = throwObjectPress;
	}
}
