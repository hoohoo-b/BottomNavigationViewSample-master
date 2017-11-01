package bottomnav.thesevchefs.com.cooktasty.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jun Jie on 31/10/2017.
 */

public class RecipeIngredient implements Parcelable{

    public IngredientDetail ingredient;
    public String serving_size;

    public RecipeIngredient(long ingredientId, String ingredientName, String ingredientImageUrl, String serving_size) {
        IngredientDetail ingredientDetail = new IngredientDetail(ingredientId, ingredientName, ingredientImageUrl);
        this.ingredient = ingredientDetail;
        this.serving_size = serving_size;
    }

    protected RecipeIngredient(Parcel in) {
        ingredient = (IngredientDetail) in.readValue(IngredientDetail.class.getClassLoader());
        serving_size = in.readString();
    }

    public static final Creator<RecipeIngredient> CREATOR = new Creator<RecipeIngredient>() {
        @Override
        public RecipeIngredient createFromParcel(Parcel in) {
            return new RecipeIngredient(in);
        }

        @Override
        public RecipeIngredient[] newArray(int size) {
            return new RecipeIngredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(ingredient);
        dest.writeString(serving_size);
    }

    public static class IngredientDetail implements Parcelable {
        public long id;
        public String name;
        public String image_url;

        public IngredientDetail(long id, String name, String image_url) {
            this.id = id;
            this.name = name;
            this.image_url = image_url;
        }

        protected IngredientDetail(Parcel in) {
            id = in.readLong();
            name = in.readString();
            image_url = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(id);
            dest.writeString(name);
            dest.writeString(image_url);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public final static Creator<IngredientDetail> CREATOR = new Creator<IngredientDetail>() {
            @Override
            public IngredientDetail createFromParcel(Parcel in) {
                return new IngredientDetail(in);
            }

            @Override
            public IngredientDetail[] newArray(int size) {
                return new IngredientDetail[size];
            }
        };
    }

}
