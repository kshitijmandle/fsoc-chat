package com.fsociety.fsocietyin.adapters;



import static android.content.Context.NOTIFICATION_SERVICE;

import static com.fsociety.fsocietyin.adapters.ChatAdapter.CHANNEL_ID;
import static com.fsociety.fsocietyin.adapters.ChatAdapter.NOT_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fsociety.fsocietyin.Activies.ChatActivity;
import com.fsociety.fsocietyin.R;
import com.fsociety.fsocietyin.models.UserModel;
import com.fsociety.fsocietyin.models.msg_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Base64;

public class UserAdapter extends  RecyclerView.Adapter<UserAdapter.viewholder>{
    FirebaseDatabase database;
    DatabaseReference myref;
    ArrayList<msg_model> messagelist;
    String LAST_MSG = "";
    Context ctx;
    ArrayList<UserModel> data = new ArrayList<>();
    public UserAdapter(Context ctx ,ArrayList<UserModel> data){
        this.ctx = ctx;

    }


    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.userchat,null);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        messagelist = new ArrayList<>();
        UserModel temp = data.get(position);
        holder.username.setText(temp.getName());
        String senderID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String reciverid = temp.getUserId();
        String senderroom = senderID + reciverid;


        database = FirebaseDatabase.getInstance();
        myref = database.getReference();
        myref.child("chats").child(senderroom).child("messages").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagelist.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    msg_model temp = ds.getValue(msg_model.class);
                    String enc_msg = temp.getMsg();
                    String person = temp.getSenderId();

                    byte[] actualByte = Base64.getDecoder()
                            .decode(enc_msg);
                    String actualString = new String(actualByte);
                    if (person.equals(reciverid)){
                        holder.date.setText("MSG : " + actualString + "     (1)");
                        holder.date.setTextColor((Color.rgb(0,255,8)));
                    }
                    else{
                        holder.date.setText("MSG : " + actualString + "     _//");
                        holder.date.setTextColor((Color.rgb(0,255,255)));
                    }

                }

                //code for notification
                //code for bitmap
                //Drawable drawable = ResourcesCompat.getDrawable(ctx.getResources() , R.drawable.elliotfs , null );
                //BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                //Bitmap largeicon = bitmapDrawable.getBitmap();

                //code for notification
                //NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);
                //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                //    Notification notification = new Notification.Builder(ctx)
                //            .setLargeIcon(largeicon)
                //            .setSmallIcon(R.drawable.ic_launcher_background)
                //            .setContentText("New message")
                //            .setSubText("msg from " + LAST_MSG)
                //            .setChannelId(CHANNEL_ID)
                //           .build();
                 //   notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "kshitij" , NotificationManager.IMPORTANCE_HIGH));
                 //   notificationManager.notify(NOT_ID, notification);
              //  }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updatedata(ArrayList<UserModel> kali){
        data.clear();
        data.addAll(kali);
        notifyDataSetChanged();
    }

    public class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;
        TextView username;
        TextView date;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.userchatimage);
            username = itemView.findViewById(R.id.userchatname);
            date = itemView.findViewById(R.id.userchatdate);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int Image_position = this.getAdapterPosition();
            UserModel temp = data.get(Image_position);
            // work after user click on userschat
           // Toast.makeText(ctx, " Hello " +  temp.getName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ctx.getApplicationContext(), ChatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("name",temp.getName());
            intent.putExtra("userid",temp.getUserId());
            ctx.startActivity(intent);



        }
    }
}
