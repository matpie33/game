package dto;

import com.jme3.scene.Spatial;

import java.util.ArrayList;
import java.util.List;

public class GameStateDTO {

	private List<DogStateDTO> dogStateDTOS = new ArrayList<>();
	private DaleStateDTO daleStateDTO;
	private List<Spatial> objectsToRemove = new ArrayList<>();

	public List<Spatial> getObjectsToRemove() {
		return objectsToRemove;
	}

	public DaleStateDTO getDaleStateDTO() {
		return daleStateDTO;
	}

	public void setDaleStateDTO(DaleStateDTO daleStateDTO) {
		this.daleStateDTO = daleStateDTO;
	}

	public List<DogStateDTO> getDogStateDTOS() {
		return dogStateDTOS;
	}


	public void addDogState(DogStateDTO dogStateDTO) {
		dogStateDTOS.add(dogStateDTO);
	}
}
