package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    static HashMap<String, User> users = new HashMap<>();
    Stage secondStage = new Stage();

    @FXML
    TextField userNameBar;
    @FXML
    TextField passwordBar;
    @FXML
    TextField createUserNameBar;
    @FXML
    TextField createPasswordBar;
    @FXML
    Button closeButton;
    @FXML
    Button exitProgramButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        parseUsers();
    }

    //tests if the username/password entered match the username/password in the 'users' hashmap
    public void loginButton() {
        String userName = userNameBar.getText();
        String pw = passwordBar.getText();
        User user = users.get(userName);
        System.out.println(users);

        if (userName.equals(user.userName) && pw.equals(user.password)) {
            System.out.println("IT WORKED!");
        }
        else {
            System.out.println("it failed");
        }

        userNameBar.setText("");
        passwordBar.setText("");
    }

    //launches Create Account window/process
    public void createButton() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("createAccount.fxml"));
        secondStage.setTitle("Create Account");
        secondStage.setScene(new Scene(root, 400, 400));
        secondStage.show();
    }

    //exits the program
    public void exitButton() {
        Stage stage = (Stage) exitProgramButton.getScene().getWindow();
        stage.close();
    }

    //creates new account and puts the username/password into the 'users' hashmap
    public void submitAccountButton() {
        String newUser = createUserNameBar.getText();
        String newPassword = createPasswordBar.getText();
        User user = new User(newUser, newPassword);
        users.put(newUser, user);
        writeToJson();

        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    //closes the Create Account window
    public void cancelButton() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    //allows user to press ENTER to submit account info
    public void onKeyPressed1(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            loginButton();
        }
    }

    //allows user to press ENTER to submit account info when creating a new account
    public void onKeyPressed2(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            submitAccountButton();
        }
    }

    //file reader
    static String readFile(String fileName) {
        File f = new File(fileName);
        try {
            FileReader fr = new FileReader(f);
            int fileSize = (int) f.length();
            char[] fileContent = new char[fileSize];
            fr.read(fileContent);
            return new String(fileContent);
        } catch (Exception e) {
            return null;
        }
    }

    //file writer
    static void writeFile(String fileName, String fileContent) {
        File f = new File(fileName);
        try {
            FileWriter fw = new FileWriter(f);
            fw.write(fileContent);
            fw.close();
        } catch (Exception e) {

        }
    }

    //write file to JSON format
    static void writeToJson() {
        JsonSerializer serializer = new JsonSerializer();
        String output = serializer.serialize(users);

        writeFile("users.json", output);
    }

    //parse user data and populate 'users' hashmap with it
    static void parseUsers() {
        String content = readFile("users.json");
        if (content != null) {
            JsonParser parser = new JsonParser();
            HashMap<String, HashMap<String, String>> usersFromFile = parser.parse(content);
            for (Map.Entry<String, HashMap<String, String>> entry : usersFromFile.entrySet()) {
                String key = entry.getKey();
                HashMap<String, String> value = entry.getValue();
                User user = new User(value.get("userName"), value.get("password"));
                users.put(key, user);
            }
        }

    }
}
