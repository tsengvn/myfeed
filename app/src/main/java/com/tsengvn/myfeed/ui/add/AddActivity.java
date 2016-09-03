package com.tsengvn.myfeed.ui.add;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.tsengvn.myfeed.R;
import com.tsengvn.myfeed.internal.di.component.AppComponent;
import com.tsengvn.myfeed.pojo.Image;
import com.tsengvn.myfeed.ui.base.BaseActivity;
import com.tsengvn.myfeed.ui.base.BasePresenter;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author : hienngo
 * @since : Sep 01, 2016.
 */
public class AddActivity extends BaseActivity implements AddView {
    private static final int REQ_PHOTO = 1;

    @Inject
    AddPresenter presenter;

    @BindView(R.id.input)
    EditText inputTextView;

    @BindView(R.id.image)
    ImageView imageView;

    private Image image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void initInjector(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
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
            presenter.savePost(inputTextView.getText().toString(), image != null ? image.getLink() : null);
            setResult(RESULT_OK);
            finish();
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
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void openGallery() {
    }
}