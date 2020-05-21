package si.uni_lj.fri.pbd.miniapp3.models.dto;

/*
 * RECIPES BY INGREDIENT DATA TRANSFER OBJECT
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RecipesByIngredientDTO {
    @SerializedName("meals")
    @Expose
    private List<RecipeDetailsDTO> recipes;

    public List<RecipeDetailsDTO> getRecipesByIngredient() {
        return recipes;
    }
}
