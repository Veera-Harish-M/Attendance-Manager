package com.veera.admin_a;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sql.StatementEvent;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AttendanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendanceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int[] flags={0,0,0,0};
    DatabaseReference refvk;
    String[] ssss,str_latecommer_abs,str_present,leaverequestt;
    ArrayList<String> b,a;
    String string,late_absss;
    private OnFragmentInteractionListener mListener;

    public AttendanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttendanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AttendanceFragment newInstance(String param1, String param2) {
        AttendanceFragment fragment = new AttendanceFragment();
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
        final View view= inflater.inflate(R.layout.fragment_attendance, container, false);
        final ListView lst_absent=view.findViewById(R.id.absent);
        final ListView lst_late=view.findViewById(R.id.latecome);
        final ListView lst_late_abs=view.findViewById(R.id.latecomeabs);
        final TextView txtv=view.findViewById(R.id.abs);
        final TextView txtlate=view.findViewById(R.id.latetv);
        final TextView txtlateabs=view.findViewById(R.id.latetvabs);
        final TextView tvleavereq=view.findViewById(R.id.tvleavereq);
        final ListView leavesreq=view.findViewById(R.id.leavereqlist);
        FloatingActionButton floatingActionButton=view.findViewById(R.id.fab);
        final String[] strength={ "201108", "201103", "201107", "201110"};


        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    a = new ArrayList<>();
                    b = new ArrayList<>();

                    try {
                        str_present = dataSnapshot.child("present").getValue().toString().replace("{", "").replace("}", "").split(",");
                        for (String s : str_present) {
                            a.add(s.substring(0, s.indexOf('=')));

                        }

                        List<String> str = new ArrayList<String>();
                        str = Arrays.asList(strength);

                        for (String s : str) {
                            if (!a.contains(s)) {
                                b.add(s);
                            }
                        }

                        txtv.setText("Absentees-" + b.size());


                    }catch (Exception e){

                        txtv.setText("Absentees-0");
                        flags[0]=1;
                    }

                     try {
                        late_absss = dataSnapshot.child("latecomer_absent").getValue().toString();
                        str_latecommer_abs = late_absss.replace("=", "              ").replace("{", "").replace("}", "").split(",");

                            int i = 0;
                            String temp;
                            for (String s : str_latecommer_abs) {
                                if (!s.isEmpty()) {
                                    i++;

                                    temp=s.substring(0, s.indexOf('='));
                                    if (b.contains(temp)) {
                                        b.remove(temp);
                                    }
                                }
                            }

                            txtlateabs.setText("Late Comers Absent-" + String.valueOf(i));
                            final ArrayAdapter<String> madapt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, str_latecommer_abs);
                            lst_late_abs.setAdapter(madapt);


                    }catch (Exception e){
                            txtlateabs.setText("Late Comers Absent-0");
                            flags[1]=1;
                     }

                     try {
                         string = dataSnapshot.child("latecomer").getValue().toString();
                         ssss = string.replace("=", "              ").replace("{", "").replace("}", "").split(",");

                         int i = 0;
                         String temp;
                         for (String s : ssss) {
                             if (!s.isEmpty()) {
                                 i++;
                                 temp=s.substring(0, s.indexOf('='));
                                 if (b.contains(temp)) {
                                     b.remove(temp);
                                 }

                             }

                         }

                         txtlate.setText("Late Comers-" + String.valueOf(i));
                         final ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ssss);
                         lst_late.setAdapter(mAdapter);


                     }catch (Exception e){
                         txtlate.setText("Late Comers-0");
                        flags[2]=1;
                     }


                     try {
                         leaverequestt = dataSnapshot.child("leave_request").getValue().toString().replace("{", "").replace("}", "").replace("=", "         ").split(",");

                         int i = 0;
                         for (String s : leaverequestt) {
                             if (!s.isEmpty()) {
                                 i++;
                             }

                         }

                         tvleavereq.setText("Leave Request-" + String.valueOf(i));
                         final ArrayAdapter<String> mad = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, leaverequestt);
                         leavesreq.setAdapter(mad);


                     }catch (Exception e){

                         tvleavereq.setText("Leave Request-0");
                         flags[3]=1;
                     }

                     try{final ArrayAdapter<String> madap = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, b);
                         lst_absent.setAdapter(madap);
                     }catch (Exception e){
                         Toast.makeText(getActivity(),"DB Failure",Toast.LENGTH_SHORT).show();
                     }



                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Cant access database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final ArrayList<String> c=new ArrayList<>();
                final ArrayList<String> d=new ArrayList<>();

                String[] latee=string.replace("{","").replace("}","").split(",");

                if(flags[2]!=1) {
                    for (String sam : latee) {
                        c.add(sam.substring(0, sam.indexOf('=')));

                    }
                }

                if(flags[1]!=1) {
                    String[] s = late_absss.replace("{", "").replace("}", "").split(",");
                    for (String sam : s) {
                        d.add(sam.substring(0, sam.indexOf('=')));

                    }
                }



                DatabaseReference rrrr=FirebaseDatabase.getInstance().getReference();
                    try {
                        rrrr.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {


                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Alert!");
                                builder.setMessage("Are you sure..You want to Send Message");

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        if(flags[0]!=1)
                                        {
                                            for(String sss:b){
                                                String ph = dataSnapshot.child("login").child(sss).child("phone").getValue().toString();
                                                SmsManager smsManager = SmsManager.getDefault();
                                                smsManager.sendTextMessage(ph, null, "Your son is Absent to College.\nMake Sure this won't happen Again.", null, null);
                                            }
                                        }

                                        if(flags[2]!=1){
                                            for (String sss :c) {
                                                String ph = dataSnapshot.child("login").child(sss).child("phone").getValue().toString();
                                                String time = dataSnapshot.child("latecomer").child(sss).getValue().toString();
                                                SmsManager smsManager = SmsManager.getDefault();
                                                smsManager.sendTextMessage(ph, null, "Your son is LATE to College.\nMake Sure this won't happen Again.\nEntry Time:" + time, null, null);
                                            }
                                        }

                                        if(flags[1]!=1){
                                            for (String sss :d) {
                                                String ph = dataSnapshot.child("login").child(sss).child("phone").getValue().toString();
                                                String time = dataSnapshot.child("latecomer_absent").child(sss).getValue().toString();
                                                SmsManager smsManager = SmsManager.getDefault();
                                                smsManager.sendTextMessage(ph, null, "Your son has been Noted for three consecutive LATE to College.\nMake Sure this won't happen Again.\nEntry Time:" + time, null, null);

                                            }
                                        }
                                        Toast.makeText(getActivity(),"Message Sent Successfully",Toast.LENGTH_SHORT).show();


                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                builder.show();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }catch (Exception e){
                        Toast.makeText(getActivity(),"Database Failure",Toast.LENGTH_SHORT).show();
                    }
            }
        });


        lst_absent.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });


        leavesreq.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        lst_late.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        lst_late_abs.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });


        leavesreq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String[] Items=((TextView)view).getText().toString().split("         ");
                final String Item=Items[0].replace(" ","");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Alert!");
                builder.setMessage("Are you sure..You want to Approve Leave to "+Item);


                ImageView image = new ImageView(getActivity());
                image.setImageResource(getResources().getIdentifier("@drawable/a"+Item, "drawable", getActivity().getPackageName()));


                builder.setView(image);



                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            refvk = FirebaseDatabase.getInstance().getReference();
                            refvk.child("leave_request").child(Item).removeValue();
                        }catch (Exception e){
                            Toast.makeText(getActivity(),"Database Error",Toast.LENGTH_SHORT).show();
                        }

                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try{
                                    String rf=dataSnapshot.child("batch").child(Item).getValue().toString();
                                    String lct=dataSnapshot.child("Attendance").child(rf).child("leave_count").getValue().toString();
                                    lct=String.valueOf(Integer.parseInt(lct)+1);
                                    refvk.child("Attendance").child(rf).child("leave_count").setValue(lct);

                                    String ph=dataSnapshot.child("login").child(Item).child("phone").getValue().toString();

                                    Toast.makeText(getActivity(), "Success👍", Toast.LENGTH_SHORT).show();

                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(ph, null, "Your leave request has been approved", null, null);

                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        ft.setReorderingAllowed(false);
                                    }
                                    ft.detach(AttendanceFragment.this).attach(AttendanceFragment.this).commit();



                                }catch (Exception e){
                                    Toast.makeText(getActivity(),"Cant access database",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                Toast.makeText(getActivity(),"Cant access database",Toast.LENGTH_SHORT).show();
                            }
                        });



                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();



                    }
        });



        lst_late_abs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String[] Items=((TextView)view).getText().toString().split("              ");
                final String Item=Items[0].replace(" ","");

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Alert!");
                builder.setMessage("Are you sure..You want to provide Attendance to "+Item);


                ImageView image = new ImageView(getActivity());
                image.setImageResource(getResources().getIdentifier("@drawable/a"+Item, "drawable", getActivity().getPackageName()));


                builder.setView(image);



                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            ref.child("latecomer_absent").child(Item).removeValue();
                        }catch (Exception e){
                            Toast.makeText(getActivity(),"Cant access database",Toast.LENGTH_SHORT).show();
                        }
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("batch").child(Item);

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                try {
                                    Date d=new Date();
                                    CharSequence dt = DateFormat.format("d-MM-yyyy", d.getTime());
                                    CharSequence dtt = DateFormat.format("HH:mm", d.getTime());
                                    DatabaseReference dd=FirebaseDatabase.getInstance().getReference();
                                    String str = dataSnapshot.getValue().toString();
                                    dd.child("Attendance").child(str).child(dt.toString()).child("location").child(dtt.toString()).setValue("Hod_ROOM");
                                    dd.child("Attendance").child(str).child("late_count").setValue("0");
                                    dd.child("present").child(Item).setValue(dtt.toString());
                                    Toast.makeText(getActivity(), "Success👍", Toast.LENGTH_SHORT).show();

                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        ft.setReorderingAllowed(false);
                                    }
                                    ft.detach(AttendanceFragment.this).attach(AttendanceFragment.this).commit();


                                }catch (Exception e){
                                    Toast.makeText(getActivity(),"Invalid Batch Number",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getActivity(), "Can't access database😕", Toast.LENGTH_SHORT).show();
                            }
                        });





                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                    }
                });

                builder.show();

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

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

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
