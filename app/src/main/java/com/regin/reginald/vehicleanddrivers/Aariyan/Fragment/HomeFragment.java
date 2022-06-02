package com.regin.reginald.vehicleanddrivers.Aariyan.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.regin.reginald.vehicleanddrivers.R;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private TextView getBtn;
    private View root;

    public HomeFragment() {
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
        root =  inflater.inflate(R.layout.fragment_home, container, false);
        initUI();
        return root;
    }

    private void initUI() {
        getBtn = root.findViewById(R.id.getBtn);
        getBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.getBtn:
                Toast.makeText(requireContext(), "Coming soon!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}