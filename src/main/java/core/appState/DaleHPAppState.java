package core.appState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import constants.NodeNames;
import constants.PhysicsControls;
import core.GameApplication;

public class DaleHPAppState extends BaseAppState {

	public static final int HP_DECREASE_VALUE = 20;
	public static final float MINIMUM_TIME_BETWEEN_HP_DECREASE_SECONDS = 1;
	public static final int INITIAL_HP_OF_DALE = 100;

	private float timeSinceLastHpDecrease;
	private SimpleApplication application;
	private int hp;
	private boolean collisionDetected;

	public int getHp() {
		return hp;
	}

	private void handleDaleState() {

		if (collisionDetected && enoughTimePassedToDecreaseHp()) {
			timeSinceLastHpDecrease = 0;
			collisionDetected = false;
			hp = hp - HP_DECREASE_VALUE;
			moveDaleBack();
			HUDAppState hudAppState = GameApplication.getInstance()
													 .getStateManager()
													 .getState(
															 HUDAppState.class);
			hudAppState.setHp(hp);
		}

		if (hp <= 0) {
			setEnabled(false);
		}

	}

	private boolean enoughTimePassedToDecreaseHp() {
		return timeSinceLastHpDecrease
				> MINIMUM_TIME_BETWEEN_HP_DECREASE_SECONDS;
	}

	public void moveDaleBack() {
		CharacterControl control = application.getRootNode()
											  .getChild(NodeNames.getDale())
											  .getControl(PhysicsControls.DALE);
		Vector3f direction = application.getCamera()
										.getDirection();
		Vector3f multiplied = direction.mult(new Vector3f(30f, 1, 30f));
		control.setPhysicsLocation(control.getPhysicsLocation()
										  .subtract(multiplied));
	}

	@Override
	protected void initialize(Application app) {
		hp = INITIAL_HP_OF_DALE;
		application = (SimpleApplication) app;
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

	@Override
	public void update(float tpf) {
		timeSinceLastHpDecrease += tpf;
		handleDaleState();
	}

	public boolean isAlive() {
		return hp > 0;
	}

	public void setCollisionDetected() {
		this.collisionDetected = true;
	}
}
