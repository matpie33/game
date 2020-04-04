package enums;

import java.util.Arrays;

public enum MovementDirection {

	FORWARD_X(1), FORWARD_Z(2), BACKWARD_X(3), BACKWARD_Z(4);

	private int value;

	MovementDirection(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static MovementDirection fromInt(int newDirection) {
		return Arrays.stream(values())
					 .filter(e -> e.getValue() == newDirection)
					 .findFirst()
					 .orElseThrow(() -> MovementDirection.getException(
							 newDirection));
	}

	private static IllegalArgumentException getException(int value) {
		return new IllegalArgumentException(
				"Incorrect movement direction " + "value: " + value);
	}
}
