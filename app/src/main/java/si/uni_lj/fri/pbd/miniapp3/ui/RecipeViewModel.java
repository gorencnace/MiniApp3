package si.uni_lj.fri.pbd.miniapp3.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import si.uni_lj.fri.pbd.miniapp3.database.Repository;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeSummaryIM;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientDTO;

public class RecipeViewModel extends AndroidViewModel {
    private MutableLiveData<List<IngredientDTO>> allIngredients;

    private Repository repository;

    public RecipeViewModel (Application application) {
        super(application);
        repository = new Repository(application);
        allIngredients = repository.getAllIngredients();
    }

    public MutableLiveData<List<IngredientDTO>> getAllIngredients() {
        return allIngredients;
    }

    public MutableLiveData<List<RecipeSummaryIM>> getRecipeSummaries(boolean fromAPI, String ingredient) {
        return repository.getRecipeSummaries(fromAPI, ingredient);
    }
}
