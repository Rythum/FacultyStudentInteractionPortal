package com.iitg.interaction.facultystudentinteractionportal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.time.format.TextStyle;
import java.util.List;

public class MessageListAdaptor extends ArrayAdapter<Messages> {
    private static final String TAG = "MessageListAdaptor";
    private Context mContext;
    private int mResource;

    public MessageListAdaptor(Context context, int resource, List<Messages> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }




    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(getItem(position)!=null) {

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
            final String sender = getItem(position).sender;
            final String receiver = getItem(position).receiver;
            final String subject = getItem(position).subject;
            final String body = getItem(position).body;
            final String date = getItem(position).date;
            final  String uniqueid = getItem(position).uniquid;
            final boolean read = getItem(position).read;

            String msgdirection;

            //Messages msg = new Messages(sender,receiver,subject,body);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            TextView sendertv = convertView.findViewById(R.id.tv_sender);
            TextView receivertv = convertView.findViewById(R.id.tv_receiver);
            TextView subjecttv = convertView.findViewById(R.id.tv_subject);
            TextView datetimetv = convertView.findViewById(R.id.tv_datetime);
            TextView bodytv = convertView.findViewById(R.id.tv_body);
            final TextView msgdirectiontv = convertView.findViewById(R.id.tv_msgdirection);
            String[] arrdate = date.split(" ");
            sendertv.setText(sender);
            receivertv.setText("To: "+receiver);
            subjecttv.setText(subject);
            datetimetv.setText(arrdate[0]+" "+arrdate[1]+ " "+arrdate[2]+" "+arrdate[3].substring(0,5));

            bodytv.setText(body);
            if (UserInfo.username.equals(sender)) {
                msgdirectiontv.setText("Sent");
                msgdirectiontv.setTextColor(Color.BLUE);

            } else {
                msgdirectiontv.setText("Received");
                msgdirectiontv.setTextColor(Color.rgb(34,139,34));
            }

            if(!read && !sender.equals(UserInfo.username)){
                datetimetv.setTextColor(Color.BLUE);
                datetimetv.setTypeface(Typeface.DEFAULT_BOLD);
                sendertv.setTypeface(Typeface.DEFAULT_BOLD);
                sendertv.setTextColor(Color.BLACK);
                subjecttv.setTypeface(Typeface.DEFAULT_BOLD);
                subjecttv.setTextColor(Color.BLACK);
            }
            else
            {
                datetimetv.setTextColor(Color.DKGRAY);
                datetimetv.setTypeface(Typeface.DEFAULT);
                sendertv.setTypeface(Typeface.DEFAULT);
                subjecttv.setTypeface(Typeface.DEFAULT);
                subjecttv.setTextColor(Color.DKGRAY);
                sendertv.setTextColor(Color.DKGRAY);



            }


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getItem(position)!=null)
                    {
                        getItem(position).read=true;
                        databaseReference.child(UserInfo.username).child("messages").child(uniqueid).setValue(getItem(position));

                        Intent intent = new Intent(getContext(), messageboxActivity.class);
                        intent.putExtra("subject", subject);
                        intent.putExtra("msgdirection", msgdirectiontv.getText().toString());
                        intent.putExtra("body", body);
                        intent.putExtra("sender", sender);
                        intent.putExtra("receiver", receiver);
                        intent.putExtra("datetime", date);
                        intent.putExtra("uniqueid",uniqueid);
                        intent.putExtra("id", position);
                        Log.d("debug","clicked on position "+position);

                        mContext.startActivity(intent);
                    }

                }
            });


        }
        return convertView;

    }



}
