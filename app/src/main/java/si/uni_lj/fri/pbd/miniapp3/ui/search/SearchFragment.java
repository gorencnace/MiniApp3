package si.uni_lj.fri.pbd.miniapp3.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import si.uni_lj.fri.pbd.miniapp3.R;
import si.uni_lj.fri.pbd.miniapp3.adapter.RecyclerViewAdapter;
import si.uni_lj.fri.pbd.miniapp3.adapter.SpinnerAdapter;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeSummaryIM;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientDTO;
import si.uni_lj.fri.pbd.miniapp3.ui.RecipeViewModel;

public class SearchFragment extends Fragment {

    private RecipeViewModel mViewModel;
    private MaterialProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        mViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        progressBar = (MaterialProgressBar) view.findViewById(R.id.progress_bar_search_fragment);
        Log.d("SEARCH FRAGMENT","mViewModel created");

        // cakamo na rezultate apija in pol šele kličemo funkcijo spinnerSetup
        mViewModel.getAllIngredients().observe(getViewLifecycleOwner(), new Observer<List<IngredientDTO>>() {
            @Override
            public void onChanged(List<IngredientDTO> ingredientDTOS) {
                spinnerSetup(ingredientDTOS);
            }
        });
    }

    private void spinnerSetup(List<IngredientDTO> ingredientDTOS) {
        Spinner spinnerIngredients = (Spinner) getView().findViewById(R.id.spinner_search_fragment);
        progressBar.setVisibility(MaterialProgressBar.INVISIBLE);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, ingredientDTOS);
        spinnerIngredients.setAdapter(spinnerAdapter);

        // ko kliknem na ingredient se recepti naložijo
        spinnerIngredients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                progressBar.setVisibility(MaterialProgressBar.VISIBLE);
                mViewModel.getRecipeSummaries(true, ((IngredientDTO) spinnerAdapter.getItem(position)).getStrIngredient()).observe(getViewLifecycleOwner(), new Observer<List<RecipeSummaryIM>>() {
                    @Override
                    public void onChanged(List<RecipeSummaryIM> recipeSummaryIMS) {
                        progressBar.setVisibility(MaterialProgressBar.INVISIBLE);
                        recyclerViewSetup(recipeSummaryIMS);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void recyclerViewSetup(List<RecipeSummaryIM> recipeSummaryIMS) {
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view_search_fragment);

        // to improve performance because we know that changes in content  do not change the layout size of RecyclerView
        recyclerView.setHasFixedSize(true);
        // using a grid layout manager
        // https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this.getContext(), recipeSummaryIMS);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}
