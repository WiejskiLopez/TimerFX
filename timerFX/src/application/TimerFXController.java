
package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.util.data.audio.AudioPlayer;

public class TimerFXController implements Initializable {

	private MaryInterface marytts;
	// SourceDataLine auline;
	// FloatControl volume;

	Timer timer;
	int iHours;
	int iMinutes;
	int iSeconds;
	int alerted = 0;

	public TimerFXController() {
	};

	@FXML // fx:id="start"
	private Button start;

	@FXML // fx:id="start"
	private Button stop;

	@FXML // fx:id="hours"
	private TextField hours;

	@FXML // fx:id="hours"
	private TextField minutes;

	@FXML // fx:id="hours"
	private TextField seconds;

	@FXML // fx:id="mainTable"
	TableView<Event> mainTable;

	@FXML
	TableColumn<Event, String> villageName, villageID, time, targetName, targetID,formation,direction,order,hide;

	@FXML
	private TextField newVillageName, newVillageID, newTargetName, newTargetID, newHour, newMinute, newSecond,newFormation,newDirection,newOrder,newHide;

	@FXML // fx:id="addButton"
	private Button addButton;

	@FXML // fx:id="deleteButton"
	private Button deleteButton;

	@FXML
	private Label curVillageName, curVillageID, curTargetName, curTargetID, curHour, curMinute, curSecond,curFormation,curDirection,curOrder,curHide;

	@FXML
	public Label curTime;

	@FXML
	private TextField filterField;

	@FXML // fx:id="filterButton"
	private Button filterButton;

	EventDataBase eventDataBase;

	@FXML
	private void jTextFieldKeyTyped(KeyEvent event) {
		if (!Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0").contains(event.getCharacter())) {
			event.consume();
		}
	}
	
	@FXML
	private void jTextFieldOrderKeyTyped(KeyEvent event) {
		if (!Arrays.asList("0","1", "2", "3", "4", "5").contains(event.getCharacter())) {
			event.consume();
		}
	}

	@FXML
	private void start(ActionEvent event) {
		timer = new Timer();
		start.setDisable(true);
		stop.setDisable(false);
		hours.setDisable(true);
		minutes.setDisable(true);
		seconds.setDisable(true);
		TimerTask task = new TimerTask() {
			public void run() {
				Platform.runLater(() -> {
					initCurEvent(mainTable);
					// The task you want to dor.
					iHours = Integer.parseInt(hours.getText());
					iMinutes = Integer.parseInt(minutes.getText());
					iSeconds = Integer.parseInt(seconds.getText());
					if (iSeconds == 0) {
						if (iMinutes == 0) {
							if (iHours == 0) {
								stop();
							} else {
								iHours = iHours - 1;
								iMinutes = 59;
								iSeconds = 59;
							}
						} else {
							iMinutes = iMinutes - 1;
							iSeconds = 59;
						}
					} else {
						iSeconds = iSeconds - 1;
					}
					hours.setText(String.valueOf(iHours));
					minutes.setText(String.valueOf(iMinutes));
					if (minutes.getText().length() == 1) {
						minutes.setText("0" + minutes.getText());
					}

					seconds.setText(String.valueOf(iSeconds));
					if (seconds.getText().length() == 1) {
						seconds.setText("0" + seconds.getText());
					}
					// readText("1");
					readTask();
				});

			}

		};
		timer.schedule(task, 0, 1000);
	}

	@FXML
	private void stop(ActionEvent event) {
		stop();
	}

