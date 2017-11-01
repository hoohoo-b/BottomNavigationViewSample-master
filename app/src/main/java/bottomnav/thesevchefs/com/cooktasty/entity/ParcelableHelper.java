package bottomnav.thesevchefs.com.cooktasty.entity;

import android.os.Parcel;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Admin on 1/11/2017.
 */

public class ParcelableHelper {

    public static void writeBoolean(Parcel destination, Boolean value) {
        if (destination != null) {
            destination.writeInt(value ? 1 : 0);
        }
    }

    public static Boolean readBoolean(Parcel in) {
        if (in != null) {
            return (in.readInt() == 1);
        }
        return false;
    }

    public static void writeDate(Parcel destination, Date dateObject) {
        destination.writeLong(dateObject.getTime());
    }

    public static Date readDate(Parcel in) {
        return new Date(in.readLong());
    }

    public static void writeTime(Parcel destination, Time timeObject) {
        destination.writeLong(timeObject.getTime());
    }

    public static Time readTime(Parcel in) {
        return new Time(in.readLong());
    }

}
