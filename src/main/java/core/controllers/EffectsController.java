package core.controllers;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import core.GameApplication;

public class EffectsController {

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

	public void createBoxDestroyEffect (Spatial box){
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
		boxParticles.setLocalTranslation(box.getLocalTranslation());
		boxParticles.emitAllParticles();
		GameApplication.getInstance()
					   .getRootNode()
					   .attachChild(boxParticles);
	}


}
