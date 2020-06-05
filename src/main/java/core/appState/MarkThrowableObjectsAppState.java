package core.appState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import constants.NodeNames;
import constants.PhysicsControls;
import core.controls.ThrowableObjectMarkerControl;
import dto.GameStateDTO;
import dto.KeyPressDTO;
import dto.ObjectsHolderDTO;

public class MarkThrowableObjectsAppState extends AbstractAppState {

	private final ObjectsHolderDTO objectsHolderDTO;
	private Application app;
	public static final int MINIMAL_DISTANCE_TO_PICK_OBJECT = 5;
	private ThrowableObjectMarkerControl throwableObjectMarkerControl;
	private GameStateDTO gameStateDTO;

	public MarkThrowableObjectsAppState(GameStateDTO gameStateDTO,
			ObjectsHolderDTO objectsHolderDTO) {
		this.gameStateDTO = gameStateDTO;
		this.objectsHolderDTO = objectsHolderDTO;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		this.app = app;
		throwableObjectMarkerControl = new ThrowableObjectMarkerControl();
		super.initialize(stateManager, app);
	}

	@Override
	public void update(float tpf) {

		markThrowableObject();
		super.update(tpf);
	}

	private void markThrowableObject() {
		SimpleApplication app = (SimpleApplication) this.app;
		Node rootNode = app.getRootNode();

		KeyPressDTO keyPressDTO = gameStateDTO.getKeyPressDTO();
		CollisionResults collisionResults = getDistanceToObjects();
		CollisionResult closestCollision = collisionResults.getClosestCollision();

		Spatial throwableCursor = objectsHolderDTO.getThrowableObjectMarker();
		if (isCloseEnoughToAnyObject(collisionResults)) {
			if (keyPressDTO.isPickObjectPress()) {

				CarriedObjectAppState carriedObjectAppState = app.getStateManager()
																 .getState(
																		 CarriedObjectAppState.class);
				carriedObjectAppState.setCarriedObject(
						closestCollision.getGeometry()
										.getParent());
				carriedObjectAppState.setEnabled(true);
				setEnabled(false);
				app.getRootNode()
				   .detachChild(objectsHolderDTO.getThrowableObjectMarker());
			}
			else {
				if (throwableCursor.getControl(
						ThrowableObjectMarkerControl.class) == null) {
					app.getRootNode()
					   .attachChild(
							   objectsHolderDTO.getThrowableObjectMarker());
					throwableObjectMarkerControl.setThrowableObject(
							closestCollision.getGeometry());
					throwableCursor.addControl(throwableObjectMarkerControl);
				}

			}

		}

		if ((collisionResults.size() == 0 || closestCollision.getDistance()
				> MINIMAL_DISTANCE_TO_PICK_OBJECT)) {
			throwableCursor.removeControl(throwableObjectMarkerControl);
			rootNode.detachChild(objectsHolderDTO.getThrowableObjectMarker());
		}

	}

	private boolean isCloseEnoughToAnyObject(
			CollisionResults collisionResults) {
		if (collisionResults == null)
			return false;
		CollisionResult closestCollision = collisionResults.getClosestCollision();
		return collisionResults.size() > 0 && closestCollision.getDistance()
				< MINIMAL_DISTANCE_TO_PICK_OBJECT;

	}

	private CollisionResults getDistanceToObjects() {
		Node rootNode = ((SimpleApplication) app).getRootNode();
		Spatial dale = objectsHolderDTO.getDale();
		Ray ray = new Ray(dale.getControl(PhysicsControls.DALE)
							  .getPhysicsLocation(),
				dale.getControl(PhysicsControls.DALE)
					.getViewDirection());
		CollisionResults collisionResults = new CollisionResults();
		Spatial throwables = rootNode.getChild(NodeNames.THROWABLES);
		for (Spatial spatial : ((Node) throwables).getChildren()) {
			spatial.collideWith(ray, collisionResults);
		}
		return collisionResults;
	}

}
