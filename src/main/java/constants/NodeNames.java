package constants;

public class NodeNames {
	private static final String THROWABLES = "throwables";
	private static final String GAME_OBJECTS = "gameObjects";
	private static final String ADDITIONAL_OBJECTS = "additionalObjects";
	private static final String IMMOBILE_OBJECTS = "immobileObjects";
	private static final String FIELD_OF_VIEW = "fieldOfView";
	private static final String DOGS = "dogs";
	private static final String SKY = "Sky";
	private static final String HOUSE = "house";
	private static final String DALE = "dalev4";
	private static final String BOX = "box";
	private static final String TRASHBIN = "trashbin";
	private static final String MARK = "mark";
	private static final String DOG = "dog";
	private static final String BROKEN_FENCE = "broken_fence";
	private static final String HOUSE_WITH_EXIT = "house_with_exit";
	private static final String MAP = "map";
	private static final String ARROW = "arrow";
	private static final String SCENE = "-scene";
	private static final String OGREMESH = "-ogremesh";
	private static final String NODE = "_node";

	public static String getImmobileObjects() {
		return IMMOBILE_OBJECTS;
	}

	public static String getFieldOfView() {
		return FIELD_OF_VIEW;
	}

	public static String getAdditionalObjects() {
		return ADDITIONAL_OBJECTS;
	}

	public static String getGameObjects() {
		return GAME_OBJECTS;
	}

	public static String getSky() {
		return SKY;
	}

	public static String getThrowables() {
		return THROWABLES;
	}

	public static String getDogs() {
		return DOGS;
	}

	public static String getHouse() {
		return HOUSE + SCENE + NODE;
	}

	public static String getDale() {
		return DALE + OGREMESH;
	}

	public static String getBox() {
		return BOX + OGREMESH;
	}

	public static String getTrashbin() {
		return TRASHBIN + OGREMESH;
	}

	public static String getMark() {
		return MARK + OGREMESH;
	}

	public static String getDog() {
		return DOG + OGREMESH;
	}

	public static String getBrokenFence() {
		return BROKEN_FENCE + OGREMESH;
	}

	public static String getHouseWithExit() {
		return HOUSE_WITH_EXIT + SCENE + NODE;
	}

	public static String getMap() {
		return MAP + SCENE + NODE;
	}

	public static String getArrow() {
		return ARROW + OGREMESH;
	}
}
