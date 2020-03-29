package core;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;

public class AnimationController implements AnimEventListener {

	public static final String STAND_ANIMATION = "stand";
	public static final String WALK_ANIMATION = "run";
	private AnimChannel channel;

	public void setUpAnimations(ModelLoader modelLoader) {
		AnimControl control = modelLoader.getDale()
										 .getControl(AnimControl.class);
		control.addListener(this);
		channel = control.createChannel();
	}


	@Override
	public void onAnimCycleDone(AnimControl animControl,
			AnimChannel animChannel, String previousAnimation) {
		if (WALK_ANIMATION.equals(previousAnimation)) {
			animChannel.setAnim(STAND_ANIMATION);
		}
	}

	@Override
	public void onAnimChange(AnimControl animControl, AnimChannel animChannel,
			String s) {

	}

	public void animateMovingForward() {

		if (!WALK_ANIMATION.equals(channel.getAnimationName())) {
			channel.setAnim(WALK_ANIMATION);
			channel.setLoopMode(LoopMode.DontLoop);
		}
	}


}
