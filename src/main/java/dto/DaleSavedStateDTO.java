package dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class DaleSavedStateDTO extends ObjectSavedStateDTO{

	@XStreamAlias("hp")
	private int hp;

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

}
