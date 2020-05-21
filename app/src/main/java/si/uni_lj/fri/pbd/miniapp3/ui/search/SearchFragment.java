package si.uni_lj.fri.pbd.miniapp3.ui.search;

/*
 * SEARCH FRAGMENT
 *
 * This fragment allows user to select ingredient for which we search the recipe for.
 *
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.List;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import si.uni_lj.fri.pbd.miniapp3.R;
import si.uni_lj.fri.pbd.miniapp3.adapter.RecyclerViewAdapter;
import si.uni_lj.fri.pbd.miniapp3.adapter.SpinnerAdapter;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeSummaryIM;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientsDTO;
import si.uni_lj.fri.pbd.miniapp3.rest.RestAPI;
import si.uni_lj.fri.pbd.miniapp3.rest.ServiceGenerator;
import si.uni_lj.fri.pbd.miniapp3.ui.RecipeViewModel;

public class SearchFragment extends Fragment {

    // FIELDS
    private RecipeViewModel mViewModel;
    private MaterialProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private long startTime;
    private boolean networkErrorOnStart;
    RestAPI mRestClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        progressBar = (MaterialProgressBar) getView().findViewById(R.id.progress_bar_search_fragment);
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh_layout_search_fragment);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view_search_fragment);
        mRestClient = ServiceGenerator.createService(RestAPI.class);

        fetchIngredients();
    }

    // Here we try to fetch ingredients
    private void fetchIngredients() {
        networkErrorOnStart = isNetworkAvailable();
        // we get ingredients with direct api call
        mRestClient.getAllIngredients().enqueue(new Callback<IngredientsDTO>() {
            @Override
            public void onResponse(Call<IngredientsDTO> call, Response<IngredientsDTO> response) {
                if (response.isSuccessful()) {
                    // if it is successful we crate spinner
                    spinnerSetup(response.body().getIngredients());
                }
            }

            @Override
            public void onFailure(Call<IngredientsDTO> call, Throwable t) {
                // else we toast an error
                Toast.makeText(getContext(), R.string.error_no_internet, Toast.LENGTH_LONG).show();
                // and enable user to connect to the network and try to get ingredients again with swipe
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeRefreshLayout.setRefreshing(false);
                        fetchIngredients();
                    }
                });
            }
        });
    }

    // Here we set up spinner
    private void spinnerSetup(List<IngredientDTO> ingredientDTOS) {
        Spinner spinnerIngredients = (Spinner) getView().findViewById(R.id.spinner_search_fragment);
        progressBar.setVisibility(MaterialProgressBar.INVISIBLE);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, ingredientDTOS);
        spinnerIngredients.setAdapter(spinnerAdapter);

        // when we select an ingredient we show recipes for it
        spinnerIngredients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                progressBar.setVisibility(MaterialProgressBar.VISIBLE);
                // we set recipes in adapter
                setRecipeSummaries(spinnerAdapter, position);
                startTime = System.currentTimeMillis();
                // we setup swipe refresher to refresh recipes (we can refresh it every 5 seconds)
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (System.currentTimeMillis() - startTime > 5000) {
                            setRecipeSummaries(spinnerAdapter, position);
                            swipeRefreshLayout.setRefreshing(false);
                            startTime = System.currentTimeMillis();
                        } else {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // well... it always selects something
            }
        });
    }

    // We set up recipes here.
    private void setRecipeSummaries(SpinnerAdapter spinnerAdapter, int position) {
        mViewModel.getRecipeSummaries(true, ((IngredientDTO) spinnerAdapter.getItem(position)).getStrIngredient()).observe(getViewLifecycleOwner(), new Observer<List<RecipeSummaryIM>>() {
            @Override
            public void onChanged(List<RecipeSummaryIM> recipeSummaryIMS) {
                progressBar.setVisibility(MaterialProgressBar.INVISIBLE);
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), R.string.error_no_internet, Toast.LENGTH_LONG).show();
                } else if (recipeSummaryIMS == null) {
                     Toast.makeText(getContext(), R.string.error_no_recipes_for_ingredient, Toast.LENGTH_LONG).show();
                }
                recyclerViewSetup(recipeSummaryIMS);
            }
        });
    }

    private void recyclerViewSetup(List<RecipeSummaryIM> recipeSummaryIMS) {
        // to improve performance because we know that changes in content  do not change the layout size of RecyclerView
        recyclerView.setHasFixedSize(true);
        // using a grid layout manager
        // https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this.getContext(), recipeSummaryIMS, true);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    // we check if network is available
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
