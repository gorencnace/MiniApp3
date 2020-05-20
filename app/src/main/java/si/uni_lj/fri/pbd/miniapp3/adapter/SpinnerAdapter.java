package si.uni_lj.fri.pbd.miniapp3.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import si.uni_lj.fri.pbd.miniapp3.R;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientsDTO;
import si.uni_lj.fri.pbd.miniapp3.ui.search.SearchFragment;
import timber.log.Timber;

// http://sakibandroid.blogspot.com/2016/12/custom-spinner-using-baseadapter.html
public class SpinnerAdapter extends BaseAdapter {
    private List<IngredientDTO> ingredients;
    private SearchFragment context;
    private LayoutInflater inflater;

    public SpinnerAdapter(SearchFragment context, List<IngredientDTO> ingredients) {
        this.ingredients = ingredients;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (ingredients == null) {
            return 0;
        }
        return ingredients.size();
    }

    @Override
    public Object getItem(int position) {
        return ingredients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(ingredients.get(position).getIdIngredient());
    }

    class Holder {
        private TextView ingredient;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = null;
        try {
            Holder holder;
            mView = convertView;

            if (mView == null) {
                inflater = (LayoutInflater) context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                mView = inflater.inflate(R.layout.spinner_item, null);

                holder = new Holder();
                holder.ingredient = (TextView) mView.findViewById(R.id.text_view_spinner_item);
                mView.setTag(holder);
            } else {
                holder = (Holder) mView.getTag();
            }
            IngredientDTO ingredientDTO = (IngredientDTO) getItem(position);
            holder.ingredient.setText(ingredientDTO.getStrIngredient());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mView;
    }
}
