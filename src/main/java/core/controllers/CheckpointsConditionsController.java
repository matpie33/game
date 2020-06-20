package core.controllers;

import com.jme3.scene.Node;
import constants.NodeNames;

public class CheckpointsConditionsController {

	private final Node rootNode;
	private int checkpointNumber;

	public CheckpointsConditionsController(Node rootNode) {
		this.rootNode = rootNode;
	}

	public boolean isNextCheckpointConditionPassedWithUpdateCheckpoint() {
		if (isNextCheckpointConditionPassed()) {
			checkpointNumber++;
			return true;
		}
		return false;
	}

	private boolean isNextCheckpointConditionPassed() {
		if (checkpointNumber == 0) {
			return ((Node) rootNode.getChild(NodeNames.getDogs())).getChildren()
															 .size() == 5;
		}

		return false;
	}

}
