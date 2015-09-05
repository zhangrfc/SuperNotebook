package com.pennapps.camnote;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.hudomju.swipe.*;

import java.util.ArrayList;

/**
 * Created by QingxiaoDong on 9/4/15.
 */
public class EventListFragment extends Fragment{

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

                Intent intent = new Intent(getActivity(),EventDetailActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getEventList();
    }

    private void getEventList() {
        EventItem item1 =  new EventItem("Penn Apps XII", "Sep. 4");
        EventItem item2 = new EventItem("Microsoft Azure", "Sep. 9");
        EventItem item3 = new EventItem("Google Resume Review", "Sep. 9");
        mEventListArrayAdapter.add(item1);
        mEventListArrayAdapter.add(item2);
        mEventListArrayAdapter.add(item3);
    }
}
