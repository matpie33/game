package core.controls;

import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.GhostControl;
import com.jme3.material.MatParam;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

public class DaleFieldOfViewControl extends AbstractControl {

	public static final String DIFFUSE_PARAM = "Diffuse";
	private ObjectsHolderDTO objectsHolderDTO;
	private Geometry spatialPreviouslyMarkedAsThrowingDestination;
	private ColorRGBA previousColorOfThrowingDestination;
	private GameStateDTO gameStateDTO;

	public DaleFieldOfViewControl(ObjectsHolderDTO objectsHolderDTO,
			GameStateDTO gameStateDTO) {
		this.objectsHolderDTO = objectsHolderDTO;
		this.gameStateDTO = gameStateDTO;
	}

	@Override
	protected void controlUpdate(float tpf) {

		handleThrowingFieldOfView();
		if (!gameStateDTO.getDaleStateDTO()
						 .isCarryingThrowableObject()) {

			resetThrowingDestination();
		}

	}

	private void handleThrowingFieldOfView() {
		boolean containsAnyThrowingDestination = false;
		for (PhysicsCollisionObject physicsCollisionObject : spatial.getControl(
				GhostControl.class)
																	.getOverlappingObjects()) {
			Node collidingObject = (Node) physicsCollisionObject.getUserObject();
			if (objectsHolderDTO.getDogs()
								.contains(collidingObject)) {
				if (gameStateDTO.getDaleStateDTO()
								.isCarryingThrowableObject()) {
					containsAnyThrowingDestination = handleThrowingDestination(
							collidingObject);
				}
				gameStateDTO.getDogStateDTOS()
							.stream()
							.filter(dogState -> dogState.getDog()
														.equals(collidingObject))
							.findFirst()
							.ifPresent(dogStateDTO -> dogStateDTO.setSeeingDale(
									true));

			}
		}
		if (!containsAnyThrowingDestination) {
			resetThrowingDestination();
		}
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
