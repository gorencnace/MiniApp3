package si.uni_lj.fri.pbd.miniapp3.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.internal.ListenerClass;
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
    private LiveData<List<RecipeDetails>> favorites;

    public Repository(Application application) {
        Database db = Database.getDatabase(application);
        recipeDao = db.recipeDao();
        mRestClient = ServiceGenerator.createService(RestAPI.class);
        favorites = recipeDao.getFavoriteRecipes();
    }

    // https://medium.com/@amtechnovation/android-architecture-component-mvvm-part-1-a2e7cff07a76

    // recipe summaries from api and database
    public MutableLiveData<List<RecipeSummaryIM>> getRecipeSummaries(boolean fromAPI, String ingredient) {
        MutableLiveData<List<RecipeSummaryIM>> recipeSummaries = new MutableLiveData<>();
        if (fromAPI) {
            mRestClient.getRecipesByIngredient(ingredient).enqueue(new Callback<RecipesByIngredientDTO>() {
                @Override
                public void onResponse(Call<RecipesByIngredientDTO> call, Response<RecipesByIngredientDTO> response) {
                    if (response.isSuccessful()) {
                        List<RecipeSummaryIM> tmp = new ArrayList<>();
                        if (response.body().getRecipesByIngredient() == null) {
                            recipeSummaries.setValue(null);
                        } else {
                            for (RecipeDetailsDTO rdDTO : response.body().getRecipesByIngredient()) {
                                tmp.add(Mapper.mapRecipeDetailsToRecipeSummaryIm(Mapper.mapRecipeDetailsDtoToRecipeDetails(false, rdDTO)));
                            }
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
            for (RecipeDetails rd : Objects.requireNonNull(favorites.getValue())) {
                tmp.add(Mapper.mapRecipeDetailsToRecipeSummaryIm(rd));
            }
            recipeSummaries.setValue(tmp);
        }
        return recipeSummaries;
    }

    public MutableLiveData<RecipeDetailsIM> getRecipeDetails(String recipeId, boolean fromAPI) {
        MutableLiveData<RecipeDetailsIM> returnRecipe = new MutableLiveData<>();
        if (!fromAPI) {
            returnRecipe.setValue(Mapper.mapRecipeDetailsToRecipeDetailsIm(recipeDao.getRecipeById(recipeId)));
        } else {
            RecipeDetails rd = recipeDao.getRecipeById(recipeId);
            mRestClient.getRecipesById(recipeId).enqueue(new Callback<RecipesByIdDTO>() {
                @Override
                public void onResponse(Call<RecipesByIdDTO> call, Response<RecipesByIdDTO> response) {
                    assert response.body() != null;
                    RecipeDetailsIM recipe = Mapper.mapRecipeDetailsDtoToRecipeDetailsIm(true, response.body().getRecipesById().get(0));
                    String check = checkForFavorites(recipe, rd);
                    switch (check) {
                        case "IN DB OK":
                            returnRecipe.setValue(Mapper.mapRecipeDetailsToRecipeDetailsIm(rd));
                            break;
                        case "IN DB NOT OK":
                            deleteRecipe(recipeId);
                            insertRecipe(recipe);
                            returnRecipe.setValue(Mapper.mapRecipeDetailsToRecipeDetailsIm(rd));
                            break;
                        default:
                            returnRecipe.setValue(Mapper.mapRecipeDetailsDtoToRecipeDetailsIm(false, response.body().getRecipesById().get(0)));
                            break;
                    }
                }

                @Override
                public void onFailure(Call<RecipesByIdDTO> call, Throwable t) {
                    returnRecipe.setValue(null);
                }
            });
        }
        return returnRecipe;
    }

    private String checkForFavorites(RecipeDetailsIM recipe, RecipeDetails rd) {
        if (rd != null) {
            if (recipe.equals(Mapper.mapRecipeDetailsToRecipeDetailsIm(rd))) {
                return "IN DB OK";
            } else {
                return "IN DB NOT OK";
            }
        }
        return "NOT IN DB";
    }

    public void insertRecipe(RecipeDetailsIM recipeDetailsIM) {
        RecipeDetails recipeDetails = Mapper.mapRecipeDetailsIMToRecipeDetails(recipeDetailsIM);
        recipeDao.insertRecipe(recipeDetails);
    }

    public void deleteRecipe(String idMeal) {
        recipeDao.deleteRecipe(idMeal);
    }

    public LiveData<List<RecipeDetails>> getFavorites() {
        return favorites;
    }
}
