package core;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;

public class AnimationController implements AnimEventListener {

	public void setUpAnimations (ModelLoader modelLoader){
		AnimControl control = modelLoader.getDale()
										 .getControl(AnimControl.class);
		control.addListener(this);
		AnimChannel channel = control.createChannel();
		channel.setAnim("my_animation");
		channel.setLoopMode(LoopMode.Loop);
	}

	@Override
	public void onAnimCycleDone(AnimControl animControl,
			AnimChannel animChannel, String s) {

	}

	@Override
	public void onAnimChange(AnimControl animControl, AnimChannel animChannel,
			String s) {

	}
}
