package core.controls;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import constants.NodeNames;
import constants.PhysicsControls;
import core.GameApplication;
import core.controllers.AnimationController;
import dto.DaleStateDTO;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

public class CarriedObjectControl extends AbstractControl {

	private GameStateDTO gameStateDTO;
	private ObjectsHolderDTO objectsHolderDTO;
	private GameApplication gameApplication;
	private AnimationController animationController;
	public static final int MINIMAL_DISTANCE_TO_PICK_OBJECT = 5;

	public CarriedObjectControl(GameStateDTO gameStateDTO,
			ObjectsHolderDTO objectsHolderDTO, AnimationController animationController) {
		this.gameStateDTO = gameStateDTO;
		this.objectsHolderDTO = objectsHolderDTO;
		gameApplication = GameApplication.getInstance();
		this.animationController = animationController;
	}

	@Override
	protected void controlUpdate(float tpf) {
		handleBeingCarried();
		handlePicking();
		handlePuttingAside();
		handleThrowingObject();

	}

	private void handlePuttingAside() {
		if (gameStateDTO.getDaleStateDTO().isPuttingAsideObject()) {
			putAsideObject();
		}
	}

	private void putAsideObject() {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		daleStateDTO.setCarryingThrowableObject(false);
		RigidBodyControl control = daleStateDTO.getCarriedObject()
											   .getObject()
											   .getParent()
											   .getControl(PhysicsControls.BOX);
		control.setKinematicSpatial(false);
		control.setKinematic(false);
		control.applyCentralForce(gameApplication.getCamera()
												 .getDirection()
												 .mult(-0.5f));

		animationController.animateStanding();
	}

	private CollisionResults getDistanceToObjects() {
		Spatial dale = objectsHolderDTO.getDale();
		Ray ray = new Ray(dale.getControl(PhysicsControls.DALE)
							  .getPhysicsLocation(),
				dale.getControl(PhysicsControls.DALE)
					.getViewDirection());
		CollisionResults collisionResults = new CollisionResults();
		Spatial throwables = gameApplication.getRootNode()
											.getChild(NodeNames.THROWABLES);
		for (Spatial spatial : ((Node) throwables).getChildren()) {
			spatial.collideWith(ray, collisionResults);
		}
		if (collisionResults.size() == 0) {
			return null;
		}
		return collisionResults;
	}

	private boolean isCloseToThrowableObject() {

		CollisionResults collisionResults = getDistanceToObjects();
		return isCloseEnoughToAnyObject(collisionResults);
	}

	private boolean isCloseEnoughToAnyObject(
			CollisionResults collisionResults) {
		if (collisionResults == null)
			return false;
		CollisionResult closestCollision = collisionResults.getClosestCollision();
		return collisionResults.size() > 0 && closestCollision.getDistance()
				< MINIMAL_DISTANCE_TO_PICK_OBJECT;

	}

	private void handleThrowingObject() {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		if (daleStateDTO.isThrowingObject()) {
			daleStateDTO.setCarryingThrowableObject(false);
			Object control = daleStateDTO.getCarriedObject()
										 .getObject()
										 .getParent()
										 .getControl(PhysicsControls.BOX);
			PhysicsControls.BOX.cast(control)
							   .setKinematic(false);
			PhysicsControls.BOX.cast(control)
							   .setKinematicSpatial(false);
			PhysicsControls.BOX.cast(control)
							   .setLinearVelocity(new Vector3f(
									   gameApplication.getCamera()
													  .getDirection()
													  .mult(80f)));
			animationController.animateStanding();
		}

	}

	private void handlePicking() {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		if (!daleStateDTO.isPickingObject()){
			return;
		}
		CollisionResults collisionResults = getDistanceToObjects();
		if (isCloseToThrowableObject()) {
			Geometry geometry = collisionResults.getClosestCollision()
												.getGeometry();
			daleStateDTO.setCarryingThrowableObject(true);
			daleStateDTO.setCarriedObject(geometry);
			hideCursor();
			animationController.animateHoldingObject();
		}
	}

	private void hideCursor() {
		gameStateDTO.setCursorNotShowing();
		gameApplication.getRootNode()
					   .detachChild(objectsHolderDTO.getArrow());
	}

	public void handleBeingCarried() {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		if (!daleStateDTO.isCarryingThrowableObject()) {
			return;
		}
		Geometry carriedObject = daleStateDTO.getCarriedObject()
											 .getObject();
		Vector3f dalePosition = objectsHolderDTO.getDale()
												.getLocalTranslation();
		float carriedObjectHeight = ((BoundingBox) objectsHolderDTO.getDale()
																   .getWorldBound()).getYExtent();
		float boxHeight = ((BoundingBox) carriedObject.getWorldBound()).getYExtent();
		RigidBodyControl control = carriedObject.getParent()
												.getControl(
														PhysicsControls.BOX);
		control.setKinematic(true);
		control.setKinematicSpatial(true);
		((Node) control.getUserObject()).setLocalTranslation(
				new Vector3f(dalePosition.getX(),
						dalePosition.getY() + carriedObjectHeight + boxHeight
								+ 1f, dalePosition.getZ()));
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}
}
