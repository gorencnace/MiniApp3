package si.uni_lj.fri.pbd.miniapp3.database.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM RecipeDetails WHERE idMeal = :idMeal")
    RecipeDetails getRecipeById(String idMeal);

    // TODO: Add the missing methods
    @Query("SELECT * FROM RecipeDetails WHERE isFavorite = 1")
    LiveData<List<RecipeDetails>> getFavoriteRecipes();

    @Insert
    void insertRecipe(RecipeDetails recipeDetails);

    @Query("DELETE FROM RecipeDetails WHERE idMeal = :idMeal")
    void deleteRecipe(String idMeal);

    @Query("SELECT * FROM RecipeDetails WHERE isFavorite = 1 AND idMeal = :idMeal")
    RecipeDetails getFavoriteRecipeById(String idMeal);

}
