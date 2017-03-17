package lsp.com.library;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsp on 2017/2/13.
 */

public class FlowLayout extends ViewGroup {

    List<ViewBean> list = new ArrayList<>();

    private IOnTabClickListener iOnTabClickListener;


    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setOnTabClickListener(IOnTabClickListener iOnTabClickListener) {
        this.iOnTabClickListener = iOnTabClickListener;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    public void initData(List<String> list) {
        MarginLayoutParams pa = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT,
                MarginLayoutParams.WRAP_CONTENT);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            TextView tv = new TextView(getContext());
            tv.setText(list.get(i));
            tv.setTag(list.get(i));
            setOnclick(i,tv);
            addView(tv, pa);
            ((MarginLayoutParams) tv.getLayoutParams()).setMargins(dp2px(getContext(), 5), dp2px(getContext(), 5),
                    dp2px(getContext(), 5), dp2px(getContext(), 5));
        }
    }

    public void initData(List<String> list, int margin, int drawable) {
        MarginLayoutParams pa = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT,
                MarginLayoutParams.WRAP_CONTENT);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            TextView tv = new TextView(getContext());
            tv.setText(list.get(i));
            tv.setTag(list.get(i));
            setOnclick(i,tv);
            addView(tv, pa);
            ((MarginLayoutParams) tv.getLayoutParams()).setMargins(dp2px(getContext(), margin), dp2px(getContext(), margin),
                    dp2px(getContext(), margin), dp2px(getContext(), margin));
            tv.setBackgroundResource(drawable);
        }
    }

    private void setOnclick(final int position, final TextView textView) {

        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                iOnTabClickListener.onTabClick(position, textView);
            }
        });
    }


    public int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

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

    public interface IOnTabClickListener {

        void onTabClick(int position, TextView textView);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            getChildAt(i).layout(list.get(i).getLeft(), list.get(i).getTop(), list.get(i).getRight(), list.get(i).getBottom());
        }

    }

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


}
