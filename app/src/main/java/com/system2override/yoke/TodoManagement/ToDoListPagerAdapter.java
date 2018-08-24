package com.system2override.yoke.TodoManagement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ToDoListPagerAdapter extends FragmentPagerAdapter {
    public ToDoListPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int tab) {
        return ToDoListFragment.newInstance(tab);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence header = "";
        switch(position)
        {
            case ToDoListFragment.ALL_TODOS:
                header = "ALL";
                break;
            case ToDoListFragment.COMPLETED_TODOS:
                header = "COMPLETED";
                break;
            case ToDoListFragment.INCOMPLETE_TODOS:
                header = "INCOMPLETE";
                break;

        }
        return header;
     }

}
