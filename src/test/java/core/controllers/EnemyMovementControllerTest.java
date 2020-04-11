package core.controllers;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import constants.PhysicsControls;
import dto.DogStateDTO;
import dto.GameStateDTO;
import enums.MovementDirection;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class EnemyMovementControllerTest {

	private GameStateDTO gameStateDTO = new GameStateDTO();
	private EnemyMovementController enemyMovementController = new EnemyMovementController(
			gameStateDTO);

	private Vector3f characterPosition = Vector3f.ZERO;

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
		DogStateDTO dogStateDTO = new DogStateDTO(dog,
				positionOfSquare, 20);
		dogStateDTO.setPositionWhereMovementBegan(150);
		dogStateDTO.setMovementDirection(MovementDirection.FORWARD_X);
		dogStateDTO.setNumberOfPixelsToMoveInGivenDirection(20);
		gameStateDTO.getDogStateDTOS()
					.add(dogStateDTO);

		//when, then
		for (int i = 0; i < 10000; i++) {
			enemyMovementController.moveEnemies(0.3f);
			Assertions.assertThat(characterPosition.getX() >= 130
					&& characterPosition.getX() <= 170
					&& characterPosition.getZ() >= -50
					&& characterPosition.getZ() <= -10)
					  .isTrue();
		}

	}
}