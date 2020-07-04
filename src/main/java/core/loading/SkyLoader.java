package core.loading;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

public class SkyLoader {

	private AssetManager assetManager;

	public SkyLoader(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public Spatial loadSky() {
		return SkyFactory.createSky(assetManager, "clouds.dds",
				SkyFactory.EnvMapType.CubeMap);
	}
}
