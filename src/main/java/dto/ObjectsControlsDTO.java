package dto;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ObjectsControlsDTO {

	private List<RigidBodyControl> treesControls = new ArrayList<>();

	private CharacterControl daleControl;

	public List<RigidBodyControl> getTreesControls() {
		return Collections.unmodifiableList(treesControls);
	}


	public CharacterControl getDaleControl() {
		return daleControl;
	}

	public void setDaleControl(CharacterControl daleControl) {
		this.daleControl = daleControl;
	}

	public void addTreeControl(RigidBodyControl rigidBodyControl) {
		treesControls.add(rigidBodyControl);
	}
}
