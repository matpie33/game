package core.controllers;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import constants.PhysicsControls;
import core.GameApplication;
import core.gui.HUDCreator;
import dto.DaleStateDTO;
import dto.GameStateDTO;
import dto.ObjectsHolderDTO;
import enums.ObjectsTypes;

public class CollisionController implements PhysicsCollisionListener {

	public static final int MINIMUM_IMPULSE_TO_DESTROY_BOX = 9;
	public static final float STARTING_SIZE = 1.5f;
	public static final float ENDING_SIZE = 0.4f;
	public static final int NUMBER_OF_TIMES_TO_REPEAT = 0;
	public static final float GRAVITY_FORCE = 12f;
	public static final float MINIMUM_ANIMATION_TIME = 3f;
	public static final float MAXIMUM_ANIMATION_TIME = 4f;
	public static final int STARTING_VELOCITY_Y = 15;
	public static final float VELOCITY_VARIATION = .60f;
	public static final int IMAGES_IN_TEXTURE_HORIZONTALLY = 3;
	public static final int IMAGES_IN_TEXTURE_VERTICALLY = 3;
	public static final int NUMBER_OF_PIECES = 15;
	public static final String MATERIAL_DEFINITION_PATH = "Common/MatDefs/Misc/Particle.j3md";
	public static final String TEXTURE_PATH = "models/debris.png";
	public static final String TEXTURE_NAME = "Texture";
	public static final int HP_DECREASE_VALUE = 20;
	private ObjectsHolderDTO objectsHolderDTO;
	private GameStateDTO gameStateDTO;
	private ObjectsMovementController objectsMovementController;
	private HUDCreator hudCreator;

	public CollisionController(ObjectsHolderDTO objectsHolderDTO,
			GameStateDTO gameStateDTO,
			ObjectsMovementController objectsMovementController,
			HUDCreator hudCreator) {
		this.objectsHolderDTO = objectsHolderDTO;
		this.gameStateDTO = gameStateDTO;
		this.objectsMovementController = objectsMovementController;
		this.hudCreator = hudCreator;
	}

	@Override
	public void collision(PhysicsCollisionEvent event) {
		handleCollision(event);
	}

	private void handleCollision(PhysicsCollisionEvent event) {
		Spatial nodeA = event.getNodeA();
		Spatial nodeB = event.getNodeB();
		ObjectsTypes nodeAType = getObjectType(nodeA);
		ObjectsTypes nodeBType = getObjectType(nodeB);
		boolean isABox = ObjectsTypes.BOX.equals(nodeAType);
		boolean isBBox = ObjectsTypes.BOX.equals(nodeBType);

		if ((isABox || isBBox)
				&& event.getAppliedImpulse() > MINIMUM_IMPULSE_TO_DESTROY_BOX) {
			clearNode(nodeA, isABox);
			clearNode(nodeB, isBBox);

			ParticleEmitter boxParticles = new ParticleEmitter("Box",
					ParticleMesh.Type.Triangle, NUMBER_OF_PIECES);
			boxParticles.setSelectRandomImage(true);
			boxParticles.setRandomAngle(true);
			boxParticles.setRotateSpeed(FastMath.TWO_PI * 4);

			boxParticles.setStartColor(new ColorRGBA(ColorRGBA.Brown));
			boxParticles.setStartSize(STARTING_SIZE);
			boxParticles.setEndSize(ENDING_SIZE);

			boxParticles.setParticlesPerSec(NUMBER_OF_TIMES_TO_REPEAT);
			boxParticles.setGravity(0, GRAVITY_FORCE, 0);
			boxParticles.setLowLife(MINIMUM_ANIMATION_TIME);
			boxParticles.setHighLife(MAXIMUM_ANIMATION_TIME);
			boxParticles.getParticleInfluencer()
						.setInitialVelocity(
								new Vector3f(0, STARTING_VELOCITY_Y, 0));
			boxParticles.getParticleInfluencer()
						.setVelocityVariation(VELOCITY_VARIATION);
			boxParticles.setImagesX(IMAGES_IN_TEXTURE_HORIZONTALLY);
			boxParticles.setImagesY(IMAGES_IN_TEXTURE_VERTICALLY);
			AssetManager assetManager = GameApplication.getInstance()
													   .getAssetManager();
			Material material = new Material(assetManager,
					MATERIAL_DEFINITION_PATH);
			material.setTexture(TEXTURE_NAME,
					assetManager.loadTexture(TEXTURE_PATH));
			boxParticles.setMaterial(material);
			boxParticles.setLocalTranslation(nodeA.getLocalTranslation());
			boxParticles.emitAllParticles();
			GameApplication.getInstance()
						   .getRootNode()
						   .attachChild(boxParticles);

		}
		if (isDogWithDaleCollision(nodeAType, nodeBType)) {
			DaleStateDTO daleStateDTO = gameStateDTO.getDaleStateDTO();
			daleStateDTO.setHp(daleStateDTO.getHp() - HP_DECREASE_VALUE);
			objectsMovementController.moveDaleBack();
			hudCreator.setHp(daleStateDTO.getHp());
		}

	}

	private boolean isDogWithDaleCollision(ObjectsTypes nodeA,
			ObjectsTypes nodeB) {

		boolean isAnyNodeDale =
				ObjectsTypes.DALE.equals(nodeA) || ObjectsTypes.DALE.equals(
						nodeB);
		boolean isAnyNodeDog =
				ObjectsTypes.DOG.equals(nodeA) || ObjectsTypes.DOG.equals(
						nodeB);
		return isAnyNodeDale && isAnyNodeDog;
	}

	private ObjectsTypes getObjectType(Spatial node) {
		if (objectsHolderDTO.getDogs()
							.contains(node)) {
			return ObjectsTypes.DOG;
		}
		if (objectsHolderDTO.getDale()
							.equals(node)) {
			return ObjectsTypes.DALE;
		}
		if (objectsHolderDTO.getBoxes()
							.contains(node)) {
			return ObjectsTypes.BOX;
		}
		return null;
	}

	private void clearNode(Spatial node, boolean isBox) {
		if (isBox && node.getParent() != null) {
			RigidBodyControl control = node.getControl(PhysicsControls.BOX);
			control.getPhysicsSpace()
				   .remove(control);
			node.getParent()
				.detachChild(node);
		}
	}

}
