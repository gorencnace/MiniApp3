package si.uni_lj.fri.pbd.miniapp3.database.dao;

/*
 * RECIPE DATA ACCESS OBJECT
 *
 * An interface defining access methods (queries) on the database,.
 */

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;

@Dao
public interface RecipeDao {

    // Get recipe with specific id
    @Query("SELECT * FROM RecipeDetails WHERE idMeal = :idMeal")
    RecipeDetails getRecipeById(String idMeal);

    // Get all favorite recipes
    @Query("SELECT * FROM RecipeDetails WHERE isFavorite = 1")
    LiveData<List<RecipeDetails>> getFavoriteRecipes();

    // Insert recipe into database
    @Insert
    void insertRecipe(RecipeDetails recipeDetails);

    // Delete recipe from database
    @Query("DELETE FROM RecipeDetails WHERE idMeal = :idMeal")
    void deleteRecipe(String idMeal);

}
