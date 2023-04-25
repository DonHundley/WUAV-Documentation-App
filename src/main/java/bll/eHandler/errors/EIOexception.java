package bll.eHandler.errors;

import bll.eHandler.*;

public class EIOexception extends AlertUser{


    @Override
    public void showAlert(ErrorFacade facade, String className, String errorType){
        facade.createIOexceptionAlert(className, errorType);
    }

}
