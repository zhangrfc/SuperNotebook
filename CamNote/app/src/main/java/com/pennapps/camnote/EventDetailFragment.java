package com.pennapps.camnote;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by QingxiaoDong on 9/5/15.
 */
public class EventDetailFragment extends Fragment {

    InstaNotebookDBHelper inDB;
    int noteID;
    ImageView pic;

    public EventDetailFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView =  inflater.inflate(R.layout.fragment_event_detail, container, false);
        inDB= new InstaNotebookDBHelper(getActivity().getApplicationContext());

        Bundle extras = getActivity().getIntent().getExtras();
        noteID = extras.getInt("id");

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


        TextView name = (TextView) rootView.findViewById(R.id.item_name);
        TextView date = (TextView) rootView.findViewById(R.id.item_date);
        final TextView location = (TextView) rootView.findViewById(R.id.item_location);
        TextView description = (TextView) rootView.findViewById(R.id.item_description);

        name.setText((CharSequence) title_str);
        date.setText((CharSequence)date_str);
        location.setText((CharSequence)address_str);
        description.setText((CharSequence)context_str);

        ImageButton mapImg = (ImageButton) rootView.findViewById(R.id.map_button);
        if(location.getText().toString().equals("")){
            mapImg.setVisibility(View.INVISIBLE);
        }
        mapImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Uri gmmIntetnUri = Uri.parse("geo:0,0?q=" + location.getText().toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntetnUri);
                mapIntent.setPackage("com.goolge.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        return rootView;
    }

}
