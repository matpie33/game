package core.initialization;

import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import constants.NodeNames;
import constants.PhysicsControls;
import core.GameApplication;
import core.animationEventListeners.DaleAnimationListener;
import core.controllers.CollisionController;
import core.controls.CarriedObjectControl;
import core.controls.DaleMovingControl;
import core.controls.DalePickingObjectsControl;
import core.util.CoordinatesUtil;
import dto.DaleStateDTO;
import dto.DogStateDTO;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;
import enums.MovementDirection;

import java.util.ArrayList;
import java.util.List;

public class ObjectsInitializer {

	public static final int INITIAL_HP_OF_DALE = 100;
	private List<Vector3f> treesCoordinates = new ArrayList<>();
	private List<Vector3f> boxesCoordinates = new ArrayList<>();
	private List<Vector3f> dogsCoordinates = new ArrayList<>();
	private static final int FIRST_COORDINATE_OF_TREE_Z = 0;
	private static final int FIRST_COORDINATE_OF_TREE_X = 15;
	private static final int INCREASE_X_BY = 20;
	private static final int INCREASE_Z_BY = 40;

	private CollisionController collisionController;
	private ObjectsHolderDTO objectsHolderDTO;
	private GameStateDTO gameStateDTO;
	private DaleAnimationListener daleAnimationListener;

	public ObjectsInitializer(ObjectsHolderDTO objectsHolderDTO,
			GameStateDTO gameStateDTO,
			DaleAnimationListener daleAnimationListener) {
		collisionController = new CollisionController(objectsHolderDTO,
				gameStateDTO);
		this.objectsHolderDTO = objectsHolderDTO;
		this.gameStateDTO = gameStateDTO;
		this.daleAnimationListener = daleAnimationListener;
	}

	private void initializeCoordinates(int numberOfTrees, int numberOfBoxes,
			int numberOfDogs) {
		boolean increaseXNow = true;
		int currentXCoordinate = FIRST_COORDINATE_OF_TREE_X;
		int currentZCoordinate;

		for (int i = 0; i < numberOfTrees; i++) {
			if (increaseXNow) {
				currentXCoordinate += INCREASE_X_BY;
				currentZCoordinate = FIRST_COORDINATE_OF_TREE_Z;
			}
			else {
				currentZCoordinate = FIRST_COORDINATE_OF_TREE_Z + INCREASE_Z_BY;
			}
			treesCoordinates.add(
					new Vector3f(currentXCoordinate, 230, currentZCoordinate));
			if (i < numberOfBoxes) {
				boxesCoordinates.add(new Vector3f(currentXCoordinate - 90, 235,
						currentZCoordinate + 10));
			}
			if (i < numberOfDogs) {
				dogsCoordinates.add(new Vector3f(50 * i, 245, -30 * i));
			}
			increaseXNow = !increaseXNow;
		}
	}

	public void addObjectsToScene() {
		List<Spatial> trees = objectsHolderDTO.getTrees();
		List<Spatial> boxes = objectsHolderDTO.getBoxes();
		List<Spatial> dogs = objectsHolderDTO.getDogs();
		initializeAndSetCoordinates(trees, boxes, dogs);
		Node throwables = new Node(NodeNames.THROWABLES);
		objectsHolderDTO.getBoxes()
						.forEach(throwables::attachChild);
		Node rootNode = GameApplication.getInstance()
									   .getRootNode();

		trees.forEach(rootNode::attachChild);
		objectsHolderDTO.getDogs()
						.forEach(rootNode::attachChild);

		rootNode.attachChild(objectsHolderDTO.getSky());
		rootNode.attachChild(objectsHolderDTO.getMark());
		rootNode.attachChild(objectsHolderDTO.getDale());
		rootNode.attachChild(objectsHolderDTO.getScene());
		rootNode.attachChild(throwables);
		//		rootNode.attachChild(objectsHolderDTO.getTerrain());
	}

