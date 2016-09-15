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
        helper = new GradeDbHelper(this);
        db = helper.getWritableDatabase();
    }

    protected void addGrade(View v) {
        String event = ((EditText) findViewById(R.id.event)).getText().toString();
        String grade = ((EditText) findViewById(R.id.grade)).getText().toString();
        String course = ((EditText) findViewById(R.id.course)).getText().toString();


// Create a new map of values, where column names are the keys
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

    ;

    protected void viewGrade(View v) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {
                GradeContract.Grade._ID,
                GradeContract.Grade.COLUMN_NAME_EVENT,
                GradeContract.Grade.COLUMN_NAME_COURSE,
                GradeContract.Grade.COLUMN_NAME_GRADE,
        };


// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String sortOrder = GradeContract.Grade.COLUMN_NAME_COURSE + "ASC";

        Cursor c = db.query(
                GradeContract.Grade.TABLE_NAME,
                projection,                               // The columns to return
                null, null, null, null,                                // The columns for the WHERE clause
                sortOrder                                 // The sort order
        );
        boolean more = c.moveToFirst();
        String result = "";
        while (more) {
            String event = c.getString(c.getColumnIndexOrThrow(GradeContract.Grade.COLUMN_NAME_EVENT));
            String grade = c.getString(c.getColumnIndexOrThrow(GradeContract.Grade.COLUMN_NAME_GRADE));
            String course = c.getString(c.getColumnIndexOrThrow(GradeContract.Grade.COLUMN_NAME_COURSE));

            result += event + ": " + grade + " (" + course + ")\n";
            more = c.moveToNext();
        }
        ;
        ((TextView) findViewById(R.id.success)).setText(result);
    }
}
