package anindya.fb;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by anind on 4/14/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> headerTitles;
    private HashMap<String, List<String>> listDataChild;

    public ExpandableListAdapter(Context mContext, List<String> headerTitles, HashMap<String, List<String>> listDataChild) {
        this.mContext = mContext;
        this.headerTitles = headerTitles;
        this.listDataChild = listDataChild;
    }

    @Override
    public int getGroupCount() {
        return headerTitles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String position = headerTitles.get(groupPosition);
        if(listDataChild.get(position) == null)
            return 0;
        return listDataChild.get(position).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headerTitles.get(groupPosition);
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return listDataChild.get(headerTitles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition    ;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = getGroup(groupPosition).toString();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.photo_header, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.photo_head);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.photo, null);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.photoItem);
        Picasso.with(mContext).load(childText).into(image);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);

    }
}
