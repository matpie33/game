package dto;

import com.jme3.scene.Geometry;

import java.util.ArrayList;
import java.util.List;

public class GameStateDTO {

	private ThrowableObjectCursorDTO throwableObjectCursorDTO = new ThrowableObjectCursorDTO();

	private List<DogMovementDTO> dogMovementDTOS = new ArrayList<>();

	public List<DogMovementDTO> getDogMovementDTOS() {
		return dogMovementDTOS;
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

	public void addDogMovement(DogMovementDTO dogMovementDTO) {
		dogMovementDTOS.add(dogMovementDTO);
	}
}
