package net.ximias.persistence;

import javafx.scene.paint.Color;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Contains application-wide constants.
 */
public interface ApplicationConstants {
	//public static final Color OTHER = new Color(1.0,0,0,1.0);
	
	Color MISSING = new Color(0.2, 1.0, 0.2, 1.0);
	
	int VS_ID = 1;
	int NC_ID = 2;
	int TR_ID = 3;
	
	int INDAR_ID = 2;
	int HOSSIN_ID = 4;
	int AMERISH_ID = 6;
	int ESAMIR_ID = 8;
	
	double DEFAULT_EFFECT_INTENSITY = 1;
	double DEFAULT_BACKGROUND_INTENSITY = 0.1;
	double DEFAULT_BACKGROUND_BRIGHTENS = 0.5;
	
	int BACKUP_POLLING_RATE_IN_MS = 2000;
	
	String[] XIMIAS_IDS = {"8287548916321388337", "5428653157989617089", "5428736826372335153"};
	
	JSONObject EMPTY_JSON = new JSONObject("{}");
	
	String VERSION_NAME = "Beta";
	String VERSION = "0.4.2";
	
	String INTRO_TEXT = "Welcome to Xou " + VERSION_NAME + " " + VERSION + ".\n" +
	                    "This is an early version of the application. Expect kinks, bugs and sharp edges.\n" +
	                    "Any and all feedback is welcome, be it bug reports, feature requests, critique, or downright complaints.\n" +
	                    "You can find a link to open a Discord server in the \"Planned features\" tab, if you want to reach out to me. \n" +
	                    "Just know that I am a single developer, and thus progress might be slow, sluggish or halting.\n" +
	                    "\n" +
	                    "That out of the way, I hope you like what you see so far.\n" +
	                    "\n" +
	                    "Have fun on the continents of Auraxis. Now with ambient lighting.\n" +
	                    "~Ximias.";
	long EXP_DATE = new GregorianCalendar(2020, Calendar.DECEMBER, 30, 0, 1).getTime().getTime();
	String EXP_MESSAGE = "This is a friendly reminder that your version is pretty old by now.\n" +
	                     "Updates and fixes has been made, so there is no longer a reason to run this old, archaic monstrosity.\n" +
	                     "Me being me, I'm not going to stop you.. -I'll assume you have your reasons.\n" +
	                     "~Ximias.";
	
	String[] PLANNED_FEATURES = new String[]{
			"Development has been started on the following:",
			"Effect settings window where effects can be modified and created from scratch. Initial pass will not include peripheral effects",
			"Shareable effect and color settings.",
			"KeyboardTab effects. Planned support for Logitech, probably Razer chroma, and maybe Steelseries.",
			"On the to-do:",
			"Finding a name that is not the working title of the project (Feel free to send in ideas)",
			"Individual continent properties and the option to modify continent ambient colors",
			"Probably a tutorial for custom effect creation. PlanetSide2 events are quirky.", //Effect for getting killed with The Commissioner or Pump action
			"Multiple skins for the application interface",
			"Effect profiles, so multiple behaviours can be saved and switched between."
	};
	
	String[] FINISHED_FEATURES = new String[]{
			"Possibly hardware-specific effects, like ripple or waves. Though those would not render correctly on the Hue",
			"Philips Hue Entertainment support",
			"Philips Hue Entertainment specific effects",
			"Incorporating character select into the nice user interface",
			"Intensity slider, to reduce how distracting effects are",
			"File logging",
			"Automatic keymap import. Set colors of in-game actions"
	};
	
	String[] REJECTED_FEATURES = new String[]{
			"THE FOLLOWING features are not possible because the PlanetSide2 census API does not provide the required information:",
			"Displaying remaining health/shields in any way.",
			"Displaying a hit effect or damage effect.",
			"Displaying an effect upon receiving a revive request.",
			"Day/Night cycle. I'd have loved that myself",
			"THE FOLLOWING features I can't be bothered to do. If you _really_ want them, go bother me. If enough people do, I might be asked:",
			"Playing any sound. \"Recursion\" already does this, if you want sound with your PlanetSide events",
			"Displaying images or multicolored effects on the screen. I might do text, but the app is designed to display effects on Philips Hue or keyboards.",
			"There is a very slim chance that this app will ever support other games, as the PlanetSide API is pretty... Unique",
	};
}
