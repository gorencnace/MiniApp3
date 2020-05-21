package si.uni_lj.fri.pbd.miniapp3.ui.favorites;

/*
 * FAVORITES FRAGMENT
 * Here are shown recipes tagged as favorites.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import si.uni_lj.fri.pbd.miniapp3.R;
import si.uni_lj.fri.pbd.miniapp3.adapter.RecyclerViewAdapter;
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;
import si.uni_lj.fri.pbd.miniapp3.models.Mapper;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeSummaryIM;
import si.uni_lj.fri.pbd.miniapp3.ui.RecipeViewModel;

public class FavoritesFragment extends Fragment {

    // FIELDS
    private RecipeViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        // observe any changes on favorite recipes
        mViewModel.getFavorites().observe(getViewLifecycleOwner(), new Observer<List<RecipeDetails>>() {
            @Override
            public void onChanged(List<RecipeDetails> recipeDetailsList) {
                // if they are changed we update RecyclerView
                recyclerViewSetup(recipeDetailsList);
            }
        });
    }

    // Setts up RecycleView
    private void recyclerViewSetup(List<RecipeDetails> recipeDetailsList) {
        // translates from RecipeDetails to RecipeDetailsIM
        List<RecipeSummaryIM> recipeSummaryIMS = new ArrayList<>();
        for (RecipeDetails rd : recipeDetailsList) {
            recipeSummaryIMS.add(Mapper.mapRecipeDetailsToRecipeSummaryIm(rd));
        }
        // sorts list of recipes by name
        Collections.sort(recipeSummaryIMS, new Comparator<RecipeSummaryIM>() {
            @Override
            public int compare(RecipeSummaryIM o1, RecipeSummaryIM o2) {
                return o1.getStrMeal().compareTo(o2.getStrMeal());
            }
        });

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view_favorites_fragment);

        // to improve performance because we know that changes in content  do not change the layout size of RecyclerView
        recyclerView.setHasFixedSize(true);
        // using a grid layout manager
        // https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this.getContext(), recipeSummaryIMS, false);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}
