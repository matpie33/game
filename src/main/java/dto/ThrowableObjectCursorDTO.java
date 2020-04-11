package dto;

import com.jme3.scene.Geometry;

public class ThrowableObjectCursorDTO {

	private boolean isShowing;
	private Geometry throwableObject;

	public boolean isShowing() {
		return isShowing;
	}

	public void setShowing(boolean showing) {
		isShowing = showing;
	}

	public Geometry getThrowableObject() {
		return throwableObject;
	}

	public void setThrowableObject(Geometry throwableObject) {
		this.throwableObject = throwableObject;
	}
}
