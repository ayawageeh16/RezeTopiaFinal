package io.krito.com.rezetopia.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.krito.com.rezetopia.R;

public class GroupDescriptionFragment extends Fragment implements View.OnClickListener {

    public GroupDescriptionFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    EditText description_tv;
    TextView edit_tv;
    Button submit;
    String editedDescription;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_group_description, container, false);
        description_tv = view.findViewById(R.id.description_tv);
        edit_tv = view.findViewById(R.id.edit_tv);
        submit = view.findViewById(R.id.submit);
        edit_tv.setOnClickListener(this);
        submit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == edit_tv){
          description_tv.setEnabled(true);
          description_tv.requestFocus();
        }else if (v == submit){
            editedDescription =description_tv.getText().toString();
            submit.setText(R.string.submit);
            Toast.makeText(getActivity(), editedDescription, Toast.LENGTH_LONG).show();
        }
    }
}
