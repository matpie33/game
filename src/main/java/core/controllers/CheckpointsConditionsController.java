package core.controllers;

import dto.GameStateDTO;

public class CheckpointsConditionsController {

	private final GameStateDTO gameStateDTO;
	private int checkpointNumber;

	public CheckpointsConditionsController(GameStateDTO gameStateDTO) {
		this.gameStateDTO = gameStateDTO;
	}

	public boolean isNextCheckpointConditionPassedWithUpdateCheckpoint() {
		if (isNextCheckpointConditionPassed()) {
			checkpointNumber++;
			return true;
		}
		return false;
	}

	private boolean isNextCheckpointConditionPassed() {
		if (checkpointNumber == 0) {
			return gameStateDTO.getDogStateDTOS()
							   .size() == 6;
		}

		return false;
	}

}
