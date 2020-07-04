package core.controllers;

import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.thoughtworks.xstream.XStream;
import constants.NodeNames;
import core.appState.DaleHPAppState;
import dto.DaleSavedStateDTO;
import dto.DogSavedStateDTO;
import dto.ObjectSavedStateDTO;
import dto.SavedGameStateDTO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class StateForSaveCreator {

	private final XStream xStream;

	public StateForSaveCreator(XStream xStream) {
		this.xStream = xStream;
	}

	public void create(File fileToSaveIn, Node rootNode,
			AppStateManager appStateManager) {
		SavedGameStateDTO savedGameStateDTO = new SavedGameStateDTO();
		savedGameStateDTO.setDaleSavedStateDTO(
				createDaleState(rootNode, appStateManager));
		savedGameStateDTO.getDogsSavedStateDTOs()
						 .addAll(createDogsState(rootNode));
		Set<ObjectSavedStateDTO> otherObjectsStateDTOs = savedGameStateDTO.getOtherObjectsStateDTOs();
		otherObjectsStateDTOs.addAll(createObjectsStates(rootNode,
				NodeNames.getThrowables()));
		otherObjectsStateDTOs.addAll(createObjectsStates(rootNode,
				NodeNames.getImmobileObjects()));

		xStream.toXML(savedGameStateDTO, getWriter(fileToSaveIn));
	}

	private FileWriter getWriter(File file)  {
		try {
			return new FileWriter(file);
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Set<ObjectSavedStateDTO> createObjectsStates(
			Node rootNode, String nodeName) {
		Node throwables = (Node) rootNode.getChild(nodeName);
		return createStateDTOsForObjects(throwables);
	}

	private Set<ObjectSavedStateDTO> createStateDTOsForObjects(
			Node throwables) {
		Set<ObjectSavedStateDTO> objectSavedStateDTOS = new HashSet<>();
		for (Spatial throwable : throwables.getChildren()) {
			ObjectSavedStateDTO objectSavedStateDTO = new ObjectSavedStateDTO();
			objectSavedStateDTO.setPathToModel(getPathToModel(throwable));
			objectSavedStateDTO.setPosition(throwable.getWorldTranslation());
			objectSavedStateDTO.setRotation(throwable.getWorldRotation());
			objectSavedStateDTOS.add(objectSavedStateDTO);
		}
		return objectSavedStateDTOS;
	}

	private Set<DogSavedStateDTO> createDogsState(Node rootNode) {
		Node dogsSet = (Node) rootNode.getChild(NodeNames.getDogs());
		Set<DogSavedStateDTO> dogSavedStateDTOS = new HashSet<>();
		for (Spatial dog : dogsSet.getChildren()) {
			DogSavedStateDTO dogSavedStateDTO = new DogSavedStateDTO();
			dogSavedStateDTO.setHp(100);
			dogSavedStateDTO.setPosition(dog.getWorldTranslation());
			dogSavedStateDTO.setRotation(dog.getWorldRotation());
			dogSavedStateDTO.setPathToModel(getPathToModel(dog));
			dogSavedStateDTOS.add(dogSavedStateDTO);

		}
		return dogSavedStateDTOS;
	}

	private DaleSavedStateDTO createDaleState(Node rootNode,
			AppStateManager appStateManager) {
		DaleSavedStateDTO daleSavedStateDTO = new DaleSavedStateDTO();
		daleSavedStateDTO.setHp(appStateManager.getState(DaleHPAppState.class)
											   .getHp());
		Spatial dale = rootNode.getChild(NodeNames.getDale());
		daleSavedStateDTO.setPathToModel(getPathToModel(dale));
		daleSavedStateDTO.setPosition(dale.getWorldTranslation());
		daleSavedStateDTO.setRotation(dale.getWorldRotation());
		return daleSavedStateDTO;
	}

	private String getPathToModel(Spatial spatial) {
		return spatial.getKey()
					  .getName();
	}

}
