package dto;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class DogSavedStateDTO extends ObjectSavedStateDTO{

	private int hp;

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}
}
