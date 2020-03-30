package core;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class CollisionHandler implements PhysicsCollisionListener {

	private ModelLoader modelLoader;
	private AssetManager assetManager;
	private Node rootNode;

	public CollisionHandler(ModelLoader modelLoader, AssetManager assetManager,
			Node rootNode) {
		this.modelLoader = modelLoader;
		this.assetManager = assetManager;
		this.rootNode = rootNode;
	}

	@Override
	public void collision(PhysicsCollisionEvent event) {
		handleCollision(event);
	}

	private void handleCollision(PhysicsCollisionEvent event) {
		Spatial nodeA = event.getNodeA();
		Spatial nodeB = event.getNodeB();
		boolean isABox = modelLoader.getBoxes()
									.contains(nodeA);
		boolean isBBox = modelLoader.getBoxes()
									.contains(nodeB);
		if ((isABox || isBBox) && event.getAppliedImpulse() > 9) {
			if (isABox && nodeA.getParent() != null) {
				nodeA.getParent()
					 .detachChild(nodeA);
			}
			if (isBBox && nodeB.getParent() != null) {
				nodeB.getParent()
					 .detachChild(nodeB);
			}

			ParticleEmitter boxParticles = new ParticleEmitter("Box",
					ParticleMesh.Type.Triangle, 15);
			boxParticles.setSelectRandomImage(true);
			boxParticles.setRandomAngle(true);
			boxParticles.setRotateSpeed(FastMath.TWO_PI * 4);

			boxParticles.setStartColor(new ColorRGBA(ColorRGBA.Brown));
			boxParticles.setStartSize(1.5f);
			boxParticles.setEndSize(0.4f);

			boxParticles.setParticlesPerSec(0);
			boxParticles.setGravity(0, 12f, 0);
			boxParticles.setLowLife(4.4f);
			boxParticles.setHighLife(4.5f);
			boxParticles.getParticleInfluencer()
						.setInitialVelocity(new Vector3f(0, 15, 0));
			boxParticles.getParticleInfluencer()
						.setVelocityVariation(.60f);
			boxParticles.setImagesX(3);
			boxParticles.setImagesY(3);
			Material mat = new Material(assetManager,
					"Common/MatDefs/Misc/Particle.j3md");
			mat.setTexture("Texture",
					assetManager.loadTexture("models/debris.png"));
			boxParticles.setMaterial(mat);
			boxParticles.setLocalTranslation(nodeA.getLocalTranslation());
			boxParticles.emitAllParticles();
			rootNode.attachChild(boxParticles);

		}

	}

}
