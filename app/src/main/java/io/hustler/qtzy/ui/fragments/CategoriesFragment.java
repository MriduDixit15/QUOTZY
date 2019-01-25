package io.hustler.qtzy.ui.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.activities.QuoteDetailsActivity;
import io.hustler.qtzy.ui.adapters.CategoriesAdapter;
import io.hustler.qtzy.ui.adapters.LocalAdapter;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
import io.hustler.qtzy.ui.database.QuotesDbHelper;
import io.hustler.qtzy.ui.pojo.Quote;
import io.hustler.qtzy.ui.utils.AdUtils;
import io.hustler.qtzy.ui.utils.IntentConstants;
import io.hustler.qtzy.ui.utils.TextUtils;
import io.hustler.qtzy.ui.utils.Toast_Snack_Dialog_Utils;

import java.util.ArrayList;

/**
 * Created by Sayi Manoj Sugavasi on 20/12/2017.
 */

/*   Copyright [2018] [Sayi Manoj Sugavasi]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/
public class CategoriesFragment extends android.support.v4.app.Fragment {
    RecyclerView categories_rv;
    CategoriesAdapter categoriesAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quote_categories_layout, container, false);
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        categories_rv = view.findViewById(R.id.rv_categories);
        categories_rv.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        categories_rv.setAdapter(new CategoriesAdapter(getActivity(), new CategoriesAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClicked(String category, String cat2, int position, GradientDrawable gradientDrawable) {
//                Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(),category+" "+position);
                bringupQuotes(category, cat2, gradientDrawable);
            }
        }));
    }

    private void bringupQuotes(String category, String cat2, GradientDrawable gradientDrawable) {
        final Dialog dialog = new Dialog(getContext(), R.style.EditTextDialog_non_floater);
        dialog.setContentView(R.layout.dialog_category_layout);
//        dialog.getWindow().setBA(gradientDrawable);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog_non_floater;


        TextView catgory_name = null;
        AdView adView;
        FloatingActionButton close_button;
        ArrayList<Quote> quoteslist = new ArrayList<>();
        dialog.show();

        quoteslist = (ArrayList<Quote>) new QuotesDbHelper(getActivity().getApplicationContext()).getQuotesByCategory(category);
        if (quoteslist.size() <= 0) {
            dialog.cancel();
            Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.no_quotes_available));
        } else {
            catgory_name = dialog.findViewById(R.id.tv_category_name);
            ArrayList<Quote> quotes = (ArrayList<Quote>) new QuotesDbHelper(getActivity().getApplicationContext()).getAllQuotes();
            ArrayList<Quotes> quotesForJson = new ArrayList<>();
            Data data = new Data();
            for (Quote qs : quotes) {
                Quotes quotes1 = new Quotes(qs.getQuote_body(), qs.getQuote_author(), null, qs.getQuote_category());
                quotesForJson.add(quotes1);
            }
            data.setData(quotesForJson);
            String datas = new Gson().toJson(data, Data.class);
            Log.e("JSON VALUES", datas);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dialog.getWindow().setStatusBarColor(Color.WHITE);
                    catgory_name.setBackgroundColor(Color.WHITE);
                    catgory_name.setBackgroundColor(gradientDrawable.getColors()[1]);


                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dialog.getWindow().setStatusBarColor(Color.WHITE);
                    catgory_name.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));

                }

//                } else {
//                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        dialog.getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
//                    }
//                }
            }

            categories_rv = dialog.findViewById(R.id.rv_category_list);
            close_button = dialog.findViewById(R.id.bt_close);
            adView = dialog.findViewById(R.id.adView);
            AdUtils.loadBannerAd(adView, getActivity());
            TextUtils.setFont(getActivity(), catgory_name, Constants.FONT_CIRCULAR);
            if (cat2 == " ") {
                catgory_name.setText(String.format("%s", category));

            } else {
                catgory_name.setText(String.format("%s & %s", cat2, category));

            }
            categories_rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            categories_rv.setAdapter(new LocalAdapter(getActivity(), quoteslist, new LocalAdapter.OnQuoteClickListener() {
                @Override
                public void onQuoteClicked(int position, GradientDrawable color, Quote quote, View view) {
                    Intent intent = new Intent(getActivity(), QuoteDetailsActivity.class);
                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), new Pair<>(view, getString(R.string.root_quote))).toBundle();
                    intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quote);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.putExtra(IntentConstants.GRADIENT_COLOR1, color.getColors());

                    } else {

                    }
                    startActivity(intent, bundle);
                }
            }));
            close_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categories_rv.setAdapter(null);
                    dialog.dismiss();
                }
            });

        }

        dialog.setCancelable(false);

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener()

        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
                    dialog.dismiss();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public static class Quotes {
        private String quote, author, country, category;

        public static Quote returnInstance(String quote, String author, String country, String category) {
            return new Quote(quote, author, country, category);
        }

        public Quotes(String quote, String author, String country, String category) {
            this.quote = quote;
            this.author = author;
            this.country = country;
            this.category = category;
        }

        public String getQuote() {
            return quote;
        }

        public void setQuote(String quote) {
            this.quote = quote;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }

    public static class Data {
        ArrayList<Quotes> data = new ArrayList<>();

        public ArrayList<Quotes> getData() {
            return data;
        }

        public void setData(ArrayList<Quotes> data) {
            this.data = data;
        }
    }
}



