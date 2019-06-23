package model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myphysio.R;

import java.util.List;

public class CustomAdapterHRList extends RecyclerView.Adapter<CustomAdapterHRList.ViewHolder>
{

    List<HealthRecord> listHR;

    public CustomAdapterHRList(List<HealthRecord> healthRecords)
    {
        this.listHR = healthRecords;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_hr_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        HealthRecord hr = listHR.get(position);
        holder.txtVwRN.setText(hr.getRecordNumber());
        holder.txtVwRD.setText(hr.getRecordDate());
        holder.txtVwStaffID.setText(hr.getStaffID());
    }

    @Override
    public int getItemCount(){
        return listHR.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtVwRN, txtVwRD, txtVwStaffID;

        public ViewHolder(View itemView)
        {
            super(itemView);

            txtVwRN = itemView.findViewById(R.id.txtVwRN);
            txtVwRD = itemView.findViewById(R.id.txtVwRD);
            txtVwStaffID = itemView.findViewById(R.id.txtVwPhy);
        }

    }
}
