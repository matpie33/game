package dto;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class ObjectSavedStateDTO {

	private Vector3f position;
	private Quaternion rotation;
	private String pathToModel;

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}

	public String getPathToModel() {
		return pathToModel;
	}

	public void setPathToModel(String pathToModel) {
		this.pathToModel = pathToModel;
	}
}
