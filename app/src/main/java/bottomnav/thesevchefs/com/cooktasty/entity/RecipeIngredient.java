package bottomnav.thesevchefs.com.cooktasty.entity;

/**
 * Created by Jun Jie on 31/10/2017.
 */

public class RecipeIngredient {

    public IngredientDetail ingredient;
    public String serving_size;

    public class IngredientDetail {
        public long id;
        public String name;
        public String image_url;
    }

}