	private void initializeAndSetCoordinates(List<Spatial> trees, List<Spatial> boxes,
			List<Spatial> dogs) {
		initializeCoordinates(trees.size(), boxes.size(), dogs.size());

		setObjectsCoordinates(trees, treesCoordinates, PhysicsControls.TREE);
		setObjectsCoordinatesForDogs(dogs, dogsCoordinates,
				PhysicsControls.DOG);
		setObjectsCoordinates(boxes, boxesCoordinates, PhysicsControls.BOX);
	}

	private void setObjectsCoordinates(List<Spatial> objects,
			List<Vector3f> coordinates,
			Class<RigidBodyControl> physicsRigidBody) {
		for (int i = 0; i < objects.size(); i++) {
			Spatial object = objects.get(i);
			object.getControl(physicsRigidBody)
				  .setPhysicsLocation(coordinates.get(i));
		}
	}

	private void setObjectsCoordinatesForDogs(List<Spatial> objects,
			List<Vector3f> coordinates, Class<CharacterControl> controlClass) {
		for (int i = 0; i < objects.size(); i++) {
			Spatial object = objects.get(i);
			CharacterControl control = object.getControl(controlClass);
			control.setPhysicsLocation(coordinates.get(i));
			addDogMovement(object, control);
		}
	}

	public void initializeObjects() {
		BulletAppState bulletAppState = initializeBulletAppState();
		initializeDogs(bulletAppState);
		initializeScene(bulletAppState);
		initializeTerrain(bulletAppState);
		initializeDale(bulletAppState);
		initializeTrees(bulletAppState);
		initializeMark();
		initializeBoxes(bulletAppState);
	}

	private void initializeTerrain(BulletAppState state) {
		//		TerrainQuad terrain = objectsHolderDTO.getTerrain();
		//		terrain.setLocalTranslation(0, 0, 0);
		//		terrain.setLocalScale(1f, 1f, 1f);
		//		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(
		//				terrain);
		//		RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0);
		//		terrain.addControl(landscape);
		//		state.getPhysicsSpace()
		//			 .add(landscape);
		//
		//		TerrainLodControl control = new TerrainLodControl(terrain, camera);
		//		terrain.addControl(control);
	}

	private void initializeBoxes(BulletAppState bulletAppState) {
		for (Spatial box : objectsHolderDTO.getBoxes()) {
			CollisionShape boxShape = CollisionShapeFactory.createBoxShape(box);

			CarriedObjectControl carriedObjectControl = new CarriedObjectControl(
					gameStateDTO, objectsHolderDTO, daleAnimationListener);
			RigidBodyControl rigidBodyControl = new RigidBodyControl(boxShape,
					0.5f);
			rigidBodyControl.setGravity(new Vector3f(0, -10f, 0));
			box.addControl(rigidBodyControl);
			box.addControl(carriedObjectControl);
			bulletAppState.getPhysicsSpace()
						  .add(rigidBodyControl);
		}

	}

	private void initializeMark() {
		objectsHolderDTO.getMark()
						.setLocalTranslation(5, 5, 5);
	}

	private void initializeTrees(BulletAppState bulletAppState) {
		List<Spatial> trees = objectsHolderDTO.getTrees();
		for (Spatial tree : trees) {
			CollisionShape collisionShape = CollisionShapeFactory.createMeshShape(
					tree);
			RigidBodyControl rigidBodyControl = new RigidBodyControl(
					collisionShape, 0);
			tree.addControl(rigidBodyControl);
			bulletAppState.getPhysicsSpace()
						  .add(rigidBodyControl);
		}
	}

	private BulletAppState initializeBulletAppState() {
		AppStateManager stateManager = GameApplication.getInstance()
													  .getStateManager();
		BulletAppState bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		bulletAppState.getPhysicsSpace()
					  .addCollisionListener(collisionController);
		return bulletAppState;
	}

	private void initializeDale(BulletAppState bulletAppState) {
		Spatial model = objectsHolderDTO.getDale();
		CapsuleCollisionShape capsuleShape = initializeDaleShape(model);
		initializeDaleState();
		initializeDaleControls(bulletAppState, model, capsuleShape);

	}

