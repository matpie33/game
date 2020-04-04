import com.jme3.system.AppSettings;
import core.GameApplication;

public class GameRunner {

	public static void main(String[] args) {
		GameApplication gameApplication = GameApplication.getInstance();
		AppSettings settings = new AppSettings(true);
		settings.setFullscreen(true);
		gameApplication.setShowSettings(false);
		gameApplication.setSettings(settings);
		gameApplication.start();
	}
}
