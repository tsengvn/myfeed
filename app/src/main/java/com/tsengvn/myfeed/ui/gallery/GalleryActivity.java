package com.tsengvn.myfeed.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.tsengvn.myfeed.R;
import com.tsengvn.myfeed.internal.di.component.AppComponent;
import com.tsengvn.myfeed.pojo.Image;
import com.tsengvn.myfeed.ui.base.BaseActivity;
import com.tsengvn.myfeed.ui.widget.FixedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author : hienngo
 * @since : Sep 03, 2016.
 */
public class GalleryActivity extends BaseActivity<GalleryPresenter> implements GalleryView{
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getPresenter().loadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().onDestroy();
    }

    @Override
    protected void initInjector(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public void onReceiveData(final List<Image> images, boolean refresh) {
        createAdapter();

        ImageAdapter imageAdapter = (ImageAdapter) recyclerView.getAdapter();
        if (refresh) {
            imageAdapter.images.clear();
        }
        imageAdapter.images.addAll(images);
        imageAdapter.notifyDataSetChanged();
    }

    private void createAdapter() {
        if (recyclerView.getAdapter() == null) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            recyclerView.setAdapter(new ImageAdapter());
        }
    }

    class ImageAdapter extends RecyclerView.Adapter<ViewHolder>{
        private List<Image> images;

        public ImageAdapter() {
            this.images = new ArrayList<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_image, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Image image = images.get(position);
            holder.imageView.setRatio(image.getRatio());

            Picasso.with(getApplicationContext()).load(image.getThumbnail()).fit().into(holder.imageView);

            if (position == images.size()-1) {
                getPresenter().loadMore();
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent data = new Intent();
                    data.putExtra("image", image);
                    setResult(RESULT_OK, data);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return images.size();
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        FixedImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = ButterKnife.findById(itemView, R.id.imageView);
        }
    }
}
