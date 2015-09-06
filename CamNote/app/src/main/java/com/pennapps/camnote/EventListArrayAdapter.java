package com.pennapps.camnote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QingxiaoDong on 9/4/15.
 */
public class EventListArrayAdapter extends ArrayAdapter<EventItem> implements UndoAdapter {

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
            //viewHolder.eventDateTextView = (TextView) convertView.findViewById(R.id.event_date);
            viewHolder.eventNameTextView = (TextView) convertView.findViewById(R.id.event_name);
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ItemViewHolder) convertView.getTag();
        }

        if (eventItem != null) {
            viewHolder.eventNameTextView.setText(eventItem.name);
            //viewHolder.eventDateTextView.setText(eventItem.date);
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

    @NonNull
    @Override
    public View getUndoView(final int position, final View convertView, @NonNull final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.undo_row, parent, false);
        }
        return view;
    }

    @NonNull
    @Override
    public View getUndoClickView(@NonNull View view) {
        return view.findViewById(R.id.undo_row_undobutton);
    }
}
