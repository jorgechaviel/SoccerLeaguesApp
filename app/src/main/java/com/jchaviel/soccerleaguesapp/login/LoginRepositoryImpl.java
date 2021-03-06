package com.jchaviel.soccerleaguesapp.login;

import com.google.firebase.database.DatabaseError;
import com.jchaviel.soccerleaguesapp.domain.FirebaseAPI;
import com.jchaviel.soccerleaguesapp.domain.FirebaseActionListenerCallback;
import com.jchaviel.soccerleaguesapp.lib.base.EventBus;
import com.jchaviel.soccerleaguesapp.login.events.LoginEvent;

/**
 * Created by jchavielreyes on 7/4/16.
 */

public class LoginRepositoryImpl implements LoginRepository {

    private EventBus eventBus;
    private FirebaseAPI firebaseAPI;

    public LoginRepositoryImpl(EventBus eventBus, FirebaseAPI firebaseAPI) {
        this.eventBus = eventBus;
        this.firebaseAPI = firebaseAPI;
    }

    @Override
    public void signUp(final String email, final String password) {
        if(email != null && !email.equals("") && password != null && !password.equals("")) {
            firebaseAPI.signup(email, password, new FirebaseActionListenerCallback() {
                @Override
                public void onSuccess() {
                    postEvent(LoginEvent.onSignUpSuccess);
                    signIn(email, password);
                }

                @Override
                public void onError(DatabaseError databaseError) {
                    postEvent(LoginEvent.onSignUpError, databaseError.getMessage(), null);
                }
            });
        } else
            postEvent(LoginEvent.onSignUpError);
    }

    @Override
    public void signIn(String email, String password) {
        if(email != null && !email.equals("") && password != null && !password.equals("")){
            firebaseAPI.login(email, password, new FirebaseActionListenerCallback() {
                @Override
                public void onSuccess() {
                    String email = firebaseAPI.getAuthEmail();
                    postEvent(LoginEvent.onSignInSuccess, email);
                }

                @Override
                public void onError(DatabaseError databaseError) {
                    postEvent(LoginEvent.onSignInError, databaseError.getMessage(), null);
                }
            });
        } else {
            firebaseAPI.checkForSession(new FirebaseActionListenerCallback() {
                @Override
                public void onSuccess() {
                    String email = firebaseAPI.getAuthEmail();
                    postEvent(LoginEvent.onSignInSuccess, email);
                }

                @Override
                public void onError(DatabaseError databaseError) {
                    postEvent(LoginEvent.onFailedToRecoverSession);
                }
            });
        }
    }


    private void postEvent(int type, String errorMessage, String currentUserEmail){
        LoginEvent loginEvent = new LoginEvent();
        loginEvent.setEventType(type);
        loginEvent.setErrorMesage(errorMessage);
        loginEvent.setCurrentUserEmail(currentUserEmail);
        eventBus.post(loginEvent);
    }

    private void postEvent(int type){
        postEvent(type, null, null);
    }

    private void postEvent(int type, String currentUserEmail){
        postEvent(type, null, currentUserEmail);
    }
}
