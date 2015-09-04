package com.pennapps.camnote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QingxiaoDong on 9/4/15.
 */
public class EventListArrayAdapter extends ArrayAdapter<EventItem> {

    private final Context mContext;

    public EventListArrayAdapter(Context context, int resource, int textViewResourceId, List<EventItem> objects) {
        super(context, resource, textViewResourceId, objects);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final EventItem eventItem = getItem(position);
        final ItemViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.event_item_view, parent, false);
            viewHolder = new ItemViewHolder();
            viewHolder.eventDateTextView = (TextView) convertView.findViewById(R.id.event_date);
            viewHolder.eventNameTextView = (TextView) convertView.findViewById(R.id.event_name);
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ItemViewHolder) convertView.getTag();
        }

        if (eventItem != null) {
            viewHolder.eventNameTextView.setText(eventItem.name);
            viewHolder.eventDateTextView.setText(eventItem.date);
        }

        return convertView;
    }

    public List<EventItem> getItems() {
        ArrayList<EventItem> games = new ArrayList<EventItem>();
        for (int i = 0; i < this.getCount(); i++) {
            games.add(this.getItem(i));
        }
        return games;
    }
}
