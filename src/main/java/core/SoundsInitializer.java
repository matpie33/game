package core;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;

public class SoundsInitializer {

	public static final String MUSIC_DIRECTORY = "music/";
	public static final String SOUND_EXTENSION = ".wav";
	private Node rootNode;
	private AssetManager assetManager;

	public SoundsInitializer(Node rootNode, AssetManager assetManager) {
		this.rootNode = rootNode;
		this.assetManager = assetManager;
	}

	public void addMusic() {
		AudioNode audio_gun = new AudioNode(assetManager,
				MUSIC_DIRECTORY + "level1" + SOUND_EXTENSION,
				AudioData.DataType.Buffer);
		audio_gun.setPositional(false);
		audio_gun.setLooping(true);
		audio_gun.setVolume(1);
//		audio_gun.play();
		rootNode.attachChild(audio_gun);
	}

}
