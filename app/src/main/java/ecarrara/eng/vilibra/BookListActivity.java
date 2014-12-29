package ecarrara.eng.vilibra;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class BookListActivity extends ActionBarActivity implements LendedBookListFragment.Callback {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(null != toolbar) {
            setSupportActionBar(toolbar);
        }

        if(findViewById(R.id.book_detail_container) != null) {
            // this container is only used for sw600dp if it is present then we have two panes.
            mTwoPane = true;

            // and we need to setup the detail fragment in the screen
            if(savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.book_detail_container, new LendedBookDetailFragment())
                        .commit();
            }
        } else {
            mTwoPane = false;
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
    public void onItemSelected(Uri selectedLending) {
        // Two pane mode should be handled here
        // For now just opening the detail
        if(mTwoPane) {
            loadBookLendingDetailFragment(selectedLending);
        } else {
            Intent intent = new Intent(this, LendedBookDetailActivity.class);
            intent.putExtra(LendedBookDetailActivity.EXTRA_KEY_BOOK_URI, selectedLending);
            startActivity(intent);
        }
    }

    @Override
    public void onEmptyList() {
        if(mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(ErrorMessageFragment.EXTRA_KEY_MESSAGE,
                    getString(R.string.empty_book_list_message));
            arguments.putString(ErrorMessageFragment.EXTRA_KEY_USER_ACTION,
                    getString(R.string.add_book_message));
            arguments.putInt(ErrorMessageFragment.EXTRA_KEY_IC_ID, R.drawable.ic_action_book_black);
            ErrorMessageFragment fragment = new ErrorMessageFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.book_detail_container, fragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onListLoaded(Uri currentLending) {
        if(mTwoPane) {
            loadBookLendingDetailFragment(currentLending);
        }
    }

    private void loadBookLendingDetailFragment(Uri lendingUri) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(LendedBookDetailActivity.EXTRA_KEY_BOOK_URI, lendingUri);
        LendedBookDetailFragment fragment = new LendedBookDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.book_detail_container, fragment)
                .commitAllowingStateLoss();
    }
}
