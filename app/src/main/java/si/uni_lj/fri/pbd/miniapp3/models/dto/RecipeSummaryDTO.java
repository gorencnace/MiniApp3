package si.uni_lj.fri.pbd.miniapp3.models.dto;

/*
 * RECIPE SUMMARY DATA TRANSFER OBJECT
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeSummaryDTO {
    @SerializedName("strMeal")
    @Expose
    private String strMeal;
    @SerializedName("strMealThumb")
    @Expose
    private String strMealThumb;
    @SerializedName("idMeal")
    @Expose
    private String idMeal;

    public String getStrMeal() {
        return strMeal;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public String getIdMeal() {
        return idMeal;
    }
}
