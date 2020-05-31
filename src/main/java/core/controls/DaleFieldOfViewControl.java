package core.controls;

import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.GhostControl;
import com.jme3.material.MatParam;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;
import enums.ThrowingState;

import java.util.ArrayList;
import java.util.List;

public class DaleFieldOfViewControl extends AbstractControl {

	public static final String DIFFUSE_PARAM = "Diffuse";
	private ObjectsHolderDTO objectsHolderDTO;
	private Geometry spatialPreviouslyMarkedAsThrowingDestination;
	private ColorRGBA previousColorOfThrowingDestination;
	private GameStateDTO gameStateDTO;
	private List<Spatial> enemiesSeeingDale = new ArrayList<>();

	public DaleFieldOfViewControl(ObjectsHolderDTO objectsHolderDTO,
			GameStateDTO gameStateDTO) {
		this.objectsHolderDTO = objectsHolderDTO;
		this.gameStateDTO = gameStateDTO;
	}

	@Override
	protected void controlUpdate(float tpf) {

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
		for (PhysicsCollisionObject physicsCollisionObject : spatial.getControl(
				GhostControl.class)
																	.getOverlappingObjects()) {
			Node collidingObject = (Node) physicsCollisionObject.getUserObject();
			if (objectsHolderDTO.getDogs()
								.contains(collidingObject)) {
				enemiesSeeingDaleInThisUpdate.add(collidingObject);
				this.enemiesSeeingDale.remove(collidingObject);
				if (gameStateDTO.getDaleStateDTO()
								.getThrowingState()
								.equals(ThrowingState.PICKING_OBJECT)) {
					containsAnyThrowingDestination = handleThrowingDestination(
							collidingObject);
				}
				findDogAndSetSeeingDale(collidingObject, true);

			}
		}
		if (!containsAnyThrowingDestination) {
			resetThrowingDestination();
		}
		return enemiesSeeingDaleInThisUpdate;
	}

	private boolean handleThrowingDestination(Node collidingObject) {
		boolean containsAnyThrowingDestination;
		changePreviouslyMarkedTargetToItsColor();

		Geometry geometry = (Geometry) (collidingObject).getChild(0);
		markObjectAsCurrentThrowingDestination(geometry);
		gameStateDTO.getDaleStateDTO()
					.setThrowingDestination(collidingObject);
		setColor(geometry, ColorRGBA.Red);
		containsAnyThrowingDestination = true;
		return containsAnyThrowingDestination;
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

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

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
