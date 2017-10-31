package bottomnav.thesevchefs.com.cooktasty.entity;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Jun Jie on 31/10/2017.
 */

public class Recipe {

    public long id;
    public String name;
    public String description;
    public long upload_by_user;
    public int difficulty_level;
    public Time time_required;
    public Date upload_datetime;
    public String image_url;
    public RecipeIngredient[] ingredients;
    public Boolean is_favourited;
    public RecipeInstruction[] instructions;

}
