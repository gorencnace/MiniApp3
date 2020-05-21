package si.uni_lj.fri.pbd.miniapp3.database;

/*
 * REPOSITORY
 *
 * Repository connects local database and remote data source with ModelView
 *
 * With a help of https://medium.com/@amtechnovation/android-architecture-component-mvvm-part-1-a2e7cff07a76
 *
 */

import android.app.Application;
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
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeDetailsDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIdDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIngredientDTO;
import si.uni_lj.fri.pbd.miniapp3.rest.RestAPI;
import si.uni_lj.fri.pbd.miniapp3.rest.ServiceGenerator;

public class Repository {

    // FIELDS
    private RestAPI mRestClient;
    private RecipeDao recipeDao;
    private LiveData<List<RecipeDetails>> favorites;

    // CONSTRUCTOR
    public Repository(Application application) {
        Database db = Database.getDatabase(application);
        recipeDao = db.recipeDao();
        mRestClient = ServiceGenerator.createService(RestAPI.class);
        favorites = recipeDao.getFavoriteRecipes();
    }

    // This method gets recipe summaries ether from database or rest client
    public MutableLiveData<List<RecipeSummaryIM>> getRecipeSummaries(boolean fromAPI, String ingredient) {
        MutableLiveData<List<RecipeSummaryIM>> recipeSummaries = new MutableLiveData<>();
        if (fromAPI) {
            // fetching from rest client
            mRestClient.getRecipesByIngredient(ingredient).enqueue(new Callback<RecipesByIngredientDTO>() {
                @Override
                public void onResponse(Call<RecipesByIngredientDTO> call, Response<RecipesByIngredientDTO> response) {
                    if (response.isSuccessful()) {
                        List<RecipeSummaryIM> tmp = new ArrayList<>();
                        if (response.body().getRecipesByIngredient() == null) {
                            // if there are no recipes with specific ingredient we return null object
                            recipeSummaries.setValue(null);
                        } else {
                            // else we return list of recipes
                            for (RecipeDetailsDTO rdDTO : response.body().getRecipesByIngredient()) {
                                tmp.add(Mapper.mapRecipeDetailsToRecipeSummaryIm(Mapper.mapRecipeDetailsDtoToRecipeDetails(false, rdDTO)));
                            }
                        }
                        recipeSummaries.setValue(tmp);
                    } else {
                        // if response was unsuccessful we also return null object
                        recipeSummaries.setValue(null);
                    }
                }

                @Override
                public void onFailure(Call<RecipesByIngredientDTO> call, Throwable t) {
                    // also return null object
                    recipeSummaries.setValue(null);
                }
            });
        } else {
            // fetching from database
            List<RecipeSummaryIM> tmp = new ArrayList<>();
            for (RecipeDetails rd : Objects.requireNonNull(favorites.getValue())) {
                tmp.add(Mapper.mapRecipeDetailsToRecipeSummaryIm(rd));
            }
            recipeSummaries.setValue(tmp);
        }
        return recipeSummaries;
    }

    // This method gets recipe details ether from database or rest client
    public MutableLiveData<RecipeDetailsIM> getRecipeDetails(String recipeId, boolean fromAPI) {
        MutableLiveData<RecipeDetailsIM> returnRecipe = new MutableLiveData<>();
        if (!fromAPI) {
            // fetching from database
            returnRecipe.setValue(Mapper.mapRecipeDetailsToRecipeDetailsIm(recipeDao.getRecipeById(recipeId)));
        } else {
            // fetching from rest client
            RecipeDetails rd = recipeDao.getRecipeById(recipeId);
            mRestClient.getRecipesById(recipeId).enqueue(new Callback<RecipesByIdDTO>() {
                @Override
                public void onResponse(Call<RecipesByIdDTO> call, Response<RecipesByIdDTO> response) {
                    if (response.isSuccessful()) {
                        // we assert that the body of a response is not empty
                        assert response.body() != null;
                        RecipeDetailsIM recipe = Mapper.mapRecipeDetailsDtoToRecipeDetailsIm(true, response.body().getRecipesById().get(0));
                        // we check whether recipe is in database or not (check the method bellow)
                        String check = checkForFavorites(recipe, rd);
                        switch (check) {
                            case "IN DB OK":
                                // if it is in database and it hasn't been updated since last
                                // fetching from client, we send it to ViewModel
                                returnRecipe.setValue(Mapper.mapRecipeDetailsToRecipeDetailsIm(rd));
                                break;
                            case "IN DB NOT OK":
                                // if it's in database but it holds different information compared
                                // latest fetch, we delete it from database and insert recipe with
                                // newest information in database
                                deleteRecipe(recipeId);
                                insertRecipe(recipe);
                                returnRecipe.setValue(Mapper.mapRecipeDetailsToRecipeDetailsIm(rd));
                                break;
                            default:
                                // if it's not in database, we just show it
                                returnRecipe.setValue(Mapper.mapRecipeDetailsDtoToRecipeDetailsIm(false, response.body().getRecipesById().get(0)));
                                break;
                        }
                    } else {
                        // else we return null object
                        returnRecipe.setValue(null);
                    }
                }

                @Override
                public void onFailure(Call<RecipesByIdDTO> call, Throwable t) {
                    // also return null object
                    returnRecipe.setValue(null);
                }
            });
        }
        return returnRecipe;
    }

    // Checks if the recipe from api equals recipe from database with same id
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

    // This method inserts recipe in database
    public void insertRecipe(RecipeDetailsIM recipeDetailsIM) {
        RecipeDetails recipeDetails = Mapper.mapRecipeDetailsIMToRecipeDetails(recipeDetailsIM);
        recipeDao.insertRecipe(recipeDetails);
    }

    // This method deletes recipe from database
    public void deleteRecipe(String idMeal) {
        recipeDao.deleteRecipe(idMeal);
    }

    // This method returns LiveData of favorite recipes
    public LiveData<List<RecipeDetails>> getFavorites() {
        return favorites;
    }
}
