最近一直在学习自定义View和自定义ViewGroup的东西，今天没事干就研究了一下FlowLayout，记录下实现步骤
参考自[鸿洋博客]( http://blog.csdn.net/lmj623565791/article/details/38352503/ )

还有[我的简书地址](http://www.jianshu.com/p/0d6e6f2a98a9)

# AndroidStudio使用
在根projcet的build.gradle中添加.

`maven { url 'https://jitpack.io' }`

在项目的build.gradle添加:

` compile 'com.github.superSp:FlowLayout:1.0'`

#### 效果图

自适应

![图片.png](http://upload-images.jianshu.io/upload_images/1168278-28361af1faf3ba64.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

固定大小

![图片.png](http://upload-images.jianshu.io/upload_images/1168278-2732872566476fee.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 使用方法——静态添加
```
<lsp.com.library.FlowLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <TextView
            style="@style/text_flag_01"
            android:text="Welcome" />

        <TextView
            style="@style/text_flag_01"
            android:text="IT工程师" />

        <TextView
            style="@style/text_flag_01"
            android:text="学习ing" />

        <TextView
            style="@style/text_flag_01"
            android:text="恋爱ing" />

        <TextView
            style="@style/text_flag_02"
            android:text="恋爱ing" />

        <TextView
            style="@style/text_flag_02"
            android:text="挣钱ing" />

        <TextView
            style="@style/text_flag_02"
            android:text="努力ing" />

        <TextView
            style="@style/text_flag_02"
            android:text="I thick i can" />
</lsp.com.library.FlowLayout>

style文件：

  <style name="text_flag_01">
        <item name="android:layout_width">wrap_content</item>
        <item name="android:layout_height">wrap_content</item>
        <item name="android:layout_margin">5dp</item>
        <item name="android:background">@drawable/flag_01</item>
    </style>

drawable文件：

样式1（flag_01）
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
<solid android:color="#7690A5"/>

    <corners android:radius="5dp"/>

    <padding android:bottom="2dp"
        android:left="2dp"
        android:right="2dp"
        android:top="2dp"/>
</shape>

样式2（flag_02）
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" >

    <solid android:color="#E7E7E7" >
    </solid>
    <corners
        android:radius="30dp"
        />

    <padding
        android:bottom="2dp"
        android:left="10dp"
        android:right="10dp"
        android:top="2dp" />
</shape>

按自己喜好设置就行~~
```
#### 使用方法——动态添加
```
        FlowLayout flowLayout = new FlowLayout(this);

        flowLayout.initData(Arrays.asList("Welcome","IT工程师","学习ing","恋爱ing"
        ,"挣钱ing","努力ing","I thick i can"));

//        flowLayout.initData(Arrays.asList("Welcome", "IT工程师", "学习ing", "恋爱ing"
//                , "挣钱ing", "努力ing", "I thick i can"), 5, R.drawable.flag_02);

        setContentView(flowLayout);
```
#### initData方法
```
public void initData(List<String> list)
public void initData(List<String> list, int margin, int drawable) 
```
#### 添加点击事件
```
 flowLayout.setOnTabClickListener(new FlowLayout.IOnTabClickListener() {

            @Override
            public void onTabClick(int position, TextView textView) {
                Toast.makeText(Test.this,position+" "+textView.getText(),Toast.LENGTH_SHORT).show();
            }
        });
        setContentView(flowLayout);
```

#### 实现思路
  ##### 构造方法
```
    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
```
##### 子view支持margin属性
```
 @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
```
#### onMeasure方法
```
 @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //测量子view的大小
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        //最终的宽和高
        int width = 0;
        int height = 0;
        //记录每一行的宽和高
        int lineWidth = 0;
        //子view数量
        int cCount = getChildCount();
        View cView;
        MarginLayoutParams params;
        int cWidth, cHeight;

        for (int i = 0; i < cCount; i++) {
            cView = getChildAt(i);
            params = (MarginLayoutParams) cView.getLayoutParams();
            cWidth = cView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            cHeight = cView.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            ViewBean viewBean = new ViewBean();
          
            if (lineWidth + cWidth < widthSize) {

                lineWidth += cWidth;

                height = Math.max(height, cHeight);

                width = Math.max(lineWidth, width);

            } else {

                width = Math.max(lineWidth, width);

                height += cHeight;

                lineWidth = cWidth;

            }

            viewBean.setLeft(lineWidth - cWidth + params.leftMargin);
            viewBean.setTop(height - cHeight + params.topMargin);
            viewBean.setRight(lineWidth - params.rightMargin);
            viewBean.setBottom(height - params.bottomMargin);

            list.add(viewBean);

        }
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width, heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }
```
##### onLayout方法
```
 @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            getChildAt(i).layout(list.get(i).getLeft(), list.get(i).getTop(), list.get(i).getRight(), list.get(i).getBottom());
        }

    }
```
##### ViewBean方法
```
private class ViewBean {
        private int left;
        private int right;
        private int top;
        private int bottom;

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public int getRight() {
            return right;
        }

        public void setRight(int right) {
            this.right = right;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getBottom() {
            return bottom;
        }

        public void setBottom(int bottom) {
            this.bottom = bottom;
        }
    }
```
##### 动态填充数据实现
```
 public void initData(List<String> list) {
        ViewGroup.MarginLayoutParams pa = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            TextView tv = new TextView(getContext());
            tv.setText(list.get(i));
            addView(tv, pa);
            ((ViewGroup.MarginLayoutParams) tv.getLayoutParams()).setMargins(DensityUtils.dp2px(getContext(), 5), DensityUtils.dp2px(getContext(), 5),
                    DensityUtils.dp2px(getContext(), 5), DensityUtils.dp2px(getContext(), 5));
            tv.setBackgroundResource(R.drawable.flag_01);
        }
    }
```
#### 添加点击事件
```
public interface IOnTabClickListener {
        void onTabClick(int position, TextView textView);
    }

private void setOnclick(final int position, final TextView textView) {
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                iOnTabClickListener.onTabClick(position, textView);
            }
        });
    }
```
##### 后记
   比较难理解的部分应该就是onMeasure了。。当设置wrap的时候找到宽度最大的值。。并且设置每个view的坐标.
