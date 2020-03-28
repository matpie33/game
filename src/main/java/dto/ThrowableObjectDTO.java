package dto;

import com.jme3.scene.Geometry;

public class ThrowableObjectDTO {

	private boolean isCarried;
	private Geometry object;

	public boolean isCarried() {
		return isCarried;
	}

	public void setCarried(boolean carried) {
		isCarried = carried;
	}

	public Geometry getObject() {
		return object;
	}

	public void setObject(Geometry object) {
		this.object = object;
	}
}
