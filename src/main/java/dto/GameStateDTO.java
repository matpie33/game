package dto;

import com.jme3.scene.Geometry;

import java.util.ArrayList;
import java.util.List;

public class GameStateDTO {

	private ThrowableObjectCursorDTO throwableObjectCursorDTO = new ThrowableObjectCursorDTO();
	private List<DogDataDTO> dogDataDTOS = new ArrayList<>();
	private DaleStateDTO daleStateDTO;

	public DaleStateDTO getDaleStateDTO() {
		return daleStateDTO;
	}

	public void setDaleStateDTO(DaleStateDTO daleStateDTO) {
		this.daleStateDTO = daleStateDTO;
	}

	public List<DogDataDTO> getDogDataDTOS() {
		return dogDataDTOS;
	}

	public void setCursorNotShowing() {
		throwableObjectCursorDTO.setShowing(false);
		throwableObjectCursorDTO.setThrowableObject(null);
	}

	public void setCursorShowingAt(Geometry geometryToShowCursorAt) {
		throwableObjectCursorDTO.setThrowableObject(geometryToShowCursorAt);
		throwableObjectCursorDTO.setShowing(true);
	}

	public boolean isCursorShowing() {
		return throwableObjectCursorDTO.isShowing();
	}

	public Geometry getSpatialOnWhichCursorIsShowing() {
		return throwableObjectCursorDTO.getThrowableObject();
	}

	public void addDogMovement(DogDataDTO dogDataDTO) {
		dogDataDTOS.add(dogDataDTO);
	}
}
