package pennapps.campicdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lenovo on 2015/8/30.
 */
public class CustomDrawableView extends View {
    private ShapeDrawable mDrawable;

    private void init() {
        int x = 0;
        int y = 0;
        int width = 300;
        int height = 50;

        mDrawable = new ShapeDrawable(new RectShape());
        mDrawable.getPaint().setColor(0xff74AC23);
        mDrawable.setBounds(x, y, x+width, y+height);
        mDrawable.setVisible(false, true);
    }

    public CustomDrawableView(Context context) {
        super(context);
        init();
    }

    public CustomDrawableView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public CustomDrawableView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        init();
    }

    public void setBounds(int x, int y, int width, int height) {
        mDrawable.setBounds(x, y, x+width, y+height);
    }

    protected void onDraw(Canvas canvas) {
        mDrawable.draw(canvas);
    }
}
