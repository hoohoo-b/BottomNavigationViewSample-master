package bottomnav.thesevchefs.com.cooktasty.entity;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Jun Jie on 31/10/2017.
 */

public class Recipe {

    long id;
    String name;
    String description;
    long upload_by_user;
    int difficulty_level;
    Time time_required;
    Date upload_datetime;
    String image_url;
    RecipeIngredient[] ingredients;
    Boolean is_favourited;
    RecipeInstruction[] instructions;

}
