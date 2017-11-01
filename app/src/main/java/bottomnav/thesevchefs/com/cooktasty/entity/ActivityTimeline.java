package bottomnav.thesevchefs.com.cooktasty.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Admin on 1/11/2017.
 */

public class ActivityTimeline implements Parcelable {

    public long user;
    public long target_user;
    public String main_object_image_url;
    public String target_object_image_url;
    public String formatted_summary_text;
    public Date datetime;

    public ActivityTimeline(long user, long target_user, String main_object_image_url, String target_object_image_url, String formatted_summary_text, Date datetime) {
        this.user = user;
        this.target_user = target_user;
        this.main_object_image_url = main_object_image_url;
        this.target_object_image_url = target_object_image_url;
        this.formatted_summary_text = formatted_summary_text;
        this.datetime = datetime;
    }


    protected ActivityTimeline(Parcel in) {
        user = in.readLong();
        target_user = in.readLong();
        main_object_image_url = in.readString();
        target_object_image_url = in.readString();
        formatted_summary_text = in.readString();
        ParcelableHelper.readDate(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(user);
        dest.writeLong(target_user);
        dest.writeString(main_object_image_url);
        dest.writeString(target_object_image_url);
        dest.writeString(formatted_summary_text);
        ParcelableHelper.writeDate(dest, datetime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ActivityTimeline> CREATOR = new Creator<ActivityTimeline>() {
        @Override
        public ActivityTimeline createFromParcel(Parcel in) {
            return new ActivityTimeline(in);
        }

        @Override
        public ActivityTimeline[] newArray(int size) {
            return new ActivityTimeline[size];
        }
    };
}
