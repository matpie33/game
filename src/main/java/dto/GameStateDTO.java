package dto;

import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import java.util.ArrayList;
import java.util.List;

public class GameStateDTO {

	private ThrowableObjectCursorDTO throwableObjectCursorDTO = new ThrowableObjectCursorDTO();
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

	public void addDogMovement(DogStateDTO dogStateDTO) {
		dogStateDTOS.add(dogStateDTO);
	}
}
