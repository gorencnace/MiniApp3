package si.uni_lj.fri.pbd.miniapp3.ui;

/*
 * VIEW MODEL
 *
 * Connects repository with views.
 *
 */

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import si.uni_lj.fri.pbd.miniapp3.database.Repository;
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeDetailsIM;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeSummaryIM;

public class RecipeViewModel extends AndroidViewModel {

    // FEILDS
    private Repository repository;

    // CONSTRUCTOR
    public RecipeViewModel (Application application) {
        super(application);
        repository = new Repository(application);
    }

    // METHODS

    public MutableLiveData<List<RecipeSummaryIM>> getRecipeSummaries(boolean fromAPI, String ingredient) {
        return repository.getRecipeSummaries(fromAPI, ingredient);
    }

    public MutableLiveData<RecipeDetailsIM> getRecipeDetailsById(String id, boolean fromAPI) {
        return repository.getRecipeDetails(id, fromAPI);
    }

    public void insertRecipe(RecipeDetailsIM recipeDetailsIM) {
        repository.insertRecipe(recipeDetailsIM);
    }

    public void deleteRecipe(String idMeal) {
        repository.deleteRecipe(idMeal);
    }

    public LiveData<List<RecipeDetails>> getFavorites() {
        return repository.getFavorites();
    }

}
