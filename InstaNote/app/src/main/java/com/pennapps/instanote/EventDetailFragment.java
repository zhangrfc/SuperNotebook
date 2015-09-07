package com.pennapps.instanote;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

/**
 * Created by QingxiaoDong on 9/5/15.
 */
public class EventDetailFragment extends Fragment {

    InstaNotebookDBHelper inDB;
    int noteID;
    ImageView pic;
    String favorite_str;

    public EventDetailFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView =  inflater.inflate(R.layout.fragment_event_detail, container, false);
        inDB= new InstaNotebookDBHelper(getActivity().getApplicationContext());

        Bundle extras = getActivity().getIntent().getExtras();
        noteID = extras.getInt("id");
        int noteID = extras.getInt("id");
        Log.i("GETIDFROM", Integer.toString(noteID));
        Cursor rs = inDB.getOneNote(noteID);
        rs.moveToFirst();

        String title_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_TITLE));
        String context_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_CONTEXT));
        String time_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_TIME));
        String date_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_DATE));
        String host_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_HOST));
        String address_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_ADDRESS));
        String picture_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_PICTURE));
        String category_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_FAVOURITE));
        favorite_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_FAVOURITE));


        TextView name = (TextView) rootView.findViewById(R.id.item_name);
        TextView date = (TextView) rootView.findViewById(R.id.item_date);
        final TextView location = (TextView) rootView.findViewById(R.id.item_location);
        TextView description = (TextView) rootView.findViewById(R.id.item_description);

        name.setText((CharSequence) title_str);
        date.setText((CharSequence)date_str);
        location.setText((CharSequence)address_str);
        description.setText((CharSequence)context_str);

        Log.i("PICSTR", picture_str);
        if (picture_str != null && !picture_str.equals("") && !picture_str.equals("pic")) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.item_pic);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picture_str));
        }
        ImageButton mapImg = (ImageButton) rootView.findViewById(R.id.map_button);
        if(location.getText().toString().equals("")){
            mapImg.setVisibility(View.INVISIBLE);
        }
        mapImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = null, chooser = null;
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:0,0?q=" + location.getText().toString()));
                chooser = Intent.createChooser(intent, "Launch Maps");
                startActivity(chooser);
            }
        });

        final FloatingActionButton floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.star_fab);

        if (favorite_str.equals("1")) {

            floatingActionButton.setImageResource(R.drawable.ic_action_yellowstar);
        } else {

            floatingActionButton.setImageResource(R.drawable.ic_star);
        }
        final String fs = favorite_str;
        final int fnoteid = noteID;
        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Log.d("fs",fs);
                if(favorite_str.equals("1")) {
                    inDB.updatefav(fnoteid, "0");
                    favorite_str = "0";
                    floatingActionButton.setImageResource(R.drawable.ic_star);

                } else {
                    inDB.updatefav(fnoteid, "1");
                    favorite_str = "1";
                    floatingActionButton.setImageResource(R.drawable.ic_action_yellowstar);
                }
            }
        });

        return rootView;
    }

}
