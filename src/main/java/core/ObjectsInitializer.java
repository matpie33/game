package core;

import com.jme3.app.state.AppStateManager;
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
import constants.NodeNames;
import dto.ObjectsControlsDTO;

import java.util.ArrayList;
import java.util.List;

public class ObjectsInitializer {

	private List<Vector3f> treesCoordinates = new ArrayList<>();
	private static final int FIRST_COORDINATE_OF_TREE_Z = 0;
	private static final int FIRST_COORDINATE_OF_TREE_X = 15;
	private static final int INCREASE_X_BY = 20;
	private static final int INCREASE_Z_BY = 40;

	private DaleState daleState;
	private ObjectsControlsDTO objectsControlsDTO = new ObjectsControlsDTO();

	private void initializeTreesCoordinates(int numberOfTrees) {
		treesCoordinates = new ArrayList<>();
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
			increaseXNow = !increaseXNow;
		}
	}

	public void addObjectsToScene(ModelLoader modelLoader, Node rootNode) {
		List<Spatial> trees = modelLoader.getTrees();
		initializeTreesCoordinates(trees.size());

		List<RigidBodyControl> treesControls = objectsControlsDTO.getTreesControls();
		for (int i = 0; i < treesControls.size(); i++) {
			RigidBodyControl control = treesControls.get(i);
			Vector3f currentCoordinate = treesCoordinates.get(i);
			control.setPhysicsLocation(new Vector3f(currentCoordinate.getX(),
					currentCoordinate.getY(),
					currentCoordinate.getZ()));
		}
		Node throwables = new Node(NodeNames.THROWABLES);
		throwables.attachChild(modelLoader.getBox());

		trees.forEach(rootNode::attachChild);
		objectsControlsDTO.getBoxControl().setPhysicsLocation(new Vector3f
				(-20,10,4));

		rootNode.attachChild(modelLoader.getMark());
		rootNode.attachChild(modelLoader.getDale());
		rootNode.attachChild(modelLoader.getScene());
		rootNode.attachChild(throwables);
	}

	public DaleState initializeObjects(ModelLoader modelLoader,
			AppStateManager stateManager) {
		BulletAppState bulletAppState = initializeBulletAppState(stateManager);
		initializeScene(modelLoader, bulletAppState);
		initializeDale(modelLoader, bulletAppState);
		initializeTree(modelLoader, bulletAppState);
		initializeMark(modelLoader, bulletAppState);
		initializeBox(modelLoader, bulletAppState);
		return daleState;
	}

	private void initializeBox(ModelLoader modelLoader,
			BulletAppState bulletAppState) {
		CollisionShape boxShape = CollisionShapeFactory.createBoxShape(
				modelLoader.getBox());
		RigidBodyControl rigidBodyControl = new RigidBodyControl(boxShape,
				0.5f);
		rigidBodyControl.setGravity(new Vector3f(0,5f,0));
		modelLoader.getBox()
				   .addControl(rigidBodyControl);
		bulletAppState.getPhysicsSpace().add(rigidBodyControl);
		objectsControlsDTO.addBoxControl(rigidBodyControl);

	}

	private void initializeMark(ModelLoader modelLoader,
			BulletAppState bulletAppState) {
		modelLoader.getMark().setLocalTranslation(5,5,5);
	}

	private void initializeTree(ModelLoader modelLoader,
			BulletAppState bulletAppState) {
		List<Spatial> trees = modelLoader.getTrees();
		for (Spatial tree : trees) {
			CollisionShape collisionShape = CollisionShapeFactory.createMeshShape(
					tree);
			RigidBodyControl rigidBodyControl = new RigidBodyControl(
					collisionShape, 0);
			objectsControlsDTO.addTreeControl(rigidBodyControl);
			tree.addControl(rigidBodyControl);
			bulletAppState.getPhysicsSpace().add(rigidBodyControl);
		}
	}

	private BulletAppState initializeBulletAppState(
			AppStateManager stateManager) {
		BulletAppState bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		return bulletAppState;
	}

	private void initializeDale(ModelLoader modelLoader,
			BulletAppState bulletAppState) {
		Spatial model = modelLoader.getDale();
		model.rotate(0, 0, 90 * FastMath.DEG_TO_RAD);
		CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(2f,
				2f,
				1);
		daleState = new DaleState();
		daleState.setCarryingThrowableObject(false);

		CharacterControl daleControl = new CharacterControl(capsuleShape,
				0.05f);
		daleState.setCharacterControl(daleControl);
		objectsControlsDTO.setDaleControl(daleControl);
		daleControl.setJumpSpeed(20);
		daleControl.setFallSpeed(30);
		daleControl.setGravity(new Vector3f(0, -10f, 0));
		model.addControl(daleControl);
		daleControl.setPhysicsLocation(new Vector3f(0, 10, -20));

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
