package core.controllers;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.scene.Spatial;
import dto.DaleStateDTO;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;
import enums.ObjectsTypes;

public class CollisionController implements PhysicsCollisionListener {

	public static final int MINIMUM_IMPULSE_TO_DESTROY_BOX = 4;

	private ObjectsHolderDTO objectsHolderDTO;
	private GameStateDTO gameStateDTO;

	public CollisionController(ObjectsHolderDTO objectsHolderDTO,
			GameStateDTO gameStateDTO) {
		this.objectsHolderDTO = objectsHolderDTO;
		this.gameStateDTO = gameStateDTO;
	}

	@Override
	public void collision(PhysicsCollisionEvent event) {
		handleCollision(event);
	}

	private void handleCollision(PhysicsCollisionEvent event) {
		Spatial nodeA = event.getNodeA();
		Spatial nodeB = event.getNodeB();
		ObjectsTypes nodeAType = getObjectType(nodeA);
		ObjectsTypes nodeBType = getObjectType(nodeB);
		boolean isABox = ObjectsTypes.BOX.equals(nodeAType);
		boolean isBBox = ObjectsTypes.BOX.equals(nodeBType);

		if ((isABox || isBBox)
				&& event.getAppliedImpulse() > MINIMUM_IMPULSE_TO_DESTROY_BOX) {
			gameStateDTO.getObjectsToRemove()
						.add(isABox ? nodeA : nodeB);
		}
		if (isDogWithDaleCollision(nodeAType, nodeBType)) {
			DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
			daleStateDTO.setCollidingWithEnemy(true);

		}
		if (isDogWithBoxCollision(nodeAType, nodeBType)) {
			gameStateDTO.getObjectsToRemove()
						.add(isABox ? nodeA : nodeB);
			//TODO why applied impulse is zero here? adding boxes to remove
			// here is redundant, use strategies maybe
			markDogNotAlive(nodeAType, nodeA, nodeB);
		}

	}

	private void markDogNotAlive(ObjectsTypes nodeAType, Spatial nodeA,
			Spatial nodeB) {

		Spatial dog;
		if (ObjectsTypes.DOG.equals(nodeAType)) {
			dog = nodeA;
		}
		else {
			dog = nodeB;
		}
		gameStateDTO.getDogStateDTOS()
					.stream()
					.filter(dogData -> dogData.getDog()
											  .equals(dog))
					.findFirst()
					.ifPresent(dogData -> dogData.setAlive(false));

	}

	private boolean isDogWithBoxCollision(ObjectsTypes nodeA,
			ObjectsTypes nodeB) {
		boolean isAnyNodeBox =
				ObjectsTypes.BOX.equals(nodeA) || ObjectsTypes.BOX.equals(
						nodeB);
		boolean isAnyNodeDog =
				ObjectsTypes.DOG.equals(nodeA) || ObjectsTypes.DOG.equals(
						nodeB);
		return isAnyNodeBox && isAnyNodeDog;
	}

	private boolean isDogWithDaleCollision(ObjectsTypes nodeA,
			ObjectsTypes nodeB) {

		boolean isAnyNodeDale =
				ObjectsTypes.DALE.equals(nodeA) || ObjectsTypes.DALE.equals(
						nodeB);
		boolean isAnyNodeDog =
				ObjectsTypes.DOG.equals(nodeA) || ObjectsTypes.DOG.equals(
						nodeB);
		return isAnyNodeDale && isAnyNodeDog;
	}

	private ObjectsTypes getObjectType(Spatial node) {
		if (objectsHolderDTO.getDogs()
							.contains(node)) {
			return ObjectsTypes.DOG;
		}
		if (objectsHolderDTO.getDale()
							.equals(node)) {
			return ObjectsTypes.DALE;
		}
		if (objectsHolderDTO.getBoxes()
							.contains(node)) {
			return ObjectsTypes.BOX;
		}
		return null;
	}



}
