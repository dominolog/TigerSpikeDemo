package pl.cubesoft.tigerspiketest.adapter;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.cubesoft.tigerspiketest.ImageLoader;
import pl.cubesoft.tigerspiketest.R;
import pl.cubesoft.tigerspiketest.data.model.Group;
import pl.cubesoft.tigerspiketest.data.model.GroupElement;
import pl.cubesoft.tigerspiketest.utils.DeviceUtils;
import pl.cubesoft.tigerspiketest.utils.TextUtils;


public class GroupAdapter extends SelectableRecyclerViewAdapter<GroupAdapter.ViewHolder>{

	private final int cellWidth;
	private final int cellHeight;
	private final int screenWidth;
	private OnItemIteractionListener onItemIteractionListener;
	private Group group;

	public void setData(Group group) {
		this.group = group;
		notifyDataSetChanged();
	}


	public final class ViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.photo)
		ImageView photo;


		@BindView(R.id.title)
		TextView title;

		@BindView(R.id.num_items)
		TextView numItems;

		@BindView(R.id.date_modified)
		TextView dateModified;

		@BindView(R.id.img_num_views)
		View imgNumViews;

		@BindView(R.id.num_views)
		TextView numViews;

		@BindView(R.id.privacy)
		View privacy;

		@BindView(R.id.options)
		View options;

		public ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);

			final int colorAccent = ContextCompat.getColor(view.getContext(), R.color.colorAccent);
			final int colorBackground = colorAccent & 0xAAFFFFFF;
			//selected.setBackgroundColor(colorBackground);

		}

	}

	private final ImageLoader imageLoader;

	private final Object tag;


	public void setOnItemIteractionListener(OnItemIteractionListener onItemIteractionListener) {
		this.onItemIteractionListener = onItemIteractionListener;
	}




	public interface OnItemIteractionListener{
		void onItemClick(View view, View transitionView, int position);
		boolean onItemLongClick(View view, int position);

		void onItemOptionsClick(View view, int position);
	}



	//private Set<Integer> mSelectedItemSet = new HashSet<Integer>();

	public GroupAdapter(Context context, ImageLoader imageLoader, Object tag, int columnCount) {
		this.imageLoader = imageLoader;
		this.tag = tag;
		Point screenSize = DeviceUtils.getScreenSize(context);
		cellWidth = screenSize.x  / columnCount;
		cellHeight = screenSize.y /columnCount;
		screenWidth = screenSize.x;
		setHasStableIds(true);
	}
	

	


	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_group_item, parent, false);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {

		//StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
		//layoutParams.setFullSpan(true);

		final GroupElement item = getItem(position);
		final String url = item.getTitlePhotoUrl();

		//holder.play.setVisibility(item.isVideo() ? View.VISIBLE : View.GONE);

		//final boolean hasPhotoSameRealm = mDataProvider.hasPhotoSameRealm(item);
		//holder.mImageViewLock.setVisibility( ac.getAccessType() != AccessType.Public ? View.VISIBLE : View.GONE);

		holder.title.setText(item.getTitle());

		holder.numItems.setText(TextUtils.formatItems(item.getItemCount()));

		if (item.getViewCount() != null ) {

			holder.numViews.setText(item.getViewCount().toString());
			holder.imgNumViews.setVisibility(View.VISIBLE);
			holder.numViews.setVisibility(View.VISIBLE);
		} else {
			holder.imgNumViews.setVisibility(View.GONE);
			holder.numViews.setVisibility(View.GONE);
		}

		if ( item.getLastUpdated() != null ) {
			holder.dateModified.setText(TextUtils.formatTimestampRelative(item.getLastUpdated()));
			holder.dateModified.setVisibility(View.VISIBLE);
		} else {
			holder.dateModified.setVisibility(View.GONE);
		}
		if (url != null) {
			imageLoader.load(Uri.parse(url),
					holder.photo, ImageLoader.Transform.NONE, tag);
		} else {
			holder.photo.setImageBitmap(null);
		}


		holder.privacy.setVisibility(item.isPrivate() != null && item.isPrivate() ? View.VISIBLE : View.GONE);

		holder.itemView.setOnClickListener(view -> {
			if (onItemIteractionListener != null) {
				onItemIteractionListener.onItemClick(holder.itemView, holder.photo, position);
			}
		});

		holder.itemView.setOnLongClickListener(view -> onItemIteractionListener != null && onItemIteractionListener.onItemLongClick(view, position));
		holder.options.setOnClickListener(view -> {
			if ( onItemIteractionListener != null) {
				onItemIteractionListener.onItemOptionsClick(view, position);
			}
		});


	}

	public GroupElement getItem(Integer position) {
		return group.getItem(position);
	}
	@Override
	public long getItemId(int position) {
		return getItem(position).getId().hashCode();
	}

	@Override
	public int getItemCount() {
		return group != null ? group.getItemCount() : 0;
	}






}
