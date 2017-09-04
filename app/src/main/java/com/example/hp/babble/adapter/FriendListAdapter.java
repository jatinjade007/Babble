package com.example.hp.babble.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.babble.bean.ChatRow;
import com.example.hp.babble.R;

import java.util.ArrayList;

/**
 * Created by HP on 4/9/2017.
 */

public class FriendListAdapter extends BaseAdapter {
    private static final String TAG="GetView";
    private Context context;
    private ArrayList<ChatRow> chatRowArrayList;
    private LayoutInflater layoutInflater;

    public FriendListAdapter(Context context, ArrayList<ChatRow> chatRowArrayList) {
        this.context = context;
        this.chatRowArrayList = chatRowArrayList;
    }

    @Override
    public int getCount() {
        return chatRowArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return chatRowArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view=layoutInflater.inflate(R.layout.chat_row,viewGroup,false);
        Log.d(TAG, "getView: ");
        //Getting All The View Inside XML
        TextView txtname= (TextView) view.findViewById(R.id.frndName);
        TextView txtmsg= (TextView) view.findViewById(R.id.txtMsg);
        ImageView imageView= (ImageView) view.findViewById(R.id.image);
        //Getting Data
        ChatRow s=chatRowArrayList.get(i);
        String name=s.getFriendName();
        String msg=s.getMessage();
        int image=s.getImage();
        //Loading Views with Corresponding data
        txtname.setText(name);
        txtmsg.setText(msg);
       // imageView.setImageResource(image);
        return view;
    }
}
