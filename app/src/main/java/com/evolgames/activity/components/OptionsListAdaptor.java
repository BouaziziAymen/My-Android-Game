package com.evolgames.activity.components;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.gameengine.R;
import com.evolgames.userinterface.view.inputs.Button;

import java.util.ArrayList;
import java.util.List;

class OptionsListAdaptor extends RecyclerView.Adapter<OptionsListAdaptor.OptionViewHolder> {
    private final OptionButtonClickListener listener;
    private final List<OptionViewHolder> viewHolderList = new ArrayList<>();
    private List<PlayerSpecialAction> playerSpecialActionList;
    private PlayerSpecialAction selectedAction;

    public OptionsListAdaptor(OptionButtonClickListener listener) {
        this.playerSpecialActionList = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public OptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OptionButton imageButton = new OptionButton(parent.getContext());
        return new OptionViewHolder(imageButton, listener);
    }

    @Override
    public void onBindViewHolder(OptionViewHolder holder, int position) {
        PlayerSpecialAction playerSpecialAction = playerSpecialActionList.get(position);
        holder.button.setState(selectedAction == playerSpecialActionList.get(position) ? Button.State.PRESSED : Button.State.NORMAL);
        holder.itemImage.setImageResource(playerSpecialAction.iconId);
        viewHolderList.add(holder);
    }

    @Override
    public int getItemCount() {
        return playerSpecialActionList.size();
    }

    // Inside MyAdapter class
    public void setPlayerSpecialActionList(List<PlayerSpecialAction> newImageList, PlayerSpecialAction selectedAction) {
        // Calculate the difference between the old and new lists
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCallback(playerSpecialActionList, newImageList));
        this.playerSpecialActionList = newImageList;
        this.selectedAction = selectedAction;
        result.dispatchUpdatesTo(this); // Dispatch the specific change events to the adapter
    }

    public void setSelectedAction(PlayerSpecialAction selectedAction) {
        this.selectedAction = selectedAction;
    }

    @Override
    public void onViewRecycled(OptionViewHolder holder) {
        super.onViewRecycled(holder);
        // Remove the recycled ViewHolder instance from the list
        viewHolderList.remove(holder);

    }

    private static class DiffCallback extends DiffUtil.Callback {
        private final List<PlayerSpecialAction> oldList;
        private final List<PlayerSpecialAction> newList;

        public DiffCallback(List<PlayerSpecialAction> oldList, List<PlayerSpecialAction> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            // Implement this method to check if the same item in the old and new lists
            // represents the same object.
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            // Implement this method to check if the contents of the same item
            // in the old and new lists are the same.
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }


    class OptionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final OptionButtonClickListener listener;
        public ImageView itemImage;
        public OptionButton button;

        public OptionViewHolder(View itemView, OptionButtonClickListener listener) {
            super(itemView);
            this.button = (OptionButton) itemView;
            this.itemImage = itemView.findViewById(R.id.icon);
            this.listener = listener;
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            for (OptionViewHolder other : viewHolderList) {
                if (other != this) {
                    other.button.setState(Button.State.NORMAL);
                }
            }
            if(getBindingAdapterPosition()>=0&&getBindingAdapterPosition()<playerSpecialActionList.size()) {
                PlayerSpecialAction action = playerSpecialActionList.get(getBindingAdapterPosition());
                listener.onClick(button, action);
            }
        }
    }

}
