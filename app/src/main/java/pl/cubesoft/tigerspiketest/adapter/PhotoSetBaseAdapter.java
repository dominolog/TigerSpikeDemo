package pl.cubesoft.tigerspiketest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;


import pl.cubesoft.tigerspiketest.ImageLoader;
import pl.cubesoft.tigerspiketest.data.model.Photo;
import pl.cubesoft.tigerspiketest.data.model.PhotoSet;
import pl.cubesoft.tigerspiketest.data.model.User;
import rx.Subscription;


public abstract class PhotoSetBaseAdapter extends SelectableRecyclerViewAdapter<PhotoSetBaseAdapter.ViewHolder>{

	protected OnItemIteractionListener onItemIteractionListener;
	private PhotoSet photoSet;
	class ViewHolder extends RecyclerView.ViewHolder {

		public ViewHolder(View itemView) {
			super(itemView);
		}
	}

	public final void setData(PhotoSet photoSet) {
		this.photoSet = photoSet;
		notifyDataSetChanged();
	}


	protected final ImageLoader imageLoader;


	public final void setOnItemIteractionListener(OnItemIteractionListener onItemIteractionListener) {
		this.onItemIteractionListener = onItemIteractionListener;
	}




	public interface OnItemIteractionListener{
		void onItemClick(View view, View transitionView, int position);
		boolean onItemLongClick(View view, int position);
		void onItemLongPlayClick(View view, int position);
		void onPhotoItemUserClick(User user);

		void onPhotoItemFavoriteClick(int position);

		void onPhotoItemCommentClick(int position);

		void onPhotoItemInfoClick(int position);

		boolean canFavorite(Photo item);


		interface Callback<T>  {
			void call(T value);
		}
		Subscription getPhotoOwner(Photo photo, Callback<User> callback);

	}



	//private Set<Integer> mSelectedItemSet = new HashSet<Integer>();

	public PhotoSetBaseAdapter(ImageLoader imageLoader) {
		this.imageLoader = imageLoader;
		setHasStableIds(true);
	}
	

	public final Photo getItem(Integer position) {
		return photoSet.getItem(position);
	}

	@Override
	public final long getItemId(int position) {
		return getItem(position).getId().hashCode();
	}

	@Override
	public final int getItemCount() {
		return photoSet != null ? photoSet.getItemCount() : 0;
	}

}
