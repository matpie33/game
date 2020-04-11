package core.animationEventListeners;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.scene.Spatial;

public class DogAnimationListener implements AnimEventListener {

	public static final String WALK = "Walk";

	private Spatial dogStateDTO;

	public DogAnimationListener(Spatial dogStateDTO) {
		this.dogStateDTO = dogStateDTO;
	}

	public void setUpAnimations() {
		AnimControl control = dogStateDTO.getControl(AnimControl.class);
		control.addListener(this);
		AnimChannel channel = control.createChannel();
		channel.setAnim(WALK);

	}

	@Override
	public void onAnimCycleDone(AnimControl control, AnimChannel channel,
			String animName) {

		channel.setAnim(WALK);
		channel.setLoopMode(LoopMode.DontLoop);

	}

	@Override
	public void onAnimChange(AnimControl control, AnimChannel channel,
			String animName) {

	}
}
