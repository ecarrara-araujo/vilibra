package ecarrara.eng.vilibra;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class BookListActivity extends ActionBarActivity implements LendedBookListFragment.Callback {

    private boolean mTwoPane;

    private static final String CURRENT_LENDING_KEY = "current_lending";
    private Uri mCurrentLending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(null != toolbar) {
            setSupportActionBar(toolbar);
        }

        if(findViewById(R.id.book_fragment_container) != null) {
            // this container is only used for sw600dp if it is present then we have two panes.
            mTwoPane = true;
        } else {
            mTwoPane = false;
            if(savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.common_fragment_container, new LendedBookListFragment())
                        .commit();
            }
        }

        if(savedInstanceState != null && savedInstanceState.containsKey(CURRENT_LENDING_KEY)) {
            mCurrentLending = savedInstanceState.getParcelable(CURRENT_LENDING_KEY);
            if(mTwoPane) {
                LendedBookDetailFragment fragment = LendedBookDetailFragment.newInstance(mCurrentLending);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.book_fragment_container, fragment)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.common_fragment_container, new LendedBookListFragment())
                        .commit();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(mCurrentLending != null) {
            outState.putParcelable(CURRENT_LENDING_KEY, mCurrentLending);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(Uri selectedLending) {
        mCurrentLending = selectedLending;
        LendedBookDetailFragment fragment = LendedBookDetailFragment.newInstance(mCurrentLending);

        if(mTwoPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.book_fragment_container, fragment)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.common_fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }


}