	private void initializeDaleControls(BulletAppState bulletAppState,
			Spatial model, CapsuleCollisionShape capsuleShape) {
		DaleMovingControl daleMovingControl = new DaleMovingControl(
				gameStateDTO, daleAnimationListener);
		DalePickingObjectsControl dalePickingObjectsControl = new DalePickingObjectsControl(
				gameStateDTO, objectsHolderDTO, daleAnimationListener);
		GhostControl ghostControl = new GhostControl(capsuleShape);
		initializeDaleCharacterControl(bulletAppState, model, capsuleShape);

		bulletAppState.getPhysicsSpace()
					  .add(ghostControl);

		model.addControl(ghostControl);
		model.addControl(dalePickingObjectsControl);
		model.addControl(daleMovingControl);
	}

	private void initializeDaleCharacterControl(BulletAppState bulletAppState,
			Spatial model, CapsuleCollisionShape capsuleShape) {
		CharacterControl characterControl = new CharacterControl(capsuleShape,
				0.05f);
		bulletAppState.getPhysicsSpace()
					  .add(characterControl);
		model.addControl(characterControl);

		characterControl.setGravity(new Vector3f(0, -40f, 0));
		characterControl.setPhysicsLocation(new Vector3f(0, 255, -20));
	}

	private void initializeDaleState() {
		DaleStateDTO daleStateDTO = new DaleStateDTO();
		daleStateDTO.setHp(INITIAL_HP_OF_DALE);
		daleStateDTO.setCarryingThrowableObject(false);
		gameStateDTO.setDaleStateDTO(daleStateDTO);
	}

	private CapsuleCollisionShape initializeDaleShape(Spatial model) {
		BoundingBox sizeOfDale = CoordinatesUtil.getSizeOfSpatial(model);
		float height = sizeOfDale.getYExtent();
		float width = sizeOfDale.getXExtent();
		model.rotate(0, 0, 90 * FastMath.DEG_TO_RAD);
		return new CapsuleCollisionShape(width, height, 1);
	}

	private void initializeDogs(BulletAppState bulletAppState) {
		for (Spatial model : objectsHolderDTO.getDogs()) {
			CapsuleCollisionShape capsuleShape = initializeDogShape(model);
			initializeDogControls(bulletAppState, model, capsuleShape);
		}
	}


	private void initializeDogControls(BulletAppState bulletAppState,
			Spatial model, CapsuleCollisionShape capsuleShape) {
		GhostControl ghostControl = new GhostControl(capsuleShape);
		CharacterControl control = new CharacterControl(capsuleShape, 0.05f);

		control.setGravity(new Vector3f(0, -40f, 0));
		model.addControl(control);
		model.addControl(ghostControl);

		bulletAppState.getPhysicsSpace()
					  .add(control);
		bulletAppState.getPhysicsSpace()
					  .add(ghostControl);
	}

	private CapsuleCollisionShape initializeDogShape(Spatial model) {
		BoundingBox dogSize = CoordinatesUtil.getSizeOfSpatial(model);
		float height = dogSize.getYExtent();
		float width = dogSize.getXExtent();
		return new CapsuleCollisionShape(height, width, 0);
	}

	private void addDogMovement(Spatial model, CharacterControl control) {
		Vector3f physicsLocation = control.getPhysicsLocation();
		DogStateDTO dogStateDTO = new DogStateDTO(model, physicsLocation, 20);
		dogStateDTO.setMovementDirection(MovementDirection.FORWARD_X);
		dogStateDTO.setNumberOfPixelsToMoveInGivenDirection(10);
		dogStateDTO.setPositionWhereMovementBegan(physicsLocation.getX());
		gameStateDTO.addDogMovement(dogStateDTO);
	}

	private void initializeScene(BulletAppState bulletAppState) {
		Spatial scene = objectsHolderDTO.getScene();
		scene.setLocalScale(10);
		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(
				scene);
		RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0);
		scene.addControl(landscape);
		landscape.setPhysicsLocation(new Vector3f(0, 230, 0));
		bulletAppState.getPhysicsSpace()
					  .add(landscape);
	}

}
