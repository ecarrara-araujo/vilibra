package br.eng.ecarrara.vilibra;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import br.eng.ecarrara.vilibra.R;
import br.eng.ecarrara.vilibra.utils.Utility;

public class LendedBookRegistrationActivity extends AppCompatActivity
        implements LendedBookRegistrationFragment.Callback{

    private static final String LOG_TAG = LendedBookRegistrationActivity.class.getSimpleName();

    private boolean mInstanceStateRestored;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lended_book_registration);

        mInstanceStateRestored = false;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mInstanceStateRestored = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utility.isConnectedToNetwork(this)) {
            if(!mInstanceStateRestored) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new LendedBookRegistrationFragment())
                        .commit();
            }
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new OfflineMessageFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lended_book_registration, menu);
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
    public void onError(String message) {
        Bundle arguments = new Bundle();
        arguments.putString(ErrorMessageFragment.EXTRA_KEY_MESSAGE, message);
        ErrorMessageFragment fragment = new ErrorMessageFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
