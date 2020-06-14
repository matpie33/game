package dto;

public class GameStateDTO {

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

}
