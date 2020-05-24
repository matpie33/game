package core.appState;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import core.GameApplication;
import dto.ObjectsHolderDTO;
import external.HeightBasedAlphaMapGenerator;

public class TerrainAppState extends AbstractAppState {

	private HeightBasedAlphaMapGenerator heightBasedAlphaMapGenerator;
	private ObjectsHolderDTO objectsHolderDTO;

	public TerrainAppState(ObjectsHolderDTO objectsHolderDTO) {
		this.objectsHolderDTO = objectsHolderDTO;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		Material material = new Material(GameApplication.getInstance()
														.getAssetManager(),
				"Common/MatDefs/Terrain/Terrain.j3md");

		Texture road = objectsHolderDTO.getRoad();
		road.setWrap(Texture.WrapMode.Repeat);
		material.setTexture("Tex3", road);
		material.setFloat("Tex3Scale", 64f);

		Texture heightMapImage = objectsHolderDTO.getHeightMap();
		AbstractHeightMap heightmap = new ImageBasedHeightMap(
				heightMapImage.getImage());
		heightmap.load();

		heightBasedAlphaMapGenerator = new HeightBasedAlphaMapGenerator(
				heightmap);
		heightBasedAlphaMapGenerator.setMaxHeight(255f);
		heightBasedAlphaMapGenerator.setTex1Height(0.1f);
		heightBasedAlphaMapGenerator.setTex2Height(1f);
		heightBasedAlphaMapGenerator.setTex3Height(3f);

		Texture texture = new Texture2D(
				heightBasedAlphaMapGenerator.renderAlphaMap());
		material.setTexture("Alpha", texture);
		int patchSize = 65;
		TerrainQuad terrain = new TerrainQuad("my terrain", patchSize, 513,
				heightmap.getHeightMap());
		objectsHolderDTO.setTerrain(terrain);

		terrain.setMaterial(material);
	}


}
