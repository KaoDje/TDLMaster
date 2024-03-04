package com.example.tdlmaster;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class CanvaUnderlineElement extends View {
    private Paint paint;
    private float x;
    private float y;
    public CanvaUnderlineElement(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        x = getWidth() / 6;
        y = 170;
        canvas.drawCircle(x, y, 15, paint);
        canvas.drawCircle(x*5, y, 15, paint);

        float stroke = 3;
        paint.setStyle(Paint.Style.FILL);
        Path path=new Path();
        path.moveTo(x*5, y-stroke);
        path.lineTo(x, y-stroke);
        path.lineTo(x, y+stroke);
        path.lineTo(x*5, y+stroke);
        canvas.drawPath(path, paint);
    }
}
