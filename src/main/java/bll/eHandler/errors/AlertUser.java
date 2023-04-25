package bll.eHandler.errors;

import bll.eHandler.*;

public abstract class AlertUser {
    public abstract void showAlert(ErrorFacade facade, String className, String errorType);
}
