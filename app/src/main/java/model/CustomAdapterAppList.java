package model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myphysio.R;

import java.util.List;

public class CustomAdapterAppList extends RecyclerView.Adapter<CustomAdapterAppList.ViewHolder>
{

    List<Appointment> listApp;

    public CustomAdapterAppList(List<Appointment> app)
    {
        this.listApp = app;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_app,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Appointment app = listApp.get(position);
        holder.txtVwBid.setText(app.getBid());
        holder.txtVwBdate.setText(app.getBdate());
        holder.txtVwBstatus.setText(app.getBstatus());
    }

    @Override
    public int getItemCount(){
        return listApp.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtVwBid, txtVwBdate, txtVwBstatus;

        public ViewHolder(View itemView)
        {
            super(itemView);

            txtVwBid = itemView.findViewById(R.id.txtVwBID);
            txtVwBdate = itemView.findViewById(R.id.txtVwBdate);
            txtVwBstatus = itemView.findViewById(R.id.txtVwStatus);
        }

    }
}
