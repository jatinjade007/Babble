package com.example.hp.babble.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.hp.babble.fragment.ChatListFragment;
import com.example.hp.babble.fragment.FriendListFragment;
import com.example.hp.babble.fragment.FriendRequestFragment;
import com.example.hp.babble.fragment.MemberListFragment;

/**
 * Created by HP on 4/10/2017.
 */

public class MyPageAdapter extends FragmentStatePagerAdapter {
    private int tabCount;

    public MyPageAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                //ChatListFragment chatListFragment=new ChatListFragment();
                //return chatListFragment;

                FriendListFragment friendListFragment=new FriendListFragment();
                return friendListFragment;
            case 1:
                FriendRequestFragment friendRequestFragment=new FriendRequestFragment();
                return friendRequestFragment;
            case 2:
                MemberListFragment memberListFragment=new MemberListFragment();
                return memberListFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
