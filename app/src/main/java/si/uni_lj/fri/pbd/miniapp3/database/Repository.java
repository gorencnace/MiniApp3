package si.uni_lj.fri.pbd.miniapp3.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import si.uni_lj.fri.pbd.miniapp3.database.dao.RecipeDao;
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;
import si.uni_lj.fri.pbd.miniapp3.models.Mapper;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeDetailsIM;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeSummaryIM;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientsDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeDetailsDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIdDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIngredientDTO;
import si.uni_lj.fri.pbd.miniapp3.rest.RestAPI;
import si.uni_lj.fri.pbd.miniapp3.rest.ServiceGenerator;
import timber.log.Timber;

public class Repository {
    private RestAPI mRestClient;
    private RecipeDao recipeDao;

    public Repository(Application application) {
        Database db = Database.getDatabase(application);
        recipeDao = db.recipeDao();
        mRestClient = ServiceGenerator.createService(RestAPI.class);
    }

    // https://medium.com/@amtechnovation/android-architecture-component-mvvm-part-1-a2e7cff07a76
    public MutableLiveData<List<IngredientDTO>> getAllIngredients() {
        MutableLiveData<List<IngredientDTO>> allIngredients = new MutableLiveData<>();
        mRestClient.getAllIngredients().enqueue(new Callback<IngredientsDTO>() {
            @Override
            public void onResponse(Call<IngredientsDTO> call, Response<IngredientsDTO> response) {
                if (response.isSuccessful()) {
                    allIngredients.setValue(response.body().getIngredients());
                }
            }

            @Override
            public void onFailure(Call<IngredientsDTO> call, Throwable t) {
                allIngredients.setValue(null);
                Timber.d("failed to connect");
            }
        });
        return allIngredients;
    }

    // recipe summaries from api and database
    public MutableLiveData<List<RecipeSummaryIM>> getRecipeSummaries(boolean fromAPI, String ingredient) {
        MutableLiveData<List<RecipeSummaryIM>> recipeSummaries = new MutableLiveData<>();
        if (fromAPI) {
            mRestClient.getRecipesByIngredient(ingredient).enqueue(new Callback<RecipesByIngredientDTO>() {
                @Override
                public void onResponse(Call<RecipesByIngredientDTO> call, Response<RecipesByIngredientDTO> response) {
                    if (response.isSuccessful()) {
                        List<RecipeSummaryIM> tmp = new ArrayList<>();
                        assert response.body() != null;
                        for (RecipeDetailsDTO rdDTO : response.body().getRecipesByIngredient()) {
                            tmp.add(Mapper.mapRecipeDetailsToRecipeSummaryIm(Mapper.mapRecipeDetailsDtoToRecipeDetails(false, rdDTO)));
                        }
                        recipeSummaries.setValue(tmp);
                    } else {
                        recipeSummaries.setValue(null);
                    }
                }

                @Override
                public void onFailure(Call<RecipesByIngredientDTO> call, Throwable t) {
                    recipeSummaries.setValue(null);
                }
            });
        } else {
            List<RecipeSummaryIM> tmp = new ArrayList<>();
            for (RecipeDetails rd : Objects.requireNonNull(recipeDao.getFavoriteRecipes().getValue())) {
                tmp.add(Mapper.mapRecipeDetailsToRecipeSummaryIm(rd));
            }
            recipeSummaries.setValue(tmp);
        }
        return recipeSummaries;
    }

    public MutableLiveData<RecipeDetailsIM> getRecipeDetails(String recipeId) {
        MutableLiveData<RecipeDetailsIM> returnRecipe = new MutableLiveData<>();
        RecipeDetails rd = recipeDao.getRecipeById(recipeId);
        if (rd != null) {
            returnRecipe.setValue(Mapper.mapRecipeDetailsToRecipeDetailsIm(rd.getFavorite(), rd));
        } else {
            mRestClient.getRecipesById(recipeId).enqueue(new Callback<RecipesByIdDTO>() {
                @Override
                public void onResponse(Call<RecipesByIdDTO> call, Response<RecipesByIdDTO> response) {
                    assert response.body() != null;
                    returnRecipe.setValue(Mapper.mapRecipeDetailsDtoToRecipeDetailsIm(false, response.body().getRecipesById().get(0)));
                }

                @Override
                public void onFailure(Call<RecipesByIdDTO> call, Throwable t) {
                    returnRecipe.setValue(null);
                }
            });
        }
        return returnRecipe;
    }
}
