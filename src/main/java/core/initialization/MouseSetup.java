package core.initialization;

import com.jme3.input.InputManager;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.*;
import core.GameApplication;

public class MouseSetup implements RawInputListener {

	private InputManager inputManager;
	private float mouseIdleTime;

	public MouseSetup() {
		inputManager = GameApplication.getInstance()
									  .getInputManager();
	}

	public void setUp() {
		inputManager.addRawInputListener(this);
	}

	public void updateTimePassed(float timePassed) {
		mouseIdleTime += timePassed;
	}

	public float getIdleTime() {
		return mouseIdleTime;
	}

	@Override
	public void beginInput() {

	}

	@Override
	public void endInput() {

	}

	@Override
	public void onJoyAxisEvent(JoyAxisEvent evt) {

	}

	@Override
	public void onJoyButtonEvent(JoyButtonEvent evt) {

	}

	@Override
	public void onMouseMotionEvent(MouseMotionEvent evt) {
		mouseIdleTime = 0;

	}

	@Override
	public void onMouseButtonEvent(MouseButtonEvent evt) {

	}

	@Override
	public void onKeyEvent(KeyInputEvent evt) {

	}

	@Override
	public void onTouchEvent(TouchEvent evt) {

	}
}
