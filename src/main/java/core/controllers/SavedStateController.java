package core.controllers;

import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import com.thoughtworks.xstream.XStream;
import dto.SavedGameStateDTO;

import java.io.File;

public class SavedStateController {

	public static final String FILE_NAME_TO_SAVE_GAME_IN = "test.xml";
	private SavedStateLoader savedStateLoader;
	private StateForSaveCreator stateForSaveCreator;

	public SavedStateController() {
		XStream xStream = new XStream();
		xStream.processAnnotations(SavedGameStateDTO.class);
		savedStateLoader = new SavedStateLoader(xStream);
		stateForSaveCreator = new StateForSaveCreator(xStream);
	}

	public void save(Node rootNode, AppStateManager appStateManager) {
		File file = new File(FILE_NAME_TO_SAVE_GAME_IN);
		stateForSaveCreator.create(file, rootNode, appStateManager);
	}

	public SavedGameStateDTO load() {
		return savedStateLoader.load(new File(FILE_NAME_TO_SAVE_GAME_IN));
	}

	public boolean doesCheckpointExist() {
		return new File(FILE_NAME_TO_SAVE_GAME_IN).exists();
	}
}
