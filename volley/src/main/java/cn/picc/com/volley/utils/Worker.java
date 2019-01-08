package cn.picc.com.volley.utils;

import android.os.Looper;

public class Worker implements Runnable {
	        private final Object mLock = new Object();
	        private Looper mLooper;
	        

	        public Worker(String name) {
	            Thread t = new Thread(null, this, name);
	            t.setPriority(Thread.MIN_PRIORITY);
	            t.start();
	            synchronized (mLock) {
	                while (mLooper == null) {
	                    try {
	                        mLock.wait();
	                    } catch (InterruptedException ex) {
	                    }
	                }
	            }
	        }
	        
	        public Looper getLooper() {
	            return mLooper;
	        }
	        
	        public void run() {
	            synchronized (mLock) {
	                Looper.prepare();
	                mLooper = Looper.myLooper();
	                mLock.notifyAll();
	            }
	            Looper.loop();
	        }
	        
	        public void quit() {
	            mLooper.quit();
	        }
	    }