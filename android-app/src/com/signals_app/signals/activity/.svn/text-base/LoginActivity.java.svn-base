package com.signals_app.signals.activity;

import java.util.Calendar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.signals_app.signals.R;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.tasks.FbProfilePicTaskMethod;
import com.signals_app.signals.tasks.LoginTaskMethod;
import com.signals_app.signals.tasks.LoginTaskMethod.LoginTaskEvent;
import com.signals_app.signals.tasks.photos.UploadProfilePhotoTaskMethod;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends SherlockFragmentActivity implements LoginTaskMethod.LoginTaskListener
{
	private EditText mEmailEditText;
	private EditText mPasswordEditText;

	private LoginTaskMethod mLoginTaskMethod;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setTitle(R.string.login);
        setContentView(R.layout.activity_login);     
        
        
        // ATTRIBUTES & TASKS
        mLoginTaskMethod = new LoginTaskMethod(this);
        mLoginTaskMethod.addLoginTaskListener(this);
        
        
        
		// USERNAME
        
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		mEmailEditText = (EditText) findViewById(R.id.email_et); 
		mEmailEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		
        InputFilter filterUsername = new InputFilter() { 
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) 
			{
                for (int i = start; i < end; i++) { 
                    if (!Character.isLetterOrDigit(source.charAt(i))) { 
                        return ""; 
                    } 
                } 
                
				return null;
			}
    	};
    	mEmailEditText.setFilters(new InputFilter[]{filterUsername, new InputFilter.LengthFilter(16)}); 
    	
    	
    	// PASSWORD
    	mPasswordEditText = (EditText) findViewById(R.id.password_edit_text); 
    	mPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    	mPasswordEditText.setEllipsize(TruncateAt.END);
    	mPasswordEditText.setSingleLine();
    	mPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
    	mPasswordEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)}); 
    	
    	
    	// DONE BUTTON
    	Button loginButton = (Button)findViewById(R.id.login_btn);

    	loginButton.setOnClickListener( new View.OnClickListener()
            {
                public void onClick(View view)
                {
                	onClickLogin();
                }
            });
    	
    	
    	// FORGOT PASSWORD
    	TextView forgotPasswordText = (TextView)this.findViewById(R.id.forgot_password_text);
    	forgotPasswordText.setText(Html.fromHtml(getString(R.string.forgot_password)));
    	


    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mLoginTaskMethod.removeLoginTaskListener(this);
        mLoginTaskMethod.cleanUp();
    }
    

    
     
    // EVENTS
    
    private void onClickLogin() 
    {
    	
    	String email = mEmailEditText.getText().toString();
    	String password = mPasswordEditText.getText().toString();
		
    	User.getInstance().setEmail(email);
    	
		setSupportProgressBarIndeterminateVisibility(true);
		


    }

	@Override
	public void loginTaskEventReceived(LoginTaskEvent evt) {
		LoginTaskMethod met = (LoginTaskMethod)evt.getSource();
		
		if( met != null && met.success() )		
    	{
        	User.getInstance().setLoggedIn(true);
        	
        	Intent broadcastIntent = new Intent();
        	broadcastIntent.setAction("com.package.ACTION_LOGIN");
        	sendBroadcast(broadcastIntent);
        	
        	Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);	
            finish();
    	}
    	
    	setSupportProgressBarIndeterminateVisibility(false);
	}
}
