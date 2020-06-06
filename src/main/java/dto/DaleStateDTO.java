package dto;

import com.jme3.math.Vector3f;
import enums.ClimbingState;

public class DaleStateDTO {

	private boolean alive;
	private int hp;
	private boolean isCollidingWithEnemy;
	private Vector3f ledgeCollisionPoint;
	private ClimbingState climbingState = ClimbingState.NOT_STARTED;

	public ClimbingState getClimbingState() {
		return climbingState;
	}

	public void setClimbingState(ClimbingState climbingState) {
		this.climbingState = climbingState;
	}

	public Vector3f getLedgeCollisionPoint() {
		return ledgeCollisionPoint;
	}

	public void setLedgeCollisionPoint(Vector3f ledgeCollisionPoint) {
		this.ledgeCollisionPoint = ledgeCollisionPoint;
	}

	public boolean isCollidingWithEnemy() {
		return isCollidingWithEnemy;
	}

	public void setCollidingWithEnemy(boolean collidingWithEnemy) {
		isCollidingWithEnemy = collidingWithEnemy;
	}

	public DaleStateDTO() {
		alive = true;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

}
