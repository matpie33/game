package core;

import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;
import constants.NodeNames;
import constants.PhysicsControls;

import java.util.ArrayList;
import java.util.List;

public class ObjectsInitializer {

	private List<Vector3f> treesCoordinates = new ArrayList<>();
	private List<Vector3f> boxesCoordinates = new ArrayList<>();
	private static final int FIRST_COORDINATE_OF_TREE_Z = 0;
	private static final int FIRST_COORDINATE_OF_TREE_X = 15;
	private static final int INCREASE_X_BY = 20;
	private static final int INCREASE_Z_BY = 40;

	private DaleState daleState;
	private CollisionHandler collisionHandler;

	public ObjectsInitializer(ModelLoader modelLoader,
			AssetManager assetManager, Node rootNode) {
		collisionHandler = new CollisionHandler(modelLoader, assetManager,
				rootNode);
	}

	private void initializeCoordinates(int numberOfTrees, int numberOfBoxes) {
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
					new Vector3f(currentXCoordinate, 0, currentZCoordinate));
			if (i < numberOfBoxes) {
				boxesCoordinates.add(new Vector3f(currentXCoordinate - 90, 4,
						currentZCoordinate - 70));
			}
			increaseXNow = !increaseXNow;
		}
	}

	public void addObjectsToScene(ModelLoader modelLoader, Node rootNode) {
		List<Spatial> trees = modelLoader.getTrees();
		List<Spatial> boxes = modelLoader.getBoxes();
		initializeCoordinates(trees.size(), boxes.size());

		setObjectsCoordinates(trees, treesCoordinates, PhysicsControls.TREE);
		setObjectsCoordinates(boxes, boxesCoordinates, PhysicsControls.BOX);
		Node throwables = new Node(NodeNames.THROWABLES);
		modelLoader.getBoxes()
				   .forEach(throwables::attachChild);

		trees.forEach(rootNode::attachChild);

		rootNode.attachChild(modelLoader.getSky());
		rootNode.attachChild(modelLoader.getMark());
		rootNode.attachChild(modelLoader.getDale());
		rootNode.attachChild(modelLoader.getScene());
		rootNode.attachChild(throwables);
	}

	private void setObjectsCoordinates(List<Spatial> objects,
			List<Vector3f> coordinates,
			Class<RigidBodyControl> physicsRigidBody) {
		for (int i = 0; i < objects.size(); i++) {
			Spatial object = objects.get(i);
			Vector3f currentCoordinate = coordinates.get(i);
			object.getControl(physicsRigidBody)
				  .setPhysicsLocation(new Vector3f(currentCoordinate.getX(),
						  currentCoordinate.getY(), currentCoordinate.getZ()));
		}
	}

	public DaleState initializeObjects(ModelLoader modelLoader,
			AppStateManager stateManager) {
		BulletAppState bulletAppState = initializeBulletAppState(stateManager);
		initializeScene(modelLoader, bulletAppState);
		initializeDale(modelLoader, bulletAppState);
		initializeTrees(modelLoader, bulletAppState);
		initializeMark(modelLoader);
		initializeBoxes(modelLoader, bulletAppState);
		return daleState;
	}


	private void initializeBoxes(ModelLoader modelLoader,
			BulletAppState bulletAppState) {
		for (Spatial box : modelLoader.getBoxes()) {
			CollisionShape boxShape = CollisionShapeFactory.createBoxShape(box);

			RigidBodyControl rigidBodyControl = new RigidBodyControl(boxShape,
					0.5f);
			rigidBodyControl.setGravity(new Vector3f(0, 5f, 0));
			box.addControl(rigidBodyControl);
			bulletAppState.getPhysicsSpace()
						  .add(rigidBodyControl);
		}

	}

	private void initializeMark(ModelLoader modelLoader) {
		modelLoader.getMark()
				   .setLocalTranslation(5, 5, 5);
	}

	private void initializeTrees(ModelLoader modelLoader,
			BulletAppState bulletAppState) {
		List<Spatial> trees = modelLoader.getTrees();
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

	private BulletAppState initializeBulletAppState(
			AppStateManager stateManager) {
		BulletAppState bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		bulletAppState.getPhysicsSpace()
					  .addCollisionListener(collisionHandler);
		return bulletAppState;
	}

	private void initializeDale(ModelLoader modelLoader,
			BulletAppState bulletAppState) {
		Spatial model = modelLoader.getDale();
		model.rotate(0, 0, 90 * FastMath.DEG_TO_RAD);
		CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(2f, 2f,
				1);
		daleState = new DaleState();
		daleState.setCarryingThrowableObject(false);

		CharacterControl daleControl = new CharacterControl(capsuleShape,
				0.05f);
		daleControl.setGravity(new Vector3f(0, -40f, 0));
		model.addControl(daleControl);
		daleControl.setPhysicsLocation(new Vector3f(0, 5, -20));

		bulletAppState.getPhysicsSpace()
					  .add(daleControl);
	}

	private void initializeScene(ModelLoader modelLoader,
			BulletAppState bulletAppState) {
		Spatial scene = modelLoader.getScene();
		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(
				scene);
		RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0);
		scene.addControl(landscape);
		bulletAppState.getPhysicsSpace()
					  .add(landscape);
	}

}
