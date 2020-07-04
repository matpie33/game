package core.controllers;

import com.thoughtworks.xstream.XStream;
import dto.SavedGameStateDTO;

import java.io.File;

public class SavedStateLoader {

	private final XStream xStream;

	public SavedStateLoader(XStream xStream) {
		this.xStream = xStream;
	}

	public SavedGameStateDTO load(File file) {
		Object savedGameState = xStream.fromXML(file);
		return ((SavedGameStateDTO) savedGameState);
	}
}
