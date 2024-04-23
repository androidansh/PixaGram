package com.example.insta_clone_firebase.for_got_pass_word;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.HomeScreenActivity;

public class CurrPassword extends Fragment {

    private ImageView back,showPass;
    private EditText currPass;
    private Button proceed;
    private Boolean show= false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_curr_password, container, false);
        back = view.findViewById(R.id.backForgot2);
        showPass = view.findViewById(R.id.showCurrPass);
        currPass = view.findViewById(R.id.currPass);
        proceed = view.findViewById(R.id.proceed);

        showPass.setOnClickListener(v->{
            if(currPass.getText().toString().length()<1){
                return;
            }
            if(show){
                show = false;
                showPass.setBackgroundResource(R.drawable.eye_on);
                currPass.setInputType(InputType.TYPE_CLASS_TEXT);
            }
            else{
                show = true;
                showPass.setBackgroundResource(R.drawable.eye_off);
                currPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        back.setOnClickListener(v->{
            getActivity().finish();
        });

        proceed.setOnClickListener(v->{
            if(currPass.getText().toString().length()<1){
                currPass.setError("Enter password correctly.");
                return;
            }
            if(currPass.getText().toString().equals(HomeScreenActivity.USER_DATA.getUser_password())){
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.for_got_frame,new NewPasswordFrag());
                ft.commit();
            }
            else{
                currPass.setError("Password does not matched.");
            }
        });

        return view;
    }
}