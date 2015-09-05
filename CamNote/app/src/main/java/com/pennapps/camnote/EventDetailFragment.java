package com.pennapps.camnote;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by QingxiaoDong on 9/5/15.
 */
public class EventDetailFragment extends Fragment {

    InstaNotebookDBHelper inDB;

    ImageView pic;
    TextView name;
    TextView date;
    TextView location;
    TextView description;

    public EventDetailFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView =  inflater.inflate(R.layout.fragment_event_detail, container, false);
        inDB= new InstaNotebookDBHelper(getActivity().getApplicationContext());

        Bundle extras = getActivity().getIntent().getExtras();
        int noteID = extras.getInt("id");

        Cursor rs = inDB.getOneNote(noteID);
        rs.moveToFirst();

        String title_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_TITLE));
        String context_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_CONTEXT));
        String time_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_TIME));
        String date_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_DATE));
        String host_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_HOST));
        String address_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_ADDRESS));
        String picture_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_PICTURE));
        String category_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_CATEGORY));


        name = (TextView) getActivity().findViewById(R.id.item_name);
        date = (TextView) getActivity().findViewById(R.id.item_date);
        location = (TextView) getActivity().findViewById(R.id.item_location);
        description = (TextView) getActivity().findViewById(R.id.item_description);

        //name.setText((CharSequence)title_str);
        //date.setText((CharSequence)date_str);
        //location.setText((CharSequence)address_str);
        //description.setText((CharSequence)context_str);

        return rootView;
    }

}
