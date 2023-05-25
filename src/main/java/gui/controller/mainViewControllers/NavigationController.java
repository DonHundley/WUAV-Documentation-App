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
import org.apache.logging.log4j.*;

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

    private static final Logger logger = LogManager.getLogger("debugLogger");

    public void setNavigationController()  {
        logger.info("setNavigationController() called.");
        viewAnchor = super.getViewPane();
        baseNavAnchor.getChildren().add(viewAnchor);
        viewAnchor.setLayoutX(300);
        viewAnchor.setLayoutY(0);

        logger.info("Checking account access level.");
        String access = loggedInUser.getAccess().toUpperCase();
        try {
            switch (access) {
                case "ADMIN":
                    adminManagerLogin();
                    break;
                case "MANAGER":
                    adminManagerLogin();
                    break;
                case "TECHNICIAN":
                    techLogin();
                    break;
                case "SALES":
                    salesLogin();
                    break;
                default:
                    logger.warn("There was an issue finding user role.");
                    String str = "User account either has no role or the role is invalid. Please contact an Administrator.";
                    super.createWarning(str);
            }
        }catch (IOException e){
            logger.error("There has been a problem loading the appropriate view file for access level " + access + ".", e);
        }
        logger.info("setNavigationController() complete.");
    }

    /**
     * This is what is used to determine the login state of admin and manager level accounts.
     * @throws IOException is handled by the switch method.
     */
    private void adminManagerLogin() throws IOException {
        logger.trace("Admin or manager logged in.");
        home = "/gui/view/mainViews/ManageTaskView.fxml";
        Node m = FXMLLoader.load(getClass().getResource(home));
        viewAnchor.getChildren().setAll(m);
        documentationExpired();
    }

    /**
     * This is what is used to determine the login state of tech level accounts.
     * @throws IOException is handled by the switch method.
     */
    private void techLogin() throws IOException{
        logger.trace("Technician logged in.");
        home = "/gui/view/mainViews/ManageTaskView.fxml";
        projectModel.loadProjectsByUser(loggedInUser);
        Node t = FXMLLoader.load(getClass().getResource(home));
        viewAnchor.getChildren().setAll(t);
        manageCustomerButton.setVisible(false);
        manageProjectsButton.setVisible(false);
        manageUserButton.setVisible(false);
    }

    /**
     * This is what is used to determine the login state of sales level accounts.
     * @throws IOException is handled by the switch method
     */
    private void salesLogin() throws IOException{
        logger.trace("Sales logged in.");
        home = "/gui/view/mainViews/ManageCustomersView.fxml";
        Node s = FXMLLoader.load(getClass().getResource(home));
        viewAnchor.getChildren().setAll(s);
        manageCustomerButton.setVisible(false);
        manageProjectsButton.setVisible(false);
        manageUserButton.setVisible(false);
    }

    /**
     * This is used to return the user to their appropriate home window based on their access level.
     * @param actionEvent triggered by the home button on the nav menu.
     */
    public void home(ActionEvent actionEvent)  {
        logger.trace("home() called in NavigationController.");
        Node n = null;
        try {
            n = FXMLLoader.load(getClass().getResource(home));
        } catch (IOException e) {
            logger.error("There was a problem loading " + home + ". " , e);
        }
        viewAnchor.getChildren().setAll(n);
        logger.trace("home() complete.");
    }

    /**
     * This is used to open the manage project view.
     * @param actionEvent is activated when the manage projects button is clicked.
     */
    public void manageProjects(ActionEvent actionEvent)  {
        logger.trace("manageProjects() called in NavigationController");
        Node n = null;
        try {
            n = FXMLLoader.load(getClass().getResource("/gui/view/mainViews/ManageProjectView.fxml"));
        } catch (IOException e) {
            logger.error("There was a problem loading ManageProjectView.fxml.",e);
        }
        viewAnchor.getChildren().setAll(n);
        logger.trace("manageProjects() complete.");
    }

    /**
     * This is used to open the manage customers view.
     * @param actionEvent is activated when the manage customers button is clicked.
     */
    public void manageCustomers(ActionEvent actionEvent)  {
        logger.trace("manageCustomers() called in NavigationController");
        Node n = null;
        try {
            n = FXMLLoader.load(getClass().getResource("/gui/view/mainViews/ManageCustomersView.fxml"));
        } catch (IOException e) {
            logger.error("There was a problem loading ManageCustomersView.fxml.", e);
        }
        viewAnchor.getChildren().setAll(n);
        logger.trace("manageCustomers() complete.");
    }

    /**
     * This is used to open the manage users view.
     * @param actionEvent is activated when the manage customers button is clicked.
     */
    public void manageUsers(ActionEvent actionEvent)  {
        logger.trace("manageUsers() called in NavigationController");
        Node n = null;
        try {
            n = FXMLLoader.load(getClass().getResource("/gui/view/mainViews/ManageUsersView.fxml"));
        } catch (IOException e) {
            logger.error("There has been a problem loading ManageUsersView.fxml.",e);
        }
        viewAnchor.getChildren().setAll(n);
        logger.trace("manageUsers() complete.");
    }


    /**
     * This is used to notify admin and manager accounts if documentation has been stored for 4 years and should be removed.
     */
    public void documentationExpired() {
        logger.info("documentationExpired() called in NavigationController");
        projectModel.loadProjects();
        ArrayList<String> expiredDocumentation = new ArrayList<>();
        logger.info("Iterating over list of projects.");
        for (ProjectWrapper p : projectModel.getProjects()) {

            LocalDateTime localDateTime = LocalDateTime.now().minusYears(4); //4 years ago date
            java.sql.Date dateTime = Date.valueOf(localDateTime.toLocalDate()); //converts to Date

            if (p.getProject().getProjDate().before(dateTime)) {
                expiredDocumentation.add(p.getProject().getProjName());
            }

        }
        logger.info("Checking if any have expired.");
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
        logger.info("documentationExpired() complete.");
    }

}
