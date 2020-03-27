package core;

import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import dto.ThrowableObjectCursorDTO;

public class GameState {

	private ThrowableObjectCursorDTO throwableObjectCursorDTO = new
			ThrowableObjectCursorDTO();


	public void setCursorNotShowing (){
		throwableObjectCursorDTO.setShowing(false);
		throwableObjectCursorDTO.setThrowableObject(null);
	}

	public void setCursorShowingAt(Geometry geometryToShowCursorAt){
		throwableObjectCursorDTO.setThrowableObject(geometryToShowCursorAt);
		throwableObjectCursorDTO.setShowing(true);
	}

	public boolean isCursorShowing (){
		return throwableObjectCursorDTO.isShowing();
	}

	public Geometry getSpatialOnWhichCursorIsShowing(){
		return throwableObjectCursorDTO.getThrowableObject();
	}

}
