package core.controllers;

import dto.GameStateDTO;

public class ObjectsStateController {

	private GameStateDTO gameStateDTO;

	public ObjectsStateController(GameStateDTO gameStateDTO) {
		this.gameStateDTO = gameStateDTO;
	}

	public void handleObjectsState (){
		if (gameStateDTO.getDaleStateDTO().getHp() <=0 ){
			gameStateDTO.getDaleStateDTO().setAlive(false);
		}
	}

}
