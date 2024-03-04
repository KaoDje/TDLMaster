package com.example.tdlmaster;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class AddTaskFragment extends Fragment {
    public AddTaskFragment() {
        // Constructeur vide requis
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate le layout pour ce fragment
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);

        EditText titleEditText = view.findViewById(R.id.titleEditText);
        EditText descriptionEditText = view.findViewById(R.id.descriptionEditText);
        Button submitTaskButton = view.findViewById(R.id.submitTaskButton);

        submitTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                ((MainActivity)getActivity()).addNewTask(title,description);
                // Ferme le fragment après l'ajout
                getActivity().getSupportFragmentManager().beginTransaction().remove(AddTaskFragment.this).commit();
                closeKeyboard();
            }
        });

        return view;
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

