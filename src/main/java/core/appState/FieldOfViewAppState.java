package core.appState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.material.MatParam;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import constants.PhysicsControls;
import core.GameApplication;
import dto.GameStateDTO;
import dto.NodeNamesDTO;

import java.util.ArrayList;
import java.util.List;

public class FieldOfViewAppState extends AbstractAppState {

	public static final String DIFFUSE_PARAM = "Diffuse";
	private NodeNamesDTO nodeNamesDTO;
	private Geometry spatialPreviouslyMarkedAsThrowingDestination;
	private ColorRGBA previousColorOfThrowingDestination;
	private GameStateDTO gameStateDTO;
	private List<Spatial> enemiesSeeingDale = new ArrayList<>();
	private SimpleApplication app;
	public static final float OFFSET_FROM_DALE_TO_FIELD_OF_VIEW = 3F;

	public FieldOfViewAppState(NodeNamesDTO nodeNamesDTO,
			GameStateDTO gameStateDTO) {
		this.nodeNamesDTO = nodeNamesDTO;
		this.gameStateDTO = gameStateDTO;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		this.app = ((SimpleApplication) app);
		super.initialize(stateManager, app);
	}

	@Override
	public void update(float tpf) {
		List<Spatial> enemiesSeeingDaleInThisUpdate = handleThrowingFieldOfView();
		enemiesSeeingDale.forEach(
				enemy -> findDogAndSetSeeingDale(enemy, false));
		enemiesSeeingDale = enemiesSeeingDaleInThisUpdate;
		moveFieldOfView();
	}

	private void moveFieldOfView() {
		Spatial dale = app.getRootNode()
						  .getChild(nodeNamesDTO.getDaleNodeName());
		CharacterControl control = dale.getControl(PhysicsControls.DALE);
		Spatial fieldOfView = GameApplication.getInstance()
											 .getRootNode()
											 .getChild(
													 nodeNamesDTO.getFieldOfViewNodeName());
		float fieldOfViewRadius = ((SphereCollisionShape) fieldOfView.getControl(
				GhostControl.class)
																	 .getCollisionShape()).getRadius();
		fieldOfView.setLocalTranslation(dale.getWorldTranslation()
											.add(control.getViewDirection()
														.mult(fieldOfViewRadius
																+ OFFSET_FROM_DALE_TO_FIELD_OF_VIEW)));
	}

	private void findDogAndSetSeeingDale(Spatial enemy, boolean seeingDale) {
		gameStateDTO.getDogStateDTOS()
					.stream()
					.filter(dog -> dog.getDog()
									  .equals(enemy))
					.findFirst()
					.ifPresent(dog -> dog.setSeeingDale(seeingDale));
	}

	private List<Spatial> handleThrowingFieldOfView() {
		boolean containsAnyThrowingDestination = false;
		List<Spatial> enemiesSeeingDaleInThisUpdate = new ArrayList<>();
		Spatial fieldOfView = app.getRootNode()
								 .getChild(
										 nodeNamesDTO.getFieldOfViewNodeName());
		for (PhysicsCollisionObject physicsCollisionObject : fieldOfView.getControl(
				GhostControl.class)
																		.getOverlappingObjects()) {
			Node collidingObject = (Node) physicsCollisionObject.getUserObject();
			if (nodeNamesDTO.getDogNodeName()
							.equals(collidingObject.getName())) {
				enemiesSeeingDaleInThisUpdate.add(collidingObject);
				this.enemiesSeeingDale.remove(collidingObject);
				if (app.getStateManager()
					   .getState(CarriedObjectAppState.class)
					   .isEnabled()) {
					containsAnyThrowingDestination = true;
					handleThrowingDestination(collidingObject);
				}
				findDogAndSetSeeingDale(collidingObject, true);

			}
		}
		if (!containsAnyThrowingDestination) {
			resetThrowingDestination();
		}
		else {
			ThrowingDestinationFollowingCameraAppState cameraAppState = app.getStateManager()
																		   .getState(
																				   ThrowingDestinationFollowingCameraAppState.class);
			cameraAppState.setEnabled(true);
			cameraAppState.setThrowingDestination(
					enemiesSeeingDaleInThisUpdate.get(0));
		}
		return enemiesSeeingDaleInThisUpdate;
	}

	private void handleThrowingDestination(Node collidingObject) {
		changePreviouslyMarkedTargetToItsColor();

		Geometry geometry = (Geometry) (collidingObject).getChild(0);
		markObjectAsCurrentThrowingDestination(geometry);
		setColor(geometry, ColorRGBA.Red);
	}

	private void markObjectAsCurrentThrowingDestination(Geometry geometry) {
		spatialPreviouslyMarkedAsThrowingDestination = geometry;
		MatParam previousColor = spatialPreviouslyMarkedAsThrowingDestination.getMaterial()
																			 .getParam(
																					 DIFFUSE_PARAM);
		this.previousColorOfThrowingDestination = (ColorRGBA) previousColor.getValue();
	}

	private void setColor(Geometry geometry, ColorRGBA color) {
		geometry.getMaterial()
				.setColor(DIFFUSE_PARAM, color);
	}

	private void resetThrowingDestination() {
		changePreviouslyMarkedTargetToItsColor();
		app.getStateManager()
		   .getState(ThrowingDestinationFollowingCameraAppState.class)
		   .setEnabled(false);
		spatialPreviouslyMarkedAsThrowingDestination = null;
	}

	private void changePreviouslyMarkedTargetToItsColor() {
		boolean hasPreviouslyMarkedDestination =
				spatialPreviouslyMarkedAsThrowingDestination != null;
		if (hasPreviouslyMarkedDestination) {
			setColor(spatialPreviouslyMarkedAsThrowingDestination,
					previousColorOfThrowingDestination);
		}
	}

}
