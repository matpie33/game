package core.controllers;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import constants.PhysicsControls;
import core.GameApplication;
import dto.DaleStateDTO;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

public class ObjectsMovementController {

	private GameStateDTO gameStateDTO;
	private ObjectsHolderDTO objectsHolderDTO;
	private Camera camera;

	public ObjectsMovementController(GameStateDTO gameStateDTO, ObjectsHolderDTO objectsHolderDTO) {
		this.gameStateDTO = gameStateDTO;
		this.objectsHolderDTO = objectsHolderDTO;
		GameApplication gameApplication = GameApplication.getInstance();
		camera = gameApplication.getCamera();
	}



	public void moveDaleBack() {
		if (!gameStateDTO.getDaleStateDTO()
						 .isAlive()) {
			return;
		}
		CharacterControl control = objectsHolderDTO.getDale()
												   .getControl(
														   PhysicsControls.DALE);
		Vector3f direction = camera.getDirection();
		Vector3f multiplied = direction.mult(new Vector3f(30f, 1, 30f));
		control.setPhysicsLocation(control.getPhysicsLocation()
										  .subtract(multiplied));
	}



}
