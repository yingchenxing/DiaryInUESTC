package edu.uestc.diaryinuestc.ui.bill;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Window;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.ui.bill.bill.Bill;
import edu.uestc.diaryinuestc.ui.bill.database.BillEngine;

public class BillStaticActivity extends AppCompatActivity {
    private PieChart inPieChart;
    private PieChart outPieChart;
    private BillEngine billEngine;
    private List<Bill> billList;
    public double[] amount;
    private double inAmount;
    private double outAmount;
    List<PieEntry> outPieEntries;
    List<PieEntry> inPieEntries;
    private TextView tittle;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bill_static);

        initAll();

        getAmount();

        show();
    }

    private void initAll() {
        calendar = Calendar.getInstance();
        tittle = findViewById(R.id.static_tittle);
        tittle.setText(calendar.get(Calendar.MONTH) + 1 + "月收支情况");
        outPieEntries = new ArrayList<>();
        inPieEntries = new ArrayList<>();
        billEngine = new BillEngine(BillStaticActivity.this);
        billList = billEngine.queryAllBills();
        amount = new double[16];
    }

    private void show() {
        inPieChart = (PieChart) findViewById(R.id.in_chart);
        outPieChart = (PieChart) findViewById(R.id.out_chart);
        //如果启用此选项，则图表中的值将以百分比形式绘制，而不是以原始值绘制
        inPieChart.setUsePercentValues(false);
        outPieChart.setUsePercentValues(false);
        //如果这个组件应该启用（应该被绘制）FALSE如果没有。如果禁用，此组件的任何内容将被绘制默认
        outPieChart.getDescription().setEnabled(false);
        inPieChart.getDescription().setEnabled(false);
        //将额外的偏移量（在图表视图周围）附加到自动计算的偏移量
        outPieChart.setExtraOffsets(5, 10, 5, 5);
        inPieChart.setExtraOffsets(5, 10, 5, 5);
        //较高的值表明速度会缓慢下降 例如如果它设置为0，它会立即停止。1是一个无效的值，并将自动转换为0.999f。
        outPieChart.setDragDecelerationFrictionCoef(0.95f);
        inPieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间字体
        outPieChart.setCenterText("支出：" + String.format("%.2f", outAmount));
        inPieChart.setCenterText("收入：" + String.format("%.2f", inAmount));
        //设置为true将饼中心清空
        outPieChart.setDrawHoleEnabled(true);
        inPieChart.setDrawHoleEnabled(true);
        //套孔，绘制在PieChart中心的颜色
        outPieChart.setHoleColor(Color.WHITE);
        inPieChart.setHoleColor(Color.WHITE);
        //设置透明圆应有的颜色。
        outPieChart.setTransparentCircleColor(Color.WHITE);
        inPieChart.setTransparentCircleColor(Color.WHITE);
        //设置透明度圆的透明度应该有0 =完全透明，255 =完全不透明,默认值为100。
        outPieChart.setTransparentCircleAlpha(110);
        inPieChart.setTransparentCircleAlpha(110);
        //设置在最大半径的百分比饼图中心孔半径（最大=整个图的半径），默认为50%
        outPieChart.setHoleRadius(58f);
        inPieChart.setHoleRadius(58f);
        //设置绘制在孔旁边的透明圆的半径,在最大半径的百分比在饼图*（max =整个图的半径），默认55% -> 5%大于中心孔默认
        outPieChart.setTransparentCircleRadius(61f);
        inPieChart.setTransparentCircleRadius(61f);
        //将此设置为true，以绘制显示在pie chart
        outPieChart.setDrawCenterText(true);
        inPieChart.setDrawCenterText(true);
        //集度的radarchart旋转偏移。默认270f -->顶（北）
        outPieChart.setRotationAngle(0);
        inPieChart.setRotationAngle(0);
        //设置为true，使旋转/旋转的图表触摸。设置为false禁用它。默认值：true
        outPieChart.setRotationEnabled(true);
        inPieChart.setRotationEnabled(true);
        //将此设置为false，以防止由抽头姿态突出值。值仍然可以通过拖动或编程高亮显示。默认值：真
        outPieChart.setHighlightPerTapEnabled(true);
        inPieChart.setHighlightPerTapEnabled(true);
        //创建Legend对象
        Legend legend_out = outPieChart.getLegend();
        Legend legend_in = inPieChart.getLegend();
        //设置垂直对齐of the Legend
        legend_out.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend_in.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //设置水平of the Legend
        legend_out.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend_in.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //设置方向
        legend_out.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend_in.setOrientation(Legend.LegendOrientation.VERTICAL);
        //其中哪一个将画在图表或外
        legend_out.setDrawInside(false);
        legend_in.setDrawInside(false);
        //设置水平轴上图例项之间的间距
        legend_out.setXEntrySpace(7f);
        legend_in.setXEntrySpace(7f);
        //设置在垂直轴上的图例项之间的间距
        legend_out.setYEntrySpace(0f);
        legend_in.setYEntrySpace(0f);
        //设置此轴上标签的所使用的y轴偏移量 更高的偏移意味着作为一个整体的Legend将被放置远离顶部。
        legend_out.setYOffset(0f);
        legend_in.setYOffset(0f);
        //设置入口标签的颜色。
        outPieChart.setEntryLabelColor(Color.WHITE);
        inPieChart.setEntryLabelColor(Color.WHITE);
        //设置入口标签的大小。默认值：13dp
        outPieChart.setEntryLabelTextSize(12f);
        inPieChart.setEntryLabelTextSize(12f);

        //模拟的数据源

        for (int i = 1; i < 9; i++) {
            String str = new Bill(1, 1, 1, 1, 1, 1, 1, i, 1, true, "").getTypeName();
            if (amount[i] != 0) {
                PieEntry pie;
                if (i == 1)
                    pie = new PieEntry((float) amount[i], str, R.color.theme_2);
                else
                    pie = new PieEntry((float) amount[i], str);
                outPieEntries.add(pie);
            }
        }
        if (amount[15] != 0) {
            PieEntry pie;
            pie = new PieEntry((float) amount[15], "其他");
            outPieEntries.add(pie);
        }


        for (int i = 9; i < 14; i++) {
            String str = new Bill(1, 1, 1, 1, 1, 1, 1, i, 1, true, "").getTypeName();
            if (amount[i] != 0) {
                PieEntry pie;
                if (i == 1)
                    pie = new PieEntry((float) amount[i], str, R.color.theme_2);
                else
                    pie = new PieEntry((float) amount[i], str);
                inPieEntries.add(pie);
            }
        }
        if (amount[14] != 0) {
            PieEntry pie;
            pie = new PieEntry((float) amount[15], "其他");
            inPieEntries.add(pie);
        }


        //设置到PieDataSet对象
        PieDataSet outSet = new PieDataSet(outPieEntries, "支出情况");
        outSet.setDrawValues(false);//设置为true，在图表绘制y
        outSet.setAxisDependency(YAxis.AxisDependency.LEFT);//设置Y轴，这个数据集应该被绘制（左或右）。默认值：左
        outSet.setAutomaticallyDisableSliceSpacing(false);//当启用时，片间距将是0时，最小值要小于片间距本身
        outSet.setSliceSpace(5f);//间隔
        outSet.setSelectionShift(10f);//点击伸出去的距离

        PieDataSet inSet = new PieDataSet(inPieEntries, "收入情况");
        inSet.setDrawValues(false);//设置为true，在图表绘制y
        inSet.setAxisDependency(YAxis.AxisDependency.LEFT);//设置Y轴，这个数据集应该被绘制（左或右）。默认值：左
        inSet.setAutomaticallyDisableSliceSpacing(false);//当启用时，片间距将是0时，最小值要小于片间距本身
        inSet.setSliceSpace(5f);//间隔
        inSet.setSelectionShift(10f);//点击伸出去的距离
        /**
         * 设置该数据集前应使用的颜色。颜色使用只要数据集所代表的条目数目高于颜色数组的大小。
         * 如果您使用的颜色从资源， 确保颜色已准备好（通过调用getresources()。getColor（…））之前，将它们添加到数据集
         * */
        ArrayList<Integer> colors = new ArrayList<Integer>();
//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        colors.remove(2);

        colors.add(ColorTemplate.getHoloBlue());
        outSet.setColors(colors);
        inSet.setColors(colors);
        //传入PieData
        PieData outData = new PieData(outSet);
        PieData inData = new PieData(inSet);
        //为图表设置新的数据对象
        outPieChart.setData(outData);
        inPieChart.setData(inData);//刷新
        outPieChart.invalidate();
        inPieChart.invalidate();
        //动画图上指定的动画时间轴的绘制
//            pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

    }

    void getAmount() {
        for (int type = 1; type < 14; type++) {
            for (Bill bill : billList) {
                if (bill.getMonth() == calendar.get(Calendar.MONTH) + 1) {
                    if (bill.getType() == type) {
                        amount[type] += bill.getAmount();
                    }
                }
            }
        }

        inAmount = 0;
        outAmount = 0;

        for (Bill bill : billList) {
            if (bill.getMonth() == calendar.get(Calendar.MONTH) + 1) {
                if (bill.getType() > 13 || bill.getType() < 1) {
                    if (bill.isIn()) {
                        amount[14] += bill.getAmount();
                        inAmount += bill.getAmount();
                    } else {
                        amount[15] += bill.getAmount();
                        outAmount += bill.getAmount();
                    }
                } else {
                    if (bill.isIn()) {
                        inAmount += bill.getAmount();
                    } else {
                        outAmount += bill.getAmount();
                    }
                }
            }
        }
    }
}
