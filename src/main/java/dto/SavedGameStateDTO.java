package dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.HashSet;
import java.util.Set;

@XStreamAlias("savedGame")
public class SavedGameStateDTO {

	@XStreamAlias("daleState")
	private DaleSavedStateDTO daleSavedStateDTO;
	@XStreamAlias("dogStates")
	private Set<DogSavedStateDTO> dogsSavedStateDTOs = new HashSet<>();
	@XStreamAlias("otherObjects")
	private Set<ObjectSavedStateDTO> otherObjectsStateDTOs = new HashSet<>();

	public DaleSavedStateDTO getDaleSavedStateDTO() {
		return daleSavedStateDTO;
	}

	public void setDaleSavedStateDTO(DaleSavedStateDTO daleSavedStateDTO) {
		this.daleSavedStateDTO = daleSavedStateDTO;
	}

	public Set<DogSavedStateDTO> getDogsSavedStateDTOs() {
		return dogsSavedStateDTOs;
	}

	public Set<ObjectSavedStateDTO> getOtherObjectsStateDTOs() {
		return otherObjectsStateDTOs;
	}
}
