package core.appState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import core.controls.ThrowableObjectMarkerControl;
import dto.ObjectsHolderDTO;

public class ThrowableObjectInRangeAppState extends BaseAppState {

	private Spatial throwableObject;
	private ObjectsHolderDTO objectsHolderDTO;
	private SimpleApplication app;
	private Spatial throwableObjectMarker;

	public ThrowableObjectInRangeAppState(ObjectsHolderDTO objectsHolderDTO) {
		this.objectsHolderDTO = objectsHolderDTO;
	}

	@Override
	protected void onEnable() {
		this.app.getRootNode()
				.attachChild(throwableObjectMarker);
	}

	@Override
	protected void onDisable() {
		this.app.getRootNode()
				.detachChild(objectsHolderDTO.getThrowableObjectMarker());
	}

	@Override
	public void initialize(Application app) {
		throwableObjectMarker = objectsHolderDTO.getThrowableObjectMarker();
		throwableObjectMarker.addControl(new ThrowableObjectMarkerControl());
		this.app = ((SimpleApplication) app);
	}

	@Override
	public void cleanup(Application app) {

	}

	public void setThrowableObject(Geometry geometry) {
		ThrowableObjectMarkerControl control = throwableObjectMarker.getControl(
				ThrowableObjectMarkerControl.class);
		control.setThrowableObject(geometry);
	}
}

