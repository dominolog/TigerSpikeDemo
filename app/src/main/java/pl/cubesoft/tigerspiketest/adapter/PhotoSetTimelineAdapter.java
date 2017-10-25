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

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


import pl.cubesoft.tigerspiketest.ImageLoader;
import pl.cubesoft.tigerspiketest.R;
import pl.cubesoft.tigerspiketest.data.model.Photo;
import pl.cubesoft.tigerspiketest.data.model.User;
import rx.Subscription;
import pl.cubesoft.tigerspiketest.utils.DeviceUtils;
import pl.cubesoft.tigerspiketest.utils.TextUtils;


public class PhotoSetTimelineAdapter extends PhotoSetBaseAdapter {

	private final int cellWidth;



	public final class ViewHolder extends PhotoSetBaseAdapter.ViewHolder {

		@BindView(R.id.photo)
		ImageView photo;

		@BindView(R.id.user_photo)
		ImageView userPhoto;


		@BindView(R.id.owner_name)
		TextView ownerName;

		@BindView(R.id.date_created)
		TextView dateCreated;

		@BindView(R.id.title)
		TextView title;


		@BindView(R.id.play)
		ImageView play;

		@BindView(R.id.selected)
		ImageView selected;


		@BindView(R.id.favorite)
		ImageView favorite;

		@BindView(R.id.comments)
		View comments;

		@BindView(R.id.info)
		View info;


		public ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);

			final int colorAccent = ContextCompat.getColor(view.getContext(), R.color.colorAccent);
			final int colorBackground = colorAccent & 0xAAFFFFFF;
			selected.setBackgroundColor(colorBackground);

		}

	}

	private final Object tag;


	//private Set<Integer> mSelectedItemSet = new HashSet<Integer>();

	public PhotoSetTimelineAdapter(Context context, ImageLoader imageLoader, Object tag, int columnCount) {
		super(imageLoader);
		this.tag = tag;
		Point screenSize = DeviceUtils.getScreenSize(context);
		cellWidth = screenSize.x / columnCount;
		setHasStableIds(true);
	}


	@Override
	public PhotoSetBaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_photoset_timeline_item, parent, false);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(PhotoSetBaseAdapter.ViewHolder baseHolder, int position) {
		ViewHolder holder = (ViewHolder) baseHolder;

		//StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
		//layoutParams.setFullSpan(true);

		final Photo item = getItem(position);

		final int width = item.getWidth() != null ? item.getWidth() : 100;
		final int height = item.getHeight() != null ? item.getHeight() : 100;

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

		Date dateCreated = item.getDateCreated();
		if ( dateCreated != null ) {
			holder.dateCreated.setText(TextUtils.formatTimestampRelative(dateCreated));
			holder.dateCreated.setVisibility(View.VISIBLE);
		} else {
			holder.dateCreated.setVisibility(View.GONE);
		}
		holder.title.setText(item.getTitle());

		imageLoader.load(Uri.parse(url),
				holder.photo, ImageLoader.Transform.NONE, tag);

		{
			Subscription subscription = (Subscription) holder.userPhoto.getTag();
			if (subscription != null) {
				subscription.unsubscribe();
			}
			subscription = onItemIteractionListener.getPhotoOwner(item, user -> {
				setPhotoOwner(holder, user);
			});
			holder.userPhoto.setTag(subscription);
		}
		holder.photo.setOnClickListener(view -> {
			if (onItemIteractionListener != null) {
				onItemIteractionListener.onItemClick(holder.itemView, holder.photo, position);
			}
		});

		holder.photo.setOnLongClickListener(view -> onItemIteractionListener != null && onItemIteractionListener.onItemLongClick(view, position));

		holder.selected.setVisibility(isItemSelected(position) ? View.VISIBLE : View.GONE);





		if (onItemIteractionListener.canFavorite(item)) {
			holder.favorite.setVisibility(View.VISIBLE);
			holder.favorite.setImageResource( item.isFavorite() ? R.drawable.ic_favorite_black_24dp : R.drawable.ic_favorite_border_black_24dp);
			holder.favorite.setOnClickListener(view1 -> {
				onItemIteractionListener.onPhotoItemFavoriteClick(position);
			});

		} else {
			holder.favorite.setVisibility(View.GONE);
			holder.favorite.setOnClickListener(null);
		}



		if (item.getNumComments() != null) {
			holder.comments.setVisibility(View.VISIBLE);
			holder.comments.setOnClickListener(view1 -> {
				onItemIteractionListener.onPhotoItemCommentClick(position);
			});
		} else {
			holder.comments.setVisibility(View.GONE);

		}

		holder.info.setOnClickListener(view -> {
			onItemIteractionListener.onPhotoItemInfoClick(position);
		});

	}

	private void setPhotoOwner(ViewHolder item, User user) {
		if (user != null) {
			if (user.getPhotoUrl() != null) {
				item.userPhoto.setVisibility(View.VISIBLE);

				imageLoader.load(Uri.parse(user.getPhotoUrl()), item.userPhoto, ImageLoader.Transform.CIRCLE, tag);

			} else {
				item.userPhoto.setVisibility(View.GONE);

			}
			if (user.getDisplayName() != null) {
				item.ownerName.setText(user.getDisplayName());
			} else {
				item.ownerName.setText(user.getName());
			}

			item.userPhoto.setOnClickListener(view1 -> {

				if ( onItemIteractionListener != null ) {
					onItemIteractionListener.onPhotoItemUserClick(user);
				}
			});
		} else {
			item.userPhoto.setVisibility(View.GONE);
		}
	}

}
