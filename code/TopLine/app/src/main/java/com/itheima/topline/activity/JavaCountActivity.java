package com.itheima.topline.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.topline.R;
import com.itheima.topline.view.SwipeBackLayout;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

public class JavaCountActivity extends AppCompatActivity {
    private TextView tv_main_title, tv_back;
    private RelativeLayout rl_title_bar;
    private SwipeBackLayout layout;
    public final static String[] years = new String[]{"应届生", "1-2年", "2-3年", "3-5 年", "5-8年", "8-10年", "10年"};
    private LineChartView chartTop;
    private ColumnChartView chartBottom;
    private LineChartData lineData;
    private ColumnChartData columnData;
    private int[] columnY = {0, 10000, 20000, 30000, 40000, 50000, 60000, 70000, 80000,
            90000, 100000};
    private TextView tv_intro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
                R.layout.base, null);
        layout.attachToActivity(this);
        setContentView(R.layout.activity_java_count);
        init();
    }
    private void init() {
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("Java统计");
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.
                rdTextColorPress));
        tv_intro = (TextView) findViewById(R.id.tv_intro);
        tv_intro.setText(getResources().getString(R.string.java_count_text));
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setVisibility(View.VISIBLE);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JavaCountActivity.this.finish();
            }
        });
        chartTop = (LineChartView) findViewById(R.id.chart_top);
        generateInitialLineData();
        chartBottom = (ColumnChartView) findViewById(R.id.chart_bottom);
        generateColumnData();
    }
    private void generateColumnData() {
        int numColumns = years.length;
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<AxisValue> axisYValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int k = 0; k < columnY.length; k++) {
            axisYValues.add(new AxisValue(k).setValue(columnY[k]));
        }
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<SubcolumnValue>();
            switch (i) { //设置柱状图中的每个条形图的值与颜色
                case 0:
                    values.add(new SubcolumnValue((float) 6000, ChartUtils.COLOR_GREEN));
                    break;
                case 1:
                    values.add(new SubcolumnValue((float) 13000, ChartUtils.
                            COLOR_ORANGE));
                    break;
                case 2:
                    values.add(new SubcolumnValue((float) 20000, ChartUtils.COLOR_BLUE));
                    break;
                case 3:
                    values.add(new SubcolumnValue((float) 26000, ChartUtils.COLOR_RED));
                    break;
                case 4:
                    values.add(new SubcolumnValue((float) 35000, ChartUtils.
                            COLOR_VIOLET));
                    break;
                case 5:
                    values.add(new SubcolumnValue((float) 50000, ChartUtils.
                            COLOR_ORANGE));
                    break;
                case 6:
                    values.add(new SubcolumnValue((float) 100000, ChartUtils.COLOR_BLUE));
                    break;
            }
            axisValues.add(new AxisValue(i).setLabel(years[i]));
            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }
        columnData = new ColumnChartData(columns);
        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        columnData.setAxisYLeft(new Axis(axisYValues).setHasLines(true).
                setMaxLabelChars(6));
        chartBottom.setColumnChartData(columnData);
        //这个设置会保证柱状图在点击年数的条目时条目会亮起来
        chartBottom.setValueSelectionEnabled(true);
        chartBottom.setZoomType(ZoomType.HORIZONTAL);
    }
    private void generateInitialLineData() {
        int numValues = 7;
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, 0));
            axisValues.add(new AxisValue(i).setLabel(years[i]));
        }
        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);
        lineData = new LineChartData(lines);
        lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(6));
        chartTop.setLineChartData(lineData);
        chartTop.setViewportCalculationEnabled(false);
        Viewport v = new Viewport(0, 100000, 6, 0);
        chartTop.setMaximumViewport(v);
        chartTop.setCurrentViewport(v);
        chartTop.setZoomType(ZoomType.HORIZONTAL);
        generateLineData();
    }
    private void generateLineData() {
        Line line = lineData.getLines().get(0);
        for (int i = 0; i < line.getValues().size(); i++) {
            PointValue value = line.getValues().get(i);
            switch (i) {  //设置折线图上的每个点的值
                case 0:
                    value.setTarget(value.getX(), (float) 6000);
                    break;
                case 1:
                    value.setTarget(value.getX(), (float) 13000);
                    break;
                case 2:
                    value.setTarget(value.getX(), (float) 20000);
                    break;
                case 3:
                    value.setTarget(value.getX(), (float) 26000);
                    break;
                case 4:
                    value.setTarget(value.getX(), (float) 35000);
                    break;
                case 5:
                    value.setTarget(value.getX(), (float) 50000);
                    break;
                case 6:
                    value.setTarget(value.getX(), (float) 100000);
                    break;
            }
        }
        chartTop.startDataAnimation(300);
    }
}
