package app.com.kucingapps.Utils;

import android.widget.Filter;

import java.util.ArrayList;

import app.com.kucingapps.Adapter.Adapter;
import app.com.kucingapps.Model.Pets;

public class CustomFilter extends Filter {
    Adapter adapter;
    ArrayList<Pets> filterList;

    public CustomFilter(ArrayList<Pets> filterList,Adapter adapter)
    {
        this.adapter=adapter;
        this.filterList=filterList;

    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString();
            //STORE OUR FILTERED PLAYERS
            ArrayList<Pets> filteredPets=new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getPid().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredPets.add(filterList.get(i));
                }
            }

            results.count=filteredPets.size();
            results.values=filteredPets;

        }else
        {
            results.count=filterList.size();
            results.values=filterList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.pets= (ArrayList<Pets>) results.values;

        //REFRESH
        adapter.notifyDataSetChanged();

    }
}
