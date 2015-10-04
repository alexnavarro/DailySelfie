package br.com.alexandrenavarro.dailyselfie;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.View;

/**
 * Created by alexandrenavarro on 10/3/15.
 */
public class PopupMenuUtils {
    public static void showStaticMenu(Context context, View view, String[] popupItems, int groupId, PopupMenu.OnMenuItemClickListener listener) {
        PopupMenu popup = new PopupMenu(context, view);

        for (int i = 0; i < popupItems.length; i++) {
            String popupItem = popupItems[i];
            popup.getMenu().add(groupId, i, i, popupItem);
        }

        popup.setOnMenuItemClickListener(listener);
        popup.show();
    }
}
