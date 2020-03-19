import core.GameSetUp;

public class GameRun {

	public static void main (String [] args){
		GameSetUp gameSetUp = new GameSetUp();
		gameSetUp.setShowSettings(false);
		gameSetUp.start();
	}
}
