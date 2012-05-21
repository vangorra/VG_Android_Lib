/* The following code was written by Matthew Wiggins
 * and is released under the APACHE 2.0 license
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.vg.lib;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class ShakeListener implements SensorEventListener 
{
  private static final int FORCE_THRESHOLD = 350; // threshold before action is considered a shake.
  private static final int TIME_THRESHOLD = 100; // minimum length of time for the shake.
  private static final int SHAKE_TIMEOUT = 500; // timeout before shake resets.
  private static final int SHAKE_DURATION = 1000;
  private static final int SHAKE_COUNT = 3;

  private SensorManager mSensorMgr;
  private float mLastX=-1.0f, mLastY=-1.0f, mLastZ=-1.0f;
  private long mLastTime;
  private OnShakeListener mShakeListener;
  private Context mContext;
  private int mShakeCount = 0;
  private long mLastShake;
  private long mLastForce;
  private boolean supported = false;

  public interface OnShakeListener
  {
    public void onShake();
  }

  public ShakeListener(Context context) 
  { 
    mContext = context;
    resume();
  }

  public void setOnShakeListener(OnShakeListener listener)
  {
    mShakeListener = listener;
  }

  public boolean isShakeSupported() {
	return this.supported;
  }
  
  public void resume() {
    mSensorMgr = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
    if (mSensorMgr == null) {
      //throw new UnsupportedOperationException("Sensors not supported");
    	return;
    }
    //boolean supported = mSensorMgr.registerListener(this, SensorManager.SENSOR_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);
    supported = mSensorMgr.registerListener(this, mSensorMgr.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    if (!supported) {
      //mSensorMgr.unregisterListener(this, SensorManager.SENSOR_ACCELEROMETER);
      mSensorMgr.unregisterListener(this, mSensorMgr.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER));
      //throw new UnsupportedOperationException("Accelerometer not supported");
      return;
    }
  }

  public void pause() {
    if (mSensorMgr != null) {
      //mSensorMgr.unregisterListener(this, SensorManager.SENSOR_ACCELEROMETER);
      mSensorMgr.unregisterListener(this, mSensorMgr.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER));
      mSensorMgr = null;
    }
  }

  /*public void onAccuracyChanged(int sensor, int accuracy) { }

  public void onSensorChanged(int sensor, float[] values) 
  {
    if (sensor != SensorManager.SENSOR_ACCELEROMETER) return;
    long now = System.currentTimeMillis();

    if ((now - mLastForce) > SHAKE_TIMEOUT) {
      mShakeCount = 0;
    }

    if ((now - mLastTime) > TIME_THRESHOLD) {
      long diff = now - mLastTime;
      float speed = Math.abs(values[SensorManager.DATA_X] + values[SensorManager.DATA_Y] + values[SensorManager.DATA_Z] - mLastX - mLastY - mLastZ) / diff * 10000;
      if (speed > FORCE_THRESHOLD) {
        if ((++mShakeCount >= SHAKE_COUNT) && (now - mLastShake > SHAKE_DURATION)) {
          mLastShake = now;
          mShakeCount = 0;
          if (mShakeListener != null) { 
            mShakeListener.onShake(); 
          }
        }
        mLastForce = now;
      }
      mLastTime = now;
      mLastX = values[SensorManager.DATA_X];
      mLastY = values[SensorManager.DATA_Y];
      mLastZ = values[SensorManager.DATA_Z];
    }
  }*/

	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void onSensorChanged(SensorEvent arg0) {
		float[] values = arg0.values;
	    if (arg0.sensor.getType() != SensorManager.SENSOR_ACCELEROMETER) return;
	    long now = System.currentTimeMillis();

	    if ((now - mLastForce) > SHAKE_TIMEOUT) {
	      mShakeCount = 0;
	      return;
	    }

	    if ((now - mLastTime) > TIME_THRESHOLD) {
	      long diff = now - mLastTime;
	      float speed = Math.abs(values[SensorManager.DATA_X] + values[SensorManager.DATA_Y] + values[SensorManager.DATA_Z] - mLastX - mLastY - mLastZ) / diff * 10000;
//	      Log.v("ShakeListenForce:", speed+"");
	      if (speed > FORCE_THRESHOLD) {
	        if ((++mShakeCount >= SHAKE_COUNT) && (now - mLastShake > SHAKE_DURATION)) {
	          mLastShake = now;
	          mShakeCount = 0;
	          if (mShakeListener != null) { 
	            mShakeListener.onShake(); 
	          }
	        }
	        mLastForce = now;
	      }
	      mLastTime = now;
	      mLastX = values[SensorManager.DATA_X];
	      mLastY = values[SensorManager.DATA_Y];
	      mLastZ = values[SensorManager.DATA_Z];
	    }
	}

}