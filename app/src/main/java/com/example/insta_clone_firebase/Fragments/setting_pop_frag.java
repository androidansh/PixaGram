package com.example.insta_clone_firebase.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.insta_clone_firebase.R;
import com.example.insta_clone_firebase.activities.bookmark_post_activity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class setting_pop_frag extends BottomSheetDialogFragment {

    private ConstraintLayout setting,activity,archive,saved;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_pop_frag, container, false);

        setting = view.findViewById(R.id.setting);
        activity = view.findViewById(R.id.activity);
        archive = view.findViewById(R.id.archive);
        saved = view.findViewById(R.id.saved);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroyView();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.homeFrame,new EditProfileFrag());
                ft.addToBackStack("");
                ft.commit();
            }
        });

        saved.setOnClickListener(v->{
            onDestroyView();
            startActivity(new Intent(getActivity(),bookmark_post_activity.class));
        });
        return view;
    }
}