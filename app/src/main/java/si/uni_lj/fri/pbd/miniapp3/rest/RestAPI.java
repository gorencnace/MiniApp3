package si.uni_lj.fri.pbd.miniapp3.rest;

import androidx.lifecycle.LiveData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientsDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIdDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIngredientDTO;

public interface RestAPI {

    @GET("list.php?i=list")
    Call<IngredientsDTO> getAllIngredients();

    // TODO: Add missing endpoints
    @GET("lookup.php")
    Call<RecipesByIdDTO> getRecipesById(@Query("i") String recipeId);

    @GET("filter.php")
    Call<RecipesByIngredientDTO> getRecipesByIngredient(@Query("i") String ingredient);

}