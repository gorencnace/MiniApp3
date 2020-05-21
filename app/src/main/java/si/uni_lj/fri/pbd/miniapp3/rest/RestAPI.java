package si.uni_lj.fri.pbd.miniapp3.rest;

/*
 * REST API
 *
 * Defines API endpoints.
 */

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientsDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIdDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIngredientDTO;

public interface RestAPI {

    @GET("list.php?i=list")
    Call<IngredientsDTO> getAllIngredients();

    @GET("lookup.php")
    Call<RecipesByIdDTO> getRecipesById(@Query("i") String recipeId);

    @GET("filter.php")
    Call<RecipesByIngredientDTO> getRecipesByIngredient(@Query("i") String ingredient);

}