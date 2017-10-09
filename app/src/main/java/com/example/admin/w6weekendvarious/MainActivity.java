package com.example.admin.w6weekendvarious;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookDialog;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.orm.SugarApp;
import com.orm.SugarConfig;
import com.orm.SugarDb;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityTag";
    TextView tvFormat;
    TextView tvContant;
    TextView tvDBResult;

    EditText etName;
    EditText etAge;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private TwitterLoginButton mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        //facebook
        //FACEBOOK
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.btnFacebookLogin);
        loginButton.setReadPermissions("email", "public_profile");
        LoginManager.getInstance().logInWithPublishPermissions(
                this,
                Arrays.asList("publish_actions"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

        //Twitter
        Twitter.initialize(this);
        mLoginButton = (TwitterLoginButton) findViewById(R.id.btnTwitterLogin);
        //mLoginButton.setClickable(true);
        mLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(TAG, "twitterLogin:success" + result);
                handleTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.w(TAG, "twitterLogin:failure", exception);
                //updateUI(null);
            }
        });

        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                if (ShareDialog.canShow(ShareLinkContent.class))

                {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                            .build();
                    shareDialog.show(linkContent);
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}
                , getResources().getDrawable(R.drawable.ic_message_black_24dp));
        stateListDrawable.addState(new int[]{},
                getResources().getDrawable(R.drawable.leak_canary_icon));

        ImageButton imageButton = (ImageButton) findViewById(R.id.ibtnCrash);
        imageButton.setImageDrawable(stateListDrawable);

        tvFormat = (TextView) findViewById(R.id.tvFormat);
        tvContant = (TextView) findViewById(R.id.tvContent);
        tvDBResult = (TextView) findViewById(R.id.tvDBResult);

        etAge = (EditText) findViewById(R.id.etAge);
        etName = (EditText) findViewById(R.id.etName);

    }

    public void doCrash(View view) {
        Timber.d("Hello");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) ;
            }
        }).start();
    }

    public void scan(View view) {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            tvFormat.setText("FORMAT: " + scanFormat);
            tvContant.setText("CONTENT: " + scanContent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //Facebook handler
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:successfb");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "FB Signed in", Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }

    //TWITTER
    private void handleTwitterSession(final TwitterSession session) {
        Log.d(TAG, "handleTwitterSession:" + session.getUserName());

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);


        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success" + session.getUserName());
                            FirebaseUser user = mAuth.getCurrentUser();
                            //user.updateEmail();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }


    public void login(View view) {

    }

    public void signOut(View view) {
        mAuth.signOut();
    }

    public void post(View view) {
        switch (view.getId()) {
            case R.id.btnFBPost:
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(bitmap).build();
                    SharePhotoContent linkContent = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();
                    shareDialog.show(linkContent);
                    Log.d(TAG, "post: ");
                }
                break;

            case R.id.btnTwitterPost:
                TweetComposer.Builder builder = new TweetComposer.Builder(this)
                        .text("just setting up my Twitter Kit.");
                builder.show();
                break;
        }
    }

    public void doDatabase(View view) {
        String age, name;
        age = etAge.getText().toString();
        name = etName.getText().toString();
        switch (view.getId()) {
            case R.id.btnSaveDB:

                Person person = new Person(name, age);
                person.save();

                break;

            case R.id.btnLoadDB:

                Person person1;
                person1 = Person.findById(Person.class, 1l);
                /*if (persons.size() > 0) {
                    person1 = persons.get(0);
                    tvDBResult.setText("Found Person: " + person1.getName());
                } else
                    tvDBResult.setText("NO RESULTS FOUND");*/
                tvDBResult.setText("Found Person: " + person1.getName());


                break;
        }
    }
}
