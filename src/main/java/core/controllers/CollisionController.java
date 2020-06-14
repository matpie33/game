package core.controllers;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;
import constants.PhysicsControls;
import core.controls.DogMovingInsideAreaControl;
import dto.DaleStateDTO;
import dto.GameStateDTO;
import dto.NodeNamesDTO;
import enums.ObjectsTypes;

public class CollisionController implements PhysicsCollisionListener {

	public static final int MINIMUM_IMPULSE_TO_DESTROY_BOX = 40;

	private NodeNamesDTO nodeNamesDTO;
	private GameStateDTO gameStateDTO;
	private ObjectsRemovingController objectsRemovingController;

	public CollisionController(NodeNamesDTO nodeNamesDTO,
			GameStateDTO gameStateDTO) {
		this.nodeNamesDTO = nodeNamesDTO;
		this.gameStateDTO = gameStateDTO;
		this.objectsRemovingController = new ObjectsRemovingController();
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
		boolean isEnoughSpeedOfBoxToDestroy = isEnoughSpeedOfBoxToDestroy(
				isABox ? nodeA : isBBox ? nodeB : null);

		if (nodeAType.equals(ObjectsTypes.NONE) || nodeBType.equals(
				ObjectsTypes.NONE)) {
			return;
		}

		if (isDogWithDaleCollision(nodeAType, nodeBType)) {
			DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
			daleStateDTO.setCollidingWithEnemy(true);

		}
		if (isDogWithImmobileObjectCollision(nodeA, nodeB, nodeAType,
				nodeBType)) {
			Spatial dogNode = nodeAType.equals(ObjectsTypes.DOG) ?
					nodeA :
					nodeB;
			dogNode.getControl(DogMovingInsideAreaControl.class)
				   .setCollidingWithObstacle();
		}

		if (isDogWithBoxCollision(nodeAType, nodeBType)
				&& isEnoughSpeedOfBoxToDestroy) {
			objectsRemovingController.removeDog(
					ObjectsTypes.DOG.equals(nodeAType) ? nodeA : nodeB);
			objectsRemovingController.removeBox(isABox ? nodeA : nodeB);
		}

	}

	private boolean isEnoughSpeedOfBoxToDestroy(Spatial box) {
		return box != null && box.getControl(PhysicsControls.BOX)
								 .getLinearVelocity()
								 .length() > 40f;
	}

	private IllegalArgumentException createExeptionForDogCollision(
			Spatial dogNode) {
		return new IllegalArgumentException(
				"Dog collided with obstacle, but dog state not " + "found: "
						+ dogNode);
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
		if (nodeNamesDTO.getDogNodeName()
						.equals(node.getName())) {
			return ObjectsTypes.DOG;
		}
		if (nodeNamesDTO.getDaleNodeName()
						.equals(node.getName())) {
			return ObjectsTypes.DALE;
		}
		if (nodeNamesDTO.getBoxNodeName()
						.contains(node.getName())) {
			return ObjectsTypes.BOX;
		}
		if (nodeNamesDTO.getFieldOfViewNodeName()
						.equals(node.getName())
				|| nodeNamesDTO.getMarkNodeName()
							   .equals(node.getName())
				|| nodeNamesDTO.getThrowableObjectMarkerNodeName()
							   .equals(node.getName())) {
			return ObjectsTypes.NONE;
		}
		return ObjectsTypes.NONE;
	}

	private boolean isDogWithImmobileObjectCollision(Spatial nodeA,
			Spatial nodeB, ObjectsTypes nodeAType, ObjectsTypes nodeBType) {
		return !isOneOfNodesScene(nodeAType, nodeBType) && (
				isNode1DogAnd2ImmobileObject(nodeAType, nodeB)
						|| isNode1DogAnd2ImmobileObject(nodeBType, nodeA));
	}

	private boolean isOneOfNodesScene(ObjectsTypes nodeAType,
			ObjectsTypes nodeBType) {
		return nodeAType.equals(ObjectsTypes.SCENE) || nodeBType.equals(
				ObjectsTypes.SCENE);
	}

	private boolean isNode1DogAnd2ImmobileObject(ObjectsTypes node1Type,
			Spatial node2) {
		if (node1Type.equals(ObjectsTypes.DOG)) {
			RigidBodyControl control = node2.getControl(RigidBodyControl.class);
			if (control != null) {
				float length = control.getLinearVelocity()
									  .length();
				return length < 0.1;
			}
		}
		return false;
	}
}
