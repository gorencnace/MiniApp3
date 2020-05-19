package si.uni_lj.fri.pbd.miniapp3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import si.uni_lj.fri.pbd.miniapp3.R;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeSummaryIM;

// https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private MutableLiveData<List<RecipeSummaryIM>> recipeSummaries;
    private LayoutInflater mInflater;

    public RecyclerViewAdapter(Context context, MutableLiveData<List<RecipeSummaryIM>> recipeSummaries) {
        this.recipeSummaries = recipeSummaries;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends
}
