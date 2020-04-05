package core.initialization;

import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import constants.NodeNames;
import constants.PhysicsControls;
import core.GameApplication;
import core.controllers.CollisionController;
import dto.DaleStateDTO;
import dto.DogMovementDTO;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;
import enums.MovementDirection;

import java.util.ArrayList;
import java.util.List;

public class ObjectsInitializer {

	private List<Vector3f> treesCoordinates = new ArrayList<>();
	private List<Vector3f> boxesCoordinates = new ArrayList<>();
	private List<Vector3f> dogsCoordinates = new ArrayList<>();
	private static final int FIRST_COORDINATE_OF_TREE_Z = 0;
	private static final int FIRST_COORDINATE_OF_TREE_X = 15;
	private static final int INCREASE_X_BY = 20;
	private static final int INCREASE_Z_BY = 40;

	private DaleStateDTO daleStateDTO;
	private CollisionController collisionController;
	private ObjectsHolderDTO objectsHolderDTO;
	private Camera camera;
	private GameStateDTO gameStateDTO;

	public ObjectsInitializer(ObjectsHolderDTO objectsHolderDTO,
			GameStateDTO gameStateDTO) {
		collisionController = new CollisionController(objectsHolderDTO);
		this.objectsHolderDTO = objectsHolderDTO;
		this.camera = GameApplication.getInstance()
									 .getCamera();
		this.gameStateDTO = gameStateDTO;
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
					new Vector3f(currentXCoordinate, 260, currentZCoordinate));
			if (i < numberOfBoxes) {
				boxesCoordinates.add(new Vector3f(currentXCoordinate - 90,
						245,
						currentZCoordinate - 70));
			}
			if (i < numberOfDogs) {
				dogsCoordinates.add(new Vector3f(40 * i, 245, -30));
			}
			increaseXNow = !increaseXNow;
		}
	}

	public void addObjectsToScene() {
		List<Spatial> trees = objectsHolderDTO.getTrees();
		List<Spatial> boxes = objectsHolderDTO.getBoxes();
		List<Spatial> dogs = objectsHolderDTO.getDogs();
		initializeCoordinates(trees.size(), boxes.size(), dogs.size());

		setObjectsCoordinates(trees, treesCoordinates, PhysicsControls.TREE);
		setObjectsCoordinatesForDogs(dogs, dogsCoordinates,
				PhysicsControls.DOG);
		setObjectsCoordinates(boxes, boxesCoordinates, PhysicsControls.BOX);
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

	public DaleStateDTO initializeObjects() {
		BulletAppState bulletAppState = initializeBulletAppState();
		initializeDogs(bulletAppState);
		initializeScene(bulletAppState);
		initializeTerrain(bulletAppState);
		initializeDale(bulletAppState);
		initializeTrees(bulletAppState);
		initializeMark();
		initializeBoxes(bulletAppState);
		return daleStateDTO;
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

			RigidBodyControl rigidBodyControl = new RigidBodyControl(boxShape,
					0.5f);
			rigidBodyControl.setGravity(new Vector3f(0, -10f, 0));
			box.addControl(rigidBodyControl);
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
		float height = ((BoundingBox) model.getWorldBound()).getYExtent();
		float width = ((BoundingBox) model.getWorldBound()).getXExtent();
		model.rotate(0, 0, 90 * FastMath.DEG_TO_RAD);
		CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(width,
				height, 1);
		daleStateDTO = new DaleStateDTO();
		daleStateDTO.setCarryingThrowableObject(false);

		CharacterControl daleControl = new CharacterControl(capsuleShape,
				0.05f);
		daleControl.setGravity(new Vector3f(0, -40f, 0));
		model.addControl(daleControl);
		daleControl.setPhysicsLocation(new Vector3f(0, 255, -20));

		bulletAppState.getPhysicsSpace()
					  .add(daleControl);
	}

	private void initializeDogs(BulletAppState bulletAppState) {
		for (Spatial model : objectsHolderDTO.getDogs()) {
			float height = ((BoundingBox) model.getWorldBound()).getYExtent();
			float width = ((BoundingBox) model.getWorldBound()).getXExtent();
			CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(
					height, width, 0);

			CharacterControl control = new CharacterControl(capsuleShape,
					0.05f);
			control.setGravity(new Vector3f(0, -40f, 0));
			model.addControl(control);

			bulletAppState.getPhysicsSpace()
						  .add(control);

		}
	}

	private void addDogMovement(Spatial model, CharacterControl control) {
		Vector3f physicsLocation = control.getPhysicsLocation();
		DogMovementDTO dogMovementDTO = new DogMovementDTO(model,
				physicsLocation, 20);
		dogMovementDTO.setMovementDirection(MovementDirection.FORWARD_X);
		dogMovementDTO.setNumberOfPixelsToMoveInGivenDirection(10);
		dogMovementDTO.setPositionWhereMovementBegan(physicsLocation.getX());
		gameStateDTO.addDogMovement(dogMovementDTO);
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
