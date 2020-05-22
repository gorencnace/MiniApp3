package si.uni_lj.fri.pbd.miniapp3.ui;

/*
 * DETAILS ACTIVITY
 *
 * This activity shows details of recipe.
 */

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.bumptech.glide.Glide;
import si.uni_lj.fri.pbd.miniapp3.R;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeDetailsIM;

public class DetailsActivity extends AppCompatActivity {

    // FEILDS
    private ImageView image;
    private TextView name;
    private TextView origin;
    private TextView ingredients;
    private TextView recipe;
    private ToggleButton favorite;
    private RecipeViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        image = (ImageView) findViewById(R.id.details_image);
        name = (TextView) findViewById(R.id.details_recipe_name);
        origin = (TextView) findViewById(R.id.details_recipe_origin);
        ingredients = (TextView) findViewById(R.id.details_ingredient_list);
        recipe = (TextView) findViewById(R.id.details_recipe);
        favorite = (ToggleButton) findViewById(R.id.details_favorite_button);

        String recipeId = getIntent().getStringExtra("recipeId");
        boolean fromAPI = getIntent().getBooleanExtra("fromAPI", true);

        // fetch data from ViewModel
        mViewModel.getRecipeDetailsById(recipeId, fromAPI).observe(this, new Observer<RecipeDetailsIM>() {
            @Override
            public void onChanged(RecipeDetailsIM recipeDetailsIM) {
                recipeSetup(recipeDetailsIM);
            }
        });
    }

    // Here we set up the UI.
    private void recipeSetup(RecipeDetailsIM recipeDetails) {
        Glide.with(this).load(recipeDetails.getStrMealThumb()).into(this.image);
        name.setText(recipeDetails.getStrMeal());
        origin.setText(recipeDetails.getStrArea());
        ingredients.setText(ingredientsStringBuild(recipeDetails));
        recipe.setText(recipeDetails.getStrInstructions());
        setFavorite(recipeDetails.getFavorite());
        // favorite button setup
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipeDetails.getFavorite()) {
                    // if we don't want that recipe to be favorite anymore, we delete it from database
                    setFavorite(false);
                    recipeDetails.setFavorite(false);
                    mViewModel.deleteRecipe(recipeDetails.getIdMeal());
                } else {
                    // if we want to add recipe as favorite we insert it in database
                    setFavorite(true);
                    recipeDetails.setFavorite(true);
                    mViewModel.insertRecipe(recipeDetails);
                }
            }
        });
    }

    // favorite button layout setup
    private void setFavorite(boolean isFavorite) {
        if (isFavorite) {
            favorite.setChecked(true);
        } else {
            favorite.setChecked(false);
        }
    }

    // help method for showing ingredients and measures together
    private String ingredientsStringBuild(RecipeDetailsIM rd) {
        StringBuilder builder = new StringBuilder();
        if (rd.getStrIngredient1() != null && !rd.getStrIngredient1().equals("")) {
            builder.append("\u2022 " + rd.getStrIngredient1() + measuresStringBuild(rd.getStrMeasure1()));
        }
        if (rd.getStrIngredient2() != null && !rd.getStrIngredient2().equals("")) {
            builder.append("\n\u2022 " + rd.getStrIngredient2() + measuresStringBuild(rd.getStrMeasure2()));
        }
        if (rd.getStrIngredient3() != null && !rd.getStrIngredient3().equals("")) {
            builder.append("\n\u2022 " + rd.getStrIngredient3() + measuresStringBuild(rd.getStrMeasure3()));
        }
        if (rd.getStrIngredient4() != null && !rd.getStrIngredient4().equals("")) {
            builder.append("\n\u2022 " + rd.getStrIngredient4() + measuresStringBuild(rd.getStrMeasure4()));
        }
        if (rd.getStrIngredient5() != null && !rd.getStrIngredient5().equals("")) {
            builder.append("\n\u2022 " + rd.getStrIngredient5() + measuresStringBuild(rd.getStrMeasure5()));
        }
        if (rd.getStrIngredient6() != null && !rd.getStrIngredient6().equals("")) {
            builder.append("\n\u2022 " + rd.getStrIngredient6() + measuresStringBuild(rd.getStrMeasure6()));
        }
        if (rd.getStrIngredient7() != null && !rd.getStrIngredient7().equals("")) {
            builder.append("\n\u2022 " + rd.getStrIngredient7() + measuresStringBuild(rd.getStrMeasure7()));
        }
        if (rd.getStrIngredient8() != null && !rd.getStrIngredient8().equals("")) {
            builder.append("\n\u2022 " + rd.getStrIngredient8() + measuresStringBuild(rd.getStrMeasure8()));
        }
        if (rd.getStrIngredient9() != null && !rd.getStrIngredient9().equals("")) {
            builder.append("\n\u2022 " + rd.getStrIngredient9() + measuresStringBuild(rd.getStrMeasure9()));
        }
        if (rd.getStrIngredient10() != null && !rd.getStrIngredient10().equals("")) {
            builder.append("\n\u2022 " + rd.getStrIngredient10() + measuresStringBuild(rd.getStrMeasure10()));
        }
        if (rd.getStrIngredient11() != null && !rd.getStrIngredient11().equals("")) {
            builder.append("\n\u2022 " + rd.getStrIngredient11() + measuresStringBuild(rd.getStrMeasure11()));
        }
        if (rd.getStrIngredient12() != null && !rd.getStrIngredient12().equals("")) {
            builder.append("\n\u2022 " + rd.getStrIngredient12() + measuresStringBuild(rd.getStrMeasure12()));
        }
        if (rd.getStrIngredient13() != null && !rd.getStrIngredient13().equals("")) {
            builder.append("\n\u2022 " + rd.getStrIngredient13() + measuresStringBuild(rd.getStrMeasure13()));
        }
        if (rd.getStrIngredient14() != null && !rd.getStrIngredient14().equals("")) {
            builder.append("\n\u2022 " + rd.getStrIngredient14() + measuresStringBuild(rd.getStrMeasure14()));
        }
        if (rd.getStrIngredient15() != null && !rd.getStrIngredient15().equals("")) {
            builder.append("\n\u2022 " + rd.getStrIngredient15() + measuresStringBuild(rd.getStrMeasure15()));
        }
        if (rd.getStrIngredient16() != null && !rd.getStrIngredient16().equals("")) {
            builder.append("\n\u2022 " + rd.getStrIngredient16() + measuresStringBuild(rd.getStrMeasure16()));
        }
        if (rd.getStrIngredient17() != null && !rd.getStrIngredient17().equals("")) {
            builder.append("\n\u2022 " + rd.getStrIngredient17() + measuresStringBuild(rd.getStrMeasure17()));
        }
        if (rd.getStrIngredient18() != null && !rd.getStrIngredient18().equals("")) {
            builder.append("\n\u2022 " + rd.getStrIngredient18() + measuresStringBuild(rd.getStrMeasure18()));
        }
        if (rd.getStrIngredient19() != null && !rd.getStrIngredient19().equals("")) {
            builder.append("\n\u2022 " + rd.getStrIngredient19() + measuresStringBuild(rd.getStrMeasure19()));
        }
        if (rd.getStrIngredient20() != null && !rd.getStrIngredient20().equals("")) {
            builder.append("\n\u2022 " + rd.getStrIngredient10() + measuresStringBuild(rd.getStrMeasure20()));
        }
        return builder.toString();
    }

    // measure builder (not all recipes have measure attribute)
    private String measuresStringBuild(String measure) {
        if (measure != null) {
            while (measure.endsWith(" ")) {
                measure = measure.substring(0, measure.length()-1);
            }
            if (!measure.equals("")) {
                return "  (" + measure + ")";
            }
        }
        return "";
    }
}
