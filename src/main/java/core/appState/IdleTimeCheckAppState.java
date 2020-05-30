package core.appState;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.*;

public class IdleTimeCheckAppState extends AbstractAppState
		implements RawInputListener {

	private float idleTime;

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		app.getInputManager()
		   .addRawInputListener(this);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		idleTime += tpf;
	}

	public float getIdleTime() {
		return idleTime;
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
		idleTime = 0;

	}

	@Override
	public void onMouseButtonEvent(MouseButtonEvent evt) {

	}

	@Override
	public void onKeyEvent(KeyInputEvent evt) {
		idleTime = 0;
	}

	@Override
	public void onTouchEvent(TouchEvent evt) {

	}
}
