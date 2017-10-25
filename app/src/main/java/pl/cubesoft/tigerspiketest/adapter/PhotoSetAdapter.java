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

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.cubesoft.tigerspiketest.ImageLoader;
import pl.cubesoft.tigerspiketest.R;
import pl.cubesoft.tigerspiketest.data.model.Photo;
import pl.cubesoft.tigerspiketest.utils.DeviceUtils;


public class PhotoSetAdapter extends PhotoSetBaseAdapter{

	private final int cellWidth;
	private final int cellHeight;
	private final int screenWidth;
	private final Object tag;



	public final class ViewHolder extends PhotoSetBaseAdapter.ViewHolder {

		@BindView(R.id.photo)
		ImageView photo;


		@BindView(R.id.play)
		ImageView play;

		@BindView(R.id.selected)
		ImageView selected;


		public ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);

			final int colorAccent = ContextCompat.getColor(view.getContext(), R.color.colorAccent);
			final int colorBackground = colorAccent & 0xAAFFFFFF;
			selected.setBackgroundColor(colorBackground);

		}
		
	}
	







	//private Set<Integer> mSelectedItemSet = new HashSet<Integer>();
	
	public PhotoSetAdapter(Context context, ImageLoader imageLoader, Object tag, int columnCount) {
		super(imageLoader);
		this.tag = tag;
		Point screenSize = DeviceUtils.getScreenSize(context);
		cellWidth = screenSize.x  / columnCount;
		cellHeight = screenSize.y /columnCount;
		screenWidth = screenSize.x;
		setHasStableIds(true);
	}
	

	


	@Override
	public PhotoSetBaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_photoset_item, parent, false);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(PhotoSetBaseAdapter.ViewHolder baseHolder, int position) {

		ViewHolder holder = (ViewHolder) baseHolder;
		//StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
		//layoutParams.setFullSpan(true);

		final Photo item = getItem(position);


		final int width = item.getWidth() != null? item.getWidth() : 100;
		final int height = item.getHeight() != null? item.getHeight() : 100;

		RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();

		params.height = height * cellWidth / (width > 0 ? width : 1);

		holder.itemView.setLayoutParams(params);

		final String url = item.getUrl();

		holder.play.setVisibility(item.getType() == Photo.Type.VIDEO ? View.VISIBLE : View.GONE);
		holder.play.setOnClickListener(view -> {
			if (onItemIteractionListener != null) {
				onItemIteractionListener.onItemLongPlayClick(view, position);
			}
		});

		//final boolean hasPhotoSameRealm = mDataProvider.hasPhotoSameRealm(item);
		//holder.mImageViewLock.setVisibility( ac.getAccessType() != AccessType.Public ? View.VISIBLE : View.GONE);


		imageLoader.load(Uri.parse(url),
					holder.photo, ImageLoader.Transform.NONE, tag);



		holder.itemView.setOnClickListener(view -> {
			if (onItemIteractionListener != null) {
				onItemIteractionListener.onItemClick(holder.itemView, holder.photo, position);
			}
		});

		holder.itemView.setOnLongClickListener(view -> onItemIteractionListener != null && onItemIteractionListener.onItemLongClick(view, position));

		holder.selected.setVisibility(isItemSelected(position) ? View.VISIBLE : View.GONE);


	}

}
