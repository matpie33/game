package enums;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;

import java.io.IOException;
import java.util.Arrays;

public enum MovementDirection {

	FORWARD_X(1, 1), FORWARD_Z(2, 1), BACKWARD_X(3, -1), BACKWARD_Z(4, -1);

	private int codeValue;
	private int directionModifier;

	MovementDirection(int codeValue, int directionModifier) {
		this.codeValue = codeValue;
		this.directionModifier = directionModifier;
	}

	public int getCodeValue() {
		return codeValue;
	}

	public int getDirectionModifier() {
		return directionModifier;
	}

	public MovementDirection getCounterDirection() {
		switch (this) {
		case FORWARD_X:
			return BACKWARD_X;
		case BACKWARD_X:
			return FORWARD_X;
		case BACKWARD_Z:
			return FORWARD_Z;
		case FORWARD_Z:
			return BACKWARD_Z;
		default:
			throw getException(codeValue);
		}

	}

	public static MovementDirection fromInt(int newDirection) {
		return Arrays.stream(values())
					 .filter(e -> e.getCodeValue() == newDirection)
					 .findFirst()
					 .orElseThrow(() -> MovementDirection.getException(
							 newDirection));
	}

	private static IllegalArgumentException getException(int value) {
		return new IllegalArgumentException(
				"Incorrect movement direction " + "codeValue: " + value);
	}

}
