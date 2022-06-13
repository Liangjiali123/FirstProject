package com.itheima.hellocharts;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LayoutInflater mInflater;
    private View view1,view2,view3;
    private List<String> mTitleList = new ArrayList<>(); //页卡标题集合
    private List<View> mViewList = new ArrayList<>();   //页卡视图集合
    //线形图
    private int[] temperature = {25, 27, 26, 25, 26, 27, 24};//图表的数据点
    //X轴的标注
    private String[] lineData = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private List<PointValue> pointValues = new ArrayList<PointValue>();
    private List<AxisValue> axisValues = new ArrayList<AxisValue>();
    private LineChartView lineChartView;
    //饼状图
    private PieChartView pieChartView;
    private PieChartData pieCharData;
    private List<SliceValue> sliceValues = new ArrayList<SliceValue>();
    private int[] pieData = {8, 24, 35, 23, 10};//饼状图中的数据
    private int[] color = {Color.parseColor("#356fb3"), Color.parseColor("#b53633"),
            Color.parseColor("#86aa3d"), Color.parseColor("#6a4b90"), Color.
            parseColor("#2e9cba")};//饼状图每块的颜色
    private String[] stateChar = {"高等教育", "职业教育", "语言培训", "K12教育", "其他"};
    //柱状图
    private String[] year = new String[]{"2013", "2014", "2015", "2016", "2017"};
    private ColumnChartView columnChartView;
    private ColumnChartData columnData;
    private int[] columnY = {500, 1000, 1500, 2000, 2500, 3000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    private void init() {
        mViewPager = (ViewPager) findViewById(R.id.vp_view);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mInflater = LayoutInflater.from(this);
        view1 = mInflater.inflate(R.layout.layout_line_chart, null);
        view2 = mInflater.inflate(R.layout.layout_pie_chart, null);
        view3 = mInflater.inflate(R.layout.layout_column_chart, null);
        //添加页卡视图
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);
        //添加页卡标题
        mTitleList.add("线形图");
        mTitleList.add("饼状图");
        mTitleList.add("柱状图");
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));//添加选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
        ViewPagerAdapter mAdapter = new ViewPagerAdapter(mViewList, mTitleList);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);  //将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
        //线形图
        lineChartView = (LineChartView) view1.findViewById(R.id.lv_chart);
        setAxisXLables(); //获取x轴的标注
        setAxisPoints();  //设置坐标点
        initLineChart();  //初始化线形图
        //饼状图
        pieChartView = (PieChartView) view2.findViewById(R.id.pv_chart);
        pieChartView.setOnValueTouchListener(selectListener);//为饼状图设置事件监听器
        setPieChartData();
        initPieChart();
        //柱状图
        columnChartView = (ColumnChartView) view3.findViewById(R.id.cv_chart);
        initColumnChart();
    }
    /**
     * 设置X轴的标注
     */
    private void setAxisXLables() {
        for (int i = 0; i < lineData.length; i++) {
            axisValues.add(new AxisValue(i).setLabel(lineData[i]));
        }
    }
    /**
     * 设置线形图中的每个数据点
     */
    private void setAxisPoints() {
        for (int i = 0; i < temperature.length; i++) {
            pointValues.add(new PointValue(i, temperature[i]));
        }
    }
    /**
     * 初始化线形图
     */
    private void initLineChart() {
        //设置线的颜色、形状等属性
        Line line = new Line();
        line.setColor(Color.parseColor("#33b5e5"));
        line.setShape(ValueShape.CIRCLE);  //线形图上数据点的形状为圆形
        line.setCubic(false);             //曲线是否平滑，即是曲线还是折线
        line.setHasLabels(true);         //曲线的数据坐标是否加上备注
        line.setHasLines(true);         //是否显示线条，如果为false 则没有曲线只显示点
        line.setHasPoints(true);       //是否显示圆点，如果为false 则没有圆点只显示线
        line.setValues(pointValues);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        //X轴
        Axis axisX = new Axis();
        axisX.setHasTiltedLabels(true);    //X轴字体是斜的显示还是直的，true是倾斜显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setMaxLabelChars(5);        //设置坐标轴标签显示的最大字符数
        axisX.setValues(axisValues);     //填充X轴的坐标名称
        data.setAxisXBottom(axisX);      //设置x轴在底部
        axisX.setHasLines(true);         //x 轴分割线
        //Y轴
        Axis axisY = new Axis();
        data.setAxisYLeft(axisY);          //设置Y轴在左侧
        axisY.setTextColor(Color.BLACK); //设置字体颜色
        axisY.setMaxLabelChars(5);       //设置坐标轴标签显示的最大字符数
        //设置线形图的行为属性，如支持缩放、滑动以及平移
        lineChartView.setInteractive(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL); //设置缩放类型为水平缩放
        lineChartView.setMaxZoom((float) 2);            //最大放大比例
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.setLineChartData(data);
        lineChartView.setVisibility(View.VISIBLE);
    }
    /**
     * 设置饼状图中的数据
     */
    private void setPieChartData() {
        for (int i = 0; i < pieData.length; ++i) {
            SliceValue sliceValue = new SliceValue((float) pieData[i], color[i]);
            sliceValues.add(sliceValue);//添加到集合中
        }
    }
    /**
     * 初始化饼状图
     */
    private void initPieChart() {
        pieCharData = new PieChartData();
        pieCharData.setHasLabels(true);                     //显示标签
        pieCharData.setHasLabelsOnlyForSelected(false); //不用点击显示占的百分比
        pieCharData.setHasLabelsOutside(false);             //数据是否显示在饼图外侧
        pieCharData.setValues(sliceValues);                 //填充数据
        pieCharData.setCenterCircleColor(Color.WHITE);   //设置环形中间的颜色
        pieCharData.setHasCenterCircle(true);             //是否显示中心圆
        pieCharData.setCenterCircleScale(0.5f);          //设置中心圆所占饼图的比例
        pieCharData.setCenterText1("数据");               //设置中心圆默认显示的文字
        pieChartView.setPieChartData(pieCharData);     //为饼图设置数据
        pieChartView.setValueSelectionEnabled(true);  //选择饼状图中的块会变大
        pieChartView.setAlpha(0.9f);                     //设置透明度
        pieChartView.setCircleFillRatio(1f);          //设置饼图大小,占整个View的比例
    }
    /**
     * 数据所占的百分比
     */
    private String calPercent(int i) {
        String result = "";
        int sum = 0;
        for (int j = 0; j < pieData.length; j++) {
            sum += pieData[j];
        }
        result = String.format("%.2f", (float) pieData[i] * 100 / sum) + "%";
        return result;
    }
    /**
     * 饼状图的事件监听器
     */
    PieChartOnValueSelectListener selectListener = new PieChartOnValueSelectListener() {
        @Override
        public void onValueDeselected() {
        }
        @Override
        public void onValueSelected(int arg0, SliceValue value) {
            //选择对应图形后，在中间部分显示相应信息
            pieCharData.setCenterText1(stateChar[arg0]);//中心圆中的第一文本
            pieCharData.setCenterText2(value.getValue() + "（" + calPercent(arg0) + ")");
        }
    };
    /**
     * 初始化柱状图
     */
    private void initColumnChart() {
        List<AxisValue> axisValues = new ArrayList<AxisValue>();//存储X轴标注
        List<AxisValue> axisYValues = new ArrayList<AxisValue>();//存储Y轴标注
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> subcolumnValues;            //存储
        for (int k = 0; k < columnY.length; k++) {
            axisYValues.add(new AxisValue(k).setValue(columnY[k]));
        }
        for (int i = 0; i < year.length; ++i) {
            subcolumnValues = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < 1; ++j) {
                switch (i + 1) {
                    case 1:
                        subcolumnValues.add(new SubcolumnValue(924, ChartUtils.COLOR_BLUE));
                        break;
                    case 2:
                        subcolumnValues.add(new SubcolumnValue(1220, ChartUtils.COLOR_GREEN));
                        break;
                    case 3:
                        subcolumnValues.add(new SubcolumnValue(1610, ChartUtils.COLOR_RED));
                        break;
                    case 4:
                        subcolumnValues.add(new SubcolumnValue(2125, ChartUtils.COLOR_ORANGE));
                        break;
                    case 5:
                        subcolumnValues.add(new SubcolumnValue(2805, ChartUtils.COLOR_VIOLET));
                        break;
                }
            }
            // 点击柱状图就展示数据量
            axisValues.add(new AxisValue(i).setLabel(year[i]));
            columns.add(new Column(subcolumnValues).setHasLabelsOnlyForSelected(true));
        }
        //X轴
        Axis axisX = new Axis(axisValues);
        axisX.setHasLines(false);
        axisX.setTextColor(Color.BLACK);
        //Y轴
        Axis axisY = new Axis(axisYValues);
        axisY.setHasLines(true);          //设置Y轴有线条显示
        axisY.setTextColor(Color.BLACK); //设置文本颜色
        axisY.setMaxLabelChars(5);      //设置坐标轴标签显示的最大字符数
        //设置柱状图的相关属性
        columnData = new ColumnChartData(columns);
        columnData.setAxisXBottom(axisX); //设置X轴在底部
        columnData.setAxisYLeft(axisY); //设置Y轴在左侧
        columnChartView.setColumnChartData(columnData);
        columnChartView.setValueSelectionEnabled(true);    //设置柱状图可以被选择
        columnChartView.setZoomType(ZoomType.HORIZONTAL);//设置缩放类型为水平缩放
    }


}
