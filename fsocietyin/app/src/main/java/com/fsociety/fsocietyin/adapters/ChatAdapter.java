package com.fsociety.fsocietyin.adapters;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fsociety.fsocietyin.R;
import com.fsociety.fsocietyin.models.msg_model;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;




public class ChatAdapter extends RecyclerView.Adapter {
    static String CHANNEL_ID = "msgchannel";
    static int  NOT_ID = 100;
    ArrayList<msg_model>  Messages;
    Context context;
    int SENDER_VIEW_TYPE = 1;
    int RECIEVER_VIEW_TYPE = 2;
    public ChatAdapter(ArrayList<msg_model>  Messages , Context context) {
        this. Messages  =  Messages;
        this.context = context;
    }





    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==SENDER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.send_layout,parent, false);
            return new senderViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.recive_layout,parent, false );
            return new recieverViewHolder(view);
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        msg_model message = Messages.get(position);
        if(holder.getClass() == senderViewHolder.class)
        {   //code for sender view holder
            String enc_msg = message.getMsg();
            byte[] actualByte = Base64.getDecoder()
                    .decode(enc_msg);

            String actualString = new String(actualByte);

            ((senderViewHolder) holder).senderMessage.setText(actualString);
        }
        else
        {
            // code for reciver holder
            String enc_msg = message.getMsg();
            byte[] actualByte = Base64.getDecoder()
                    .decode(enc_msg);

            String actualString = new String(actualByte);
            ((recieverViewHolder) holder).recieverMessage.setText(actualString);
        }
    }

    @Override
    public int getItemCount() {
        return Messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(Messages.get(position).getSenderId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_VIEW_TYPE;
        }
        else
        {
            return RECIEVER_VIEW_TYPE;
        }
    }

    public class senderViewHolder extends RecyclerView.ViewHolder{
        TextView senderMessage;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage = itemView.findViewById(R.id.sendtextview);

        }
    }

    public class recieverViewHolder extends RecyclerView.ViewHolder{
        TextView recieverMessage;
        public recieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieverMessage = itemView.findViewById(R.id.recivetexview);

        }
    }
}