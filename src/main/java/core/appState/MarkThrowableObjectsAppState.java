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

public class MarkThrowableObjectsAppState extends AbstractAppState {

	private SimpleApplication app;
	public static final int MINIMAL_DISTANCE_TO_PICK_OBJECT = 5;
	private GameStateDTO gameStateDTO;

	public MarkThrowableObjectsAppState(GameStateDTO gameStateDTO) {
		this.gameStateDTO = gameStateDTO;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		this.app = (SimpleApplication) app;
		super.initialize(stateManager, app);
	}

	@Override
	public void update(float tpf) {

		markThrowableObject();
		super.update(tpf);
	}

	private void markThrowableObject() {
		Node rootNode = app.getRootNode();

		KeyPressDTO keyPressDTO = gameStateDTO.getKeyPressDTO();
		CollisionResults collisionResults = getDistanceToObjects();
		CollisionResult closestCollision = collisionResults.getClosestCollision();

		Spatial throwableCursor = rootNode.getChild(NodeNames.getArrow());
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
				throwableCursor.setCullHint(Spatial.CullHint.Always);
			}
			else {
				throwableCursor.setCullHint(Spatial.CullHint.Never);
				throwableCursor.getControl(ThrowableObjectMarkerControl.class)
							   .setThrowableObject(
									   closestCollision.getGeometry());

			}

		}

		if ((collisionResults.size() == 0 || closestCollision.getDistance()
				> MINIMAL_DISTANCE_TO_PICK_OBJECT)) {
			throwableCursor.setCullHint(Spatial.CullHint.Always);
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
		Node rootNode = app.getRootNode();
		Spatial dale = rootNode.getChild(NodeNames.getDale());
		Ray ray = new Ray(dale.getControl(PhysicsControls.DALE)
							  .getPhysicsLocation(),
				dale.getControl(PhysicsControls.DALE)
					.getViewDirection());
		CollisionResults collisionResults = new CollisionResults();
		Spatial throwables = rootNode.getChild(NodeNames.getThrowables());
		for (Spatial spatial : ((Node) throwables).getChildren()) {
			spatial.collideWith(ray, collisionResults);
		}
		return collisionResults;
	}

}
