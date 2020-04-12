package dto;

import com.jme3.scene.Spatial;

public class ThrowingDTO {

	private boolean isCarried;
	private Spatial carriedObject;
	private Spatial throwingDestination;

	public Spatial getThrowingDestination() {
		return throwingDestination;
	}

	public void setThrowingDestination(Spatial throwingDestination) {
		this.throwingDestination = throwingDestination;
	}

	public boolean isCarried() {
		return isCarried;
	}

	public void setCarried(boolean carried) {
		isCarried = carried;
	}

	public Spatial getCarriedObject() {
		return carriedObject;
	}

	public void setCarriedObject(Spatial carriedObject) {
		this.carriedObject = carriedObject;
	}
}
