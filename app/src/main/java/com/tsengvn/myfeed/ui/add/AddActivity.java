package com.tsengvn.myfeed.ui.add;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.squareup.picasso.Picasso;
import com.tsengvn.myfeed.R;
import com.tsengvn.myfeed.internal.di.component.AppComponent;
import com.tsengvn.myfeed.pojo.Image;
import com.tsengvn.myfeed.ui.base.BaseActivity;
import com.tsengvn.myfeed.ui.gallery.GalleryActivity;
import com.tsengvn.myfeed.ui.widget.FixedImageView;

import butterknife.BindView;

/**
 * @author : hienngo
 * @since : Sep 01, 2016.
 */
public class AddActivity extends BaseActivity<AddPresenter> implements AddView {
    private static final int REQ_PHOTO = 1;

    @BindView(R.id.input)
    EditText inputTextView;

    @BindView(R.id.image)
    FixedImageView imageView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Image image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void initInjector(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.action_save) {
            getPresenter().savePost(inputTextView.getText().toString(),
                    image != null ? image.getLink() : null,
                    image != null ? image.getRatio() : 0);
            return true;
        } else if (itemId == R.id.action_photo) {
            openGallery();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_PHOTO && resultCode == RESULT_OK) {
            image = data.getParcelableExtra("image");
            imageView.setRatio(image.getRatio());
            Picasso.with(this).load(image.getLink()).fit().into(imageView);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void openGallery() {
        startActivityForResult(new Intent(this, GalleryActivity.class), REQ_PHOTO);
    }

    @Override
    public void onPostAdded() {
        setResult(RESULT_OK);
        finish();
    }
}
