package br.com.alexandrenavarro.dailyselfie;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelfieDetailActivity extends AppCompatActivity {

    public static final String PIC_URL = "picURL";

    @Bind(R.id.iv_pic)
    protected ImageView ivPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_detail);
        ButterKnife.bind(this);
        Picasso.with(this).load(Uri.parse(getIntent().getStringExtra(PIC_URL))).fit().into(ivPic);
    }
}
