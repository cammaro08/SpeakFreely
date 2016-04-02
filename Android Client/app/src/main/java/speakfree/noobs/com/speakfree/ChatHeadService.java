package speakfree.noobs.com.speakfree;

        import android.app.Service;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.PixelFormat;
        import android.os.IBinder;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import com.example.chatheads.R;

public class ChatHeadService extends Service {

    private WindowManager windowManager;
    private ImageView chatHead;
    float pxs1;
    float pxs2;
    WindowManager.LayoutParams params;
    WindowManager.LayoutParams params2;
    View main_layout;
    LayoutInflater li;
    boolean destroy = false;
    public static TextView display;
    private Button button_exit;
    private Button button_accept;


    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        chatHead = new ImageView(this);
        chatHead.setImageResource(R.mipmap.de_logo);
        li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        main_layout = li.inflate(R.layout.main, null);
        display = (TextView)main_layout.findViewById(R.id.display);
        button_exit = (Button)main_layout.findViewById(R.id.button2);
        button_accept = (Button)main_layout.findViewById(R.id.button);


        params= new WindowManager.LayoutParams(
                //WindowManager.LayoutParams.WRAP_CONTENT,
                //WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params2 = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );


        //converting dip to pixels so i can use setWidth and setHeight :)
        float dps1 = 100;
        pxs1 = dps1 * getResources().getDisplayMetrics().density;
        float dps2 = 100;
        pxs2 = dps2 * getResources().getDisplayMetrics().density;
        params.width = (int) pxs1;
        params.height = (int) pxs1;

        float dps3 = 300;
        float pxs3 = dps3 * getResources().getDisplayMetrics().density;
        float dps4 = 400;
        float pxs4 = dps4 * getResources().getDisplayMetrics().density;
        params2.width = (int) pxs3;
        params2.height = (int) pxs4;


        //setting the start point and gravity settings
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        params2.gravity = Gravity.TOP | Gravity.LEFT;

        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(getApplication(), ChatHeadService.class));
            }
        });

        button_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dialogIntent = new Intent(getApplication(), Speech.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);
            }
        });

        //this code is for dragging the chat head
        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private int initialTouchX;
            private int initialTouchY;
            float x_diff;
            float y_diff;



            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = (int)  event.getRawX();
                        initialY = (int) event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        initialTouchX = (int) event.getRawX();
                        initialTouchY = (int) event.getRawY();
                        x_diff = Math.abs(initialX - initialTouchX);
                        y_diff = Math.abs(initialY - initialTouchY);
                        if (x_diff < 5 & y_diff < 5) {
                            if (destroy == true){
                                windowManager.removeView(main_layout);
                                destroy = false;
                            }else {
                                params2.x = params.x - (int) pxs1;
                                params2.y = params.y + (int) pxs2;
                                windowManager.addView(main_layout, params2);
                                destroy = true;
                            }
                            return true;
                        } else {
                            return true;
                        }
                    case MotionEvent.ACTION_MOVE:
                            params.x = (int) event.getRawX()- (chatHead.getWidth()/2);
                            params.y = (int) event.getRawY()- (chatHead.getHeight()/2);
                            windowManager.updateViewLayout(chatHead, params);
                            return true;
                }

                return false;
            }
        });

        windowManager.addView(chatHead, params);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null)
            windowManager.removeView(chatHead);
        windowManager.removeView(main_layout);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}