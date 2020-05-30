package dto;

public class KeyPressDTO {
	private boolean moveForwardOrMoveInLedgePress;
	private boolean letGoLedgePress;
	private boolean moveForwardPress;
	private boolean moveBackwardPress;
	private boolean moveRightPress;
	private boolean moveLeftPress;
	private boolean jumpPress;

	public boolean isJumpPress() {
		return jumpPress;
	}

	public void setJumpPress(boolean jumpPress) {
		this.jumpPress = jumpPress;
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

	public boolean isLetGoLedgePress() {
		return letGoLedgePress;
	}

	public void setLetGoLedgePress(boolean letGoLedgePress) {
		this.letGoLedgePress = letGoLedgePress;
	}

	public boolean isMoveForwardOrMoveInLedgePress() {
		return moveForwardOrMoveInLedgePress;
	}

	public void setMoveInLedgePress(
			boolean moveForwardOrMoveInLedgePress) {
		this.moveForwardOrMoveInLedgePress = moveForwardOrMoveInLedgePress;
	}
}
