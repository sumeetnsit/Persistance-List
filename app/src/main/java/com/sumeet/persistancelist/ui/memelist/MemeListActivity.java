package com.sumeet.persistancelist.ui.memelist;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sumeet.persistancelist.R;
import com.sumeet.persistancelist.data.Meme;
import com.sumeet.persistancelist.ui.meme_detail.MemeDetailFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public class MemeListActivity extends AppCompatActivity
        implements MemeListFragment.MemeListFragmentInteractionListener,
        MemeDetailFragment.MemeDetailFragmentInteractionListener {

    private int backPressedCountInListing;
    @Nullable
    private Disposable backPressDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meme_list_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MemeListFragment.newInstance())
                    .commitNow();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.estonia_white)));
    }

    @Override
    public void onCardClicked(@NonNull Meme meme) {
        if (meme.getName() != null &&
                meme.getUrl() != null) {

            if (getWindow() != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, MemeDetailFragment.newInstance(meme.getUrl(), meme.getName()))
                    .addToBackStack(MemeDetailFragment.class.getSimpleName())
                    .commit();
        }
    }

    @Override
    public void setToolbar(@NonNull String memeName) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(memeName);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager() != null &&
                getSupportFragmentManager().findFragmentById(R.id.container)
                        instanceof MemeDetailFragment) {
            setToolbar(getString(R.string.memes));
        } else if (backPressedCountInListing == 0) {
            backPressedCountInListing++;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            backPressDisposable =
                    Single.just("delay")
                            .delay(1500, TimeUnit.MILLISECONDS)
                            .doOnSuccess(__ -> backPressedCountInListing = 0).subscribe();
            return;
        }

        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backPressDisposable != null) {
            backPressDisposable.dispose();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
