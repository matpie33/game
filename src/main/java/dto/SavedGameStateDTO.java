package dto;

import java.util.HashSet;
import java.util.Set;

public class SavedGameStateDTO {

	private DaleSavedStateDTO daleSavedStateDTO;
	private Set<DogSavedStateDTO> dogsSavedStateDTOs = new HashSet<>();
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
