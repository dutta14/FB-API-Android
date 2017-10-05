package anindya.fb;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import anindya.fb.detailmodel.Albums;
import anindya.fb.detailmodel.AlbumsData;
import anindya.fb.detailmodel.PhotosData;

import static android.view.View.GONE;

public class AlbumsFragment extends Fragment {

    Albums albums;
    View mAlbumFragmentView;
    String token;

    ExpandableListAdapter adapter;
    ExpandableListView mAlbumList;
    List<String> mAlbumTitles;
    HashMap<String, List<String>> mAlbumPhotos;
    TextView noAlbums;
    int mLastPosition = Integer.MAX_VALUE;

    public AlbumsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAlbumFragmentView = inflater.inflate(R.layout.fragment_albums, container, false);
        mAlbumList = (ExpandableListView) mAlbumFragmentView.findViewById(R.id.album_list);
        noAlbums = (TextView) mAlbumFragmentView.findViewById(R.id.no_albums);
        token = getString(R.string.fb_access_token);
        return mAlbumFragmentView;
    }

    void setData(Albums albums) {
        this.albums = albums;
        if(albums == null) {
            mAlbumList.setVisibility(GONE);
            noAlbums.setVisibility(View.VISIBLE);
        } else {
            mAlbumList.setVisibility(View.VISIBLE);
            noAlbums.setVisibility(View.GONE);
            prepareListData();
            adapter = new ExpandableListAdapter(getContext(), mAlbumTitles, mAlbumPhotos);
            mAlbumList.setAdapter(adapter);

            mAlbumList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    if(mLastPosition != groupPosition) {
                        mAlbumList.collapseGroup(mLastPosition);
                        mLastPosition = groupPosition;
                    }
                }
            });
        }
    }

    private void prepareListData() {
        mAlbumTitles = new ArrayList<>();
        for(AlbumsData d: albums.getData()) {
            mAlbumTitles.add(d.getName());
        }

        mAlbumPhotos = new HashMap<>();
        for(AlbumsData d: albums.getData()) {
            String key = d.getName();
            ArrayList<String> urls = new ArrayList<>();
            if(d.getPhotos() != null) {
                for (PhotosData p : d.getPhotos().getData()) {
                    try {
                        String url = String.format(getString(R.string.pic_url), p.getId(), token);
                        urls.add(url);
                    } catch (Exception e) {}
                }
                mAlbumPhotos.put(key, urls);
            }
        }
    }

}
