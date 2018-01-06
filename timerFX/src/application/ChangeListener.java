package application;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class ChangeListener implements javafx.beans.value.ChangeListener<String> {

	private int maxLength;
	private int maxValue;
	private TextField textField;

	public ChangeListener(TextField textField, int maxLength, int maxValue) {
		this.textField = textField;
		this.maxLength = maxLength;
		this.maxValue = maxValue;
	}

	public int getMaxLength() {
		return maxLength;
	}

	@Override
	public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
		if (newValue == null && this.maxLength == 2) {
			textField.setText("00");
			return;
		} else if (newValue == null && this.maxLength == 1) {
			textField.setText("0");
			return;
		}

		if (newValue.length() > maxLength && this.maxLength == 2) {
			textField.setText(newValue.substring(1, 3));
		} else if (newValue.length() > maxLength && this.maxLength == 1) {
			textField.setText(newValue.substring(1, 2));
		} else {
			if (newValue.length() == 1 && this.maxLength == 2) {
				textField.setText("0" + newValue);
			} else {
				textField.setText(newValue);
			}
		}
		int n = Integer.parseInt(textField.getText());
		if (n > maxValue) {
			textField.setText(Integer.toString(maxValue));
		}
	}

}