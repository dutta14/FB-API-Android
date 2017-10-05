package anindya.fb;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import anindya.fb.detailmodel.Posts;
import anindya.fb.detailmodel.PostsData;

public class PostsFragment extends Fragment {

    ListView list;
    TextView noPosts;
    Context mContext;
    String profileName, profilePic;

    public PostsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_posts, container, false);
        list = (ListView) v.findViewById(R.id.posts_list);
        noPosts = (TextView) v.findViewById(R.id.no_posts);
        mContext = getContext();
        return v;
    }

    void setData(Posts posts, String profileName, String profilePic) {
        if(posts == null) {
            noPosts.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        } else {
            noPosts.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            PostRowAdapter adapter = new PostRowAdapter(mContext, posts.getData());
            this.profileName = profileName;
            this.profilePic = profilePic;
            list.setAdapter(adapter);
        }

    }

    class PostRowAdapter extends ArrayAdapter<PostsData> {

        List<PostsData> data;

        public PostRowAdapter(Context context, List<PostsData> data) {
            super(context, 0, data);
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.posts_row, parent, false);

            ImageView picture = (ImageView) rowView.findViewById(R.id.profilepic);
            Picasso.with(mContext).load(profilePic).resize(50, 50).into(picture);

            TextView nameView = (TextView) rowView.findViewById(R.id.name),
                    timeView = (TextView) rowView.findViewById(R.id.time),
                    msgView = (TextView) rowView.findViewById(R.id.message);

            nameView.setText(profileName);

            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+0000'"),
                    output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date d = input.parse(data.get(position).getCreatedTime());
                String date = output.format(d);
                timeView.setText(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String message = data.get(position).getMessage();
            if(message == null)
                message = data.get(position).getStory();
            msgView.setText(message);

            return rowView;
        }
    }
}
