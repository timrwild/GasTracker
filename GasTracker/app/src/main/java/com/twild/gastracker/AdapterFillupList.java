package com.twild.gastracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;


public class AdapterFillupList extends ArrayAdapter<String> {

    private ArrayList<String> arrayListDate;
    private ArrayList<String> arrayListMileage;
    private ArrayList<String> arrayListAmount;
    private ArrayList<String> arrayListFull;
    private ArrayList<String> arrayListPrice;
    private ArrayList<String> arrayListMPG;

    public AdapterFillupList(Context context, int resource, ArrayList<String> date, ArrayList<String> mileage,
                             ArrayList<String> amount, ArrayList<String> full, ArrayList<String> price, ArrayList<String> mpg)
    {

        /*
         * It seems that this is the constructor class for the adapter. Whenever I create the
         * adapter, this is what gets called. It needs just two things besides all the data I
         * want to pass into the adapter for each item on the list.
         * The first thing it needs is a context. Where does the adapter exist? In this case,
         * it exists in the actual activity that contains the fragments, which is what makes
         * it different from the usual list adapter. Second, it needs a specific resource file
         * to look at so it knows where to look for the listview. I have absolutely no clue why
         * I need to stick the date on there at the end of calling super, but the data doesn't
         * show up without it.
         */

        super(context, resource, date);

        arrayListDate = date;
        arrayListMileage = mileage;
        arrayListAmount = amount;
        arrayListFull = full;
        arrayListPrice = price;
        arrayListMPG = mpg;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        /*
         * This part is fairly straightforward (thankfully). First, we need to get a
         * layout inflater from the context (the actual activity). I'm not exactly sure
         * what the layout inflater is, but hey, it works, so I'm not going to complain.
         * After that, we need to create a view based on that inflater. The layout we're
         * inflating is the custom layout for the efficiency notes. I'm not sure what the
         * "parent" parameter is for, but I'm guessing that it's telling it where the
         * inflated layouts are going to go. The "false" is there just because it is.
         * After that view is created, we need to take the notes from the arrays at the
         * desired position, and send it to the specific text boxes. Lastly, we return
         * the completely constructed view.
         */


        LayoutInflater fillupListInflater = LayoutInflater.from(getContext());

        View fillupListView = fillupListInflater.inflate(R.layout.adapter_fillup_info, parent, false);

        String date = arrayListDate.get(position);
        String mileage = arrayListMileage.get(position);
        String amount = arrayListAmount.get(position);
        String full;
        if (arrayListFull.get(position).equals("0"))
        {
            full = "";
        }
        else
        {
            full = "\u2611";
        }
        String price = arrayListPrice.get(position);
        String mpg = arrayListMPG.get(position);

        TextView textViewDate = (TextView) fillupListView.findViewById(R.id.data_table_fillup_date);
        TextView textViewMileage = (TextView) fillupListView.findViewById(R.id.data_table_fillup_mileage);
        TextView textViewAmount = (TextView) fillupListView.findViewById(R.id.data_table_fillup_amount);
        TextView textViewFull = (TextView) fillupListView.findViewById(R.id.data_table_fillup_full);
        TextView textViewPrice = (TextView) fillupListView.findViewById(R.id.data_table_fillup_price);
        TextView textViewMPG = (TextView) fillupListView.findViewById(R.id.data_table_fillup_mpg);

        textViewDate.setText(date);
        textViewMileage.setText(mileage);
        textViewAmount.setText(amount);
        textViewFull.setText(full);
        textViewPrice.setText(price);
        textViewMPG.setText(mpg);

        return fillupListView;
    }
}