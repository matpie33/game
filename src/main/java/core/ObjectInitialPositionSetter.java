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

import java.util.ArrayList;
import java.util.List;

public class ObjectInitialPositionSetter {

	private List<Vector3f> treesCoordinates = new ArrayList<>();
	private static final int FIRST_COORDINATE_OF_TREE_Z = 0;
	private static final int FIRST_COORDINATE_OF_TREE_X = 15;
	private static final int INCREASE_X_BY = 20;
	private static final int INCREASE_Z_BY = 40;

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

		for (int i = 0; i < trees.size(); i++) {
			Spatial spatial = trees.get(i);
			Vector3f currentCoordinate = treesCoordinates.get(i);
			spatial.move(currentCoordinate.getX(), currentCoordinate.getY(),
					currentCoordinate.getZ());
			rootNode.attachChild(spatial);
		}

		rootNode.attachChild(modelLoader.getDale());
		rootNode.attachChild(modelLoader.getScene());
	}

	public void initializeObjects(ModelLoader modelLoader,
			AppStateManager stateManager) {
		BulletAppState bulletAppState = initializeBulletAppState(stateManager);
		initializeScene(modelLoader, bulletAppState);
		initializeDale(modelLoader, bulletAppState);
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
		CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f,
				1);
		CharacterControl player = new CharacterControl(capsuleShape, 0.05f);
		player.setJumpSpeed(20);
		player.setFallSpeed(30);
		player.setGravity(new Vector3f(0, -10f, 0));
		model.addControl(player);
		player.setPhysicsLocation(new Vector3f(0, 10, 0));

		bulletAppState.getPhysicsSpace()
					  .add(player);
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
