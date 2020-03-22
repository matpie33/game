package core;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class ObjectPositionRotationHandler {

	public void moveObjectForward(Spatial object) {
		object.move(new Vector3f(0, 0, 1));
	}

}
