package gui.controller.mainViewControllers;

import be.*;
import gui.model.*;
import io.github.palexdev.materialfx.controls.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.apache.logging.log4j.*;

import java.io.*;
import java.net.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class NavigationController extends BaseController implements Initializable{
    // FXML
    @FXML private AnchorPane baseNavAnchor;
    @FXML private MFXButton manageProjectsButton;
    @FXML private MFXButton manageCustomerButton;
    @FXML private MFXButton manageUserButton;

    // anchor
    private AnchorPane viewAnchor;

    // Models
    private final AuthenticationModel authenticationModel = AuthenticationModel.getInstance();
    private final ProjectModel projectModel = ProjectModel.getInstance();

    // private BE
    private final User loggedInUser = authenticationModel.getLoggedInUser();

    // Variables
    private String home;

    // Logger
    private static final Logger logger = LogManager.getLogger("debugLogger");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewAnchor = super.getViewPane();
        baseNavAnchor.getChildren().add(viewAnchor);
        viewAnchor.setLayoutX(300);
        viewAnchor.setLayoutY(0);

        setNavigationController();
    }

    /**
     * This method determines which view will be set in view anchor depending on the access level of the user.
     */
    private void setNavigationController()  {
        logger.info("setNavigationController() called.");
        String access = loggedInUser.getAccess().toUpperCase();
        try {
            switch (access) {
                case "ADMIN", "MANAGER" -> adminManagerLogin();
                case "TECHNICIAN" -> techLogin();
                case "SALES" -> salesLogin();
                default -> {
                    logger.warn("There was an issue finding user role.");
                    String str = "User account either has no role or the role is invalid. Please contact an Administrator.";
                    super.createWarning(str);
                }
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
        Node m = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(home)));
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
        Node t = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(home)));
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
        Node s = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(home)));
        viewAnchor.getChildren().setAll(s);
        manageCustomerButton.setVisible(false);
        manageProjectsButton.setVisible(false);
        manageUserButton.setVisible(false);
    }

    /**
     * This is used to return the user to their appropriate home window based on their access level.
     */
    public void home()  {
        logger.trace("home() called in NavigationController.");
        Node n = null;
        try {
            n = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(home)));
        } catch (IOException e) {
            logger.error("There was a problem loading " + home + ". " , e);
        }
        viewAnchor.getChildren().setAll(n);
        logger.trace("home() complete.");
    }

    /**
     * This is used to open the manage project view.
     */
    public void manageProjects()  {
        logger.trace("manageProjects() called in NavigationController");
        Node n = null;
        try {
            n = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/gui/view/mainViews/ManageProjectView.fxml")));
        } catch (IOException e) {
            logger.error("There was a problem loading ManageProjectView.fxml.",e);
        }
        viewAnchor.getChildren().setAll(n);
        logger.trace("manageProjects() complete.");
    }

    /**
     * This is used to open the manage customers view.
     */
    public void manageCustomers()  {
        logger.trace("manageCustomers() called in NavigationController");
        Node n = null;
        try {
            n = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/gui/view/mainViews/ManageCustomersView.fxml")));
        } catch (IOException e) {
            logger.error("There was a problem loading ManageCustomersView.fxml.", e);
        }
        viewAnchor.getChildren().setAll(n);
        logger.trace("manageCustomers() complete.");
    }

    /**
     * This is used to open the manage users view.
     */
    public void manageUsers()  {
        logger.trace("manageUsers() called in NavigationController");
        Node n = null;
        try {
            n = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/gui/view/mainViews/ManageUsersView.fxml")));
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
            alert.getContentText();
            alert.show();
        }
        logger.info("documentationExpired() complete.");
    }


}
