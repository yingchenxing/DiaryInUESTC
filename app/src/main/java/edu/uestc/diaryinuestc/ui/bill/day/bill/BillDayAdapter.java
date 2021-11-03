package edu.uestc.diaryinuestc.ui.bill.day.bill;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uestc.diaryinuestc.R;

public class BillDayAdapter extends RecyclerView.Adapter<BillDayAdapter.ViewHolder>{
    private List<BillDay> mBillDayList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView billDate;
        TextView billIn;
        TextView billOut;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            billDate = itemView.findViewById(R.id.bill_group_date);
            billIn = itemView.findViewById(R.id.bill_group_in);
            billOut = itemView.findViewById(R.id.bill_group_out);
        }
    }

    public BillDayAdapter(List<BillDay> mBillDayList) {
        this.mBillDayList = mBillDayList;
    }

    @NonNull
    @Override
    public BillDayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_group_item, parent, false);
        BillDayAdapter.ViewHolder viewHolder = new BillDayAdapter.ViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BillDayAdapter.ViewHolder holder, int position) {
        BillDay billDay = mBillDayList.get(position);
        holder.billDate.setText(billDay.getDate1()+billDay.getDate2());
        holder.billIn.setText(billDay.getInAmount());
        holder.billOut.setText(billDay.getOutAmount());
    }

    @Override
    public int getItemCount() {
        return mBillDayList.size();
    }
}
