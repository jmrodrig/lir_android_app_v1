package com.example.ze.lir_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by Ze on 12/04/2015.
 */
public class StoryListAdapter extends BaseAdapter {
    private ImageLoader.ImageCache imageCache;
    private ImageLoader loader;
    private ArrayList<StoryItem> stories = new ArrayList<StoryItem>();
    private Context appContext;
    private StoryListFragment parentObj;

    public StoryListAdapter(Context Appctxt, StoryListFragment parent ) {
        appContext = Appctxt;
        parentObj = parent;
        loader = RequestsSingleton.getInstance(appContext.getApplicationContext()).getImageLoader();
    }

    public void setStoryList(ArrayList<StoryItem> storyList) {
        stories = storyList;
    }

    public int getCount() {
        return stories.size();
    }

    public StoryItem getItem(int position) {
        return stories.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) appContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View row = convertView;
        if (row == null) {
            row = inflater.inflate(R.layout.story_row, null, false);
            ViewHolder holder = new ViewHolder();
            holder.userNameTextView = (TextView) row.findViewById(R.id.user_name);
            holder.storyTextTextView = (TextView) row.findViewById(R.id.story_text);
            holder.storyImageView = (NetworkImageView) row.findViewById(R.id.story_thumbnail);
            holder.userImageView = (NetworkImageView) row.findViewById(R.id.user_image);
            holder.deleteStoryButton = (Button) row.findViewById(R.id.delete_story_button);
            row.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) row.getTag();
        holder.userNameTextView.setText(getItem(position).getUserName());
        holder.storyTextTextView.setText(getItem(position).getStoryText());
        String urlImage = getItem(position).getUrlThumbnail();
        holder.storyImageView.setImageUrl(urlImage, loader);
        String userImg = getItem(position).getUrlUserThumbnail();
        holder.userImageView.setImageUrl(userImg, loader);
        if (userImg.equals("")) {
            holder.userImageView.setImageResource(R.drawable.placeholder_user);
        }
        holder.deleteStoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentObj.deleteStory(getItem(position).getStoryId());
            }
        });
        if (isUserOwnerOfStory(getItem(position)))
            holder.deleteStoryButton.setVisibility(View.VISIBLE);
        else
            holder.deleteStoryButton.setVisibility(View.GONE);


        return (row);
    }

    static class ViewHolder{
        TextView userNameTextView, storyTextTextView;
        NetworkImageView storyImageView, userImageView;
        Button deleteStoryButton;
    }

    private Boolean isUserOwnerOfStory(StoryItem st) {
        String userId = SessionUser.getInstance().getUserEmail();
        if (userId.equals(st.getAuthor().getAuthorId()))
            return true;
        else
            return false;
    }
}


