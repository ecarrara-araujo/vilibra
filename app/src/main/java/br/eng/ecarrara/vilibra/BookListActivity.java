package br.eng.ecarrara.vilibra;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import br.eng.ecarrara.vilibra.LendedBookListFragment;
import br.eng.ecarrara.vilibra.LoanedBookDetailFragment;
import br.eng.ecarrara.vilibra.SettingsActivity;
import br.eng.ecarrara.vilibra.android.presentation.LoanedBookListAdapter;
import br.eng.ecarrara.vilibra.notification.BookLendingNotificationService;

import static android.Manifest.permission.READ_CONTACTS;
import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;


public class BookListActivity extends AppCompatActivity
        implements LoanedBookListAdapter.OnItemClickListener {

    private static final String WAS_CHECKING_FOR_PERMISSION = "was_checking_for_permission";
    private static final int CONTACTS_PERMISSION_REQUEST_ID = 2000;

    private boolean mTwoPane;
    private boolean wasCheckingForPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            wasCheckingForPermission = savedInstanceState
                    .getBoolean(WAS_CHECKING_FOR_PERMISSION, false);
        }

        if(hasContactsReadPermission()) {
           setUpUI();
        } else {
            wasCheckingForPermission = true;
            ActivityCompat.requestPermissions(this,
                    new String[]{READ_CONTACTS}, CONTACTS_PERMISSION_REQUEST_ID);
        }

    }

    private boolean hasContactsReadPermission() {
        return PermissionChecker.checkCallingOrSelfPermission(this, READ_CONTACTS)
                == PERMISSION_GRANTED;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(WAS_CHECKING_FOR_PERMISSION, wasCheckingForPermission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        wasCheckingForPermission = false;
        if(requestCode == CONTACTS_PERMISSION_REQUEST_ID) {
            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                setUpUI();
            }
        }
    }

    private void setUpUI() {
        setContentView(R.layout.activity_book_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if(null != toolbar) {
            setSupportActionBar(toolbar);
        }

        if(findViewById(R.id.book_fragment_container) != null) {
            // this container is only used for sw600dp if it is present then we have two panes.
            mTwoPane = true;
        } else {
            mTwoPane = false;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.common_fragment_container, new LendedBookListFragment())
                    .commitAllowingStateLoss();
        }

        // Start the service once the app is opened. It will execute once and re schedule itself
        // to execute later.
        // TODO: Check for a more effective way to do this
        startService(new Intent(this, BookLendingNotificationService.class));
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        //else if (id == R.id.action_notify_user){
        //    //TODO: Action added for testing purposes comment it out before release
        //    Intent intent = new Intent(this, BookLendingNotificationService.class);
        //    this.startService(intent);
        //}

        return super.onOptionsItemSelected(item);
    }

    
    @Override public void onUserItemClicked(Uri lendingUri) {
        LoanedBookDetailFragment fragment = LoanedBookDetailFragment.newInstance(lendingUri);

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
