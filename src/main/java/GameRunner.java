import com.jme3.system.AppSettings;
import core.GameSetUp;

public class GameRunner {

	public static void main(String[] args) {
		GameSetUp gameSetUp = new GameSetUp();
		AppSettings settings = new AppSettings(true);
		gameSetUp.setShowSettings(false);
		gameSetUp.setSettings(settings);
		gameSetUp.start();
	}
}
