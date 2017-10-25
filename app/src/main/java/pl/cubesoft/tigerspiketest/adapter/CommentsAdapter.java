package pl.cubesoft.tigerspiketest.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.cubesoft.tigerspiketest.ImageLoader;
import pl.cubesoft.tigerspiketest.R;
import pl.cubesoft.tigerspiketest.data.model.Comment;
import pl.cubesoft.tigerspiketest.data.model.CommentPool;
import pl.cubesoft.tigerspiketest.data.model.User;
import pl.cubesoft.tigerspiketest.utils.TextUtils;


public class CommentsAdapter extends SelectableRecyclerViewAdapter<CommentsAdapter.ViewHolder>{

	private OnItemIteractionListener onItemIteractionListener;
	private CommentPool commentPool;

	public void setData(CommentPool commentPool) {
		this.commentPool = commentPool;
		notifyDataSetChanged();
	}


	public final class ViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.photo)
		ImageView photo;

		@BindView(R.id.creator)
		TextView creator;

		@BindView(R.id.body)
		TextView body;

		@BindView(R.id.date_created)
		TextView dateCreated;








		public ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);



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

		void onItemPhotoClick(View view, ImageView transitionView, int position);
	}



	//private Set<Integer> mSelectedItemSet = new HashSet<Integer>();

	public CommentsAdapter(Context context, ImageLoader imageLoader, Object tag) {
		this.imageLoader = imageLoader;
		this.tag = tag;
		setHasStableIds(true);
	}
	

	


	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_comment_item, parent, false);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {

		//StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
		//layoutParams.setFullSpan(true);

		final Comment item = getItem(position);

		holder.body.setText(Html.fromHtml(item.getBody()));
		holder.dateCreated.setText(TextUtils.formatTimestampRelative(item.getCreatedAt()));

		User creator = item.getCreator();
		if ( creator != null ) {
			holder.creator.setText(creator.getDisplayName());
			final String url = creator.getPhotoUrl();

			//holder.play.setVisibility(item.isVideo() ? View.VISIBLE : View.GONE);

			//final boolean hasPhotoSameRealm = mDataProvider.hasPhotoSameRealm(item);
			//holder.mImageViewLock.setVisibility( ac.getAccessType() != AccessType.Public ? View.VISIBLE : View.GONE);


			imageLoader.load(Uri.parse(url),
					holder.photo, ImageLoader.Transform.CIRCLE, tag);



		} else {
			if ( item.getCreatorName() != null ) {
				holder.creator.setText(item.getCreatorName());
			}
			holder.photo.setImageBitmap(null);
			holder.photo.setOnClickListener(null);
		}

		holder.photo.setOnClickListener(view -> {
			if (onItemIteractionListener != null) {
				onItemIteractionListener.onItemPhotoClick(holder.itemView, holder.photo, position);
			}
		});

		holder.itemView.setOnClickListener(view -> {
			if (onItemIteractionListener != null) {
				onItemIteractionListener.onItemClick(holder.itemView, holder.photo, position);
			}
		});

		holder.itemView.setOnLongClickListener(view -> onItemIteractionListener != null && onItemIteractionListener.onItemLongClick(view, position));



	}

	public Comment getItem(Integer position) {
		return commentPool != null ? commentPool.getItem(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId().hashCode();
	}

	@Override
	public int getItemCount() {
		return commentPool != null ? commentPool.getItemCount() : 0;
	}






}
