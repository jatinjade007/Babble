package com.example.hp.babble.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.hp.babble.adapter.FriendListAdapter;
import com.example.hp.babble.bean.ChatRow;
import com.example.hp.babble.R;

import java.util.ArrayList;

public class ChatListFragment extends Fragment {

    private static final String TAG="ChatListFragment";
    private ListView listView;
    private ChatRow chatRow;
    private ArrayList<ChatRow> chatRowArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat_list, container, false);
        listView= (ListView) view.findViewById(R.id.chatList);
        String[] friendName={"ABC","DEF","XYZ","PQR","MNO","ABC","DEF","XYZ","PQR","MNO"};
        String[] msg={"Welcome","hii","how r u","Welcome","Welcome","Welcome","Welcome","Welcome","Welcome","Welcome"};
        int[] image={R.drawable.image,R.drawable.image,R.drawable.image,R.drawable.image,R.drawable.image,R.drawable.image,R.drawable.image,R.drawable.image,R.drawable.image,R.drawable.image};
        chatRowArrayList=new ArrayList<ChatRow>();
        for (int i=0;i<friendName.length;i++)
        {
            chatRow=new ChatRow(image[i],friendName[i],msg[i]);
            chatRowArrayList.add(chatRow);
        }
        FriendListAdapter adapter=new FriendListAdapter(getActivity(),chatRowArrayList);

/*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),ChatActivity.class);
                startActivity(intent);
            }
        });*/
        listView.setAdapter(adapter);
        return view;
    }
}
