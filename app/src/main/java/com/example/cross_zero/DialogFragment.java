package com.example.cross_zero;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class DialogFragment extends Fragment {

    public static final String TAG = "DialogFragment";
    public static final String SIDE_ITEM = "side item";
    private DialogFragment dialogFragment;
    private View root;
    private Button exit, again;
    private TextView winner;
    private String side;
    private OnDialogClickListener onDialogClickListener;


    public DialogFragment(String side, OnDialogClickListener onDialogClickListener){
        this.side = side;
        this.onDialogClickListener = onDialogClickListener;
        dialogFragment = this;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.alert_dialog_activity, container, false);
        winner = root.findViewById(R.id.winner);
        winner.setText(side);

        exit = root.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDialogClickListener.onExitClick();
                getActivity().getSupportFragmentManager().beginTransaction().remove(dialogFragment).commit();
            }
        });
        again = root.findViewById(R.id.again);
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDialogClickListener.onAgainClick();
                getActivity().getSupportFragmentManager().beginTransaction().remove(dialogFragment).commit();
            }
        });
        return root;
    }




    interface OnDialogClickListener {
        void onExitClick();
        void onAgainClick();
    }

}