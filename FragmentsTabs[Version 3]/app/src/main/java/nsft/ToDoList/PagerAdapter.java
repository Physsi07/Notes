package nsft.ToDoList;

import android.support.v4.app.*;

/**
 * Created by Starlyn on 3/5/18.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    int numberOfTabs;

    /* CONSTRUCTOR */
    public PagerAdapter(FragmentManager fm, int numOfTabs) {

        super(fm);
        this.numberOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        /* SWITCH STATEMENT THAT MAKES TELL ME IN WHICH TAB AM I */
        switch (position) {
            case 0:
                    Tab1 tab1 = new Tab1();
                    return tab1;
            case 1:
                    Tab2 tab2 = new Tab2();
                    return tab2;
            default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
