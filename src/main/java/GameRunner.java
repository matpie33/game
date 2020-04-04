import com.jme3.system.AppSettings;
import core.GameSetUp;

public class GameRunner {

	public static void main(String[] args) {
		GameSetUp gameSetUp = GameSetUp.getInstance();
		AppSettings settings = new AppSettings(true);
		settings.setFullscreen(true);
		gameSetUp.setShowSettings(false);
		gameSetUp.setSettings(settings);
		gameSetUp.start();
	}
}
