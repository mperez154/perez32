package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Assignment_32 extends Application {
	protected TextArea taResults = new TextArea();

	protected BorderPane getPane() {

		// Create required panes
		VBox paneforSelections = new VBox(20); 	// Main control panel
		HBox paneForInput = new HBox(15); 		// Panel storing inputs
		HBox paneForButtons = new HBox(10); 	// Panel storing buttons
		HBox paneDropDown = new HBox(10);	
		

		// Creating buttons
		Button btSearch = new Button("Search");

		// Creating labels
		Label lbSearch = new Label("Search customers:");
		Label lbWhere = new Label("Where:");
		Label lbOrderBy = new Label("Order By:");

		// Creating checkboxes for all the fields
		CheckBox chkName = new CheckBox("Name");
		CheckBox chkAddress = new CheckBox("Address");
		CheckBox chkCity = new CheckBox("City");
		CheckBox chkState = new CheckBox("State");
		CheckBox chkZip = new CheckBox("Zip");

		// Creating text input
		TextField tfWhere = new TextField();
		
		//Creating drop down menu
		ComboBox<String> cbOrderBy = new ComboBox<>();
		cbOrderBy.getItems().addAll("Name", "Address", "City", "State", "Zip");
		cbOrderBy.setValue("Name");

		// Adding hbox paneForInput to VBox
		paneforSelections.getChildren().addAll(paneForInput, paneForButtons);

		// Setting up input pane
		paneForInput.getChildren().addAll(lbSearch, chkName, chkAddress, chkCity, chkState, chkZip);
		paneForInput.setAlignment(Pos.CENTER_LEFT);
		paneForInput.setPadding(new Insets(5, 1, 5, 1)); // Padding (Top, Right, Bottom, Left)

		// setting up buttons pane
		paneForButtons.getChildren().addAll(lbWhere, tfWhere, btSearch);
		paneForButtons.setAlignment(Pos.CENTER_LEFT);
		paneForButtons.setPadding(new Insets(5, 1, 5, 15));
		
		//Setting up drop down pane
		paneDropDown.getChildren().addAll(lbOrderBy, cbOrderBy);
		paneDropDown.setAlignment(Pos.CENTER_LEFT);
		paneDropDown.setPadding(new Insets(5,1,5,1));
		
		// Create a pane for results
		HBox paneForResults = new HBox(20);
		paneForResults.getChildren().add(taResults);
		taResults.setPadding(new Insets(5, 5, 5, 5));

		// Create a main pane holding all children pane
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(5, 5, 5, 5));
		pane.setTop(paneForInput);
		pane.setLeft(paneDropDown);
		pane.setCenter(paneForButtons);	
		pane.setBottom(paneForResults);
		
		//System.out.println("ORDER BY " + cbOrderBy.getValue());

		// retrieving table data on button click event
		btSearch.setOnAction(e -> {
			String columns = "";
			String whereConditions = "";
			String orderBy = " ORDER BY " + cbOrderBy.getValue().toString();
			int counter = 0;

			try {
				// Series of nested if statements, checks if columns field is empty + validates which check boxes are selected
				if (chkName.isSelected()) {
					if (columns.equals("") || columns.equals(null)) {
						columns = "name";
						counter++;
					} else {
						columns = columns + ", name";
						counter++;
					}

				}
				if (chkAddress.isSelected()) {
					if (columns.equals("") || columns.equals(null)) {
						columns = "address";
						counter++;
					} else {
						columns = columns + ", address";
						counter++;
					}
				}
				if (chkCity.isSelected()) {
					if (columns.equals("") || columns.equals(null)) {
						columns = "city";
						counter++;
					} else {
						columns = columns + ", city";
						counter++;
					}

				}
				if (chkState.isSelected()) {
					if (columns.equals("") || columns.equals(null)) {
						columns = "state";
						counter++;
					} else {
						columns = columns + ", state";
						counter++;
					}

				}
				if (chkZip.isSelected()) {
					if (columns.equals("") || columns.equals(null)) {
						columns = "zip";
						counter++;
					} else {
						columns = columns + ", zip";
						counter++;
					}
				}
				if (!chkName.isSelected() && !chkAddress.isSelected() && !chkCity.isSelected() && !chkState.isSelected()
						&& !chkZip.isSelected()) {
					// If no check boxes are selected, default to showing everything
					columns = "*";
					counter = 6;
				}
				// If statement to see if the where text field is empty or not
				// If empty, it inserts "where custid > 0" so that all values are displayed
				if (tfWhere.getText().equals(null) || tfWhere.getText().equals("")) {
					whereConditions = " WHERE custid > 0";
				} else
					whereConditions = " WHERE " + tfWhere.getText();
			
				// Loading driver for my SQL
				Class.forName("com.mysql.jdbc.Driver");
				System.out.println("Driver loaded");

				// Connect to a database
				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/pcparts", "root", "Bulldogs25");
				System.out.println("DB Connected");

				// Create statement
				Statement statement = connection.createStatement();

				// Execute a statement
				ResultSet resultSet = statement.executeQuery("SELECT " + columns + " FROM customers" + whereConditions + orderBy);

				// Clears Text Area so that previous data is not displayed when iteration below executes
				taResults.clear();

				// Iterate through results and print the data
				while (resultSet.next()) {
					for (int i = 1; i <= counter; i++) {
						taResults.appendText(resultSet.getString(i) + "\t\t");
					}
					taResults.appendText("\n\n");
				}

				// Close the connection
				connection.close();
			} catch (Exception ex) {
				System.out.println("Failed");
				ex.printStackTrace();
			}
		});
		return pane;
	}

	@Override
	public void start(Stage primaryStage) {
		// Create a scene and place it in the stage
		Scene scene = new Scene(getPane());
		primaryStage.setTitle("Marco Perez: Chapter 32"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
	}

	public static void main(String[] args) {
		launch(args);
	}
}
