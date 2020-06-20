package core.appState;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bullet.BulletAppState;
import core.controllers.CollisionDetectionAppState;

public class AddPhysicsAppState extends BaseAppState {
	@Override
	protected void initialize(Application app) {
		BulletAppState bulletAppState = new BulletAppState();
		bulletAppState.setDebugEnabled(true);
		app.getStateManager()
		   .attach(bulletAppState);
		bulletAppState.getPhysicsSpace()
					  .addCollisionListener(app.getStateManager()
											   .getState(
													   CollisionDetectionAppState.class));
	}

	@Override
	protected void cleanup(Application app) {

	}

	@Override
	protected void onEnable() {

	}

	@Override
	protected void onDisable() {

	}
}
