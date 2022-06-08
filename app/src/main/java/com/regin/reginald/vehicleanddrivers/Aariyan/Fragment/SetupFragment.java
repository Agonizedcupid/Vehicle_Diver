package com.regin.reginald.vehicleanddrivers.Aariyan.Fragment;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.regin.reginald.vehicleanddrivers.Aariyan.Activity.LandingActivity;
import com.regin.reginald.vehicleanddrivers.R;

public class SetupFragment extends Fragment {

    private View root;
    private ConstraintLayout snackBarLayout;

    public SetupFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_setup, container, false);

        initUI();
        return root;
    }

    private void initUI() {
        snackBarLayout = root.findViewById(R.id.snackBarLayout);
        LandingActivity.resetDatabaseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(snackBarLayout, getResources().getString(R.string.resetDatabaseTitle), Snackbar.LENGTH_SHORT)
                        .setAction(getResources().getString(R.string.yes), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resetDatabase();
                            }
                        }).show();
            }
        });

    }

    //Reset the database
    private void resetDatabase() {
        Toast.makeText(requireContext(), "Database Reset", Toast.LENGTH_SHORT).show();
    }
}