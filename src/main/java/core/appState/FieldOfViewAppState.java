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
import constants.NodeNames;
import constants.PhysicsControls;
import core.GameApplication;
import dto.GameStateDTO;

import java.util.HashSet;
import java.util.Set;

public class FieldOfViewAppState extends AbstractAppState {

	public static final String DIFFUSE_PARAM = "Diffuse";
	private Geometry spatialPreviouslyMarkedAsThrowingDestination;
	private ColorRGBA previousColorOfThrowingDestination;
	private GameStateDTO gameStateDTO;
	private Set<Spatial> enemiesSeeingDale = new HashSet<>();
	private SimpleApplication app;
	public static final float OFFSET_FROM_DALE_TO_FIELD_OF_VIEW = 3F;

	public FieldOfViewAppState(GameStateDTO gameStateDTO) {
		this.gameStateDTO = gameStateDTO;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		this.app = ((SimpleApplication) app);
		super.initialize(stateManager, app);
	}

	@Override
	public void update(float tpf) {
		Set<Spatial> enemiesSeeingDaleInThisUpdate = handleThrowingFieldOfView();
		enemiesSeeingDale.forEach(
				enemy -> findDogAndSetSeeingDale(enemy, false));
		enemiesSeeingDale = enemiesSeeingDaleInThisUpdate;
		moveFieldOfView();
	}

	private void moveFieldOfView() {
		Spatial dale = app.getRootNode()
						  .getChild(NodeNames.getDale());
		CharacterControl control = dale.getControl(PhysicsControls.DALE);
		Spatial fieldOfView = GameApplication.getInstance()
											 .getRootNode()
											 .getChild(
													 NodeNames.getFieldOfView());
		float fieldOfViewRadius = ((SphereCollisionShape) fieldOfView.getControl(
				GhostControl.class)
																	 .getCollisionShape()).getRadius();
		fieldOfView.setLocalTranslation(dale.getWorldTranslation()
											.add(control.getViewDirection()
														.mult(fieldOfViewRadius
																+ OFFSET_FROM_DALE_TO_FIELD_OF_VIEW)));
	}

	private void findDogAndSetSeeingDale(Spatial enemy, boolean seeingDale) {
		app.getStateManager()
		   .getState(DogFollowingDaleAppState.class)
		   .setEnemySeeingDale(seeingDale, enemy);
	}

	private Set<Spatial> handleThrowingFieldOfView() {
		boolean containsAnyThrowingDestination = false;
		Set<Spatial> enemiesSeeingDaleInThisUpdate = new HashSet<>();
		Spatial fieldOfView = app.getRootNode()
								 .getChild(
										 NodeNames.getFieldOfView());
		for (PhysicsCollisionObject physicsCollisionObject : fieldOfView.getControl(
				GhostControl.class)
																		.getOverlappingObjects()) {
			Node collidingObject = (Node) physicsCollisionObject.getUserObject();
			if (NodeNames.getDog()
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
		return enemiesSeeingDaleInThisUpdate;
	}

	private void handleThrowingDestination(Node collidingObject) {
		changePreviouslyMarkedTargetToItsColor();

		Geometry geometry = (Geometry) (collidingObject).getChild(0);
		markObjectAsCurrentThrowingDestination(geometry);
		gameStateDTO.getDaleStateDTO()
					.setThrowingDestination(collidingObject);
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
		gameStateDTO.getDaleStateDTO()
					.setThrowingDestination(null);
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
