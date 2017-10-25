package pl.cubesoft.tigerspiketest.data;

import android.content.Intent;
import android.net.Uri;

import rx.Observable;

/**
 * Created by CUBESOFT on 20.09.2017.
 */

public interface AuthProvider {

    public interface LoginResult {
        Intent getLoginIntent();
        boolean getResult();
    }
    Observable<LoginResult> login(String username, String password);

    Observable<Boolean> handleRedirectUri(Uri uri);

    boolean isLoggedIn();

    boolean isCurrentUser(String userId);

    void logOut();

    enum AuthType {
        CREDENTIALS,
        OAUTH
    }

    AuthType getAuthType();

    interface NotAuthenticatedEvent {
    }

}
