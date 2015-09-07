package com.pennapps.instanote;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * Created by QingxiaoDong on 9/5/15.
 */
public class EditActivity extends ActionBarActivity{

    InstaNotebookDBHelper inDB;
    int noteID;

    EditText title;
    EditText date;
    EditText location;
    EditText detail;


    String title_str;
    String context_str;
    String time_str;
    String date_str;
    String host_str;
    String address_str;
    String picture_str;
    String category_str;

    private static final int REQUEST_DATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        title = (EditText)findViewById(R.id.item_title);
        date = (EditText)findViewById(R.id.item_date);
        location = (EditText)findViewById(R.id.item_location);
        detail = (EditText)findViewById(R.id.item_detail);
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Intent intent = new Intent(view.getContext(), EditDateFragmentActivity.class);
                    startActivityForResult(intent, REQUEST_DATE);
                }
            }
        });
        // no conflicts
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditDateFragmentActivity.class);
                startActivityForResult(intent, REQUEST_DATE);
            }
        });

        inDB= new InstaNotebookDBHelper(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        noteID = extras.getInt("id");
        Log.d(Integer.toString(noteID), "noteID");
        Cursor rs = inDB.getOneNote(noteID);
        rs.moveToFirst();

        title_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_TITLE));
        context_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_CONTEXT));
        time_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_TIME));
        date_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_DATE));
        host_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_HOST));
        address_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_ADDRESS));
        picture_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_PICTURE));
        category_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_FAVOURITE));

        title.setText(title_str);
        date.setText(date_str);
        location.setText(address_str);
        detail.setText(context_str);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_DATE && resultCode == RESULT_OK) {
            int year = (int) data.getExtras().get("year");
            int month = (int) data.getExtras().get("month");
            int day = (int) data.getExtras().get("day");
            int hour = (int) data.getExtras().get("hour");
            int minute = (int) data.getExtras().get("minute");
            date.setText(Integer.toString(year) + "/" +
                    Integer.toString(month) + "/" +
                    Integer.toString(day) + " " +
                    Integer.toString(hour) + ":" +
                    Integer.toString(minute));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete){
            inDB.deleteNote(noteID);
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_done) {
            title_str = title.getText().toString();
            date_str = date.getText().toString();
            address_str = location.getText().toString();
            context_str = detail.getText().toString();
            inDB.updateNote(noteID,title_str,context_str,time_str,date_str,host_str,address_str,
                    picture_str,category_str);
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
