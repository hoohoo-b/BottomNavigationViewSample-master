package bottomnav.thesevchefs.com.cooktasty.utilities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import bottomnav.thesevchefs.com.cooktasty.R;
import bottomnav.thesevchefs.com.cooktasty.entity.ActivityTimeline;

/**
 * Created by Admin on 2/11/2017.
 */

public class TimelineListAdapter extends ArrayAdapter<ActivityTimeline> {

    private List<ActivityTimeline> timelines;
//    private ListItemClickListener mOnClickListener;

    public TimelineListAdapter(@NonNull Context context) {
        super(context, 0);
        this.timelines = new ArrayList<>();
//        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ActivityTimeline timeline = timelines.get(position);
        Context ctxt = getContext();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.timeline_row, parent, false);
        }

        ImageView ivMainObjectImage = (ImageView) convertView.findViewById(R.id.main_object_image);
        ImageView ivTargetObjectImage = (ImageView) convertView.findViewById(R.id.target_object_image);
        TextView tvTimelineText = (TextView) convertView.findViewById(R.id.timeline_text);
        TextView tvTimelineDatetime = (TextView) convertView.findViewById(R.id.timeline_datetime);

        SimpleDateFormat dt = new SimpleDateFormat("MMMMM dd 'at' hh:mm aaa");

        tvTimelineText.setText(timeline.formatted_summary_text);
        tvTimelineDatetime.setText(dt.format(timeline.datetime));

        if (timeline.main_object_image_url == null){
            ivMainObjectImage.setImageDrawable(ctxt.getResources()
                                                    .getDrawable(R.drawable.ic_userprofile_placeholder));
        } else {
            Picasso.with(ctxt)
                    .load(timeline.main_object_image_url)
                    .fit()
                    .into(ivMainObjectImage);
        }
        if (timeline.target_object_image_url == null){
            ivMainObjectImage.setImageDrawable(ctxt.getResources()
                                                    .getDrawable(R.drawable.ic_userprofile_placeholder));
        } else {
            Picasso.with(ctxt)
                    .load(timeline.target_object_image_url)
                    .fit()
                    .into(ivTargetObjectImage);
        }

        return convertView;
    }

    public void addActivityTimelineListData(List<ActivityTimeline> timelines) {
        this.timelines.addAll(timelines);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (timelines == null) return 0;
        return timelines.size();
    }

//    public interface ListItemClickListener {
//        void onListItemClick(ActivityTimeline timeline);
//    }

}
