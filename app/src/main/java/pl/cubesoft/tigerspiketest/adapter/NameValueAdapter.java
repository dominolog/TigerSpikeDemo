package pl.cubesoft.tigerspiketest.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.cubesoft.tigerspiketest.R;


public class NameValueAdapter extends RecyclerView.Adapter<NameValueAdapter.ViewHolder>{


	private List<Pair<String, String>> data;

	public void setData(List<Pair<String, String>> data) {
		this.data = data;
		notifyDataSetChanged();
	}


	public final class ViewHolder extends RecyclerView.ViewHolder {


		@BindView(R.id.name)
		TextView name;

		@BindView(R.id.value)
		TextView value;


		public ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);



		}

	}



	public NameValueAdapter() {
		setHasStableIds(true);
	}
	

	


	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_name_value_item, parent, false);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {

		//StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
		//layoutParams.setFullSpan(true);

		final Pair<String, String> item = getItem(position);

		holder.name.setText(item.first);
		holder.value.setText(item.second);
	}

	public Pair<String, String> getItem(Integer position) {
		return data != null ? data.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).hashCode();
	}

	@Override
	public int getItemCount() {
		return data != null ? data.size() : 0;
	}






}
