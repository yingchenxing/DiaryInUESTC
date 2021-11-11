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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import edu.uestc.diaryinuestc.R;

public class BillStaticActivity extends AppCompatActivity {
    PieChart pieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_static);




        show();
    }

        private void show() {
            pieChart = (PieChart) findViewById(R.id.chart) ;
            //如果启用此选项，则图表中的值将以百分比形式绘制，而不是以原始值绘制
            pieChart.setUsePercentValues(true);
            //如果这个组件应该启用（应该被绘制）FALSE如果没有。如果禁用，此组件的任何内容将被绘制默认
            pieChart.getDescription().setEnabled(false);
            //将额外的偏移量（在图表视图周围）附加到自动计算的偏移量
            pieChart.setExtraOffsets(5, 10, 5, 5);
            //较高的值表明速度会缓慢下降 例如如果它设置为0，它会立即停止。1是一个无效的值，并将自动转换为0.999f。
            pieChart.setDragDecelerationFrictionCoef(0.95f);
            //设置中间字体
            pieChart.setCenterText("刘某人\n我仿佛听到有人说我帅");
            //设置为true将饼中心清空
            pieChart.setDrawHoleEnabled(true);
            //套孔，绘制在PieChart中心的颜色
            pieChart.setHoleColor(Color.WHITE);
            //设置透明圆应有的颜色。
            pieChart.setTransparentCircleColor(Color.WHITE);
            //设置透明度圆的透明度应该有0 =完全透明，255 =完全不透明,默认值为100。
            pieChart.setTransparentCircleAlpha(110);
            //设置在最大半径的百分比饼图中心孔半径（最大=整个图的半径），默认为50%
            pieChart.setHoleRadius(58f);
            //设置绘制在孔旁边的透明圆的半径,在最大半径的百分比在饼图*（max =整个图的半径），默认55% -> 5%大于中心孔默认
            pieChart.setTransparentCircleRadius(61f);
            //将此设置为true，以绘制显示在pie chart
            pieChart.setDrawCenterText(true);
            //集度的radarchart旋转偏移。默认270f -->顶（北）
            pieChart.setRotationAngle(0);
            //设置为true，使旋转/旋转的图表触摸。设置为false禁用它。默认值：true
            pieChart.setRotationEnabled(true);
            //将此设置为false，以防止由抽头姿态突出值。值仍然可以通过拖动或编程高亮显示。默认值：真
            pieChart.setHighlightPerTapEnabled(true);
            //创建Legend对象
            Legend l = pieChart.getLegend();
            //设置垂直对齐of the Legend
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            //设置水平of the Legend
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            //设置方向
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            //其中哪一个将画在图表或外
            l.setDrawInside(false);
            //设置水平轴上图例项之间的间距
            l.setXEntrySpace(7f);
            //设置在垂直轴上的图例项之间的间距
            l.setYEntrySpace(0f);
            //设置此轴上标签的所使用的y轴偏移量 更高的偏移意味着作为一个整体的Legend将被放置远离顶部。
            l.setYOffset(0f);
            //设置入口标签的颜色。
            pieChart.setEntryLabelColor(Color.WHITE);
            //设置入口标签的大小。默认值：13dp
            pieChart.setEntryLabelTextSize(12f);
            //模拟的数据源
            PieEntry x1 = new PieEntry(15.8f , "one" , R.color.theme_2) ;
            PieEntry x2 = new PieEntry(15.8f , "two") ;
            PieEntry x3 = new PieEntry(15.8f , "three") ;
            PieEntry x4 = new PieEntry(15.8f , "four") ;
            PieEntry x5 = new PieEntry(15.8f , "five") ;
            PieEntry x6 = new PieEntry(15.8f , "six") ;
            PieEntry x7 = new PieEntry(15.8f , "seven") ;
            PieEntry x8 = new PieEntry(15.8f , "eight") ;
            PieEntry x9 = new PieEntry(15.8f , "nine") ;
            PieEntry x10 = new PieEntry(15.8f , "ten") ;
            //添加到List集合
            List<PieEntry> list = new ArrayList<>() ;
            list.add(x1) ;
            list.add(x2) ;
            list.add(x3) ;
            list.add(x4) ;
            list.add(x5) ;
            list.add(x6) ;
            list.add(x7) ;
            list.add(x8) ;
            list.add(x9) ;
            list.add(x10) ;
            //设置到PieDataSet对象
            PieDataSet set = new PieDataSet(list , "表一") ;
            set.setDrawValues(false);//设置为true，在图表绘制y
            set.setAxisDependency(YAxis.AxisDependency.LEFT);//设置Y轴，这个数据集应该被绘制（左或右）。默认值：左
            set.setAutomaticallyDisableSliceSpacing(false);//当启用时，片间距将是0时，最小值要小于片间距本身
            set.setSliceSpace(5f);//间隔
            set.setSelectionShift(10f);//点击伸出去的距离
            /**
             * 设置该数据集前应使用的颜色。颜色使用只要数据集所代表的条目数目高于颜色数组的大小。
             * 如果您使用的颜色从资源， 确保颜色已准备好（通过调用getresources()。getColor（…））之前，将它们添加到数据集
             * */
            ArrayList<Integer> colors = new ArrayList<Integer>();
            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);
            colors.add(ColorTemplate.getHoloBlue());
            set.setColors(colors);
            //传入PieData
            PieData data = new PieData(set);
            //为图表设置新的数据对象
            pieChart.setData(data);
            //刷新
            pieChart.invalidate();
//            //动画图上指定的动画时间轴的绘制
//            pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        }
    }
