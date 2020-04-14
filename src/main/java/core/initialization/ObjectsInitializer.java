package core.initialization;

import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Sphere;
import constants.NodeNames;
import core.GameApplication;
import core.controllers.CollisionController;
import core.controls.*;
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
	private IdleTimeChecker idleTimeChecker;

	public ObjectsInitializer(ObjectsHolderDTO objectsHolderDTO,
			GameStateDTO gameStateDTO, IdleTimeChecker idleTimeChecker) {
		collisionController = new CollisionController(objectsHolderDTO,
				gameStateDTO);
		this.objectsHolderDTO = objectsHolderDTO;
		this.gameStateDTO = gameStateDTO;
		this.idleTimeChecker = idleTimeChecker;
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
				dogsCoordinates.add(
						new Vector3f(30 * (i + 1), 245, 100 * (i + 1)));
			}
			increaseXNow = !increaseXNow;
		}
	}

	public void addObjectsToScene() {
		List<Spatial> trees = objectsHolderDTO.getTrees();
		List<Spatial> boxes = objectsHolderDTO.getBoxes();
		List<Spatial> dogs = objectsHolderDTO.getDogs();
		initializeCoordinates(trees.size(), boxes.size(), dogs.size());

		Node rootNode = GameApplication.getInstance()
									   .getRootNode();
		BulletAppState bulletAppState = initializeBulletAppState();
		initializeBoxes(bulletAppState, rootNode);
		initializeScene(bulletAppState, rootNode);
		initializeDogs(bulletAppState, rootNode);
		initializeTrees(bulletAppState, rootNode);
		initializeDale(bulletAppState, rootNode);
		initializeDaleFieldOfView(bulletAppState);
		objectsHolderDTO.getDogs()
						.forEach(rootNode::attachChild);
		rootNode.attachChild(objectsHolderDTO.getSky());
		rootNode.attachChild(objectsHolderDTO.getMark());
		rootNode.attachChild(objectsHolderDTO.getScene());
		initializeCamera(rootNode);
		rootNode.attachChild(objectsHolderDTO.getFieldOfView());
		//		rootNode.attachChild(objectsHolderDTO.getTerrain());
	}

	private void initializeDaleFieldOfView(BulletAppState bulletAppState) {
		Sphere sphere = new Sphere(5, 5, 5);
		Geometry sphereGeometry = new Geometry("fieldOfView", sphere);
		sphereGeometry.setCullHint(Spatial.CullHint.Always);
		Material material = new Material(GameApplication.getInstance()
														.getAssetManager(),
				"Common/MatDefs/Misc/Unshaded.j3md");
		sphereGeometry.setMaterial(material);

		SphereCollisionShape sphereCollisionShape = new SphereCollisionShape(
				50);
		GhostControl control = new GhostControl(sphereCollisionShape);
		Vector3f daleCoordinates = objectsHolderDTO.getDale()
												   .getWorldTranslation();
		sphereGeometry.setLocalTranslation(daleCoordinates);
		control.setPhysicsLocation(daleCoordinates);

		sphereGeometry.addControl(control);
		sphereGeometry.addControl(
				new DaleFieldOfViewControl(objectsHolderDTO, gameStateDTO));

		bulletAppState.getPhysicsSpace()
					  .add(control);
		objectsHolderDTO.setFieldOfView(sphereGeometry);
	}

	private void initializeCamera(Node rootNode) {
		Camera camera = GameApplication.getInstance()
									   .getCamera();
		CameraNode cameraNode = new CameraNode("Main camera", camera);
		cameraNode.addControl(
				new DaleFollowingCameraControl(gameStateDTO, camera,
						objectsHolderDTO, idleTimeChecker));
		cameraNode.removeControl(CameraControl.class);
		rootNode.attachChild(cameraNode);
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

	private void initializeBoxes(BulletAppState bulletAppState, Node rootNode) {
		Node throwables = new Node(NodeNames.THROWABLES);
		List<Spatial> boxes = objectsHolderDTO.getBoxes();
		for (int i = 0; i < boxes.size(); i++) {
			Spatial box = boxes.get(i);
			Vector3f coordintes = boxesCoordinates.get(i);
			CollisionShape boxShape = CollisionShapeFactory.createBoxShape(box);

			CarriedObjectControl carriedObjectControl = new CarriedObjectControl(
					gameStateDTO, objectsHolderDTO);
			RigidBodyControl rigidBodyControl = new RigidBodyControl(boxShape,
					5f);
			rigidBodyControl.setGravity(new Vector3f(0, -30f, 0));
			rigidBodyControl.setPhysicsLocation(coordintes);
			box.setLocalTranslation(coordintes);

			box.addControl(rigidBodyControl);
			box.addControl(carriedObjectControl);
			bulletAppState.getPhysicsSpace()
						  .add(rigidBodyControl);
			throwables.attachChild(box);
		}
		rootNode.attachChild(throwables);

	}

	private void initializeMark() {
		objectsHolderDTO.getMark()
						.setLocalTranslation(5, 5, 5);
	}

	private void initializeTrees(BulletAppState bulletAppState, Node rootNode) {
		List<Spatial> trees = objectsHolderDTO.getTrees();
		for (int i = 0; i < trees.size(); i++) {
			Spatial tree = trees.get(i);
			Vector3f coordinates = treesCoordinates.get(i);
			CollisionShape collisionShape = CollisionShapeFactory.createMeshShape(
					tree);
			RigidBodyControl rigidBodyControl = new RigidBodyControl(
					collisionShape, 0);
			tree.setLocalTranslation(coordinates);
			rigidBodyControl.setPhysicsLocation(coordinates);
			tree.addControl(rigidBodyControl);
			bulletAppState.getPhysicsSpace()
						  .add(rigidBodyControl);
			rootNode.attachChild(tree);
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

	private void initializeDale(BulletAppState bulletAppState, Node rootNode) {
		Spatial model = objectsHolderDTO.getDale();
		CapsuleCollisionShape capsuleShape = initializeDaleShape(model);
		initializeDaleState();
		initializeDaleControls(bulletAppState, model, capsuleShape);
		rootNode.attachChild(model);

	}

	private void initializeDaleControls(BulletAppState bulletAppState,
			Spatial model, CapsuleCollisionShape capsuleShape) {
		DaleMovingControl daleMovingControl = new DaleMovingControl(
				gameStateDTO, objectsHolderDTO);
		DalePickingObjectsControl dalePickingObjectsControl = new DalePickingObjectsControl(
				gameStateDTO, objectsHolderDTO);
		GhostControl ghostControl = new GhostControl(capsuleShape);
		CharacterControl characterControl = new CharacterControl(capsuleShape,
				0.05f);
		Vector3f coordinates = new Vector3f(0, 255, -20);
		model.setLocalTranslation(coordinates);
		characterControl.setGravity(new Vector3f(0, -40f, 0));
		characterControl.setPhysicsLocation(coordinates);
		ghostControl.setPhysicsLocation(coordinates);

		bulletAppState.getPhysicsSpace()
					  .add(characterControl);
		model.addControl(characterControl);

		bulletAppState.getPhysicsSpace()
					  .add(ghostControl);

		model.addControl(ghostControl);
		model.addControl(dalePickingObjectsControl);
		model.addControl(daleMovingControl);
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

	private void initializeDogs(BulletAppState bulletAppState, Node rootNode) {
		List<Spatial> dogs = objectsHolderDTO.getDogs();
		for (int i = 0; i < dogs.size(); i++) {
			Spatial model = dogs.get(i);
			Vector3f coordinates = dogsCoordinates.get(i);
			CapsuleCollisionShape capsuleShape = initializeDogShape(model);
			initializeDogControls(bulletAppState, model, capsuleShape,
					coordinates);
			rootNode.attachChild(model);
		}
	}

	private void initializeDogControls(BulletAppState bulletAppState,
			Spatial model, CapsuleCollisionShape capsuleShape,
			Vector3f coordinates) {
		GhostControl ghostControl = new GhostControl(capsuleShape);
		CharacterControl control = new CharacterControl(capsuleShape, 0.05f);
		DogStateDTO dogStateDTO = addDogState(model, control);
		DogMovementControl dogMovementControl = new DogMovementControl(
				dogStateDTO, objectsHolderDTO, gameStateDTO);

		model.setLocalTranslation(coordinates);
		ghostControl.setPhysicsLocation(coordinates);
		control.setPhysicsLocation(coordinates);

		control.setGravity(new Vector3f(0, -40f, 0));
		model.addControl(control);
		model.addControl(ghostControl);
		model.addControl(dogMovementControl);

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

	private DogStateDTO addDogState(Spatial model, CharacterControl control) {
		Vector3f physicsLocation = control.getPhysicsLocation();
		DogStateDTO dogStateDTO = new DogStateDTO(model, physicsLocation, 20);
		dogStateDTO.setMovementDirection(MovementDirection.FORWARD_X);
		dogStateDTO.setNumberOfPixelsToMoveInGivenDirection(10);
		dogStateDTO.setPositionWhereMovementBegan(physicsLocation.getX());
		gameStateDTO.addDogState(dogStateDTO);
		return dogStateDTO;
	}

	private void initializeScene(BulletAppState bulletAppState, Node rootNode) {
		Spatial scene = objectsHolderDTO.getScene();
		scene.setLocalScale(10);
		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(
				scene);
		RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0);
		scene.addControl(landscape);
		Vector3f coordinates = new Vector3f(0, 230, 0);
		landscape.setPhysicsLocation(coordinates);
		scene.setLocalTranslation(coordinates);

		bulletAppState.getPhysicsSpace()
					  .add(landscape);
		rootNode.attachChild(scene);
	}

}
