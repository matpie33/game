package core.controllers;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import constants.PhysicsControls;
import core.controls.DogMovementControl;
import dto.DogStateDTO;
import dto.GameStateDTO;
import dto.NodeNamesDTO;
import enums.MovementDirection;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class DogMovementControlTest {

	public static final float TPF = 0.25f;
	private Vector3f characterPosition = Vector3f.ZERO;

	private NodeNamesDTO nodeNamesDTO = new NodeNamesDTO();

	@Test
	public void shouldTestThatEnemiesStayInSquare() {

		//given
		Spatial dog = Mockito.mock(Spatial.class);
		CharacterControl characterControl = Mockito.mock(
				CharacterControl.class);
		when(dog.getControl(PhysicsControls.DOG)).thenReturn(characterControl);
		when(characterControl.getPhysicsLocation()).thenReturn(
				characterPosition);
		when(characterControl.onGround()).thenReturn(true);

		Mockito.doAnswer(invocation -> {
			Vector3f vector = invocation.getArgumentAt(0, Vector3f.class);
			characterPosition.addLocal(vector);
			return null;
		})
			   .when(characterControl)
			   .setWalkDirection(Mockito.any());

		Vector3f positionOfSquare = new Vector3f(150, 0, -30);
		characterPosition.set(150, 0, -30);
		DogStateDTO dogStateDTO = new DogStateDTO(dog, positionOfSquare, 20);
		dogStateDTO.setPositionWhereMovementBegan(150);
		dogStateDTO.setMovementDirection(MovementDirection.FORWARD_X);
		dogStateDTO.setNumberOfPixelsToMoveInGivenDirection(20);
		GameStateDTO gameState = new GameStateDTO();
		DogMovementControl dogMovementControl = new DogMovementControl(
				dogStateDTO, nodeNamesDTO, gameState);

		//when, then
		for (int i = 0; i < 10000; i++) {
			dogMovementControl.moveEnemies(TPF);
			Assertions.assertThat(characterPosition.getX() >= 130
					&& characterPosition.getX() <= 170
					&& characterPosition.getZ() >= -50
					&& characterPosition.getZ() <= -10)
					  .isTrue();
		}

	}
}