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
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Sphere;
import constants.NodeNames;
import constants.PhysicsControls;
import core.GameApplication;
import core.controllers.CollisionController;
import core.controls.*;
import core.util.CoordinatesUtil;
import dto.*;
import enums.MovementDirection;

import java.util.List;

public class ObjectsInitializer {

	public static final int INITIAL_HP_OF_DALE = 100;

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

	public void addObjectsToScene(List<SpatialDTO> spatials) {

		BulletAppState bulletAppState = initializeBulletAppState();
		Node rootNode = GameApplication.getInstance()
									   .getRootNode();
		Node throwables = new Node(NodeNames.THROWABLES);
		rootNode.attachChild(throwables);
		for (SpatialDTO spatialDTO : spatials) {
			String spatialName = spatialDTO.getPathToModel()
										   .replace("/", "");
			Spatial spatial = setSpatialPositionAndRotation(spatialDTO);
			if (spatialName.startsWith("house") || spatialName.startsWith(
					"fence") || spatialName.startsWith("trash")
					|| spatialName.startsWith("pylon")) {
				initializeRigidObject(bulletAppState, spatial, rootNode);
			}
			if (spatialName.startsWith("dale")) {
				initializeDale(bulletAppState, spatial);
			}
			if (spatialName.startsWith("map")) {
				initializeScene(bulletAppState, rootNode, spatial);
			}
			if (spatialName.startsWith("dog")) {
				initializeDog(bulletAppState, rootNode, spatial);
			}
			if (spatialName.startsWith("tree")) {
				initializeTree(bulletAppState, rootNode, spatial);
			}
			if (spatialName.startsWith("box")) {
				initializeBox(bulletAppState, spatial, throwables);
			}
			else {
				rootNode.attachChild(spatial);
			}
			CharacterControl control = spatial.getControl(
					CharacterControl.class);
			if (control != null) {
				control.setViewDirection(spatialDTO.getRotation()
												   .mult(new Vector3f(0, 0,
														   1)));
			}

		}

		initializeDaleFieldOfView(bulletAppState);
		rootNode.attachChild(objectsHolderDTO.getSky());
		//		rootNode.attachChild(objectsHolderDTO.getScene());
		rootNode.attachChild(objectsHolderDTO.getFieldOfView());
		initializeCamera(rootNode);
		//		rootNode.attachChild(objectsHolderDTO.getTerrain());
	}

	private void initializeRigidObject(BulletAppState bulletAppState,
			Spatial spatial, Node rootNode) {
		CollisionShape shape = CollisionShapeFactory.createMeshShape(spatial);
		RigidBodyControl control = new RigidBodyControl(shape, 0);
		spatial.addControl(control);
		control.setPhysicsLocation(spatial.getLocalTranslation());
		bulletAppState.getPhysicsSpace()
					  .add(control);
		rootNode.attachChild(spatial);
	}

	private Spatial setSpatialPositionAndRotation(SpatialDTO spatialDTO) {
		Spatial spatial = spatialDTO.getSpatial();
		spatial.setLocalTranslation(spatialDTO.getPosition());
		spatial.setLocalRotation(spatialDTO.getRotation());

		return spatial;
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
		camera.lookAtDirection(objectsHolderDTO.getDale()
											   .getControl(PhysicsControls.DALE)
											   .getViewDirection(),
				Vector3f.UNIT_Y);
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

	private void initializeBox(BulletAppState bulletAppState, Spatial box,
			Node throwables) {
		objectsHolderDTO.addBox(box);
		CollisionShape boxShape = CollisionShapeFactory.createBoxShape(box);

		CarriedObjectControl carriedObjectControl = new CarriedObjectControl(
				gameStateDTO, objectsHolderDTO);
		RigidBodyControl rigidBodyControl = new RigidBodyControl(boxShape, 5f);
		rigidBodyControl.setGravity(new Vector3f(0, -30f, 0));
		rigidBodyControl.setPhysicsLocation(box.getLocalTranslation());

		box.addControl(rigidBodyControl);
		box.addControl(carriedObjectControl);
		bulletAppState.getPhysicsSpace()
					  .add(rigidBodyControl);
		throwables.attachChild(box);

	}

	private void initializeTree(BulletAppState bulletAppState, Node rootNode,
			Spatial tree) {
		objectsHolderDTO.addTree(tree);
		CollisionShape collisionShape = CollisionShapeFactory.createMeshShape(
				tree);
		RigidBodyControl rigidBodyControl = new RigidBodyControl(collisionShape,
				0);
		rigidBodyControl.setPhysicsLocation(tree.getLocalTranslation());
		tree.addControl(rigidBodyControl);
		bulletAppState.getPhysicsSpace()
					  .add(rigidBodyControl);
		rootNode.attachChild(tree);
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

	private void initializeDale(BulletAppState bulletAppState, Spatial model) {
		objectsHolderDTO.setDale(model);
		CapsuleCollisionShape capsuleShape = initializeDaleShape(model);
		initializeDaleState();
		initializeDaleControls(bulletAppState, model, capsuleShape);

	}

	private void initializeDaleControls(BulletAppState bulletAppState,
			Spatial model, CapsuleCollisionShape capsuleShape) {
		DaleMovingControl daleMovingControl = new DaleMovingControl(
				gameStateDTO, objectsHolderDTO);
		DalePickingObjectsControl dalePickingObjectsControl = new DalePickingObjectsControl(
				gameStateDTO, objectsHolderDTO);
		DaleClimbingControl daleClimbingControl = new DaleClimbingControl(
				gameStateDTO, objectsHolderDTO);
		GhostControl ghostControl = new GhostControl(capsuleShape);
		CharacterControl characterControl = new CharacterControl(capsuleShape,
				0.05f);
		characterControl.setGravity(new Vector3f(0, -40f, 0));
		Vector3f position = model.getLocalTranslation();
		characterControl.setPhysicsLocation(position);
		ghostControl.setPhysicsLocation(position);

		bulletAppState.getPhysicsSpace()
					  .add(characterControl);
		model.addControl(characterControl);
		model.addControl(daleClimbingControl);

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
		float yExtent = sizeOfDale.getYExtent();
		float zExtent = sizeOfDale.getZExtent();

		return new CapsuleCollisionShape(zExtent,
				yExtent, 1);
	}

	private void initializeDog(BulletAppState bulletAppState, Node rootNode,
			Spatial model) {
		objectsHolderDTO.addDog(model);
		CapsuleCollisionShape capsuleShape = initializeDogShape(model);
		initializeDogControls(bulletAppState, model, capsuleShape);
		rootNode.attachChild(model);
	}

	private void initializeDogControls(BulletAppState bulletAppState,
			Spatial model, CapsuleCollisionShape capsuleShape) {
		GhostControl ghostControl = new GhostControl(capsuleShape);
		CharacterControl control = new CharacterControl(capsuleShape, 0.05f);
		DogStateDTO dogStateDTO = addDogState(model, control);
		DogMovementControl dogMovementControl = new DogMovementControl(
				dogStateDTO, objectsHolderDTO, gameStateDTO);

		ghostControl.setPhysicsLocation(model.getLocalTranslation());
		control.setPhysicsLocation(model.getLocalTranslation());

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

	private void initializeScene(BulletAppState bulletAppState, Node rootNode,
			Spatial scene) {
		objectsHolderDTO.setScene(scene);
		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(
				scene);
		RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0);
		scene.addControl(landscape);
		landscape.setPhysicsLocation(scene.getLocalTranslation());

		bulletAppState.getPhysicsSpace()
					  .add(landscape);
		rootNode.attachChild(scene);
	}

}
