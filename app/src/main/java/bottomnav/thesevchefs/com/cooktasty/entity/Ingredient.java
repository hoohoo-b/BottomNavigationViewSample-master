package bottomnav.thesevchefs.com.cooktasty.entity;

/**
 * Created by Jun Jie on 31/10/2017.
 */

public class Ingredient {

    IngredientDetail ingredient;
    String serving_size;

    private class IngredientDetail {
        long id;
        String name;
        String image_url;
    }

}
