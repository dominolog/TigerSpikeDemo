package pl.cubesoft.tigerspiketest.provider.flickr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import oauth.signpost.basic.DefaultOAuthProvider;
import okhttp3.OkHttpClient;
import pl.cubesoft.tigerspiketest.data.AuthProvider;
import rx.Observable;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;


/**
 * Created by CUBESOFT on 20.09.2017.
 */

class AuthProviderImpl implements AuthProvider {

    private static final String PREF_AUTH_TOKEN = "auth_token";
    private static final String PREF_AUTH_TOKEN_SECRECT = "auth_token_secrect";


    private final OkHttpOAuthConsumer consumer;
    private final SharedPreferences sharedPreferences;
    private AuthToken authToken;
    private DefaultOAuthProvider provider;
    private String currentUserId;

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    private class AuthToken {
        private final String authToken;
        private final String authTokenSecrect;

        AuthToken(String authToken, String authTokenSecrect) {
            this.authToken = authToken;
            this.authTokenSecrect = authTokenSecrect;
        }
    }

    public AuthProviderImpl(SharedPreferences sharedPreferences, OkHttpClient.Builder httpClientBuilder) {
        consumer = new OkHttpOAuthConsumer(Constants.API_KEY, Constants.API_SECRET);
        //consumer.setTokenWithSecret(Constants.FLICKR_TOKEN_SECRENT, Constants.FLICKR_AUTH_TOKEN_SECRET);
        httpClientBuilder.addNetworkInterceptor(new SigningInterceptor(consumer));

        this.sharedPreferences = sharedPreferences;
        authToken = loadAuthToken();
        if ( authToken != null ) {
            consumer.setTokenWithSecret(authToken.authToken, authToken.authTokenSecrect);
        }
    }




    @Override
    public Observable<LoginResult> login(String username, String password) {
        return Observable.fromCallable(() -> {




            // create a new service provider object and configure it with
            // the URLs which provide request tokens, access tokens, and
            // the URL to which users are sent in order to grant permission
            // to your application to access protected resources
            provider = new DefaultOAuthProvider(Constants.REQUEST_TOKEN_ENDPOINT_URL,
                    Constants.ACCESS_TOKEN_ENDPOINT_URL,
                    Constants.AUTHORIZE_WEBSITE_URL);

            // fetches a request token from the service provider and builds
            // a url based on AUTHORIZE_WEBSITE_URL and CALLBACK_URL to
            // which your app must now send the user


            final String url = provider.retrieveRequestToken(consumer, Constants.OAUTH_CALLBACK_URL);

            return new LoginResult () {

                @Override
                public Intent getLoginIntent() {
                    return new Intent()
                            .setAction(Intent.ACTION_VIEW)
                            .setData(Uri.parse(url));
                }

                @Override
                public boolean getResult() {
                    return true;
                }
            };
        });
    }

    @Override
    public Observable<Boolean> handleRedirectUri(Uri uri) {
        return Observable.fromCallable(() -> {
            if (uri != null && uri.toString().startsWith(Constants.OAUTH_CALLBACK_URL)) {
                // use the parameter your API exposes for the code (mostly it's "code")
                String oAuthToken = uri.getQueryParameter("oauth_token");
                String oAuthVerifier = uri.getQueryParameter("oauth_verifier");
                if (oAuthToken != null && oAuthVerifier != null) {
                    provider.retrieveAccessToken(consumer, oAuthVerifier);

                    saveAuthToken(authToken = new AuthToken(consumer.getToken(), consumer.getTokenSecret()));

                    return true;

                    // get access token
                    // we'll do that in a minute
                } else if (uri.getQueryParameter("error") != null) {
                    // show an error message here
                }
            }
            return false;
        });
    }

    @Override
    public boolean isLoggedIn() {
        return authToken != null;
    }

    @Override
    public boolean isCurrentUser(String userId) {
        return currentUserId != null && currentUserId.equals(userId);
    }

    @Override
    public void logOut() {
        saveAuthToken(new AuthToken(null, null));
        authToken = null;
        consumer.setTokenWithSecret(null, null);
    }

    @Override
    public AuthType getAuthType() {
        return AuthType.OAUTH;
    }


    private AuthToken loadAuthToken() {
        final String authToken = sharedPreferences.getString(PREF_AUTH_TOKEN, null);
        final String authTokenSecret = sharedPreferences.getString(PREF_AUTH_TOKEN_SECRECT, null);
        return authToken != null && authTokenSecret != null ? new AuthToken(authToken, authTokenSecret) : null;
    }

    private void saveAuthToken(AuthToken authToken) {
        sharedPreferences.edit()
                .putString(PREF_AUTH_TOKEN, authToken.authToken)
                .putString(PREF_AUTH_TOKEN_SECRECT, authToken.authTokenSecrect)
                .apply();
    }
}
