package model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myphysio.R;

import java.util.List;

public class CustomAdapterEVList extends RecyclerView.Adapter<CustomAdapterEVList.ViewHolder>
{

    List<Rehab> listRehab;

    public CustomAdapterEVList(List<Rehab> rehabs)
    {
        this.listRehab = rehabs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ev,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Rehab rehab = listRehab.get(position);
        holder.txtVwRPID.setText(rehab.getRehabPlanID());

        if (rehab.getEvLink().equalsIgnoreCase("null")) {
            holder.txtVwEL.setText("NO");
        } else {
            holder.txtVwEL.setText("YES");
        }

        if (rehab.getEvFeedback().equalsIgnoreCase("null")) {
            holder.txtVwRPfed.setText("NO");
        } else {
            holder.txtVwRPfed.setText("YES");
        }
    }

    @Override
    public int getItemCount(){
        return listRehab.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtVwRPID, txtVwEL,txtVwRPfed;

        public ViewHolder(View itemView)
        {
            super(itemView);

            txtVwRPID = itemView.findViewById(R.id.txtVwRPID);
            txtVwEL = itemView.findViewById(R.id.txtVwEL);
            txtVwRPfed = itemView.findViewById(R.id.txtVwRPfed);
        }

    }
}
