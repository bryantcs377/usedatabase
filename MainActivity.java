package cs377.usedatabase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    GradeDbHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     // ***********************
        helper = new GradeDbHelper(this);
        db = helper.getWritableDatabase();
    }
    //**********************
    protected void addGrade(View v) {
        // Retrieve fields from screen
        String event = ((EditText) findViewById(R.id.event)).getText().toString();
        String grade = ((EditText) findViewById(R.id.grade)).getText().toString();
        String course = ((EditText) findViewById(R.id.course)).getText().toString();


        // Create a new map of field values, where column names are the keys
        //   ***  PreLoad 4 or 5 Reviews ???
        ContentValues values = new ContentValues();

        values.put(GradeContract.Grade.COLUMN_NAME_EVENT, event);
        values.put(GradeContract.Grade.COLUMN_NAME_GRADE, grade);
        values.put(GradeContract.Grade.COLUMN_NAME_COURSE, course);
    // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(GradeContract.Grade.TABLE_NAME, null, values);
        if (newRowId == -1) {
            TextView txt = (TextView) findViewById(R.id.success);
            txt.setText("Add Failed");
        } else {
            TextView txt = (TextView) findViewById(R.id.success);
            txt.setText("Added Grade");
        }
    }

    // Define a projection that specifies which columns from the database
    // you will actually use after this query.

    protected void viewGrades(View v) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {
                GradeContract.Grade._ID,
                GradeContract.Grade.COLUMN_NAME_EVENT,
                GradeContract.Grade.COLUMN_NAME_COURSE,
                GradeContract.Grade.COLUMN_NAME_GRADE,
        };


        //   FeedEntry???  Connect with Query???
        // Filter results WHERE "title" = 'My Title'
        // String selection = FeedEntry.COLUMN_NAME_COURSE + " = ?";
        //String[] selectionArgs = { "History of Europe" };

        // How you want the results sorted in the resulting Cursor
        //   **** If we know column name, this will give column sort
        //   **** How to get FILTER value??
        String sortOrder =
                GradeContract.Grade.COLUMN_NAME_COURSE + " ASC";

        Cursor c = db.query(

                GradeContract.Grade.TABLE_NAME,           // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause *** ? FILTER
                null,                                     // The values for the WHERE clause *** ? FILTER
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );


        boolean more = c.moveToFirst();
        String result = "";
        while(more == true) {

            String event = c.getString(c.getColumnIndexOrThrow(GradeContract.Grade.COLUMN_NAME_EVENT));
            String grade = c.getString(c.getColumnIndexOrThrow(GradeContract.Grade.COLUMN_NAME_GRADE));
            String course = c.getString(c.getColumnIndexOrThrow(GradeContract.Grade.COLUMN_NAME_COURSE));
            result += event +": " + grade + " (" + course + ") \n";
            more = c.moveToNext();
        }
        //  **** Might set fields with ListView??
        ((TextView) findViewById(R.id.success)).setText(result);

    }

}

