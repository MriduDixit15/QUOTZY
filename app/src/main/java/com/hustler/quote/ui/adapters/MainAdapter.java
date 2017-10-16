package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.activities.QuoteDetailsActivity;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.pojo.QuotesFromFC;
import com.hustler.quote.ui.superclasses.App;

import java.util.List;
import java.util.Random;

/**
 * Created by Sayi on 07-10-2017.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    Activity activity;
    List<QuotesFromFC> dataFromNet;
    Random random;
    OnItemClicListener itemTouchHelper;

    public MainAdapter(Activity activity, List<QuotesFromFC> dataFromNet, OnItemClicListener listener) {
        this.activity = activity;
        this.dataFromNet = dataFromNet;
        itemTouchHelper = listener;
        random = new Random();


    }


    public MainAdapter(Activity activity) {
        this.activity = activity;
    }

    public MainAdapter() {

    }

    public interface OnItemClicListener {
        void onItemClickHappened(QuotesFromFC quotesFromFC,View view);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MainViewHolder(activity.getLayoutInflater().inflate(R.layout.rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, int position) {
        int color=getMatColor("100");
        holder.itemView.setBackgroundColor(color);
        final QuotesFromFC quote = dataFromNet.get(position);
        quote.setColor(color);
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.slideup));
        String genre;
        if (quote.getTags().size() <= 0) {
            genre = "anonymous";
        } else {
            genre = quote.getTags().get(0);
        }
        holder.tv.setText(quote.getBody());
        holder.tv2.setText(quote.getAuthor());
        holder.tv3.setText(genre);
        holder.tv.setTypeface(App.getZingCursive(activity, Constants.FONT_ZINGSANS));
        holder.tv2.setTypeface(App.getZingCursive(activity, Constants.FONT_ZINGCURSIVE));
        holder.tv3.setTypeface(App.getZingCursive(activity, Constants.FONT_NEVIS));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemTouchHelper != null) {
                    itemTouchHelper.onItemClickHappened(quote,v);
                }
            }
        });


    }


    public int getMatColor(String typeColor) {
        int returnColor = Color.BLACK;
        int arrayId = activity.getResources().getIdentifier("mdcolor_" + typeColor, "array", activity.getApplicationContext().getPackageName());

        if (arrayId != 0) {
            TypedArray colors = activity.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.BLACK);
            colors.recycle();
        }
        return returnColor;
    }

    @Override
    public int getItemCount() {
        if (dataFromNet.size() < 0) {
            return 0;
        } else {
            return dataFromNet.size();
        }
    }


    public void updateQuotes(List<QuotesFromFC> quotesFromFCList) {
        this.dataFromNet = quotesFromFCList;
        notifyDataSetChanged();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        TextView tv, tv2, tv3;
        LinearLayout rootView;

        public MainViewHolder(View itemView) {
            super(itemView);
            rootView=(LinearLayout) itemView.findViewById(R.id.root_ll);
            tv = (TextView) itemView.findViewById(R.id.main_quote);
            tv2 = (TextView) itemView.findViewById(R.id.quote_author);
            tv3 = (TextView) itemView.findViewById(R.id.quote_genre_end);

        }

    }
}