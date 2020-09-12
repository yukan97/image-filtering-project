package s13742.yuliia_kanarovska.imagefilteringapp.utils;

import android.content.Context;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.List;

import s13742.yuliia_kanarovska.imagefilteringapp.R;

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.FiltersAdapterViewHolder> {

    private List<ThumbnailModel> thumbnails_list;
    private final FiltersAdapterOnClickHandler myClickHandler;

    public interface FiltersAdapterOnClickHandler {
        void onClick(Filter filter);
    }

    public FiltersAdapter(List<ThumbnailModel> thumbnails_list, FiltersAdapterOnClickHandler clickHandler ){
        this.thumbnails_list = thumbnails_list;
        this.myClickHandler = clickHandler;
    }

    public class FiltersAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener{
        public final View thumbnail_view;


        public FiltersAdapterViewHolder(View view) {
            super(view);
            thumbnail_view = view;
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            ThumbnailModel thumbnail = thumbnails_list.get(adapterPosition);
            myClickHandler.onClick(thumbnail.filter);

        }
    }

    @NonNull
    @Override
    public FiltersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.miniatures_scroll_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new FiltersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FiltersAdapterViewHolder holder, final int position) {
        ThumbnailModel thumbnail = thumbnails_list.get(position);
        ((TextView)holder.thumbnail_view.findViewById(R.id.filter_title)).setText(thumbnail.filterTitle);
        ((ImageView)holder.thumbnail_view.findViewById(R.id.thumbnail)).setImageBitmap(thumbnail.img);

    }

    @Override
    public int getItemCount() {
        if(thumbnails_list == null) return 0;
        return thumbnails_list.size();
    }


}
