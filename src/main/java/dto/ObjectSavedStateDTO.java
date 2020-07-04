package dto;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("objectState")
public class ObjectSavedStateDTO {

	@XStreamAlias("position")
	private Vector3f position;
	@XStreamAlias("rotation")
	private Quaternion rotation;
	@XStreamAlias("pathToModel")
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
