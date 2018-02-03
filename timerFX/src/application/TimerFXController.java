
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
import javafx.scene.control.ChoiceBox;
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
	//int iHours;
	//int iMinutes;
	//int iSeconds;
	
	TimeEx timeEx;
	int alerted = 0;

	public TimerFXController() {
		timeEx = new TimeEx();
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
	private TextField newVillageName, newVillageID, newTargetName, newTargetID, newHour, newMinute, newSecond,newFormation;

	@FXML 
	private ChoiceBox<String> newOrder,newHide,newSpeed,newDirection;
	
	@FXML // fx:id="addButton"
	private Button addButton;

	@FXML // fx:id="deleteButton"
	private Button deleteButton;

	@FXML
	private Label curVillageName, curVillageID, curTargetName, curTargetID, curHour, curMinute, curSecond,curFormation,curDirection,curOrder,curSpeed,curHide;

	@FXML
	public Label curTime;

	@FXML
	private TextField filterField;

	@FXML // fx:id="filterButton"
	private Button filterButton;
	
	@FXML 
	private ChoiceBox<String> choiceSpeed;
	
	

	EventDataBase eventDataBase;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		hours.textProperty().addListener(new ChangeListener(hours, 2, 23));
		minutes.textProperty().addListener(new ChangeListener(minutes, 2, 59));
		seconds.textProperty().addListener(new ChangeListener(seconds, 2, 59));
		
		stop.setDisable(true);
		
		//choiceSpeed = new ChoiceBox<>();
		
		choiceSpeed.getItems().add("1x");
		choiceSpeed.getItems().add("2x");
		choiceSpeed.getItems().add("3x");
		choiceSpeed.getItems().add("4x");
		choiceSpeed.getItems().add("5x");
		choiceSpeed.getItems().add("6x");
		choiceSpeed.setValue("1x");
		
		
		newOrder.getItems().add("0");
		newOrder.getItems().add("1");
		newOrder.getItems().add("2");
		newOrder.getItems().add("3");
		newOrder.getItems().add("4");
		newOrder.getItems().add("5");
		newOrder.setValue("0");
		
		newHide.getItems().add("Yes");
		newHide.getItems().add("No");
		newHide.setValue("No");
		
		newSpeed.getItems().add("1x");
		newSpeed.getItems().add("2x");
		newSpeed.getItems().add("3x");
		newSpeed.getItems().add("4x");
		newSpeed.getItems().add("5x");
		newSpeed.getItems().add("6x");
		newSpeed.setValue("1x");
		
		

		newDirection.getItems().add("Front");
		newDirection.getItems().add("Back");
		newDirection.getItems().add("North");
		newDirection.getItems().add("South");
		newDirection.setValue("Front");
		
		
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

		//newOrder.textProperty().addListener(new ChangeListener(newOrder, 1, 5));
		try {
			marytts = new LocalMaryInterface();
		} catch (MaryConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
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
					timeEx.setTimeEx(hours.getText(), minutes.getText(), seconds.getText());
					if(timeEx.getTime()==0)
					{
					  stop();	
					}
					else
					{
						timeEx.outSecond();	
					}
					hours.setText(timeEx.getStringHours());
					minutes.setText(timeEx.getStringMinutes());
					seconds.setText(timeEx.getStringSeconds());
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
		if (!newVillageName.getText().equals("") && !newOrder.getValue().equals("")) {
			EventListWrapper data = getEventListWrapper();
			newEvent.setVillageName(newVillageName.getText());
			newEvent.setVillageID(newVillageID.getText());
			newEvent.setTime(newHour.getText() + ":" + newMinute.getText() + ":" + newSecond.getText());
			newEvent.setTargetName(newTargetName.getText());
			newEvent.setTargetID(newTargetID.getText());
			newEvent.setFormation(newFormation.getText());
			newEvent.setDirection(newDirection.getValue());
			newEvent.setOrder(newOrder.getValue());
			newEvent.setSpeed(newSpeed.getValue());
			newEvent.setHide(newHide.getValue());
			
			
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
			
			newDirection.setValue("Front");
			newOrder.setValue("0");
			newSpeed.setValue("1x");
			newHide.setValue("No");
			
			

			
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



	private void initCurEvent(TableView<Event> mainTable) {
		ObservableList<Event> eventSelected, allEvents;
		allEvents = mainTable.getItems();
		setCurrValues("", "", "00:00:00", "", "","","","","","");
		final String counterTime = hours.getText() + ":" + minutes.getText() + ":" + seconds.getText();
		allEvents.forEach((event) -> {
			if (event.getTimeOrder().compareTo(counterTime) < 0) {
				if (event.getTimeOrder().compareTo(curTime.getText()) > 0) {
					curTime.setText(event.getTime());
					setCurrValues(event.getVillageName(), event.getVillageID(), event.getTime(), event.getTargetName(),
							event.getTargetID(),event.getFormation(),event.getDirection(),event.getOrder(),event.getSpeed(),event.getHide());
				}
			}
		});
	}

	ObservableList<Event> getEvent() {
		ObservableList<Event> events = FXCollections.observableArrayList();
		return events;
	}

	private void setCurrValues(String villageName, String villageID, String time, String targetName, String targetID,String formation,String direction,String order,String speed,String hide) {
		curVillageName.setText(villageName);
		curVillageID.setText(villageID);
		curTime.setText(time);
		curTargetName.setText(targetName);
		curTargetID.setText(targetID);
		curFormation.setText(formation);
		curDirection.setText(direction);
		curOrder.setText(order);
		curSpeed.setText(speed);
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
		TimeEx timer = new TimeEx(hours.getText(),minutes.getText(),seconds.getText());
		TimeEx event = new TimeEx(curTime.getText());

		
		int timerOrder = Integer.parseInt(curOrder.getText());
		
		int del = timer.getTime() - (event.getTime()+timerOrder);
		if (del < 51) {
			if (del == 50) {
				readText("Next attack in 50 seconds");
			} else if (del == 20 || del == 30) {
				readText("Hello Pawel");
			//	readText("Prepare attack villa" + curVillageName.getText());
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
	
	@FXML
	private void tableViewOnMouseClicked(MouseEvent event) {
		ObservableList<Event> eventSelected;
		eventSelected = mainTable.getSelectionModel().getSelectedItems();
		
		
		TimeEx timeEx = new TimeEx(eventSelected.get(0).getTime());
		
		newVillageName.setText(eventSelected.get(0).getVillageName());
		newVillageID.setText(eventSelected.get(0).getVillageID());
		
		newHour.setText(timeEx.getStringHours());
		newMinute.setText(timeEx.getStringMinutes());
		newSecond.setText(timeEx.getStringSeconds());
		newTargetName.setText(eventSelected.get(0).getTargetName());
		newTargetID.setText(eventSelected.get(0).getTargetID());
		newFormation.setText(eventSelected.get(0).getFormation());
		newDirection.setValue(eventSelected.get(0).getDirection());
		newOrder.setValue(eventSelected.get(0).getOrder());
		newSpeed.setValue(eventSelected.get(0).getSpeed());
		newHide.setValue(eventSelected.get(0).getHide());
	}
}
