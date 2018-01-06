package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.prefs.Preferences;

public class EventDataBase {

	private File file;

	public EventDataBase() {
		setPersonFilePath();
	}

	public void setPersonFilePath() {
		file = new File("EventDataBase.xml");
		if (!file.exists()) {
			FileWriter fileWriter = null;

			try {
				fileWriter = new FileWriter(file);
				fileWriter.write("");
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
