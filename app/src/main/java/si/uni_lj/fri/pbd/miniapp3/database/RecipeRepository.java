package si.uni_lj.fri.pbd.miniapp3.database;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import si.uni_lj.fri.pbd.miniapp3.database.dao.RecipeDao;
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientsDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeDetailsDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIngredientDTO;
import si.uni_lj.fri.pbd.miniapp3.rest.RestAPI;
import si.uni_lj.fri.pbd.miniapp3.rest.ServiceGenerator;

public class RecipeRepository {
    private RecipeDao recipeDao;
    private RestAPI mRestClient;
    private MutableLiveData<List<RecipeDetailsDTO>> searchRecipes = new MutableLiveData<>();
    private LiveData<List<RecipeDetails>> favoriteRecipes;
    private List<IngredientDTO> allIngredientsDTO;

    public RecipeRepository(Application application) {
        Database db;
        db = Database.getDatabase(application);
        recipeDao = db.recipeDao();
        favoriteRecipes = recipeDao.getFavoriteRecipes();
        // https://stackoverflow.com/questions/35666960/service-generator-retrofit
        mRestClient = ServiceGenerator.createService(RestAPI.class);
        getAllIngredients();
    }

    public void getAllIngredients() {
        mRestClient.getAllIngredients().enqueue(new Callback<IngredientsDTO>() {
            @Override
            public void onResponse(Call<IngredientsDTO> call, Response<IngredientsDTO> response) {
                if (response.isSuccessful()) {
                    allIngredientsDTO = response.body().getIngredients();
                }
            }

            @Override
            public void onFailure(Call<IngredientsDTO> call, Throwable t) {
                // TODO kaj se zgodi, ko ne dobimo ingredientov???
            }
        });
    }

    public void findRecipesByIngredient(String ingredient) {
        mRestClient.getRecipesByIngredient(ingredient).enqueue(new Callback<RecipesByIngredientDTO>() {
            @Override
            public void onResponse(Call<RecipesByIngredientDTO> call, Response<RecipesByIngredientDTO> response) {
                if (response.isSuccessful()) {
                    searchRecipes.postValue(response.body().getRecipesByIngredient());
                }
            }

            @Override
            public void onFailure(Call<RecipesByIngredientDTO> call, Throwable t) {
                // TODO kaj se zgodi, ko ne dobimo rezultata???
            }
        });
    }

    public MutableLiveData<List<RecipeDetailsDTO>> getSearchRecipes() {
        return searchRecipes;
    }

    public List<IngredientDTO> getAllIngredientsDTO() {
        return allIngredientsDTO;
    }
}
