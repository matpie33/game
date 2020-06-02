package dto;

import java.util.ArrayList;
import java.util.List;

public class GameStateDTO {

	private List<DogStateDTO> dogStateDTOS = new ArrayList<>();
	private DaleStateDTO daleStateDTO;
	private KeyPressDTO keyPressDTO = new KeyPressDTO();

	public KeyPressDTO getKeyPressDTO() {
		return keyPressDTO;
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
