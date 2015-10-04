package br.com.alexandrenavarro.dailyselfie;

import com.squareup.otto.Bus;

/**
 * Created by alexandrenavarro on 10/3/15.
 */
public  class BusUtils {
    public static final Bus getBus(){
        return new Bus();
    }
}
