package edu.uestc.diaryinuestc.ui.bill.day.bill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.ui.bill.bill.BillAdapter;

public class BillDayAdapter extends RecyclerView.Adapter<BillDayAdapter.ViewHolder>{
    private List<BillDay> mBillDayList;
    private Context mContext;
    private LinearLayoutManager layoutManager;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView billDate;
        TextView billIn;
        TextView billOut;
        RecyclerView billList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            billDate = itemView.findViewById(R.id.bill_group_date);
            billIn = itemView.findViewById(R.id.bill_group_in);
            billOut = itemView.findViewById(R.id.bill_group_out);
            billList = itemView.findViewById(R.id.bill_rv);

        }
    }

    public BillDayAdapter(List<BillDay> mBillDayList) {
        this.mBillDayList = mBillDayList;
    }

    @NonNull
    @Override
    public BillDayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.bill_group_item, parent, false);
        BillDayAdapter.ViewHolder holder = new BillDayAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BillDayAdapter.ViewHolder holder, int position) {
        BillDay billDay = mBillDayList.get(position);
        holder.billDate.setText(billDay.getMonth() + "月" + billDay.getDate1() + "日  星期" + billDay.getDate2());
        holder.billIn.setText(String.format("%.2f", billDay.getInAmount()));
        holder.billOut.setText(String.format("%.2f", billDay.getOutAmount()));

        //加载另一个adapter
        BillAdapter billAdapter = new BillAdapter(mBillDayList.get(position).getBillList());
        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        holder.billList.setLayoutManager(layoutManager);
        holder.billList.setAdapter(billAdapter);
        holder.billList.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mBillDayList.size();
    }
}
