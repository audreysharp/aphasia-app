package com.example.audrey.gridview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ContextMenu;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static int RESULT_LOAD_IMAGE = 1;
    private GridView gridview;
    private CustomGrid.Holder gridSquare;
    private TextToSpeech engine;

    public static int[] mThumbIds = {
            R.drawable.sample_1, R.drawable.sample_2,
            R.drawable.sample_3, R.drawable.sample_4,
            R.drawable.sample_5, R.drawable.sample_6,
            R.drawable.sample_7, R.drawable.sample_1
    };

    public static String[] captions = {
            "Dog 1", "Dog 2",
            "Dog 3", "Dog 4",
            "Dog 5", "Dog 6",
            "Dog 7", "Dog 8",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new CustomGrid(MainActivity.this, captions, mThumbIds));

        engine = new TextToSpeech(this, this);

        registerForContextMenu(gridview);
        gridview.setLongClickable(false);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                gridSquare = ((CustomGrid.Holder) v.getTag());
                openContextMenu(v);
            }
        });
    }

    @Override
    public void onInit(int status) {
        /*Log.d("Speech", "OnInit - Status ["+status+"]");

        if (status == TextToSpeech.SUCCESS) {
            Log.d("Speech", "Success!");
            engine.setLanguage(Locale.UK);
        }*/
    }

    /*
        Sets grid square to selected image
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData(); // get image from data
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst(); // move to first row

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgString = cursor.getString(columnIndex);
                cursor.close();

                ImageView newImage = gridSquare.os_img;
                newImage.setImageBitmap(BitmapFactory.decodeFile(imgString));
            } else {
                Toast.makeText(this, "You haven't picked an image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /*
    Creates context menu
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Actions");
        menu.add(0, v.getId(), 0, "Speak Image");
        menu.add(0, v.getId(), 0, "Change Image");
        menu.add(0, v.getId(), 0, "Edit Image Text");
        menu.add(0, v.getId(), 0, "Exit");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Speak Image") {
            function1(item.getItemId());
        } else if (item.getTitle() == "Change Image") {
            function2(item.getItemId());
        } else if (item.getTitle() == "Edit Image Text") {
            function3(item.getItemId());
        } else {
            return false;
        }
        return true;
    }

    public void function1(int id) {
        TextView textView = gridSquare.os_text;
        String speak = textView.getText().toString();
        engine.setLanguage(Locale.US);
        engine.speak(speak, TextToSpeech.QUEUE_ADD, null);
    }

    public void function2(int id) {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public void function3(int id) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Edit Image Text");
        alert.setMessage("Message");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                TextView textView = gridSquare.os_text;
                textView.setText(input.getText().toString());
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled
            }
        });
        alert.show();
    }
}
