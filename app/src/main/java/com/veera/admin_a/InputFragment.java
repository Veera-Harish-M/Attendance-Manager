package com.veera.admin_a;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InputFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public InputFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InputFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InputFragment newInstance(String param1, String param2) {
        InputFragment fragment = new InputFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        final View view= inflater.inflate(R.layout.fragment_input, container, false);
        final EditText batch=view.findViewById(R.id.inp_batch);
        final EditText latecount=view.findViewById(R.id.inp_latecount);
        final EditText rfidno=view.findViewById(R.id.inp_rfidno);
        final EditText leavect=view.findViewById(R.id.inp_leavecount);
        Button add=view.findViewById(R.id.imp_add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=0;
                if(batch.getText().toString().isEmpty()){
                    batch.requestFocus();
                    batch.setError("Mandatory Field");
                }
                else if(latecount.getText().toString().isEmpty()){
                    latecount.requestFocus();
                    latecount.setError("Mandatory Field");
                }
                else if(rfidno.getText().toString().isEmpty()){
                    rfidno.requestFocus();
                    rfidno.setError("Mandatory Field");
                }else if(leavect.getText().toString().isEmpty()){
                    leavect.requestFocus();
                    leavect.setText("Mandatory Field");
                }
                else{
                    i=1;
                }
                if(i==1){
                    try {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        reference.child("Attendance").child(rfidno.getText().toString()).child("late_count").setValue(latecount.getText().toString());
                        reference.child("Attendance").child(rfidno.getText().toString()).child("leave_count").setValue(leavect.getText().toString());
                        reference.child("Attendance").child(rfidno.getText().toString()).child("batch").setValue(batch.getText().toString());
                        reference.child("batch").child(batch.getText().toString()).setValue(rfidno.getText().toString());
                        batch.setText("");
                        rfidno.setText("");
                        latecount.setText("");
                        Toast.makeText(getActivity(),"User Updated Successfully",Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(getActivity(),"Can't access Database Right NOW !",Toast.LENGTH_SHORT).show();
                    }

                    }
            }
        });

        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_attendance,menu);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

/*    @Override

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
