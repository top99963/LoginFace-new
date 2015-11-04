package com.example.user.loginface;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {


    private TextView mTextdetail;
    private CallbackManager mCallbackManager;
    private ProfilePictureView profilePicture;
    private  TextView username;
    private TextView id;
    private ProfileTracker profileTracker;


    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                updateUI();
            }

            @Override
            public void onCancel() {
                updateUI();
            }

            @Override
            public void onError(FacebookException error) {
                updateUI();
            }
        });

        //setContentView(R.layout.activity_main);

    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        //loginButton.registerCallback(mCallbackManager, mCallback);
        username = (TextView) view.findViewById(R.id.username);
        id = (TextView) view.findViewById(R.id.profileID);
        profilePicture = (ProfilePictureView)view.findViewById((R.id.profilepic));
        // Callback registration
        profileTracker = new ProfileTracker(){
            protected void onCurrentProfileChanged(Profile profile,Profile profile1){
                updateUI();
            }
        };

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUI() {
        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        Profile profile = Profile.getCurrentProfile();

        if (loggedIn && (profile != null)) {
            profilePicture.setProfileId(profile.getId());
            username.setText(profile.getName()); // profile.getName aow wai getname ma use
            id.setText(profile.getId()); // profile.getID aow wai getId ma use
        } else {
            profilePicture.setProfileId(null);
            username.setText(null);
            id.setText(null);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    public void onResume() {
        super.onResume();
        updateUI();
    }




}
