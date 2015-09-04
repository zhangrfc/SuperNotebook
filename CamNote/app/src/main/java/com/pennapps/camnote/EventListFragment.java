package com.pennapps.camnote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by QingxiaoDong on 9/4/15.
 */
public class EventListFragment extends Fragment {

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
