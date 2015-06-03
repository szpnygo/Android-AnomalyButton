package info.smemo.androidanomalybutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 不规则图片点击事件
 * 透明元素不响应
 * 支持两种模式
 * 一种只有press（点击背景，显示出来press的图，所以用press为判断）
 * 一种正常的selectoer（正常的）
 */
public class AnomalyButton extends ImageView {

    //当前布局尺寸
    private int width = -1;
    private int height = -1;

    private Bitmap bitmap;
    private Context context;

    //实际图片缩放比例
    float viewScaleX;
    float viewScaleY;

    private int bgColor;

    public AnomalyButton(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AnomalyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public AnomalyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.AnomalyButton);
        bgColor=a.getColor(R.styleable.AnomalyButton_bgcolor,android.R.color.transparent);
        init();
    }

    public void init() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event);
        }
        //获取背景selector
        if (width == -1 || height == -1) {
            Object object=getDrawable();
            if(object instanceof BitmapDrawable){
                //如果没有使用selector
                BitmapDrawable bitmapDrawable=(BitmapDrawable)object;
                bitmap=bitmapDrawable.getBitmap();
            }else {

                //获取selector
                StateListDrawable drawable = ((StateListDrawable) object);
                Class<StateListDrawable> c = StateListDrawable.class;
                try {
                    //获取背景selecttor个数，如果有两个，则取当前的
                    Method methodCount = c.getMethod("getStateCount");
                    int count = (int) methodCount.invoke(drawable);
                    if (count > 1) {
                        Drawable selectorDrawable = drawable.getCurrent();
                        bitmap = ((BitmapDrawable) selectorDrawable).getBitmap();
                    } else {
                        //获取没有显示的特定图片
                        Method method = c.getMethod("getStateDrawable", int.class);
                        Drawable selectorDrawable = (Drawable) method.invoke(drawable, 0);
                        bitmap = ((BitmapDrawable) selectorDrawable).getBitmap();
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            width = getWidth();
            height = getHeight();

            viewScaleX = (width / (float) bitmap.getWidth());
            viewScaleY = (height / (float) bitmap.getHeight());
        }
        int x = (int) (event.getX() / viewScaleX);
        int y = (int) (event.getY() / viewScaleY);
        //判断请求是否超出范围
        if (null == bitmap || x < 0 || y < 0 || x >= width || y >= height) {
            return false;
        }
        //获取像素
        int pixel = bitmap.getPixel(x, y);
        //判断像素是否是指定空白颜色，阻止请求
        if (pixel == bgColor) {
            return false;
        }
        return super.onTouchEvent(event);
    }

}