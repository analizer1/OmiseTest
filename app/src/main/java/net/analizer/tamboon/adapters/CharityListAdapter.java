package net.analizer.tamboon.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import net.analizer.domainlayer.models.Charity;
import net.analizer.tamboon.R;
import net.analizer.tamboon.databinding.LayoutCharityItemBinding;

import java.util.List;

public class CharityListAdapter extends RecyclerView.Adapter<CharityListAdapter.CharityItemHolder> {

    private List<Charity> mCharityList;
    private View.OnClickListener onClickListener;
    private OnCharityClickListener mOnCharityClickListener;

    public CharityListAdapter(@NonNull List<Charity> charityList) {
        this.mCharityList = charityList;
        this.onClickListener = view -> {
            Charity charity = (Charity) view.getTag();
            if (mOnCharityClickListener != null) {
                mOnCharityClickListener.onCharityClicked(charity);
            }
        };
    }

    @Override
    public CharityItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        LayoutCharityItemBinding itemBinding =
                LayoutCharityItemBinding.inflate(layoutInflater, parent, false);

        itemBinding.getRoot().setOnClickListener(onClickListener);
        return new CharityItemHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(CharityItemHolder holder, int position) {
        holder.bind();

        Charity charity = mCharityList.get(position);
        holder.itemBinding.getRoot().setTag(charity);
        holder.itemBinding.charityNameTextView.setText(charity.getCharityName());

        Picasso.with(holder.itemBinding.charityProfileImageView.getContext())
                .load(charity.logoUrl)
                .error(R.drawable.ic_launcher_background)
                .into(holder.itemBinding.charityProfileImageView);
    }

    @Override
    public int getItemCount() {
        return mCharityList != null ? mCharityList.size() : 0;
    }

    public void clear() {
        if (mCharityList != null) {
            mCharityList.clear();
        }
    }

    public void setOnClickListener(@Nullable OnCharityClickListener charityClickListener) {
        this.mOnCharityClickListener = charityClickListener;
    }

    public interface OnCharityClickListener {
        void onCharityClicked(Charity charity);
    }

    static class CharityItemHolder extends RecyclerView.ViewHolder {
        LayoutCharityItemBinding itemBinding;

        public CharityItemHolder(LayoutCharityItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bind() {
            itemBinding.executePendingBindings();
        }
    }
}
