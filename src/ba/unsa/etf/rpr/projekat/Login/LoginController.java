package ba.unsa.etf.rpr.projekat.Login;

import ba.unsa.etf.rpr.projekat.HomePage.EmployeeHomePageController;
import ba.unsa.etf.rpr.projekat.HrmsDAO;
import ba.unsa.etf.rpr.projekat.HomePage.AdminHomePageController;
import ba.unsa.etf.rpr.projekat.Employee.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class LoginController {
    public HrmsDAO dao;
    public TextField usernameField;
    public PasswordField fieldPassword;
    public GridPane gridPaneLogin;

    public LoginController() {
    }

    @FXML
    public void initialize() {
        dao = HrmsDAO.getInstance();
        gridPaneLogin.getStyleClass().add("groupBox");
    }

    public void loginAction(ActionEvent actionEvent) {
//        Map users = new HashMap();
//        Scanner ulaz;
//        double[] brojevi = new double[1000];
//        int vel = 0;
//
//        try {
//            ulaz = new Scanner(new FileReader("brojevi.txt"));
//        } catch(FileNotFoundException e) {
//            System.out.println("Datoteka brojevi.txt ne postoji ili se ne može otvoriti.");
//            System.out.println("Greška: " + e);
//            return; // kraj programa
//        }
//
//        try {
//            // Učitavamo brojeve
//            while (ulaz.hasNext()) {
//                brojevi[vel] = ulaz.nextDouble();
//                vel = vel + 1;
//                if (vel == 1000) break;
//            }
//        } catch(Exception e) {
//            System.out.println("Problem pri čitanju/pisanju podataka.");
//            System.out.println("Greška: " + e);
//
//        } finally {
//            // Bez obzira došlo do izuzetka ili ne, datoteke treba zatvoriti
//            ulaz.close();
//        }

        Set<User> users = dao.users();
        for(User k: users) {
            if(k.getUsername().equals(usernameField.getText()) && k.getPassword().equals(fieldPassword.getText())) {
                Node n = (Node) actionEvent.getSource();
                Stage stage1 = (Stage) n.getScene().getWindow();
                stage1.close();

                try {
                    for(Employee e: dao.employees()) {
                        if ((e.getFirstName().toLowerCase().charAt(0) + e.getLastName().toLowerCase()).equals(usernameField.getText())) {
                            dao.setCurrentEmployee(e);
                        }
                    }
                    Stage stage = new Stage();
                    FXMLLoader loader;
                    if(k.getPrivilege().equals("Admin")) {
                        loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/adminHomePage.fxml"));
                        loader.setController(new AdminHomePageController());
                        stage.setTitle("HRMS");
                    }
                    else {
                        loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/employeeHomePage.fxml"));
                        loader.setController(new EmployeeHomePageController());
                        stage.setOnHiding(event -> {
                            dao.setCurrentEmployee(null);
                        });
                    }
                    Parent root = null;
                    root = loader.load();
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Neispravni podaci!");
        alert.show();
    }
}
