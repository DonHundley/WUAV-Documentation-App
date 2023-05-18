package gui.controller.mainViewControllers;

import be.*;
import gui.model.*;
import io.github.palexdev.materialfx.controls.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

import java.io.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class NavigationController extends BaseController{
    @FXML private AnchorPane baseNavAnchor;
    @FXML private AnchorPane navAnchor;
     //x300 y0
    @FXML private ImageView imageWUAV;
    @FXML private MFXButton manageProjectsButton;
    @FXML private MFXButton manageCustomerButton;
    @FXML private MFXButton manageUserButton;

    private AnchorPane viewAnchor;


    private AuthenticationModel authenticationModel = AuthenticationModel.getInstance();
    private ProjectModel projectModel = ProjectModel.getInstance();
    private User loggedInUser = authenticationModel.getLoggedInUser();
    private String home;

    public void setNavigationController() throws IOException {
        super.getViewPane();
        viewAnchor = super.getViewPane();
        baseNavAnchor.getChildren().add(viewAnchor);
        viewAnchor.setLayoutX(300);
        viewAnchor.setLayoutY(0);

        String access = loggedInUser.getAccess().toUpperCase();
        switch (access){
            case "ADMIN":
                adminManagerLogin();
                break;
            case "MANAGER":
                adminManagerLogin();
                break;
            case "TECHNICIAN":
                home = "/gui/view/mainViews/ManageTaskView.fxml";
                projectModel.loadProjectsByUser(loggedInUser);
                Node t = FXMLLoader.load(getClass().getResource(home));
                viewAnchor.getChildren().setAll(t);
                manageCustomerButton.setVisible(false);
                manageProjectsButton.setVisible(false);
                manageUserButton.setVisible(false);
                break;
            case "SALES":
                home = "/gui/view/mainViews/ManageCustomersView.fxml";
                Node s = FXMLLoader.load(getClass().getResource(home));
                viewAnchor.getChildren().setAll(s);
                manageCustomerButton.setVisible(false);
                manageProjectsButton.setVisible(false);
                manageUserButton.setVisible(false);
                break;
            default:
                String str = "User account either has no role or the role is invalid. Please contact an Administrator.";
                super.createWarning(str);
        }
    }

    private void adminManagerLogin() throws IOException {
        home = "/gui/view/mainViews/ManageTaskView.fxml";
        Node m = FXMLLoader.load(getClass().getResource(home));
        viewAnchor.getChildren().setAll(m);
        documentationExpired();
    }

    public void home(ActionEvent actionEvent) throws IOException {
        Node n = FXMLLoader.load(getClass().getResource(home));
        viewAnchor.getChildren().setAll(n);
    }

    public void manageProjects(ActionEvent actionEvent) throws IOException {
        Node n = FXMLLoader.load(getClass().getResource("/gui/view/mainViews/ManageProjectView.fxml"));
        viewAnchor.getChildren().setAll(n);
    }

    public void manageCustomers(ActionEvent actionEvent) throws IOException {
        Node n = FXMLLoader.load(getClass().getResource("/gui/view/mainViews/ManageCustomersView.fxml"));
        viewAnchor.getChildren().setAll(n);
    }

    public void manageUsers(ActionEvent actionEvent) throws IOException {
        Node n = FXMLLoader.load(getClass().getResource("/gui/view/mainViews/ManageUsersView.fxml"));
        viewAnchor.getChildren().setAll(n);
    }



    public void documentationExpired() {
        projectModel.loadProjects();
        ArrayList<String> expiredDocumentation = new ArrayList<>();
        for (ProjectWrapper p : projectModel.getProjects()) {

            LocalDateTime localDateTime = LocalDateTime.now().minusYears(4); //4 years ago date
            java.sql.Date dateTime = Date.valueOf(localDateTime.toLocalDate()); //converts to Date

            if (p.getDateCreated().before(dateTime)) {
                expiredDocumentation.add(p.getProject().getProjName());
            }

        }
        if(expiredDocumentation.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Expired Documentation");
            alert.setHeaderText("Remember to manage the documentation that has been stored for 48+ months:");
            alert.setContentText(expiredDocumentation.toString().replace("[","").replace("]",""));
            Optional<ButtonType> result = alert.showAndWait();
            alert.getContentText();
            if (result.get() == ButtonType.CLOSE) {
                alert.close();
            }
        }

    }

    public AnchorPane getViewAnchor() {return viewAnchor;}
}
