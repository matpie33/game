package dto;

import com.jme3.scene.Spatial;
import enums.ClimbingState;
import enums.ThrowingState;

public class DaleStateDTO {

	private ThrowingDTO carriedObject = new ThrowingDTO();
	private ClimbingState climbingState = ClimbingState.NOT_STARTED;
	private ThrowingState throwingState = ThrowingState.NOT_STARTED;

	public ThrowingState getThrowingState() {
		return throwingState;
	}

	public ClimbingState getClimbingState() {
		return climbingState;
	}

	public void setClimbingState(ClimbingState climbingState) {
		this.climbingState = climbingState;
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
