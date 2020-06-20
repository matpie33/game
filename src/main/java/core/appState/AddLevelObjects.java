package core.appState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
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
import core.controllers.CollisionDetectionAppState;
import core.controls.DaleFollowingCameraControl;
import core.controls.DogMovingInsideAreaControl;
import core.controls.ThrowableObjectMarkerControl;
import core.util.CoordinatesUtil;
import dto.GameStateDTO;
import dto.SpatialDTO;

import java.util.List;

public class AddLevelObjects {

	private GameStateDTO gameStateDTO;
	private Application app;

	public AddLevelObjects(GameStateDTO gameStateDTO) {
		this.gameStateDTO = gameStateDTO;
	}

	public void addObjects(AppStateManager stateManager, Application app) {
		this.app = app;
		addObjectsToMap(stateManager, (SimpleApplication) app);
	}

	private void addObjectsToMap(AppStateManager stateManager,
			SimpleApplication app) {
		BulletAppState bulletAppState = stateManager.getState(BulletAppState.class);
		Node rootNode = app.getRootNode();
		createFirstLevelNodes(rootNode);
		createGameObjectsMainNodes(app.getRootNode());
		List<SpatialDTO> spatials = stateManager.getState(LevelAppState.class)
												.getSpatialDTOS();
		for (SpatialDTO spatialDTO : spatials) {
			System.out.println(spatialDTO.getSpatial()
										 .getName());
			String spatialName = spatialDTO.getSpatial()
										   .getName();
			Spatial spatial = setSpatialPositionAndRotation(spatialDTO);
			if (spatialName.equals(NodeNames.getHouse()) || spatialName.equals(
					NodeNames.getHouseWithExit()) || spatialName.equals(
					NodeNames.getTrashbin())) {
				initializeImmobileObject(bulletAppState, spatial, rootNode);
			}
			if (spatialName.equals(NodeNames.getDale())) {
				initializeDale(bulletAppState, spatial, rootNode);
			}
			if (spatialName.equals(NodeNames.getMap())) {
				initializeScene(bulletAppState, rootNode, spatial);
			}
			if (spatialName.equals(NodeNames.getDog())) {
				initializeDog(bulletAppState, rootNode, spatial);
			}
			if (spatialName.equals(NodeNames.getBrokenFence())) {
				initializeBrokenFence(bulletAppState, spatial, rootNode);
			}
			if (spatialName.equals(NodeNames.getBox())) {
				initializeBox(bulletAppState, spatial, rootNode);
			}
			if (spatialName.equals(NodeNames.getArrow())) {
				spatial.addControl(new ThrowableObjectMarkerControl());
				spatial.setCullHint(Spatial.CullHint.Always);
				Node gameObjects = (Node) rootNode.getChild(
						NodeNames.getGameObjects());
				gameObjects.attachChild(spatial);
			}
			if (spatialName.equals(NodeNames.getMark())) {
				spatial.setCullHint(Spatial.CullHint.Always);
			}
			if (spatialName.equals(NodeNames.getSky())) {
				Node gameObjects = (Node) rootNode.getChild(
						NodeNames.getGameObjects());
				gameObjects.attachChild(spatial);
			}

			CharacterControl control = spatial.getControl(
					CharacterControl.class);
			if (control != null) {
				control.setViewDirection(spatialDTO.getRotation()
												   .mult(new Vector3f(0, 0,
														   1)));
			}

		}

		initializeDaleFieldOfView(bulletAppState, rootNode);
		//		gameObjects.attachChild(objectsHolderDTO.getScene());

		//		gameObjects.attachChild(objectsHolderDTO.getTerrain());
	}

	private void createGameObjectsMainNodes(Node rootNode) {
		Node gameObjects = (Node) rootNode.getChild(NodeNames.getGameObjects());
		Node throwables = new Node(NodeNames.getThrowables());
		Node dogs = new Node(NodeNames.getDogs());
		Node immobileObjects = new Node(NodeNames.getImmobileObjects());
		gameObjects.attachChild(dogs);
		gameObjects.attachChild(throwables);
		gameObjects.attachChild(immobileObjects);
	}

	private void createFirstLevelNodes(Node rootNode) {
		Node gameObjects = new Node(NodeNames.getGameObjects());
		Node additionalObjects = new Node(NodeNames.getAdditionalObjects());
		rootNode.attachChild(gameObjects);
		rootNode.attachChild(additionalObjects);
	}

	private void initializeBrokenFence(BulletAppState bulletAppState,
			Spatial spatial, Node rootNode) {
		Node immobileObjects = (Node) rootNode.getChild(
				NodeNames.getImmobileObjects());
		CollisionShape boxShape = CollisionShapeFactory.createBoxShape(spatial);

		RigidBodyControl rigidBodyControl = new RigidBodyControl(boxShape, 5f);
		rigidBodyControl.setGravity(new Vector3f(0, -30f, 0));
		rigidBodyControl.setPhysicsLocation(spatial.getLocalTranslation());

		spatial.addControl(rigidBodyControl);
		bulletAppState.getPhysicsSpace()
					  .add(rigidBodyControl);
		immobileObjects.attachChild(spatial);
	}

	private void initializeImmobileObject(BulletAppState bulletAppState,
			Spatial spatial, Node rootNode) {
		Node immobileObjects = (Node) rootNode.getChild(
				NodeNames.getImmobileObjects());
		CollisionShape shape = CollisionShapeFactory.createMeshShape(spatial);
		RigidBodyControl control = new RigidBodyControl(shape, 0);
		spatial.addControl(control);
		control.setPhysicsLocation(spatial.getLocalTranslation());
		bulletAppState.getPhysicsSpace()
					  .add(control);
		immobileObjects.attachChild(spatial);
	}

