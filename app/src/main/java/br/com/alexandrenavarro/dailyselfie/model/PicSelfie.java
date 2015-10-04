package br.com.alexandrenavarro.dailyselfie.model;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by alexandrenavarro on 10/1/15.
 */
public class PicSelfie extends SugarRecord<PicSelfie> {

    private String photoUri;
    private String photoName;
    private Date date;

    public PicSelfie(){}

    public PicSelfie(String photoUri, String photoName, Date date){
        this.photoUri = photoUri;
        this.photoName = photoName;
        this.date = date;
    }

    public String getPhotoName() {
        return photoName;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public Date getDate() {
        return date;
    }
}
