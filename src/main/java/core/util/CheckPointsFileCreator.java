package core.util;

import java.io.File;
import java.io.IOException;

public class CheckPointsFileCreator {

	public static final String CHECKPOINT_FILENAME = "checkpoint.j3o";

	public File createFile() {
		try {
			File file = new File(CHECKPOINT_FILENAME);
			file.createNewFile();
			return file;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getCheckpointFilename() {
		return CHECKPOINT_FILENAME;
	}
}
