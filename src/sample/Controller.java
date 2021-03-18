package sample;

import java.net.URL;
import java.sql.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Controller implements Initializable{
    private String prev;
    private ResultSet rs;
    private ArrayList<String> persons;

    @FXML private TextField text_field;
    @FXML private Button b;

    @FXML private TableView<Person> tableView;
    @FXML private TableColumn<Person, String> c1;
    @FXML private TableColumn<Person, String> c2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonPressed();
        System.out.println("test top");
        c1.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        c2.setCellValueFactory(new PropertyValueFactory<Person, String>("lastname"));
//        tableView.getItems().setAll(this.persons);

        try {
            tableView.getItems().setAll(parsePersonList());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void buttonPressed(){
        b.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> {
                    this.prev = this.text_field.getText(); // this is the sql code
                    readData();
                });
    }

    public void readData(){
            this.persons = new ArrayList<String>();
            String conn_url = "jdbc:sqlite:C:/sqlite/db/chinook.db";
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(conn_url);
                Statement stmt = conn.createStatement();
                this.rs = stmt.executeQuery("select FirstName, LastName from employees");
//                this.rs = stmt.executeQuery(this.prev);

                while(rs!=null && rs.next()){
                    String first_name = rs.getString(1);
                    String last_name = rs.getString(2);
                    this.persons.add(first_name);
                    this.persons.add(last_name);
                    System.out.println(this.persons);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    private ArrayList<Person> parsePersonList() throws SQLException {
        ArrayList persons = new ArrayList();
        while(this.rs!=null && this.rs.next()){
                String first_name = this.rs.getString(1);
                String last_name = this.rs.getString(2);
                persons.add(new Person(first_name, last_name));
            }
        return persons;
    }

    private static class Person {
        private SimpleStringProperty firstName;
        private SimpleStringProperty lastName;

        private Person(String fName, String lName) {
            this.firstName = new SimpleStringProperty(fName);
            this.lastName = new SimpleStringProperty(lName);
        }

        public String getFirstName() {
            return firstName.get();
        }

        public void setFirstName(String fName) {
            firstName.set(fName);
        }

        public String getLastName() {
            return lastName.get();
        }

        public void setLastName(String fName) {
            lastName.set(fName);
        }
    }

}
