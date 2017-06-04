package things.useful.opencv_demo_6;

import android.Manifest;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AbsRuntimePermission implements CameraBridgeViewBase.CvCameraViewListener2{

    Button but1;
    Button but2;
    Button but3;
    Button but4;
    Button but5;

    int pozicija = 1;


    private static final String TAG="MainActivity";
    private static final int REQUEST_PERMISSION = 10;
    JavaCameraView javaCameraView1;
    Mat mRgba, imgGray, imgCanny, imgHAOS;
    BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {

            switch (status){
                case BaseLoaderCallback.SUCCESS:{
                    javaCameraView1.enableView();
                    break;
                }
                default:{
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };

    static {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestAppPermissions(new String[]{
                        Manifest.permission.CAMERA},
                R.string.msg,REQUEST_PERMISSION);

        javaCameraView1 = (JavaCameraView)findViewById(R.id.java_camera_view1);
        javaCameraView1.setVisibility(SurfaceView.VISIBLE);
        javaCameraView1.setCvCameraViewListener(this);

        but1 = (Button)findViewById(R.id.button1);
        but2 = (Button)findViewById(R.id.button2);
        but3 = (Button)findViewById(R.id.button3);
        but4 = (Button)findViewById(R.id.button4);
        but5 = (Button)findViewById(R.id.button5);

        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pozicija = 1;
            }
        });

        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pozicija = 2;
            }
        });
        but3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pozicija = 3;
            }
        });
        but4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pozicija = 4;
            }
        });
        but5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pozicija = 5;
            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(javaCameraView1!=null)
            javaCameraView1.disableView();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(javaCameraView1!=null)
            javaCameraView1.disableView();
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(OpenCVLoader.initDebug()){
            Log.d(TAG, "OpenCV secsesfully loaded :)");
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);

        }else{
            Log.d(TAG, "OpenCV not loaded :(");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallBack);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8SC4); //zato sto imamo 4 kanala upotrebljavamo    CV_8SC4
        imgGray = new Mat(height, width, CvType.CV_8SC1);
        imgCanny = new Mat(height, width, CvType.CV_8SC1);
        imgHAOS = new Mat(height, width, CvType.CV_8UC4);

    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {



        //Imgproc.cvtColor(mRgba, imgGray, Imgproc.COLOR_RGB2Luv);
        //Imgproc.Canny(imgGray, imgCanny, 50, 150);



        switch (pozicija){
            case 1:
                mRgba = inputFrame.rgba();
                Imgproc.cvtColor(mRgba, imgGray, Imgproc.COLOR_BGR2GRAY);
                break;
            case 2:
                mRgba = inputFrame.rgba();
                Imgproc.cvtColor(mRgba, imgGray, Imgproc.COLOR_RGB2Luv);
                break;
            case 3:
                mRgba = inputFrame.rgba();
                Imgproc.cvtColor(mRgba, imgGray, Imgproc.COLOR_RGB2GRAY);
                Imgproc.Canny(imgGray, imgCanny, 50, 150);
                return imgCanny;
            case 4:
                mRgba = inputFrame.rgba();
                return mRgba;
            case 5:
                mRgba = inputFrame.rgba();
                Imgproc.cvtColor(mRgba, mRgba, Imgproc.COLOR_RGB2Lab);
                return mRgba;
            default:
                mRgba = inputFrame.rgba();
                break;
        }


        return imgGray;
    }
}
