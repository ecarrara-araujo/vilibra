package ecarrara.eng.vilibra;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ecarrara on 26/12/2014.
 */
public class ErrorMessageFragment extends Fragment {

    private final String LOG_TAG = ErrorMessageFragment.class.getSimpleName();

    public static final String EXTRA_KEY_MESSAGE = "message";
    public static final String EXTRA_KEY_USER_ACTION = "user_action";
    public static final String EXTRA_KEY_IC_ID = "image_id";

    private TextView mMessageTextView;
    private TextView mUserActionTextView;
    private ImageView mErrorIcon;

    public static ErrorMessageFragment newInstance(int imageId, String message, String userAction) {
        Bundle arguments = new Bundle();
        arguments.putString(ErrorMessageFragment.EXTRA_KEY_MESSAGE, message);
        arguments.putString(ErrorMessageFragment.EXTRA_KEY_USER_ACTION, userAction);
        arguments.putInt(ErrorMessageFragment.EXTRA_KEY_IC_ID, imageId);
        ErrorMessageFragment fragment = new ErrorMessageFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public ErrorMessageFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_error_message, container, false);

        int imageId = getArguments().getInt(EXTRA_KEY_IC_ID, R.drawable.ic_action_warning);
        mErrorIcon = (ImageView) rootView.findViewById(R.id.error_icon_image_view);
        mErrorIcon.setImageResource(imageId);

        String errorMessage = getArguments().getString(EXTRA_KEY_MESSAGE,
                getString(R.string.default_error_message));
        mMessageTextView = (TextView) rootView.findViewById(R.id.message_text_view);
        mMessageTextView.setText(errorMessage);

        String userAction = getArguments().getString(EXTRA_KEY_USER_ACTION, "");
        mUserActionTextView = (TextView) rootView.findViewById(R.id.user_action_text_view);
        mUserActionTextView.setText(userAction);

        return rootView;
    }

}
