package core.appState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import constants.NodeNames;
import constants.PhysicsControls;
import core.GameApplication;
import core.util.CoordinatesUtil;
import dto.GameStateDTO;

public class CarriedObjectAppState extends AbstractAppState {

	private GameStateDTO gameStateDTO;
	private GameApplication gameApplication;
	private Spatial carriedObject;
	private SimpleApplication app;

	public CarriedObjectAppState(GameStateDTO gameStateDTO) {
		this.gameStateDTO = gameStateDTO;
		gameApplication = GameApplication.getInstance();
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		this.app = ((SimpleApplication) app);
		setEnabled(false);
		super.initialize(stateManager, app);
	}

	public void setCarriedObject(Spatial carriedObject) {
		this.carriedObject = carriedObject;
	}

	private void handlePuttingAside() {
		putAsideObject();
	}

	private void putAsideObject() {
		RigidBodyControl control = carriedObject.getControl(
				PhysicsControls.BOX);
		control.setKinematicSpatial(false);
		control.setKinematic(false);
		control.applyImpulse(gameApplication.getCamera()
											.getDirection()
											.mult(-20), Vector3f.ZERO);

		setEnabled(false);
		app.getStateManager()
		   .getState(MarkThrowableObjectsAppState.class)
		   .setEnabled(true);

	}

	private void handleThrowingObject() {
		Object control = carriedObject.getControl(PhysicsControls.BOX);
		PhysicsControls.BOX.cast(control)
						   .setKinematic(false);
		PhysicsControls.BOX.cast(control)
						   .setKinematicSpatial(false);
		Vector3f force = gameApplication.getCamera()
										.getDirection()
										.mult(250f);
		force.setY(force.getY() + 60f);
		PhysicsControls.BOX.cast(control)
						   .applyImpulse(force, new Vector3f(0, 0.5f, 0));
		setEnabled(false);
		app.getStateManager()
		   .getState(MarkThrowableObjectsAppState.class)
		   .setEnabled(true);

	}

	private void handleBeingCarried() {
		Spatial dale = app.getRootNode()
						  .getChild(NodeNames.getDale());
		Vector3f dalePosition = dale.getLocalTranslation();
		BoundingBox daleSize = CoordinatesUtil.getSizeOfSpatial(dale);
		BoundingBox carriedObjectSize = CoordinatesUtil.getSizeOfSpatial(
				carriedObject);
		float daleHeight = daleSize.getYExtent();
		float boxHeight = carriedObjectSize.getYExtent();
		RigidBodyControl boxControl = carriedObject.getControl(
				PhysicsControls.BOX);
		boxControl.setKinematic(true);
		boxControl.setKinematicSpatial(true);
		carriedObject.setLocalTranslation(new Vector3f(dalePosition.getX(),
				dalePosition.getY() + daleHeight + boxHeight + 1f,
				dalePosition.getZ()));
		carriedObject.setLocalRotation(
				new Quaternion().fromAngleAxis(0, new Vector3f(1, 1, 1)));
	}

	@Override
	public void update(float tpf) {
		if (gameStateDTO.getKeyPressDTO()
						.isThrowObjectPress()) {
			handleThrowingObject();
		}
		else if (gameStateDTO.getKeyPressDTO()
							 .isPutAsideObjectPress()) {
			handlePuttingAside();
		}
		else {
			handleBeingCarried();
		}
	}
}
