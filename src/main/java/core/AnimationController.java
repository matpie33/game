package core;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;

public class AnimationController implements AnimEventListener {

	public static final String STAND_ANIMATION = "stand";
	public static final String RUN_ANIMATION = "run";
	public static final String WALK_BACK_ANIMATION = "walk_back";
	private AnimChannel channel;
	private DaleState daleState;

	public AnimationController(DaleState daleState) {
		this.daleState = daleState;
	}

	public void setUpAnimations(ModelLoader modelLoader) {
		AnimControl control = modelLoader.getDale()
										 .getControl(AnimControl.class);
		control.addListener(this);
		channel = control.createChannel();
	}


	@Override
	public void onAnimCycleDone(AnimControl animControl,
			AnimChannel animChannel, String previousAnimation) {
		if (daleState.isMovingForward()){
			animChannel.setAnim(RUN_ANIMATION);
		}
		else if (daleState.isMovingBackward()){
			animChannel.setAnim(WALK_BACK_ANIMATION);
		}
		else{
			animChannel.setAnim(STAND_ANIMATION);
		}

	}

	@Override
	public void onAnimChange(AnimControl animControl, AnimChannel animChannel,
			String s) {

	}

	public void animateMovingForward() {

		if (!RUN_ANIMATION.equals(channel.getAnimationName())) {
			channel.setAnim(RUN_ANIMATION);
			channel.setLoopMode(LoopMode.DontLoop);
		}
	}

	public void animateMovingBackward() {

		if (!WALK_BACK_ANIMATION.equals(channel.getAnimationName())) {
			channel.setAnim(WALK_BACK_ANIMATION);
			channel.setLoopMode(LoopMode.DontLoop);
		}
	}


}
