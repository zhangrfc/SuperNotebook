package com.pennapps.instanote;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by QingxiaoDong on 9/4/15.
 */
public class EventDetailActivity extends ActionBarActivity {
    //private ShareActionProvider mShareActionProvider;

    InstaNotebookDBHelper inDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
       // MenuItem item = menu.findItem(R.id.action_share);
       // mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Bundle extras = getIntent().getExtras();
        int noteID = extras.getInt("id");
        inDB = new InstaNotebookDBHelper(getApplicationContext());
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            Bundle dataBundle = new Bundle();
            Log.d(Integer.toString(noteID),"IDrow");
            dataBundle.putInt("id", noteID);
            Intent editIntent = new Intent(EventDetailActivity.this, EditActivity.class);
            editIntent.putExtras(dataBundle);
            startActivity(editIntent);

        } else if (id == R.id.action_share) {
            Cursor rs= inDB.getOneNote(noteID);
            rs.moveToFirst();
            String title_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_TITLE));
            String context_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_CONTEXT));
            String date_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_DATE));
            String address_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_ADDRESS));
            String shareText = "Date " + date_str + "\nTitle " + title_str +
                    "\nLocation " + address_str + "\nDetail " + context_str;
            Log.d("String", shareText);
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            sendIntent.setType("text/plain");
           // mShareActionProvider.setShareIntent(sendIntent);
            startActivity(sendIntent);
        }


        return super.onOptionsItemSelected(item);
    }
}
