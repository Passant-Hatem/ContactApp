package com.example.contactapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class ContactInfo extends AppCompatActivity {


    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    TextView charTxtV ,nameTxtV;
    EditText  nameEditTxt ,emailEditTxt ,phoneEditTxt;
    ImageView callImgV ,emailImgV ,editImgV ,deleteImgV;
    Button submitBtn;

    boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        charTxtV = findViewById(R.id.charTxtV);
        nameTxtV = findViewById(R.id.nameTxtV);

        nameEditTxt = findViewById(R.id.nameEditTxt);
        emailEditTxt = findViewById(R.id.emailEditTxt);
        phoneEditTxt = findViewById(R.id.phoneEditTxt);
        submitBtn = findViewById(R.id.submitBtn);

        nameEditTxt.setVisibility(View.GONE);
        emailEditTxt.setVisibility(View.GONE);
        phoneEditTxt.setVisibility(View.GONE);
        submitBtn.setVisibility(View.GONE);

        final int index = getIntent().getIntExtra("index" ,0);

        charTxtV.setText(ApplictationClass.contacts.get(index).getName().toUpperCase().charAt(0) + "");
        nameTxtV.setText(ApplictationClass.contacts.get(index).getName());

        nameEditTxt.setText(ApplictationClass.contacts.get(index).getName());
        emailEditTxt.setText(ApplictationClass.contacts.get(index).getEmail());
        phoneEditTxt.setText(ApplictationClass.contacts.get(index).getNumber());

        callImgV = findViewById(R.id.callImgV);
        callImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:" + ApplictationClass.contacts.get(index).getNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });

        emailImgV = findViewById(R.id.emailImgV);
        emailImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL ,ApplictationClass.contacts.get(index).getEmail());
                startActivity(Intent.createChooser(intent ,"send email to " + ApplictationClass.contacts.get(index).getName()));
            }
        });

        editImgV  = findViewById(R.id.editImgV);
        editImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit = !edit;
                if(edit){
                    nameEditTxt.setVisibility(View.VISIBLE);
                    emailEditTxt.setVisibility(View.VISIBLE);
                    phoneEditTxt.setVisibility(View.VISIBLE);
                    submitBtn.setVisibility(View.VISIBLE);
                }
                else {
                    nameEditTxt.setVisibility(View.GONE);
                    emailEditTxt.setVisibility(View.GONE);
                    phoneEditTxt.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.GONE);
                }
            }
        });

        deleteImgV = findViewById(R.id.deleteImgV);
        deleteImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(ContactInfo.this);
                dialog.setMessage("Are you sure you want to delete contact?");

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showProgress(true);

                        Backendless.Persistence.of(Contact.class).remove(ApplictationClass.contacts.get(index), new AsyncCallback<Long>() {
                            @Override
                            public void handleResponse(Long response) {
                                ApplictationClass.contacts.remove(index);
                                Toast.makeText(ContactInfo.this, "Contact deleted successfully!", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                ContactInfo.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(ContactInfo.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                dialog.show();
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameEditTxt.getText().toString().isEmpty() || emailEditTxt.getText().toString().isEmpty()
                 || phoneEditTxt.getText().toString().isEmpty()){
                    Toast.makeText(ContactInfo.this, "Enter all fields!", Toast.LENGTH_SHORT).show();
                }
                else{
                    ApplictationClass.contacts.get(index).setName(nameEditTxt.getText().toString().trim());
                    ApplictationClass.contacts.get(index).setEmail(emailEditTxt.getText().toString().trim());
                    ApplictationClass.contacts.get(index).setNumber(phoneEditTxt.getText().toString().trim());

                    showProgress(true);

                    Backendless.Persistence.save(ApplictationClass.contacts.get(index), new AsyncCallback<Contact>() {
                        @Override
                        public void handleResponse(Contact response) {
                            charTxtV.setText(ApplictationClass.contacts.get(index).getName().toUpperCase().charAt(0) + "");
                            nameTxtV.setText(ApplictationClass.contacts.get(index).getName());
                            Toast.makeText(ContactInfo.this, "Contact successfully updated", Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(ContactInfo.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });
                }
            }
        });

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}