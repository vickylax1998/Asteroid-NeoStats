package com.ranium.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ranium.asteroid_neostats.R;
import com.ranium.pojo.NeoFeed;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NeoFeedAdapter extends RecyclerView.Adapter<NeoFeedAdapter.NeoviewHolder> {
    ArrayList<NeoFeed> arrayList;
    Context context;

    public NeoFeedAdapter(ArrayList<NeoFeed> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public NeoFeedAdapter.NeoviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView  = layoutInflater.inflate(R.layout.custom_layout_neo_feed, parent, false);
        itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new NeoviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NeoFeedAdapter.NeoviewHolder holder, int position) {
        NeoFeed neoFeed =arrayList.get(position);
        holder.tvname.setText("Asteriod id : "+neoFeed.getAsteriod_id());
        holder.tvSize.setText("Average Size of the asteriod : \n"+
                "Min : "+neoFeed.getMin_size()+" km \n"+
                "Max : "+neoFeed.getMin_size()+" km ");

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class NeoviewHolder extends RecyclerView.ViewHolder {
        TextView tvname,tvSize;
        public NeoviewHolder(@NonNull View itemView) {
            super(itemView);
            tvname=itemView.findViewById(R.id.textViewName);
            tvSize=itemView.findViewById(R.id.textViewSize);
        }
    }
}
