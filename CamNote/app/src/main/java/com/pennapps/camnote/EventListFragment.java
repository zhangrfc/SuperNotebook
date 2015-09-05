package com.pennapps.camnote;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by QingxiaoDong on 9/4/15.
 */


public class EventListFragment extends Fragment{
    public static int CurrentID;

    InstaNotebookDBHelper inDB;

    EventListArrayAdapter mEventListArrayAdapter;

    public EventListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView =  inflater.inflate(R.layout.fragment_event_list, container, false);

        mEventListArrayAdapter = new EventListArrayAdapter(getActivity(), R.layout.event_item_view,
                R.id.event_item, new ArrayList<EventItem>());
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_events);
        listView.setAdapter(mEventListArrayAdapter);

        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(listView),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {
                                inDB.deleteNote(mEventListArrayAdapter.getItem(position).id);
                                mEventListArrayAdapter.remove(mEventListArrayAdapter.getItem(position));
                                mEventListArrayAdapter.notifyDataSetChanged();
                            }
                        });
        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
                    // Toast.makeText(getActivity().getApplication(), "Position " + position, Toast.LENGTH_SHORT).show();
                }
            }
        });

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

                Intent intent = new Intent(getActivity(), DisplayNote.class);
                intent.putExtras(dataBundle);

                startActivity(intent);
            }
        });



        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        inDB = new InstaNotebookDBHelper(getActivity().getApplicationContext());
        getEventList();
        CurrentID = inDB.numberOfRows();
        Log.d(Integer.toString(CurrentID), "currentID");
    }

    @Override
    public void onStop(){
        super.onStop();
        //inDB.clearAll();
    }

    private void getEventList() {
        mEventListArrayAdapter.clear();

        ArrayList array_list = inDB.getAllNotes();
        for(int i = 0; i < array_list.size(); i++){
            Note note = (Note)array_list.get(i);
            EventItem item = new EventItem(Integer.parseInt(note.NOTE_COLUMN_ID), note.NOTE_COLUMN_TITLE,
                    note.NOTE_COLUMN_DATE);
            mEventListArrayAdapter.add(item);
            CurrentID = inDB.numberOfRows();
            Log.d(Integer.toString(CurrentID), "currentID");
        }


    }
}
