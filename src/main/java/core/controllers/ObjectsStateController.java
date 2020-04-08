package core.controllers;

import core.gui.HUDCreator;
import dto.DaleStateDTO;
import dto.GameStateDTO;

public class ObjectsStateController {

	private GameStateDTO gameStateDTO;
	public static final int HP_DECREASE_VALUE = 20;
	public static final float MINIMUM_TIME_BETWEEN_HP_DECREASE_SECONDS = 1;

	private float timeSinceLastHpDecrease;
	private ObjectsMovementController objectsMovementController;
	private HUDCreator hudCreator;

	public ObjectsStateController(GameStateDTO gameStateDTO,
			ObjectsMovementController objectsMovementController,
			HUDCreator hudCreator) {
		this.gameStateDTO = gameStateDTO;
		timeSinceLastHpDecrease = 0;
		this.objectsMovementController = objectsMovementController;
		this.hudCreator = hudCreator;
	}

	public void handleObjectsState(float tpf) {
		timeSinceLastHpDecrease += tpf;
		handleDaleState();
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
