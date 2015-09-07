package com.pennapps.instanote;

/**
 * Created by zhangrf on 2015/9/5.
 */

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayNote extends ActionBarActivity {
    int from_Where_I_Am_Coming = 0;
    private InstaNotebookDBHelper mydb ;

    TextView title ;
    TextView context;
    TextView time;
    TextView date;
    TextView host;
    TextView address;
    TextView picture;
    TextView category;

    int id_To_Update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_note);
        title = (TextView) findViewById(R.id.editText1);
        context = (TextView) findViewById(R.id.editText2);
        time = (TextView) findViewById(R.id.editText3);
        date = (TextView) findViewById(R.id.editText4);
        host = (TextView) findViewById(R.id.editText5);
        address = (TextView) findViewById(R.id.editText6);
        picture = (TextView) findViewById(R.id.editText7);
        category = (TextView) findViewById(R.id.editText8);

        mydb = new InstaNotebookDBHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            int Value = extras.getInt("id");

            if(Value>0){
                //means this is the view part not the add contact part.
                Cursor rs = mydb.getOneNote(Value);
                id_To_Update = Value;
                rs.moveToFirst();

                String title_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_TITLE));
                String context_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_CONTEXT));
                String time_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_TIME));
                String date_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_DATE));
                String host_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_HOST));
                String address_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_ADDRESS));
                String picture_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_PICTURE));
                String category_str = rs.getString(rs.getColumnIndex(InstaNotebookDBHelper.NOTE_COLUMN_FAVOURITE));

                if (!rs.isClosed())
                {
                    rs.close();
                }
                Button b = (Button)findViewById(R.id.button1);
                b.setVisibility(View.INVISIBLE);

                title.setText((CharSequence)title_str);
                title.setFocusable(false);
                title.setClickable(false);

                context.setText((CharSequence)context_str);
                context.setFocusable(false);
                context.setClickable(false);

                time.setText((CharSequence)time_str);
                time.setFocusable(false);
                time.setClickable(false);

                date.setText((CharSequence)date_str);
                date.setFocusable(false);
                date.setClickable(false);

                host.setText((CharSequence)host_str);
                host.setFocusable(false);
                host.setClickable(false);

                address.setText((CharSequence)address_str);
                address.setFocusable(false);
                address.setClickable(false);

                picture.setText((CharSequence)picture_str);
                picture.setFocusable(false);
                picture.setClickable(false);

                category.setText((CharSequence)category_str);
                category.setFocusable(false);
                category.setClickable(false);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Bundle extras = getIntent().getExtras();

        if(extras !=null)
        {
            int Value = extras.getInt("id");
            if(Value>0){
                getMenuInflater().inflate(R.menu.menu_display_note, menu);
            }

            else{
                getMenuInflater().inflate(R.menu.menu_main, menu);
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch(item.getItemId())
        {
            case R.id.Edit_Contact:
                Button b = (Button)findViewById(R.id.button1);
                b.setVisibility(View.VISIBLE);
                title.setEnabled(true);
                title.setFocusableInTouchMode(true);
                title.setClickable(true);

                context.setEnabled(true);
                context.setFocusableInTouchMode(true);
                context.setClickable(true);

                time.setEnabled(true);
                time.setFocusableInTouchMode(true);
                time.setClickable(true);

                date.setEnabled(true);
                date.setFocusableInTouchMode(true);
                date.setClickable(true);

                host.setEnabled(true);
                host.setFocusableInTouchMode(true);
                host.setClickable(true);

                address.setEnabled(true);
                address.setFocusableInTouchMode(true);
                address.setClickable(true);

                picture.setEnabled(true);
                picture.setFocusableInTouchMode(true);
                picture.setClickable(true);

                category.setEnabled(true);
                category.setFocusableInTouchMode(true);
                category.setClickable(true);

                return true;
            case R.id.Delete_Contact:
            /*
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deleteContact)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteNote(id_To_Update);
                                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog d = builder.create();
                d.setTitle("Are you sure");
                d.show();

                return true;
                */
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void run(View view)
    {
        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            int Value = extras.getInt("id");
            if(Value>0){
                if(mydb.updateNote(id_To_Update, title.getText().toString(), context.getText().toString(),
                        time.getText().toString(), date.getText().toString(),
                        host.getText().toString(), address.getText().toString(),
                        picture.getText().toString(), category.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                if(mydb.insertNote(title.getText().toString(), context.getText().toString(),
                        time.getText().toString(), date.getText().toString(),
                        host.getText().toString(), address.getText().toString(),
                        picture.getText().toString(), category.getText().toString()) != -1){
                    Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(getApplicationContext(), "not done", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
