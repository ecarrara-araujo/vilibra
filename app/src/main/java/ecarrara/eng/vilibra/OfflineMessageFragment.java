package ecarrara.eng.vilibra;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ecarrara on 26/12/2014.
 */
public class OfflineMessageFragment extends Fragment {

    public OfflineMessageFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_offline_message, container, false);
        return rootView;
    }
}
