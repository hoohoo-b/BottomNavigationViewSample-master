package bottomnav.thesevchefs.com.cooktasty.entity;

import android.icu.util.Calendar;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import org.jcodec.common.DictionaryCompressor;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jun Jie on 31/10/2017.
 */

public class RecipeInstruction implements Parcelable {
    public int step_num;
    public String instruction;
    public Time time_required;
    public String image_url;

    public RecipeInstruction(int step_num, String instruction, Time time_required, String image_url) {
        this.step_num = step_num;
        this.instruction = instruction;
        this.time_required = time_required;
        this.image_url = image_url;
    }

    protected RecipeInstruction(Parcel in) {

        step_num = in.readInt();
        instruction = in.readString();
        time_required = ParcelableHelper.readTime(in);
        image_url = in.readString();
    }

    public static final Creator<RecipeInstruction> CREATOR = new Creator<RecipeInstruction>() {
        @Override
        public RecipeInstruction createFromParcel(Parcel in) {
            return new RecipeInstruction(in);
        }

        @Override
        public RecipeInstruction[] newArray(int size) {
            return new RecipeInstruction[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(step_num);
        dest.writeString(instruction);
        ParcelableHelper.writeTime(dest, time_required);
        dest.writeString(image_url);
    }

    public void setTime_required(String hour, String min) throws ParseException {
        long hh = TimeUnit.HOURS.toMillis(Integer.valueOf(hour));
        long mm = TimeUnit.MINUTES.toMillis(Integer.valueOf(min));
        time_required.setTime(hh + mm);
    }
}
