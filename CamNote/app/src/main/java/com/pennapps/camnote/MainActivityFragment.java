package com.pennapps.camnote;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        Button button1 = (Button) rootView.findViewById(R.id.button_courses);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventListActivity.class);
                startActivity(intent);
            }
        });

        Button button2 = (Button) rootView.findViewById(R.id.button_activity);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventListActivity.class);
                startActivity(intent);
            }
        });

        Button button3 = (Button) rootView.findViewById(R.id.button_info);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventListActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
