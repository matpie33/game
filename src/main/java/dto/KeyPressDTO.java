package dto;

public class KeyPressDTO {
	private boolean moveForwardOrMoveInLedgePress;
	private boolean letGoLedgePress;

	public boolean isLetGoLedgePress() {
		return letGoLedgePress;
	}

	public void setLetGoLedgePress(boolean letGoLedgePress) {
		this.letGoLedgePress = letGoLedgePress;
	}

	public boolean isMoveForwardOrMoveInLedgePress() {
		return moveForwardOrMoveInLedgePress;
	}

	public void setMoveForwardOrMoveInLedgePress(
			boolean moveForwardOrMoveInLedgePress) {
		this.moveForwardOrMoveInLedgePress = moveForwardOrMoveInLedgePress;
	}
}
