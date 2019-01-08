package cn.picc.com.volley.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.util.Log;

import java.io.File;

public class ImageFolderViewer {
	private MediaScannerConnection mMediaScannerConnection;
	private String mPath;
	private Context mContext;
	private final String FILE_TYPE = "images/*";
	private boolean scan;
	private MediaScannerConnectionClientImpl mMediaScannerConnectionClient;

	private ImageFolderViewer() {
		mMediaScannerConnectionClient = new MediaScannerConnectionClientImpl();
	}

	private static final ImageFolderViewer viewer = new ImageFolderViewer();

	public static void view(Context context, String path) {
		viewer.scan = false;
		viewer.mContext = context;
		viewer.mPath = toValidPath(path);
		viewer.startScan();
		
		viewer.mContext.sendStickyBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
				.parse("file://" + FileTools.getSdcardPath(context))));
	}

	public static void scan(Context context, String path) {
		viewer.scan = true;
		viewer.mContext = context;
		viewer.mPath = path;
		viewer.startScan();
	}

	private void startScan() {
		Log.d("Connected", "success " + mMediaScannerConnection);
		if (mMediaScannerConnection != null) {
			mMediaScannerConnection.disconnect();
		}

		mMediaScannerConnection = new MediaScannerConnection(mContext,
				mMediaScannerConnectionClient);
		mMediaScannerConnection.connect();
	}

	private static String toValidPath(String path){
		Log.d("toValidPath",path);
		File f =new File(path);
		if(!f.exists()){
			File parent = f.getParentFile();
			for(File s:parent.listFiles()){
				if(s.exists()){
					Log.d("toValidPath","TO:"+s.getAbsolutePath());
					return s.getAbsolutePath();
				}
			}
			Log.d("toValidPath","Parent:"+parent.getAbsolutePath());
			return parent.getAbsolutePath()+"/NO.JPG";
		}
		Log.d("toValidPath","exists");
		return f.getAbsolutePath();
	}
	
	private class MediaScannerConnectionClientImpl implements
            MediaScannerConnectionClient {
		public void onMediaScannerConnected() {
			Log.d("onMediaScannerConnected", "success "
					+ mMediaScannerConnection);
			
			mMediaScannerConnection.scanFile(mPath, FILE_TYPE);

		}

		public void onScanCompleted(String path, Uri uri) {
			try {
				Log.d("onScanCompleted ", uri + " success "
						+ mMediaScannerConnection);
				Log.d("onScanCompleted ","path:"+path);
				if (!scan && uri != null) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(uri, "image/*");
					mContext.startActivity(intent);
				}
			} finally {
				mMediaScannerConnection.disconnect();
				mMediaScannerConnection = null;
			}
		}
	}

}