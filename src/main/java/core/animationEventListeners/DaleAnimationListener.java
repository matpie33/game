package core.animationEventListeners;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.scene.Spatial;
import constants.NodeNames;
import core.appState.CarriedObjectAppState;
import core.appState.DaleHPAppState;
import core.appState.DaleLedgeGrabAppState;
import dto.GameStateDTO;
import dto.KeyPressDTO;
import enums.ClimbingState;

public class DaleAnimationListener implements AnimEventListener {

	public static final String STAND_ANIMATION = "stand";
	public static final String RUN_ANIMATION = "run";
	public static final String WALK_BACK_ANIMATION = "walk_back";
	public static final String HOLDING_OBJECT = "hold_object";
	private static final String DEAD_ANIMATION = "dead";
	private static final String GRABBING_LEDGE = "grabbingLedge";
	private static final String MOVE_IN_LEDGE = "moveInLedge";
	private final Application app;
	private AnimChannel channel;
	private GameStateDTO gameStateDTO;

	public DaleAnimationListener(GameStateDTO gameStateDTO,
			Application app) {
		this.gameStateDTO = gameStateDTO;
		this.app = app;
	}

	public void setUpAnimations() {
		Spatial dale = getDale();
		AnimControl control = dale.getControl(AnimControl.class);
		control.addListener(this);
		channel = control.createChannel();

	}

	private Spatial getDale() {
		return ((SimpleApplication) app).getRootNode()
										.getChild(NodeNames.getDale());
	}

	@Override
	public void onAnimCycleDone(AnimControl animControl,
			AnimChannel animChannel, String previousAnimation) {
		if (previousAnimation.equals(DEAD_ANIMATION)) {
			return;
		}
		channel.setLoopMode(LoopMode.Loop);

		DaleHPAppState hpState = app.getStateManager()
									.getState(DaleHPAppState.class);
		if (!hpState.isAlive()) {
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
		KeyPressDTO keyPressDTO = gameStateDTO.getKeyPressDTO();
		DaleHPAppState hpState = app.getStateManager()
									.getState(DaleHPAppState.class);
		if (!hpState.isAlive()) {
			return;
		}

		boolean onGround = getDale().getControl(CharacterControl.class)
									.onGround();
		if (onGround) {
			if (keyPressDTO.isMoveForwardPress()) {
				setAnimation(RUN_ANIMATION);
			}
			else if (keyPressDTO.isMoveBackwardPress()) {
				setAnimation(WALK_BACK_ANIMATION);
			}
			else if (app.getStateManager()
						.getState(CarriedObjectAppState.class)
						.isEnabled()) {
				setAnimation(HOLDING_OBJECT);
			}
			else {
				setAnimation(STAND_ANIMATION);
			}

		}
		else {
			DaleLedgeGrabAppState ledgeGrabAppState = app.getStateManager()
											 .getState(
													 DaleLedgeGrabAppState.class);
			if (ledgeGrabAppState.getClimbingState()
					   .equals(ClimbingState.GRABBING_LEDGE)) {
				setAnimation(GRABBING_LEDGE);
			}
			else if (ledgeGrabAppState.getClimbingState()
							.equals(ClimbingState.MOVE_IN)) {
				setAnimation(MOVE_IN_LEDGE);
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
