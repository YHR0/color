/*
 * Copyright 2014 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package colorrecognition.example.com.colorrecognition;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import serial.utils.SerialPort;

public abstract class SerialPortActivity extends Activity {
    
	private SerialPort mSerialPort = null;
	private InputStream mInputStream=null;
	protected OutputStream mOutputStream=null;	
	private ReadThread mReadThread=null;     
    
    final static int IOCTRL_PMU_RFID_ON  =	0x03;
	final static int IOCTRL_PMU_RFID_OFF =	0x04;
	   
	final static int IOCTRL_PMU_BARCODE_ON  = 0x05;
	final static int IOCTRL_PMU_BARCODE_OFF = 0x06;
	final static int IOCTRL_PMU_BARCODE_TRIG_LOW  = 0x12;
	private Toast t=null;
	//public static SerialPortActivity instance = null;


	public static String bytesToHexString(byte[] src){  
	    StringBuilder stringBuilder = new StringBuilder("");  
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }  
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv);  
	    }  
	    return stringBuilder.toString();  
	} 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		FTexit.getInstance().addActivity(this);
		 try {
				OpenPort();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  	
	}

    private void OpenPort() throws SecurityException, IOException
    {
    	if(mSerialPort==null)
    	{    		
    		//String path ="/dev/s3c2410_serial0";//2.3  6410:3  A8:0
    		String path ="/dev/ttyAMA4";//4.0
    		int baudrate = Integer.decode("38400");
    		int nbits = Integer.decode("8");
    		int nstop = Integer.decode("1");
    		
    		String sVerify ="N";
    		char cVerify = sVerify.charAt(0);
    		
    		/* Check parameters */
    		if ( (path.length() == 0) || (baudrate == -1) || nbits == -1 ||nstop == -1 || cVerify == 'C')
    		{
    			throw new InvalidParameterException();
    		}
    		/* Open the serial port */
    		mSerialPort = new SerialPort(new File(path), baudrate, nbits, cVerify, nstop, 0);
    		mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();
			mSerialPort.sri_Init();
			mSerialPort.sri_IOCTL(IOCTRL_PMU_BARCODE_TRIG_LOW);
			mSerialPort.sri_IOCTL(IOCTRL_PMU_BARCODE_ON);
			mSerialPort.sri_IOCTL(IOCTRL_PMU_RFID_ON);
    		mReadThread = new ReadThread();
			mReadThread.start();
    	}
    	else
    	{
    		//view1.setText("�����Ѵ�");
    		return;
    	}
    	
    }
    
	private class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			while(true) {
				try {
					Thread.sleep(200);
					int num=mInputStream.available();
					byte[] buff = new byte[num];
					int ret=0;
					ret = mInputStream.read(buff);
					if(ret>15)
					{
						onDataReceived(buff, ret);
					}			
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
	 
	
	public void closeSerialPort() {
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
	}
	
	protected abstract void onDataReceived(final byte[] buffer, final int size);

	@Override
	protected void onDestroy() {
		if (mReadThread != null)
			mReadThread.interrupt();
		closeSerialPort();
		if(mSerialPort!=null){
		mSerialPort.sri_IOCTL(IOCTRL_PMU_RFID_OFF);
		mSerialPort.sri_IOCTL(IOCTRL_PMU_BARCODE_OFF);
		mSerialPort.sri_DeInit();
		mSerialPort = null;
		}
	
		super.onDestroy();
	}
}