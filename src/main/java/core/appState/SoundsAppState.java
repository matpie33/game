package core.appState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;

public class SoundsAppState extends AbstractAppState {

	public static final String MUSIC_DIRECTORY = "music/";
	public static final String SOUND_EXTENSION = ".wav";

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		AudioNode music = new AudioNode(app.getAssetManager(),
				MUSIC_DIRECTORY + "level1" + SOUND_EXTENSION,
				AudioData.DataType.Buffer);
		music.setPositional(false);
		music.setLooping(true);
		music.setVolume(0.1f);
		music.play();
		((SimpleApplication) app).getRootNode()
								 .attachChild(music);
	}

}
