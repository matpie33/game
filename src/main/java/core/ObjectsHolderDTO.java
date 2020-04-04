package core;

import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.texture.Texture;

import java.util.ArrayList;
import java.util.List;

public class ObjectsHolderDTO {

	private List<Spatial> trees = new ArrayList<>();
	private List<Spatial> boxes = new ArrayList<>();
	private Spatial dale;
	private Spatial scene;
	private Spatial mark;
	private Spatial arrow;
	private Spatial sky;

	private Texture road;
	private Texture heightMap;

	private TerrainQuad terrain;

	public TerrainQuad getTerrain() {
		return terrain;
	}

	public void setTerrain(TerrainQuad terrain) {
		this.terrain = terrain;
	}

	public List<Spatial> getTrees() {
		return trees;
	}

	public void addTree(Spatial tree) {
		trees.add(tree);
	}

	public List<Spatial> getBoxes() {
		return boxes;
	}

	public void addBox(Spatial box) {
		boxes.add(box);
	}

	public Spatial getDale() {
		return dale;
	}

	public void setDale(Spatial dale) {
		this.dale = dale;
	}

	public Spatial getScene() {
		return scene;
	}

	public void setScene(Spatial scene) {
		this.scene = scene;
	}

	public Spatial getMark() {
		return mark;
	}

	public void setMark(Spatial mark) {
		this.mark = mark;
	}

	public Spatial getArrow() {
		return arrow;
	}

	public void setArrow(Spatial arrow) {
		this.arrow = arrow;
	}

	public Spatial getSky() {
		return sky;
	}

	public void setSky(Spatial sky) {
		this.sky = sky;
	}

	public Texture getRoad() {
		return road;
	}

	public void setRoad(Texture road) {
		this.road = road;
	}

	public Texture getHeightMap() {
		return heightMap;
	}

	public void setHeightMap(Texture heightMap) {
		this.heightMap = heightMap;
	}
}
