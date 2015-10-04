package br.com.alexandrenavarro.dailyselfie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.alexandrenavarro.dailyselfie.model.PicSelfie;

/**
 * Created by alexandrenavarro on 10/1/15.
 */
public class SelfieAdapter extends RecyclerView.Adapter<SelfieAdapter.ViewHolder> {

    private List<PicSelfie> pics;
    private Context context;

    public SelfieAdapter(Context context, List<PicSelfie> pics) {
        this.pics = pics;
        this.context = context;
    }

    @Override
    public SelfieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_selfie_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelfieAdapter.ViewHolder holder, int position) {
        final PicSelfie picSelfie = pics.get(position);
        Picasso.with(context).load(Uri.parse(picSelfie.getPhotoUri())).resize(300, 200).into(holder.mImageView);
        holder.mTextView.setText(picSelfie.getPhotoName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelfieDetailActivity.class);
                intent.putExtra(SelfieDetailActivity.PIC_URL, picSelfie.getPhotoUri());
                context.startActivity(intent);
            }
        });

        holder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] popupItems = context.getResources().getStringArray(R.array.popup_menu_delete);

                PopupMenuUtils.showStaticMenu(context, v, popupItems, v.getId(), new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == 0) {
                            pics.remove(picSelfie);
                            picSelfie.delete();
                            notifyDataSetChanged();
                        }

                        return true;
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return pics.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mImageView;
        public ImageButton mImageButton;


        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.tv_pic_name);
            mImageView = (ImageView) v.findViewById(R.id.imv_pic);
            mImageButton = (ImageButton) v.findViewById(R.id.btn_more_options);
        }
    }
}
