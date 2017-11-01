package bottomnav.thesevchefs.com.cooktasty.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Jun Jie on 31/10/2017.
 */

public class Recipe implements Parcelable {

    public long id;
    public String name;
    public String description;
    public long upload_by_user;
    public int difficulty_level;
    public Time time_required;
    public Date upload_datetime;
    public String image_url;
    public Boolean is_favourited;
    public RecipeIngredient[] ingredients;
    public RecipeInstruction[] instructions;

    public Recipe(long id, String name, String description, long upload_by_user, int difficulty_level, Time time_required, Date upload_datetime, String image_url, Boolean is_favourited, RecipeIngredient[] ingredients, RecipeInstruction[] instructions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.upload_by_user = upload_by_user;
        this.difficulty_level = difficulty_level;
        this.time_required = time_required;
        this.upload_datetime = upload_datetime;
        this.image_url = image_url;
        this.is_favourited = is_favourited;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    protected Recipe(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        upload_by_user = in.readLong();
        difficulty_level = in.readInt();
        time_required = ParcelableHelper.readTime(in);
        upload_datetime = ParcelableHelper.readDate(in);
        image_url = in.readString();
        is_favourited = ParcelableHelper.readBoolean(in);
        ingredients = in.createTypedArray(RecipeIngredient.CREATOR);
        instructions = in.createTypedArray(RecipeInstruction.CREATOR);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeLong(upload_by_user);
        dest.writeInt(difficulty_level);
        ParcelableHelper.writeTime(dest, time_required);
        ParcelableHelper.writeDate(dest, upload_datetime);
        dest.writeString(image_url);
        ParcelableHelper.writeBoolean(dest, is_favourited);
        dest.writeTypedArray(ingredients, 0);
        dest.writeTypedArray(instructions, 0);
    }

}
