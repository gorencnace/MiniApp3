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
        observerSetup(view);
        //recyclerViewSetup(view);
        //spinnerSetup(view);
    }

    private void observerSetup(View view) {
        mViewModel.getAllIngredients().observe(getViewLifecycleOwner(), new Observer<List<IngredientDTO>>() {
            @Override
            public void onChanged(List<IngredientDTO> ingredientDTOS) {
                spinnerSetup(view);
            }
        });
    }

    private void spinnerSetup(View view) {
        // spinner first try:
        Spinner spinnerIngredients = (Spinner) view.findViewById(R.id.spinner_search_fragment);
        progressBar.setVisibility(MaterialProgressBar.INVISIBLE);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, mViewModel.getAllIngredients());
        spinnerIngredients.setAdapter(spinnerAdapter);

        spinnerIngredients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recyclerViewSetup(view, ((IngredientDTO) spinnerAdapter.getItem(position)).getStrIngredient());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void recyclerViewSetup(View view, String ingredient) {
        progressBar.setVisibility(MaterialProgressBar.VISIBLE);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_search_fragment);

        // TODO: https://developer.android.com/guide/topics/ui/layout/recyclerview
        // to improve performance because we know that changes in content  do not change the layout size of RecyclerView
        recyclerView.setHasFixedSize(true);
        // using a grid layout manager
        // https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mViewModel.getRecipeSummaries(true, ingredient));
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}
