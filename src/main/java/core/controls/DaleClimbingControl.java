package core.controls;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import core.GameApplication;

import java.util.ArrayList;
import java.util.List;

public class DaleClimbingControl extends AbstractControl {

	private Camera camera;
	private Node rootNode;

	public DaleClimbingControl() {
		GameApplication instance = GameApplication.getInstance();
		camera = instance.getCamera();
		rootNode = instance.getRootNode();
	}

	@Override
	protected void controlUpdate(float tpf) {
		CharacterControl control = spatial.getControl(CharacterControl.class);
		Vector3f extent = ((BoundingBox) spatial.getWorldBound()).getExtent(
				new Vector3f());
		CollisionResult closestCollision = getClosestObjectFromCharacterHeadForward(
				control, extent);
		boolean isVeryCloseToObstacle = isVeryCloseToObstacle(control, extent,
				closestCollision);
		if (isVeryCloseToObstacle) {
			boolean enoughSpaceToWalkOnIt = isEnoughSpaceToWalkOnIt(
					closestCollision.getContactPoint(),
					control.getViewDirection(), extent);
			if (enoughSpaceToWalkOnIt) {
				control.setEnabled(false);
			}
		}

	}

	private boolean isEnoughSpaceToWalkOnIt(Vector3f contactPoint,
			Vector3f viewDirection, Vector3f extent) {
		List<Vector3f> points = getPointsToCheckEnoughHeightToWalk(contactPoint,
				viewDirection, extent);

		for (Vector3f toCheck : points) {
			if (!isEnoughToStandInPoint(extent, toCheck)) {
				return false;
			}
		}
		return true;
	}

	private List<Vector3f> getPointsToCheckEnoughHeightToWalk(
			Vector3f contactPoint, Vector3f viewDirection,
			Vector3f extentOfCharacter) {
		Vector3f point = contactPoint.add(
				extentOfCharacter.mult(viewDirection));
		Vector3f leftFromPoint = point.add(camera.getLeft()
												 .mult(extentOfCharacter));
		Vector3f rightFromPoint = point.add(camera.getLeft()
												  .negate()
												  .mult(extentOfCharacter));
		List<Vector3f> points = new ArrayList<>();
		points.add(point);
		points.add(leftFromPoint);
		points.add(rightFromPoint);
		return points;
	}

	private boolean isEnoughToStandInPoint(Vector3f extent,
			Vector3f farthestPoint) {
		Vector3f sizeOfSpatialDirectionY = Vector3f.UNIT_Y.mult(extent);
		Vector3f rayStart = farthestPoint.add(sizeOfSpatialDirectionY);
		Ray ray = new Ray(rayStart, Vector3f.UNIT_Y.negate());
		CollisionResults collisionResults = new CollisionResults();
		rootNode.collideWith(ray, collisionResults);
		CollisionResult closestCollision = collisionResults.getClosestCollision();
		return closestCollision.getDistance() - sizeOfSpatialDirectionY.length()
				< 0.5f;
	}

	private boolean isVeryCloseToObstacle(CharacterControl control,
			Vector3f extent, CollisionResult closestCollision) {
		return closestCollision != null &&
				closestCollision.getDistance() - extent.mult(
						control.getViewDirection())
													   .length() < 0.5f
				&& !control.onGround();
	}

	private CollisionResult getClosestObjectFromCharacterHeadForward(
			CharacterControl control, Vector3f extent) {
		Ray ray = new Ray(spatial.getWorldTranslation()
								 .add(Vector3f.UNIT_Y.mult(extent.getY())),
				control.getViewDirection());
		CollisionResults collisionResults = new CollisionResults();
		rootNode.collideWith(ray, collisionResults);
		return collisionResults.getClosestCollision();
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}
}
