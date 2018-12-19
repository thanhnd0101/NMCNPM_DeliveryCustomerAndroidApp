package com.example.niot.deliveryfood;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {
    private String name;
    private String phone;
    private String email;
    private String password;

    EditText nameET;
    EditText phoneET;
    EditText emailET;
    EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        nameET = findViewById(R.id.nameEdit);
        phoneET = findViewById(R.id.phoneEdit);
        passwordET = findViewById(R.id.passwordEdit);
        emailET = findViewById(R.id.emailEdit);
    }

    private void getInput(){
        name = nameET.getText().toString();
        phone = phoneET.getText().toString();
        password = phoneET.getText().toString();
        email = emailET.getText().toString();
    }

    private boolean checkInput(){
        getInput();
        if(name.isEmpty() || phone.isEmpty() || password.isEmpty() || email.isEmpty())
            return false;
        return true;
    }


    public void submitRegisterInfoClicked(View view) {
        if(!checkInput())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Fail!").setMessage(R.string.str_missing_info).setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
        }
    }

}
