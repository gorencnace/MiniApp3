package si.uni_lj.fri.pbd.miniapp3.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import si.uni_lj.fri.pbd.miniapp3.R;
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeSummaryIM;
import si.uni_lj.fri.pbd.miniapp3.ui.DetailsActivity;

// https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
// https://medium.com/@haxzie/how-to-create-custom-recyclerview-adapter-with-multiple-view-items-b65bfdafc112 po tem
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<RecipeSummaryIM> recipeSummaries;
    private Context mContext;
    private boolean fromAPI;

    public RecyclerViewAdapter(Context mContext, List<RecipeSummaryIM> recipeSummaryIMS, boolean fromAPI) {
        this.recipeSummaries = recipeSummaryIMS;
        this.mContext = mContext;
        this.fromAPI = fromAPI;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.layout_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeSummaryIM recipeSummary = recipeSummaries.get(position);
        holder.setMealThumb(recipeSummary.getStrMealThumb(), mContext);
        holder.setMealName(recipeSummary.getStrMeal());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fromAPI || isNetworkAvailable()) {
                    Intent intent = new Intent(mContext, DetailsActivity.class);
                    intent.putExtra("recipeId", recipeSummary.getIdMeal());
                    intent.putExtra("fromAPI", fromAPI);
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, R.string.error_no_internet, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeSummaries == null ? 0 : recipeSummaries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mealThumb;
        private TextView mealName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mealThumb = (ImageView) itemView.findViewById(R.id.image_view);
            mealName = (TextView) itemView.findViewById(R.id.text_view_content);
        }

        public void setMealThumb(String strMealThumb, Context context) {
            Glide.with(context).load(strMealThumb).into(this.mealThumb);
        }

        public void setMealName(String mealName) {
            this.mealName.setText(mealName);
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
