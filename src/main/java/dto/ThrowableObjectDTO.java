package dto;

import com.jme3.scene.Spatial;

public class ThrowableObjectDTO {

	private boolean isCarried;
	private Spatial object;

	public boolean isCarried() {
		return isCarried;
	}

	public void setCarried(boolean carried) {
		isCarried = carried;
	}

	public Spatial getObject() {
		return object;
	}

	public void setObject(Spatial object) {
		this.object = object;
	}
}
