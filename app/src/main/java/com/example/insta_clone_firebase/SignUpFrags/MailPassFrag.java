package com.example.insta_clone_firebase.SignUpFrags;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.SignUpActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailPassFrag extends Fragment {

    private TextView msg1,msg2,msg3;
    private EditText createEmail,createPassword,otp;
    private Button verifyOtp,next;
    private String OTP_VERIFIED = "";
    private TextView whiteScr;
    private ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mail_pass, container, false);
        initialiseViews(view);
        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(createEmail.getText().toString().isEmpty()) {
                    createEmail.setError("Enter email to receive OTP.");
                }
            }
        });

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OTP_VERIFIED.equals("")){
                    whiteScr.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.VISIBLE);
                    createEmail.setEnabled(false);
                    otp.setEnabled(false);
                    new Thread(new RunningClass()).start();
//                    Handler threadHandler = new Handler(Looper.getMainLooper());
//                    threadHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            send_text_email(createEmail.getText().toString().trim());
//                        }
//                    });
                }
                else{
                    if(otp.getText().toString().trim().isEmpty()){
                        otp.setError("Enter the OTP first.");
                    }
                    else{
                        if(otp.getText().toString().trim().equals(OTP_VERIFIED)){
                            otp.setEnabled(false);
                            createEmail.setTextColor(0xFF03AF0A);
                            verifyOtp.setEnabled(false);
                            createPassword.setVisibility(View.VISIBLE);
                            createPassword.setEnabled(true);
                            next.setEnabled(true);
                            next.setVisibility(View.VISIBLE);
                            msg1.setVisibility(View.INVISIBLE);
                            msg3.setVisibility(View.INVISIBLE);
                            msg2.setVisibility(View.VISIBLE);
                        }
                    }
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(createPassword.getText().toString().trim().isEmpty()){
                    createPassword.setError("Please enter the password.");
                } else if (createEmail.getText().toString().trim().isEmpty()) {
                    createEmail.setError("Please enter email.");
                }
                else{
                    progress.setVisibility(View.VISIBLE);
                    whiteScr.setVisibility(View.VISIBLE);
                    next.setEnabled(false);
                    createPassword.setEnabled(false);
                    createUserRun run = new createUserRun();
                    new Thread(run).start();
                }

            }
        });


        return view;
    }

    class createUserRun implements  Runnable{

        @Override
        public void run() {
            CreateUserWithPassword(createEmail.getText().toString().trim(),createPassword.getText().toString().trim());
        }
    }
    private void CreateUserWithPassword(String email, String password)
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(getContext(), "Created User.", Toast.LENGTH_SHORT).show();
                String user_uid = auth.getCurrentUser().getUid();

                SignUpActivity.createUserModel.setUser_uid(user_uid);
                SignUpActivity.createUserModel.setUser_mail(email);
                SignUpActivity.createUserModel.setUser_password(password);

            // adding data to firebase db
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("Instagram User Emails").document(user_uid);
                Map<String,String> obj = new HashMap<>();
                obj.put("user_uid",user_uid);
                obj.put("user_id",SignUpActivity.createUserModel.getUser_id());
                obj.put("email",email);
                docRef.set(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused)
                    {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.signUpFrame,new AddDataFrag());
                        ft.commit();
                        System.out.println("email added to database");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        whiteScr.setVisibility(View.INVISIBLE);
                        progress.setVisibility(View.INVISIBLE);
                        next.setEnabled(true);
                        System.out.println("error in adding data in db = " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                createEmail.setEnabled(true);
                createPassword.setEnabled(true);
                whiteScr.setVisibility(View.INVISIBLE);
                progress.setVisibility(View.INVISIBLE);
                next.setEnabled(true);
                Toast.makeText(getContext(), "Error in creating account" + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    private void initialiseViews(View view)
    {
        msg1 = view.findViewById(R.id.msg1);
        msg2 = view.findViewById(R.id.msg2);
        msg3 = view.findViewById(R.id.msg3);

        whiteScr = view.findViewById(R.id.whiteScr);
        progress = view.findViewById(R.id.progress);

        createEmail = view.findViewById(R.id.createEmail);
        otp = view.findViewById(R.id.otp);
        createPassword = view.findViewById(R.id.createPassword);

        verifyOtp = view.findViewById(R.id.verifyOTP);
        next = view.findViewById(R.id.next2);

    }

    // for email sending
    class RunningClass implements Runnable{
        @Override
        public void run() {
           // Toast.makeText(getContext(), "Entering in thread.", Toast.LENGTH_SHORT).show();
            try {
                send_text_email(createEmail.getText().toString().trim());
            }
            catch (Exception e){
                createEmail.setEnabled(true);
                System.out.println("Error in sending email = " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    private void send_text_email( String email)
    {
        Properties properties = System.getProperties();

        // host setup
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.auth","true");

        // getting session object
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("004instagramrobo778","hvwxjxiiecagusow");
            }
        });
        Random random = new Random();
        int random_otp = random.nextInt(999999) + 100000;
        OTP_VERIFIED = random_otp + "";
        String msg = "Hello! user. Your one time OTP for email verification is '" + random_otp +
                "'. Do  not share it with others. This is valid for only 5 minutes.";
        String subject = "noreply@instagram.com";
        String from = "0909anshumanofficial2004@gmail.com";

        MimeMessage m = new MimeMessage(session);

        try
        {
            m.setFrom(new InternetAddress(from));
            m.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            m.setSubject(subject);
            m.setText(msg);
            Transport.send(m);
            System.out.println("Email Sent.");
            Handler threadHandler = new Handler(Looper.getMainLooper());
            //Toast.makeText(getContext(), "success in entering  in thread.", Toast.LENGTH_SHORT).show();
            threadHandler.post(new Runnable() {
                @Override
                public void run() {
                    verifyOtp.setText("Verify OTP");
                    otp.setEnabled(true);
                    whiteScr.setVisibility(View.INVISIBLE);
                    progress.setVisibility(View.INVISIBLE);
                    verifyOtp.setEnabled(true);
                    createEmail.setEnabled(false);

                }
            });

        }
        catch(Exception e)
        {
            Handler threadHandler = new Handler(Looper.getMainLooper());
//            Toast.makeText(getContext(), "success in entering  in thread.", Toast.LENGTH_SHORT).show();
            threadHandler.post(new Runnable() {
                @Override
                public void run() {
                    verifyOtp.setText("Verify OTP");
                    otp.setEnabled(true);
                    whiteScr.setVisibility(View.INVISIBLE);
                    progress.setVisibility(View.INVISIBLE);
                    createEmail.setEnabled(true);

                }
            });
            System.out.println("error in email sending  = " + e);
            e.printStackTrace();
        }
    }}