import com.jme3.system.AppSettings;
import core.GameApplication;

import java.awt.*;

public class GameRunner {

	public static void main(String[] args) {
		GameApplication gameApplication = GameApplication.getInstance();
		AppSettings settings = new AppSettings(true);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
//		settings.setResolution(screenSize.width, screenSize.height);
		gameApplication.setShowSettings(false);
		gameApplication.setSettings(settings);
		gameApplication.start();
	}
}
