package dto;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("dogState")
public class DogSavedStateDTO extends ObjectSavedStateDTO{

	@XStreamAlias("hp")
	private int hp;

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}
}
