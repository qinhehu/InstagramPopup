package com.example.qinhe.inspopup;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.POST;

/**
 * Created by QinHe on 2017/3/1.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<String> mSources = new ArrayList<>();
    private Context mContext;

    public ListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String url = mSources.get(position);
        loadImageView(url, R.drawable.img_default_meizi, holder.imageView);
        holder.imageView.setTag(position);
    }


    private void loadImageView(String url, int defresId, ImageView img) {
        if (null != img) {
            if (!TextUtils.isEmpty(url)) {
                DrawableTypeRequest request = Glide.with(mContext).load(url);
                request.crossFade();
                request.placeholder(defresId);
                request.diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img);
            } else {
                img.setImageResource(defresId);
            }
        }
    }


    @Override
    public int getItemCount() {
        return mSources.size();
    }

    public void setSource(List<String> sources) {
        mSources.clear();
        mSources.addAll(sources);
        notifyDataSetChanged();
    }

    private View.OnLongClickListener mLongClickListener;

    public void setInsPressListener(
            View.OnLongClickListener longClickListener
    ) {
        mLongClickListener = longClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imagview);
            if (mLongClickListener != null) {
                imageView.setOnLongClickListener(mLongClickListener);
            }
        }
    }
}
