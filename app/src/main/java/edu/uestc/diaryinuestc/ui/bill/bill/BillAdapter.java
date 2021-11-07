package edu.uestc.diaryinuestc.ui.bill.bill;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.ui.bill.BillEditActivity;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {
    private List<Bill> mBillList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView billImage;
        TextView billContent;
        TextView billType;
        TextView billAmount;
        ConstraintLayout billCv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            billImage = itemView.findViewById(R.id.bill_item_img);
            billContent = itemView.findViewById(R.id.bill_item_content);
            billType = itemView.findViewById(R.id.bill_item_type);
            billAmount = itemView.findViewById(R.id.bill_item_amount);
            billCv = itemView.findViewById(R.id.bill_item);
        }
    }

    public BillAdapter(List<Bill> mBillList) {
        this.mBillList = mBillList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.bill_item, parent, false);
        BillAdapter.ViewHolder holder = new BillAdapter.ViewHolder(view);

        //添加进入编辑界面事件
        holder.billCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BillEditActivity.class);
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bill bill = mBillList.get(position);
        //判断收入还是支出
        if (bill.isIn())
            holder.billAmount.setText("+" + bill.getAmount());
        else
            holder.billAmount.setText("-" + bill.getAmount());
        holder.billType.setText(bill.getTypeName());
        holder.billContent.setText(bill.getContent());
        holder.billImage.setImageResource(bill.getImgId());
    }

    @Override
    public int getItemCount() {
        return mBillList.size();
    }
}
