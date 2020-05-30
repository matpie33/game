package core.controls;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import core.util.CoordinatesUtil;

public class ThrowableObjectMarkerControl extends AbstractControl {

	private Spatial throwableObject;

	public void setThrowableObject(Spatial throwableObject) {
		this.throwableObject = throwableObject;
	}

	@Override
	protected void controlUpdate(float tpf) {
		BoundingBox throwableObjectSize = CoordinatesUtil.getSizeOfSpatial(
				throwableObject);
		float yDimensionArrow = CoordinatesUtil.getSizeOfSpatial(spatial)
											   .getYExtent();
		float yDimensionCollisionObject = throwableObjectSize.getYExtent();
		Vector3f throwableObjectPosition = throwableObject.getWorldTranslation();
		spatial.setLocalTranslation(throwableObjectPosition.getX(),
				throwableObjectPosition.getY() + yDimensionArrow
						+ yDimensionCollisionObject,
				throwableObjectPosition.getZ());
		spatial.rotate(0, 2 * tpf, 0);
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}
}
