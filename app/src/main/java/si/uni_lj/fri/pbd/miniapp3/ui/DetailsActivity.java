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
    private TextView measures;
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
        measures = (TextView) findViewById(R.id.details_ingredient_measures);
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
        ingredients.setText(recipeDetails.getStrIngredient1());
        measures.setText(recipeDetails.getStrMeasure1());
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

    public void setFavorite(boolean isFavorite) {
        if (isFavorite) {
            favorite.setChecked(true);
        } else {
            favorite.setChecked(false);
        }
    }
}
