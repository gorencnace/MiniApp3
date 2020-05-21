package si.uni_lj.fri.pbd.miniapp3.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

import si.uni_lj.fri.pbd.miniapp3.R;
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeDetailsIM;

// check this for button https://stackoverflow.com/questions/34980309/favourite-button-android

public class DetailsActivity extends AppCompatActivity {

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

        mViewModel.getRecipeDetailsById(recipeId, fromAPI).observe(this, new Observer<RecipeDetailsIM>() {
            @Override
            public void onChanged(RecipeDetailsIM recipeDetailsIM) {
                recipeSetup(recipeDetailsIM);
            }
        });
    }

    private void recipeSetup(RecipeDetailsIM recipeDetails) {
        Glide.with(this).load(recipeDetails.getStrMealThumb()).into(this.image);
        name.setText(recipeDetails.getStrMeal());
        origin.setText(recipeDetails.getStrArea());
        ingredients.setText(ingredientsStringBuild(recipeDetails));
        recipe.setText(recipeDetails.getStrInstructions());
        setFavorite(recipeDetails.getFavorite());

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipeDetails.getFavorite()) {
                    setFavorite(false);
                    recipeDetails.setFavorite(false);
                    mViewModel.deleteRecipe(recipeDetails.getIdMeal());
                } else {
                    setFavorite(true);
                    recipeDetails.setFavorite(true);
                    mViewModel.insertRecipe(recipeDetails);
                }
            }
        });
    }

    private void setFavorite(boolean isFavorite) {
        if (isFavorite) {
            favorite.setChecked(true);
        } else {
            favorite.setChecked(false);
        }
    }

    private String ingredientsStringBuild(RecipeDetailsIM rd) {
        StringBuilder builder = new StringBuilder();
        if (rd.getStrIngredient1() != null && !rd.getStrIngredient1().equals("")) {
            builder.append(rd.getStrIngredient1() + " (" + rd.getStrMeasure1() + ")");
        }
        if (rd.getStrIngredient2() != null && !rd.getStrIngredient2().equals("")) {
            builder.append("\n" + rd.getStrIngredient2() + " (" + rd.getStrMeasure2() + ")");
        }
        if (rd.getStrIngredient3() != null && !rd.getStrIngredient3().equals("")) {
            builder.append("\n" + rd.getStrIngredient3() + " (" + rd.getStrMeasure3() + ")");
        }
        if (rd.getStrIngredient4() != null && !rd.getStrIngredient4().equals("")) {
            builder.append("\n" + rd.getStrIngredient4() + " (" + rd.getStrMeasure4() + ")");
        }
        if (rd.getStrIngredient5() != null && !rd.getStrIngredient5().equals("")) {
            builder.append("\n" + rd.getStrIngredient5() + " (" + rd.getStrMeasure5() + ")");
        }
        if (rd.getStrIngredient6() != null && !rd.getStrIngredient6().equals("")) {
            builder.append("\n" + rd.getStrIngredient6() + " (" + rd.getStrMeasure6() + ")");
        }
        if (rd.getStrIngredient7() != null && !rd.getStrIngredient7().equals("")) {
            builder.append("\n" + rd.getStrIngredient7() + " (" + rd.getStrMeasure7() + ")");
        }
        if (rd.getStrIngredient8() != null && !rd.getStrIngredient8().equals("")) {
            builder.append("\n" + rd.getStrIngredient8() + " (" + rd.getStrMeasure8() + ")");
        }
        if (rd.getStrIngredient9() != null && !rd.getStrIngredient9().equals("")) {
            builder.append("\n" + rd.getStrIngredient9() + " (" + rd.getStrMeasure9() + ")");
        }
        if (rd.getStrIngredient10() != null && !rd.getStrIngredient10().equals("")) {
            builder.append("\n" + rd.getStrIngredient10() + " (" + rd.getStrMeasure10() + ")");
        }
        if (rd.getStrIngredient11() != null && !rd.getStrIngredient11().equals("")) {
            builder.append("\n" + rd.getStrIngredient11() + " (" + rd.getStrMeasure11() + ")");
        }
        if (rd.getStrIngredient12() != null && !rd.getStrIngredient12().equals("")) {
            builder.append("\n" + rd.getStrIngredient12() + " (" + rd.getStrMeasure12() + ")");
        }
        if (rd.getStrIngredient13() != null && !rd.getStrIngredient13().equals("")) {
            builder.append("\n" + rd.getStrIngredient13() + " (" + rd.getStrMeasure13() + ")");
        }
        if (rd.getStrIngredient14() != null && !rd.getStrIngredient14().equals("")) {
            builder.append("\n" + rd.getStrIngredient14() + " (" + rd.getStrMeasure14() + ")");
        }
        if (rd.getStrIngredient15() != null && !rd.getStrIngredient15().equals("")) {
            builder.append("\n" + rd.getStrIngredient15() + " (" + rd.getStrMeasure15() + ")");
        }
        if (rd.getStrIngredient16() != null && !rd.getStrIngredient16().equals("")) {
            builder.append("\n" + rd.getStrIngredient16() + " (" + rd.getStrMeasure16() + ")");
        }
        if (rd.getStrIngredient17() != null && !rd.getStrIngredient17().equals("")) {
            builder.append("\n" + rd.getStrIngredient17() + " (" + rd.getStrMeasure17() + ")");
        }
        if (rd.getStrIngredient18() != null && !rd.getStrIngredient18().equals("")) {
            builder.append("\n" + rd.getStrIngredient18() + " (" + rd.getStrMeasure18() + ")");
        }
        if (rd.getStrIngredient19() != null && !rd.getStrIngredient19().equals("")) {
            builder.append("\n" + rd.getStrIngredient19() + " (" + rd.getStrMeasure19() + ")");
        }
        if (rd.getStrIngredient20() != null && !rd.getStrIngredient20().equals("")) {
            builder.append("\n" + rd.getStrIngredient10() + " (" + rd.getStrMeasure20() + ")");
        }
        return builder.toString();
    }
}
