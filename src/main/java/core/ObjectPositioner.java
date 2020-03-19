package core;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.ArrayList;
import java.util.List;

public class ObjectPositioner {

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
	}

}
