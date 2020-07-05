package core.controllers;

import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import com.thoughtworks.xstream.XStream;
import dto.SavedGameStateDTO;

import java.io.File;

public class GameSaveAndLoadController {

	public static final String FILE_NAME_TO_SAVE_GAME_IN = "test.xml";
	private GameStateLoader gameStateLoader;
	private GameStateSaver gameStateSaver;

	public GameSaveAndLoadController() {
		XStream xStream = new XStream();
		xStream.processAnnotations(SavedGameStateDTO.class);
		gameStateLoader = new GameStateLoader(xStream);
		gameStateSaver = new GameStateSaver(xStream);
	}

	public void save(Node rootNode, AppStateManager appStateManager) {
		File file = new File(FILE_NAME_TO_SAVE_GAME_IN);
		gameStateSaver.create(file, rootNode, appStateManager);
	}

	public SavedGameStateDTO load() {
		return gameStateLoader.load(new File(FILE_NAME_TO_SAVE_GAME_IN));
	}

	public boolean doesCheckpointExist() {
		return new File(FILE_NAME_TO_SAVE_GAME_IN).exists();
	}
}
