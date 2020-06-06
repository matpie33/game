package core.controls;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.GhostControl;
import com.jme3.material.MatParam;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import core.appState.CarriedObjectAppState;
import dto.GameStateDTO;
import dto.NodeNamesDTO;
import enums.ThrowingState;

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
		if (!gameStateDTO.getDaleStateDTO()
						 .getThrowingState()
						 .equals(ThrowingState.PICKING_OBJECT)) {

			resetThrowingDestination();
		}
		enemiesSeeingDale.forEach(
				enemy -> findDogAndSetSeeingDale(enemy, false));
		enemiesSeeingDale = enemiesSeeingDaleInThisUpdate;
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
