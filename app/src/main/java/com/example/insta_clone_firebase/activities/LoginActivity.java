package com.example.insta_clone_firebase.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.insta_clone_firebase.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
   private EditText email,password;
   private Button signIn,createAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        signIn = findViewById(R.id.signIn);
        createAcc = findViewById(R.id.SignUp);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginEmailPassword(email.getText().toString(),password.getText().toString());
                if(!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
                }
            }
        });
        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, HomeScreenActivity.class));
                //get_all_post_storage.getPosts(getApplicationContext());
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });
//        running Running = new running();
//        new Thread(Running).start();

    }


    private void LoginEmailPassword(String email, String password) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        System.out.println("email = " + email);
        System.out.println("Password = " + password);
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>()
        {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                System.out.println("Logged in Successfully");
                System.out.println("Auth credential :: " + authResult.getCredential());
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference ref = db.collection("Instagram User Emails").document(auth.getCurrentUser().getUid());
                ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String db_id  = documentSnapshot.get("user_id").toString();
//                        System.out.println("database id  = " + db_id);

                        SharedPreferences pref =getSharedPreferences("Pref_Logged_Session", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString("database_id",db_id);
                        edit.putBoolean("isLogged",true);
                        edit.apply();
                        startActivity(new Intent(LoginActivity.this, HomeScreenActivity.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Error   = "  + e.getStackTrace());
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, "Error in login", Toast.LENGTH_SHORT).show();

            }
        });
    }

}