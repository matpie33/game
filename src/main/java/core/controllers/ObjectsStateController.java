package core.controllers;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import core.GameApplication;
import core.gui.HUDCreator;
import core.util.CoordinatesUtil;
import dto.DaleStateDTO;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

public class ObjectsStateController {

	private GameStateDTO gameStateDTO;
	public static final int HP_DECREASE_VALUE = 20;
	public static final float MINIMUM_TIME_BETWEEN_HP_DECREASE_SECONDS = 1;

	private float timeSinceLastHpDecrease;
	private ObjectsMovementController objectsMovementController;
	private HUDCreator hudCreator;
	private ObjectsHolderDTO objectsHolderDTO;

	public ObjectsStateController(GameStateDTO gameStateDTO,
			ObjectsMovementController objectsMovementController,
			HUDCreator hudCreator, ObjectsHolderDTO objectsHolderDTO) {
		this.gameStateDTO = gameStateDTO;
		timeSinceLastHpDecrease = 0;
		this.objectsMovementController = objectsMovementController;
		this.hudCreator = hudCreator;
		this.objectsHolderDTO = objectsHolderDTO;
	}

	public void handleObjectsState(float tpf) {
		timeSinceLastHpDecrease += tpf;
		handleDaleState();
		handleCursorState();
	}

	private void handleCursorState() {
		if (gameStateDTO.isCursorShowing() && !GameApplication.getInstance()
															  .getRootNode()
															  .getChildren()
															  .contains(
																	  objectsHolderDTO.getArrow())) {
			showArrow();
		}
		if (!gameStateDTO.isCursorShowing()) {
			objectsHolderDTO.getArrow()
							.removeFromParent();
		}
	}

	private void showArrow() {
		Geometry throwableObject = gameStateDTO.getSpatialOnWhichCursorIsShowing();
		Spatial arrow = objectsHolderDTO.getArrow();
		BoundingBox throwableObjectSize = CoordinatesUtil.getSizeOfSpatial(
				throwableObject);
		float yDimensionArrow = CoordinatesUtil.getSizeOfSpatial(arrow)
											   .getYExtent();
		float xDimensionCollisionObject = throwableObjectSize.getXExtent();
		float yDimensionCollisionObject = throwableObjectSize.getYExtent();
		float zDimensionCollisionObject = throwableObjectSize.getZExtent();
		Vector3f throwableObjectPosition = throwableObject.getWorldTranslation();
		arrow.setLocalTranslation(
				throwableObjectPosition.getX() + xDimensionCollisionObject,
				throwableObjectPosition.getY() + yDimensionArrow
						+ yDimensionCollisionObject,
				throwableObjectPosition.getZ() - zDimensionCollisionObject);
		GameApplication.getInstance()
					   .getRootNode()
					   .attachChild(arrow);
	}

	private void handleDaleState() {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		if (daleStateDTO.getHp() <= 0) {
			gameStateDTO.getDaleStateDTO()
						.setAlive(false);
		}

		if (daleStateDTO.isCollidingWithEnemy()
				&& enoughTimePassedToDecreaseHp()) {
			timeSinceLastHpDecrease = 0;
			daleStateDTO.setCollidingWithEnemy(false);
			daleStateDTO.setHp(daleStateDTO.getHp() - HP_DECREASE_VALUE);
			objectsMovementController.moveDaleBack();
			hudCreator.setHp(daleStateDTO.getHp());
		}

	}

	private boolean enoughTimePassedToDecreaseHp() {
		return timeSinceLastHpDecrease
				> MINIMUM_TIME_BETWEEN_HP_DECREASE_SECONDS;
	}

}
