package core.appState;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import core.GameApplication;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.screen.Screen;

public class HUDAppState extends AbstractAppState {

	public static final String HUD_SCREEN_NAME = "HUD_SCREEN";
	public static final String LAYER1_NAME = "layer1";
	public static final String PANEL1_NAME = "panel1";
	public static final String HP_LABEL_NAME = "hp_label";
	public static final String LAVA_COLOR = "#790604";
	private Screen hudScreen;
	private static final String HP_FORMAT = "HP: %d%%";

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		GameApplication gameApplication = GameApplication.getInstance();
		NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(
				gameApplication.getAssetManager(),
				gameApplication.getInputManager(),
				gameApplication.getAudioRenderer(),
				gameApplication.getViewPort());
		Nifty nifty = niftyDisplay.getNifty();
		nifty.loadStyleFile("nifty-default-styles.xml");
		nifty.loadControlFile("nifty-default-controls.xml");
		hudScreen = new ScreenBuilder(HUD_SCREEN_NAME) {{
			controller(new DefaultScreenController());

			layer(new LayerBuilder(LAYER1_NAME) {{

				childLayoutVertical();

				panel(new PanelBuilder(PANEL1_NAME) {{

					childLayoutCenter();
					padding("10px");

					control(new LabelBuilder(HP_LABEL_NAME, "HP: 000%") {{
						color(LAVA_COLOR);
						alignLeft();
						valignTop();
						height("5%");
					}});

				}});
			}});
		}}.build(nifty);
		nifty.addScreen(HUD_SCREEN_NAME, hudScreen);


		nifty.gotoScreen(HUD_SCREEN_NAME);
		gameApplication.getGuiViewPort()
					   .addProcessor(niftyDisplay);

		setHp(100);
	}


	public void setHp (int hp){
		Label control = hudScreen.findNiftyControl(HP_LABEL_NAME,
				Label.class);
		control.setText(String.format(HP_FORMAT, hp));
	}


}
