package anindya.fb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import anindya.fb.datamodel.Datum;
import anindya.fb.datamodel.Result;
import anindya.fb.detailmodel.ItemDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TableFragment extends Fragment {

    String title, type;
    ListView list;
    Context mContext;
    RowAdapter adapter;
    Result mResult = null;
    Button prev, next;
    LinearLayout pagination;
    int from = 0, to = 10;
    List<Datum> result;

    public TableFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_table_data, container, false);
            list = (ListView) v.findViewById(R.id.list);
            pagination = (LinearLayout) v.findViewById(R.id.pagination);
            mContext = getContext();

            title = getArguments().getString("title");
            type = getArguments().getString("type");

            next = (Button) v.findViewById(R.id.next);
            prev = (Button) v.findViewById(R.id.prev);
            prev.setEnabled(false);
            next.setEnabled(false);

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(result != null) {
                        from = Math.min(from + 10, result.size());
                        to = Math.min(from + 10, result.size());
                        adapter = new RowAdapter(mContext, result.subList(from, to), title, type);
                        list.setAdapter(adapter);
                        next.setEnabled(to != result.size());
                        prev.setEnabled(true);
                    }
                }
            });

            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(result != null) {
                        from = Math.max(from - 10, 0);
                        to = Math.max(from + 10, 0);
                        adapter = new RowAdapter(mContext, result.subList(from, to), title, type);
                        list.setAdapter(adapter);
                        prev.setEnabled(from != 0);
                        next.setEnabled(true);
                    }
                }
            });

            if (type.equals("favorite"))
                setAdapter();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapter();
    }

    void setAdapter() {
        if(type.equals("favorite")) {
            adapter = new RowAdapter(mContext, getData(), title, type);
            list.setAdapter(adapter);
            pagination.setVisibility(View.GONE);
        } else if (mResult != null) {
            setResultAdapter();
        }
    }

    private void setResultAdapter() {
        result = getData();
        from = 0;
        to = Math.min(result.size(), 10);
        next.setEnabled(to != result.size());
        adapter = new RowAdapter(mContext, result.subList(from,to), title, type);
        list.setAdapter(adapter);
    }

    void setResult(Result r) {
        mResult = r;
        try {
           setResultAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Datum> getData() {
        Result result = SearchFragment.mResult;
        switch(type) {
            case "result":
                switch (title) {
                    case "Users": return result.getUser().getData();
                    case "Pages": return result.getPage().getData();
                    case "Events": return result.getEvent().getData();
                    case "Places": return result.getPlace().getData();
                    case "Groups": return result.getGroup().getData();
                }
                return null;
            case "favorite":
                return FavoritesList.getFavorites(title, mContext);
        }
        return null;
    }
}

class RowAdapter extends ArrayAdapter<Datum> {

    static final String BASE_URL = "http://lowcost-env.tigynm9tp6.us-west-2.elasticbeanstalk.com/";
    static final String DETAIL_COMPLETE = "details_complete";
    static final String DETAIL_MESSAGE = "details_message" ;

    private List<Datum> data;
    private Context mContext;
    private String title;
    private String type;

    RowAdapter(Context context, List<Datum> data, String title, String type) {
        super(context, 0, data);
        mContext = context;
        this.data = data;
        this.title = title;
        this.type = type;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.data_row, parent, false);

        final Datum curData = data.get(position);

        final String imageUrl = curData.getPicture().getData().getUrl();
        ImageView pic = (ImageView) rowView.findViewById(R.id.profilepic);
        Picasso.with(mContext).load(imageUrl).resize(100, 100).into(pic);

        TextView name = (TextView) rowView.findViewById(R.id.name);
        name.setText(curData.getName());

        FavoritesList.initList(mContext);
        final ImageView favorites = (ImageView) rowView.findViewById(R.id.fav);
        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FavoritesList.contains(curData)) {
                    favorites.setImageResource(R.drawable.favorites_off);
                    FavoritesList.remove(curData);
                    if(type.equals("favorite")) {
                        data.remove(curData);
                        notifyDataSetChanged();
                    }
                } else {
                    favorites.setImageResource(R.drawable.favorites_on);
                    FavoritesList.add(curData, title);
                }

            }
        });
        favorites.setImageResource(FavoritesList.contains(curData)? R.drawable.favorites_on : R.drawable.favorites_off);

        ImageView details = (ImageView) rowView.findViewById(R.id.details);
        details.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RetrofitInterface apiService = retrofit.create(RetrofitInterface.class);
                Call<ItemDetail> call = apiService.getDetails(curData.getId());

                call.enqueue(new Callback<ItemDetail>() {
                    @Override
                    public void onResponse(Call<ItemDetail> call, Response<ItemDetail> response) {
                        ItemDetail mResult = response.body();
                        Intent intent = new Intent(DETAIL_COMPLETE);
                        intent.putExtra(DETAIL_MESSAGE, mResult);
                        intent.putExtra("profile", curData);
                        intent.putExtra("title", title);
                        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(mContext);
                        manager.sendBroadcast(intent);
                    }

                    @Override
                    public void onFailure(Call<ItemDetail> call, Throwable t) {}
                });
                mContext.startActivity(new Intent(mContext, DetailsActivity.class));
            }
        });

        return rowView;
    }

    @Override
    public int getCount() {
        return Math.min(super.getCount(),10);
    }
}