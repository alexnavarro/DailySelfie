package br.com.alexandrenavarro.dailyselfie;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.orm.query.Select;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import br.com.alexandrenavarro.dailyselfie.model.PicSelfie;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_TAKE_PHOTO = 1;

    String mCurrentPhotoPath;
    String mPhotoName;

    @Bind(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private List<PicSelfie> pics = new LinkedList<>();
    private Date mDate;
    private AlarmManager mAlarmManager;
    private PendingIntent mNotificationReceiverPendingIntent;
    private Intent mNotificationReceiverIntent;

    private static final long INITIAL_ALARM_DELAY = 2 * 60 * 1000L;

    public static final long INTERVAL_TWO_MINUTES = 2 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAdapter = new SelfieAdapter(this, pics);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        registerForContextMenu(mRecyclerView);

//        List<PicSelfie> sugarRecords = PicSelfie.listAll(PicSelfie.class);

        List<PicSelfie> sugarRecords = Select.from(PicSelfie.class)
                .orderBy("date desc")
                .list();

        if (sugarRecords != null || !sugarRecords.isEmpty()) {
            pics.addAll(sugarRecords);
        }

        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        mNotificationReceiverIntent = new Intent(this, AlarmNotificationReceiver.class);
        mNotificationReceiverPendingIntent = PendingIntent.
                getBroadcast(this, 0, mNotificationReceiverIntent, 0);

        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY,
                INTERVAL_TWO_MINUTES,
                mNotificationReceiverPendingIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusUtils.getBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusUtils.getBus().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_camera) {
            dispatchTakePictureIntent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            PicSelfie picSelfie = new PicSelfie(mCurrentPhotoPath, mPhotoName, mDate);
            picSelfie.save();
            ((LinkedList) pics).addFirst(picSelfie);
            mAdapter.notifyDataSetChanged();

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        mDate = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(mDate);
        mPhotoName = timeStamp;
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

}
