package dto;

import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.texture.Texture;

import java.util.ArrayList;
import java.util.List;

public class NodeNamesDTO {

	private String treeNodeName;
	private String boxNodeName;
	private String dogNodeName;
	private String daleNodeName;
	private String sceneNodeName;
	private String markNodeName;
	private String throwableObjectMarkerNodeName;
	private String skyNodeName;
	private String fieldOfViewNodeName;

	public String getTreeNodeName() {
		return treeNodeName;
	}

	public void setTreeNodeName(String treeNodeName) {
		this.treeNodeName = treeNodeName;
	}

	public String getBoxNodeName() {
		return boxNodeName;
	}

	public void setBoxNodeName(String boxNodeName) {
		this.boxNodeName = boxNodeName;
	}

	public String getDogNodeName() {
		return dogNodeName;
	}

	public void setDogNodeName(String dogNodeName) {
		this.dogNodeName = dogNodeName;
	}

	public String getDaleNodeName() {
		return daleNodeName;
	}

	public void setDaleNodeName(String daleNodeName) {
		this.daleNodeName = daleNodeName;
	}

	public String getSceneNodeName() {
		return sceneNodeName;
	}

	public void setSceneNodeName(String sceneNodeName) {
		this.sceneNodeName = sceneNodeName;
	}

	public String getMarkNodeName() {
		return markNodeName;
	}

	public void setMarkNodeName(String markNodeName) {
		this.markNodeName = markNodeName;
	}

	public String getThrowableObjectMarkerNodeName() {
		return throwableObjectMarkerNodeName;
	}

	public void setThrowableObjectMarkerNodeName(
			String throwableObjectMarkerNodeName) {
		this.throwableObjectMarkerNodeName = throwableObjectMarkerNodeName;
	}

	public String getSkyNodeName() {
		return skyNodeName;
	}

	public void setSkyNodeName(String skyNodeName) {
		this.skyNodeName = skyNodeName;
	}

	public String getFieldOfViewNodeName() {
		return fieldOfViewNodeName;
	}

	public void setFieldOfViewNodeName(String fieldOfViewNodeName) {
		this.fieldOfViewNodeName = fieldOfViewNodeName;
	}
}