	@FXML
	private void addButtonClick() {
        String filtr = filterField.getText();
        filterField.setText("");
       
		Event newEvent = new Event();
		if (!newVillageName.getText().equals("") && !newOrder.getText().equals("")) {
			EventListWrapper data = getEventListWrapper();
			newEvent.setVillageName(newVillageName.getText());
			newEvent.setVillageID(newVillageID.getText());
			newEvent.setTime(newHour.getText() + ":" + newMinute.getText() + ":" + newSecond.getText());
			newEvent.setTargetName(newTargetName.getText());
			newEvent.setTargetID(newTargetID.getText());
			newEvent.setFormation(newFormation.getText());
			newEvent.setDirection(newDirection.getText());
			newEvent.setOrder(newOrder.getText());
			System.out.println("System"+newHide.getText());
			newEvent.setHide(newHide.getText());
			if (data.getEvents() == null)
			{
				List<Event> list = new ArrayList<Event>();
				list.add(newEvent);
				data.setEvents(list);
			}
			else
			{
			  data.getEvents().add(newEvent);
			}
			try {

				marshalToFile(data);
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				filterTable();
			 filterField.setText(filtr);
			newVillageName.clear();
			newVillageID.clear();
			newHour.setText("00");
			newMinute.setText("00");
			newSecond.setText("00");
			newTargetName.clear();
			newTargetID.clear();
			
			newFormation.clear();
			newDirection.clear();
			newOrder.clear();
			newHide.clear();
			
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Alert");
			alert.setHeaderText("");
			alert.setContentText("Village name and Order must exist");
			alert.showAndWait().ifPresent(rs -> {
				if (rs == ButtonType.OK) {
					System.out.println("Pressed OK.");
				}
			});
		}
	}

	@FXML
	private void deleteButtonClick() {
		ObservableList<Event> eventSelected, allEvents;
		allEvents = mainTable.getItems();
		eventSelected = mainTable.getSelectionModel().getSelectedItems();
        String filtr = filterField.getText();
        filterField.setText("");

		EventListWrapper data = getEventListWrapper();

		data.getEvents().removeIf((Event event) ->event.equal(eventSelected.get(0)));
		try {
			marshalToFile(data);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		filterTable();
	    filterField.setText(filtr);

	}

	private void stop() {
		timer.cancel();
		start.setDisable(false);
		stop.setDisable(true);
		hours.setDisable(false);
		minutes.setDisable(false);
		seconds.setDisable(false);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		hours.textProperty().addListener(new ChangeListener(hours, 2, 23));
		minutes.textProperty().addListener(new ChangeListener(minutes, 2, 59));
		seconds.textProperty().addListener(new ChangeListener(seconds, 2, 59));
		
		stop.setDisable(true);
		
		
		
		
		
		eventDataBase = new EventDataBase();
		//mainTable.setItems(getEvent());
		filterTable();
		villageName.setCellValueFactory(new PropertyValueFactory<>("villageName"));
		villageID.setCellValueFactory(new PropertyValueFactory<>("villageID"));
		time.setCellValueFactory(new PropertyValueFactory<>("time"));
		targetName.setCellValueFactory(new PropertyValueFactory<>("targetName"));
		targetID.setCellValueFactory(new PropertyValueFactory<>("targetID"));
		
		formation.setCellValueFactory(new PropertyValueFactory<>("formation"));
		direction.setCellValueFactory(new PropertyValueFactory<>("direction"));
		order.setCellValueFactory(new PropertyValueFactory<>("order"));
		hide.setCellValueFactory(new PropertyValueFactory<>("hide"));

		newHour.textProperty().addListener(new ChangeListener(newHour, 2, 23));
		newMinute.textProperty().addListener(new ChangeListener(newMinute, 2, 59));
		newSecond.textProperty().addListener(new ChangeListener(newSecond, 2, 59));

		newOrder.textProperty().addListener(new ChangeListener(newOrder, 1, 5));
		try {
			marytts = new LocalMaryInterface();
		} catch (MaryConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initCurEvent(TableView<Event> mainTable) {
		ObservableList<Event> eventSelected, allEvents;
		allEvents = mainTable.getItems();
		setCurrValues("", "", "00:00:00", "", "","","","","");
		final String counterTime = hours.getText() + ":" + minutes.getText() + ":" + seconds.getText();
		allEvents.forEach((event) -> {
			if (event.getTimeOrder().compareTo(counterTime) < 0) {
				if (event.getTimeOrder().compareTo(curTime.getText()) > 0) {
					curTime.setText(event.getTime());
					setCurrValues(event.getVillageName(), event.getVillageID(), event.getTime(), event.getTargetName(),
							event.getTargetID(),event.getFormation(),event.getDirection(),event.getOrder(),event.getHide());
				}
			}
		});
	}

	ObservableList<Event> getEvent() {
		ObservableList<Event> events = FXCollections.observableArrayList();
	//	events.add(new Event("1", "2", "00:00:20", "4", "56"));
	//	events.add(new Event("1", "2", "00:00:30", "4", "56"));
	//	events.add(new Event("1", "2", "00:00:15", "4", "56"));
	//	events.add(new Event("1", "2", "00:00:10", "4", "56"));
	//	events.add(new Event("1", "2", "00:00:05", "4", "56"));
		return events;
	}

	private void setCurrValues(String villageName, String villageID, String time, String targetName, String targetID,String formation,String direction,String order,String hide) {
		curVillageName.setText(villageName);
		curVillageID.setText(villageID);
		curTime.setText(time);
		curTargetName.setText(targetName);
		curTargetID.setText(targetID);
		curFormation.setText(formation);
		curDirection.setText(direction);
		curOrder.setText(order);
		curHide.setText(hide);
	}

	private void readText(String text) {
		AudioPlayer ap = new AudioPlayer();
		AudioInputStream audio = null;

		try {
			audio = marytts.generateAudio(text);
		} catch (SynthesisException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ap.setAudio(audio);

		ap.start();
	}

	private void readTask() {
		if (curVillageName.getText() == "")
			return;

		int timerSeconds = Integer.parseInt(seconds.getText()) + 60 * Integer.parseInt(minutes.getText())
				+ 3600 * Integer.parseInt(hours.getText());
		int attackSeconds = Integer.parseInt(curTime.getText().substring(6, 8))
				+ 60 * Integer.parseInt(curTime.getText().substring(3, 5))
				+ 3600 * Integer.parseInt(curTime.getText().substring(0, 2));
		
		int timerOrder = Integer.parseInt(curOrder.getText());
		
		int del = timerSeconds - (attackSeconds+timerOrder);
		if (del < 51) {
			if (del == 50) {
				readText("Next attack in 50 seconds");
			} else if (del == 20 || del == 30) {
				readText("Prepare attack villa" + curVillageName.getText());
				alerted = 1;
			} else if (del > 3 && del < 11 && alerted == 0) {
				readText("Villa" + curVillageName.getText());
				alerted = 1;
			} else if (del > 0 && del < 11 && alerted == 1) {
				readText(Integer.toString(del));
			} else if (del == 0) {
				readText("Fire");
				alerted = 0;
			}

		}
	}

	@FXML
	private void filterTable() {
		ObservableList<Event> events = FXCollections.observableArrayList();

		List<Event> data = getEventsDataFromFile();

		if (data != null)
			events.addAll(data);

		FilteredList<Event> filteredData = new FilteredList(events, e -> true);

		// 2. Set the filter Predicate whenever the filter changes.
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(event -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();

				if (event.getTargetID().toLowerCase().contains(lowerCaseFilter)) {
					return true; // Filter matches first name.
				} else {
					return false; // Filter matches last name.
				}
			});
		});

		// 3. Wrap the FilteredList in a SortedList.
		SortedList<Event> sortedData = new SortedList<>(filteredData);

		// 4. Bind the SortedList comparator to the TableView comparator.
		sortedData.comparatorProperty().bind(mainTable.comparatorProperty());

		// 5. Add sorted (and filtered) data to the table.
		System.out.println("mainTable");
		mainTable.setItems(sortedData);
	}

	public EventListWrapper getEventListWrapper() {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(EventListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();
			System.out.println("wr");
			return (EventListWrapper) um.unmarshal(eventDataBase.getFile());
		} catch (Exception e) { // catches ANY exception

		}
		System.out.println("null");
		return new EventListWrapper();
	}

	public List<Event> getEventsDataFromFile() {
		try {
			JAXBContext context = JAXBContext.newInstance(EventListWrapper.class);
			System.out.printf("a");
			Unmarshaller um = context.createUnmarshaller();
			System.out.printf("b");
			EventListWrapper wrapper = getEventListWrapper();
			System.out.printf("c");
			return wrapper.getEvents();
		} catch (Exception e) { // catches ANY exception

		}
		return null;
	}
	
	private static void marshalToFile(EventListWrapper data) throws JAXBException
	{
	    JAXBContext jaxbContext = JAXBContext.newInstance(EventListWrapper.class);
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    jaxbMarshaller.marshal(data, new File("EventDataBase.xml"));
	}

}
