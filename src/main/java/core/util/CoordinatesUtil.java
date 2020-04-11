package core.util;

import com.jme3.bounding.BoundingBox;
import com.jme3.scene.Spatial;

public class CoordinatesUtil {

	public static BoundingBox getSizeOfSpatial(Spatial spatial){
		return ((BoundingBox) spatial.getWorldBound());
	}

}