	private Spatial setSpatialPositionAndRotation(SpatialDTO spatialDTO) {
		Spatial spatial = spatialDTO.getSpatial();
		spatial.setLocalTranslation(spatialDTO.getPosition());
		spatial.setLocalRotation(spatialDTO.getRotation());

		return spatial;
	}

	private void initializeDaleFieldOfView(BulletAppState bulletAppState,
			Node rootNode) {
		Node gameObjects = (Node) rootNode.getChild(NodeNames.getGameObjects());
		Sphere sphere = new Sphere(5, 5, 5);
		Geometry sphereGeometry = new Geometry(NodeNames.getFieldOfView(),
				sphere);
		sphereGeometry.setCullHint(Spatial.CullHint.Always);
		Material material = new Material(GameApplication.getInstance()
														.getAssetManager(),
				"Common/MatDefs/Misc/Unshaded.j3md");
		sphereGeometry.setMaterial(material);

		SphereCollisionShape sphereCollisionShape = new SphereCollisionShape(
				50);
		GhostControl control = new GhostControl(sphereCollisionShape);
		Vector3f daleCoordinates = rootNode.getChild(NodeNames.getDale())
										   .getWorldTranslation();
		sphereGeometry.setLocalTranslation(daleCoordinates);
		control.setPhysicsLocation(daleCoordinates);

		sphereGeometry.addControl(control);

		bulletAppState.getPhysicsSpace()
					  .add(control);
		gameObjects.attachChild(sphereGeometry);

	}

	public void initializeCamera(Node rootNode) {
		Camera camera = GameApplication.getInstance()
									   .getCamera();
		CameraNode cameraNode = new CameraNode("Main camera", camera);
		cameraNode.addControl(
				new DaleFollowingCameraControl(gameStateDTO, camera));
		cameraNode.removeControl(CameraControl.class);
		camera.lookAtDirection(rootNode.getChild(NodeNames.getDale())
									   .getControl(PhysicsControls.DALE)
									   .getViewDirection(), Vector3f.UNIT_Y);
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
			Node rootNode) {
		Node throwables = (Node) rootNode.getChild(NodeNames.getThrowables());
		CollisionShape boxShape = CollisionShapeFactory.createBoxShape(box);

		RigidBodyControl rigidBodyControl = new RigidBodyControl(boxShape, 5f);
		rigidBodyControl.setGravity(new Vector3f(0, -30f, 0));
		rigidBodyControl.setPhysicsLocation(box.getLocalTranslation());

		box.addControl(rigidBodyControl);
		bulletAppState.getPhysicsSpace()
					  .add(rigidBodyControl);
		throwables.attachChild(box);

	}

	private void initializeTree(BulletAppState bulletAppState, Node rootNode,
			Spatial tree) {
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



	private void initializeDale(BulletAppState bulletAppState, Spatial model,
			Node rootNode) {
		Node gameObjects = (Node) rootNode.getChild(NodeNames.getGameObjects());
		CapsuleCollisionShape capsuleShape = initializeDaleShape(model);
		initializeDaleControls(bulletAppState, model, capsuleShape);
		gameObjects.attachChild(model);

	}

	private void initializeDaleControls(BulletAppState bulletAppState,
			Spatial model, CapsuleCollisionShape capsuleShape) {
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

		bulletAppState.getPhysicsSpace()
					  .add(ghostControl);

		model.addControl(ghostControl);
	}

	private CapsuleCollisionShape initializeDaleShape(Spatial model) {
		BoundingBox sizeOfDale = CoordinatesUtil.getSizeOfSpatial(model);
		float yExtent = sizeOfDale.getYExtent();
		float zExtent = sizeOfDale.getZExtent();

		return new CapsuleCollisionShape(zExtent, yExtent, 1);
	}

	private void initializeDog(BulletAppState bulletAppState, Node rootNode,
			Spatial model) {
		Node dogs = (Node) rootNode.getChild(NodeNames.getDogs());
		CapsuleCollisionShape capsuleShape = initializeDogShape(model);
		initializeDogControls(bulletAppState, model, capsuleShape);
		dogs.attachChild(model);
	}

	private void initializeDogControls(BulletAppState bulletAppState,
			Spatial model, CapsuleCollisionShape capsuleShape) {
		GhostControl ghostControl = new GhostControl(capsuleShape);
		CharacterControl control = new CharacterControl(capsuleShape, 0.05f);

		DogMovingInsideAreaControl dogMovingInsideAreaControl = new DogMovingInsideAreaControl();

		ghostControl.setPhysicsLocation(model.getLocalTranslation());
		control.setPhysicsLocation(model.getLocalTranslation());

		control.setGravity(new Vector3f(0, -40f, 0));
		model.addControl(control);
		model.addControl(ghostControl);
		model.addControl(dogMovingInsideAreaControl);

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

	private void initializeScene(BulletAppState bulletAppState, Node rootNode,
			Spatial scene) {
		Node immobileObjects = (Node) rootNode.getChild(
				NodeNames.getImmobileObjects());
		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(
				scene);
		RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0);
		scene.addControl(landscape);
		landscape.setPhysicsLocation(scene.getLocalTranslation());

		bulletAppState.getPhysicsSpace()
					  .add(landscape);
		immobileObjects.attachChild(scene);
	}

}
