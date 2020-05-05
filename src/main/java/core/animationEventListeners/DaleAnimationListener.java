package core.animationEventListeners;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import dto.DaleStateDTO;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;

public class DaleAnimationListener implements AnimEventListener {

	public static final String STAND_ANIMATION = "stand";
	public static final String RUN_ANIMATION = "run";
	public static final String WALK_BACK_ANIMATION = "walk_back";
	public static final String HOLDING_OBJECT = "hold_object";
	private static final String DEAD_ANIMATION = "dead";
	private static final String GRABBING_LEDGE = "grabbingLedge";
	private static final String MOVE_IN_LEDGE = "moveInLedge";
	private AnimChannel channel;
	private GameStateDTO gameStateDTO;
	private ObjectsHolderDTO objectsHolderDTO;

	public DaleAnimationListener(GameStateDTO gameStateDTO,
			ObjectsHolderDTO objectsHolderDTO) {
		this.gameStateDTO = gameStateDTO;
		this.objectsHolderDTO = objectsHolderDTO;
	}

	public void setUpAnimations() {
		AnimControl control = objectsHolderDTO.getDale()
											  .getControl(AnimControl.class);
		control.addListener(this);
		channel = control.createChannel();

	}

	@Override
	public void onAnimCycleDone(AnimControl animControl,
			AnimChannel animChannel, String previousAnimation) {
		if (previousAnimation.equals(DEAD_ANIMATION)) {
			return;
		}
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		if (!daleStateDTO.isAlive()) {
			setAnimation(DEAD_ANIMATION);
			channel.setLoopMode(LoopMode.DontLoop);
		}
		else {
			handleAnimation();
		}

	}

	@Override
	public void onAnimChange(AnimControl animControl, AnimChannel animChannel,
			String s) {

	}

	public void handleAnimation() {
		DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
		if (!daleStateDTO.isAlive()){
			return;
		}

		if (daleStateDTO.isGrabbingLedge().inProgress()) {
			setAnimation(GRABBING_LEDGE);
		}
		else if (daleStateDTO.isMoveInLedge().inProgress()) {
			setAnimation(MOVE_IN_LEDGE);
		}
		else if (daleStateDTO.isMovingForward()) {
			setAnimation(RUN_ANIMATION);
		}
		else if (daleStateDTO.isMovingBackward()) {
			setAnimation(WALK_BACK_ANIMATION);
		}
		else {
			if (daleStateDTO.isCarryingThrowableObject()) {
				setAnimation(HOLDING_OBJECT);
			}
			else {
				setAnimation(STAND_ANIMATION);
			}
		}
	}

	public void setAnimation(String animation) {
		if (!animation.equals(channel.getAnimationName())) {
			channel.setAnim(animation);
		}
	}
}
