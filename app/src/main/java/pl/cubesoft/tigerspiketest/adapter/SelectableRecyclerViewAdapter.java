package pl.cubesoft.tigerspiketest.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import java.util.ArrayList;

/**
 * Created by Dominik Tomczak on 21/03/2016.
 */
public abstract class SelectableRecyclerViewAdapter<VH extends RecyclerView.ViewHolder>  extends RecyclerView.Adapter<VH> {

    // …
    private final SparseBooleanArray selectedItems = new SparseBooleanArray();

    // …

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);


    }

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        }
        else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public void selectAllItems() {
        for ( int i=0; i<getItemCount(); ++i) {
            selectedItems.put(i, true);
        }
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public ArrayList<Integer> getSelectedItems() {
        ArrayList<Integer> items =
                new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void setSelectedItems(ArrayList<Integer> items) {
        selectedItems.clear();

        for (Integer item : items ) {
            selectedItems.append(item, true);
        }

        notifyDataSetChanged();
    }

    public boolean isItemSelected(int position) {
        return selectedItems.get(position);
    }


}
