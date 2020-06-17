package dto;

import com.jme3.scene.Spatial;
import enums.ThrowingState;

public class DaleStateDTO {

	private ThrowingDTO carriedObject = new ThrowingDTO();
	private ThrowingState throwingState = ThrowingState.NOT_STARTED;

	public ThrowingState getThrowingState() {
		return throwingState;
	}

	public void setThrowingDestination(Spatial throwingDestination) {
		carriedObject.setThrowingDestination(throwingDestination);
	}

	public Spatial getThrowingDestination() {
		return carriedObject.getThrowingDestination();
	}

	public boolean hasThrowingDestination() {
		return getThrowingState().equals(ThrowingState.PICKING_OBJECT)
				&& carriedObject.getThrowingDestination() != null;
	}

	public void clearThrowingDestination() {
		carriedObject.setThrowingDestination(null);
	}
}
