package si.uni_lj.fri.pbd.miniapp3.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientsDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIdDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIngredientDTO;

public interface RestAPI {

    @GET("list.php?i=list")
    Call<IngredientsDTO> getAllIngredients();

    // TODO: Add missing endpoints
    @GET("lookup.php?i={recipeId}")
    Call<RecipesByIdDTO> getRecipesById(@Path("recipeId") String recipeId);

    @GET("filter.php?i={ingredient}")
    Call<RecipesByIngredientDTO> getRecipesByIngredient(@Path("ingredient") String ingredient);

}