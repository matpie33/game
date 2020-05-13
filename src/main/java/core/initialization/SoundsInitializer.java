package core.initialization;

import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import core.GameApplication;

public class SoundsInitializer {

	public static final String MUSIC_DIRECTORY = "music/";
	public static final String SOUND_EXTENSION = ".wav";

	public void addMusic() {
		GameApplication gameApplication = GameApplication.getInstance();
		AudioNode music = new AudioNode(gameApplication.getAssetManager(),
				MUSIC_DIRECTORY + "level1" + SOUND_EXTENSION,
				AudioData.DataType.Buffer);
		music.setPositional(false);
		music.setLooping(true);
		music.setVolume(0.1f);
		music.play();
		gameApplication.getRootNode()
					   .attachChild(music);
	}

}
