package dto;

import com.jme3.scene.Spatial;

public class ThrowingDTO {

	private Spatial carriedObject;
	private Spatial throwingDestination;

	public Spatial getThrowingDestination() {
		return throwingDestination;
	}

	public void setThrowingDestination(Spatial throwingDestination) {
		this.throwingDestination = throwingDestination;
	}

	public Spatial getCarriedObject() {
		return carriedObject;
	}

	public void setCarriedObject(Spatial carriedObject) {
		this.carriedObject = carriedObject;
	}
}
