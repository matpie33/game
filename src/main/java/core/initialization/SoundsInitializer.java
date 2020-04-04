package core.initialization;

import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import core.GameApplication;

public class SoundsInitializer {

	public static final String MUSIC_DIRECTORY = "music/";
	public static final String SOUND_EXTENSION = ".wav";

	public void addMusic() {
		GameApplication gameApplication = GameApplication.getInstance();
		AudioNode audio_gun = new AudioNode(gameApplication.getAssetManager(),
				MUSIC_DIRECTORY + "level1" + SOUND_EXTENSION,
				AudioData.DataType.Buffer);
		audio_gun.setPositional(false);
		audio_gun.setLooping(true);
		audio_gun.setVolume(1);
		//		audio_gun.play();
		gameApplication.getRootNode()
					   .attachChild(audio_gun);
	}

}
