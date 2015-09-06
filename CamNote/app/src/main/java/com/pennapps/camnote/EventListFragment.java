package com.pennapps.camnote;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.melnykov.fab.FloatingActionButton;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.TimedUndoAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by QingxiaoDong on 9/4/15.
 */


public class EventListFragment extends Fragment{
    public static int CurrentID;
    private String photoPath;
    private static final int REQUEST_TAKE_PHOTO_PAINT = 1;
    private DynamicListView listView;
    private int size;

    InstaNotebookDBHelper inDB;

    EventListArrayAdapter mEventListArrayAdapter;
    TimedUndoAdapter mTimedUndoAdapter;
    RadioGroup sortRadioGroup;
    int currentSortCode;

    public EventListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView =  inflater.inflate(R.layout.fragment_event_list, container, false);

        mEventListArrayAdapter = new EventListArrayAdapter(getActivity(), R.layout.event_item_view,
                R.id.event_item, new ArrayList<EventItem>());
        DynamicListView listView = (DynamicListView) rootView.findViewById(R.id.list_view_events);

        mTimedUndoAdapter = new TimedUndoAdapter(mEventListArrayAdapter, rootView.getContext(),
                new OnDismissCallback() {
                    @Override
                    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            inDB.deleteNote(mEventListArrayAdapter.getItem(position).id);
                            mEventListArrayAdapter.remove(mEventListArrayAdapter.getItem(position));
                            mEventListArrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
        );
        mTimedUndoAdapter.setAbsListView(listView);
        listView.setAdapter(mTimedUndoAdapter);
        listView.enableSimpleSwipeUndo();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                EventItem eventItem = mEventListArrayAdapter.getItem(position);
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", eventItem.id);
                Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });


        FloatingActionButton floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.camera_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Tag", "Clicked");

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);

                // Integrating Camera Paint
                File image = null;
                try {
                    image = createImageFile();
                } catch (IOException ex) {
                    Log.e("exception", ex.toString());
                }
                photoPath = image.getAbsolutePath();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
                startActivityForResult(intent, REQUEST_TAKE_PHOTO_PAINT);
            }
        });

        this.listView = listView;

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        currentSortCode = CustomEventComparator.ST_ORDER;
        inDB = new InstaNotebookDBHelper(getActivity().getApplicationContext());
        size = getEventList();
        Log.d(Integer.toString(inDB.numberOfRows()), "numofRows");
        //inDB.insertNote("Title", "context", "time", "date", "host", "add", "pic", "cat");
        //inDB.insertNote("Title2", "context", "time", "date", "host", "add", "pic", "cat");
        Log.d(Integer.toString(CurrentID), "currentID");

    }

    @Override
    public void onStop(){
        super.onStop();
        //inDB.clearAll();
    }

    // Captures when Camera activity returns.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Old request
        if (requestCode == REQUEST_TAKE_PHOTO_PAINT && resultCode == getActivity().RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            // Check if image really exist
            if (checkIfImgExists(photoPath))
                openCamPaintActivity(photoPath);
        }
    }


    private int getEventList() {
        mEventListArrayAdapter.clear();

        ArrayList array_list = inDB.getAllNotes();
        for(int i = 0; i < array_list.size(); i++){
            Note note = (Note)array_list.get(i);
            EventItem item = new EventItem(Integer.parseInt(note.NOTE_COLUMN_ID), note.NOTE_COLUMN_TITLE,
                    note.NOTE_COLUMN_DATE, note.NOTE_COLUMN_FAVOURITE);
            mEventListArrayAdapter.add(item);
        }
        return array_list.size();
    }

    public void openCamPaintActivity(String imgPath) {
        Intent intent = new Intent(getActivity().getApplicationContext(), CamPaintActivity.class);
        intent.putExtra("IMAGE", imgPath);
        startActivity(intent);
    }

    private boolean checkIfImgExists(String path) {
        // Check if image really exist
        File image = new File(path);
        if (image.exists()) {
            Log.i("image", "file exist.");
            Log.i("image", path);
            return true;
        } else Log.i("image", "file does not exist. ");
        return false;
        // Pass the path of photo to next activity.
    }

    // Create an empty image file, for future storage purpose
    private File createImageFile() throws IOException {
        // Create a file name to avoid collision
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "/JPEG_" + timeStamp + ".jpg";
        File storageDir = getActivity().getExternalFilesDir(null);
        return new File(storageDir.getAbsolutePath() + imageFileName);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort) {

            final Dialog sortDialog = new Dialog(getActivity());
            sortDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            sortDialog.setTitle(R.string.sort_preference_label);
            sortDialog.setContentView(R.layout.sort_dialog);
            RadioButton rb = null;
            sortRadioGroup = (RadioGroup) sortDialog.findViewById(R.id.sort_dialog_choices);
            rb = (RadioButton) sortRadioGroup.findViewById(currentSortCode);
            if (rb != null) {
                rb.setChecked(true);
            }

            sortDialog.show();

            if (sortRadioGroup != null) {
                sortRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if (i == currentSortCode) {
                        } else {
                            mEventListArrayAdapter.sort(new CustomEventComparator(i));
                            DynamicListView listView = (DynamicListView) getView().findViewById(R.id.list_view_events);
                            mTimedUndoAdapter.setAbsListView(listView);
                            listView.setAdapter(mTimedUndoAdapter);
                            listView.enableSimpleSwipeUndo();
                            listView.setAdapter(mEventListArrayAdapter);
                            currentSortCode = i;
                        }
                    }
                });
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
