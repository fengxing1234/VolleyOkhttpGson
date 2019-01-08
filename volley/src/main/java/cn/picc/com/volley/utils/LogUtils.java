/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.picc.com.volley.utils;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;

import java.util.Iterator;
import java.util.Set;

/**
 * Helper methods that make logging more consistent throughout the app.
 */
public class LogUtils {

    private static final int LOG_PREFIX_LENGTH = 0;
    private static final int MAX_LOG_TAG_LENGTH = 23;

    public static String makeLogTag(String str) {
        String prefix = "";
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return prefix
                    + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH
                    - 1);
        }

        return prefix + str;
    }

    /**
     * WARNING: Don't use this when obfuscating class names with Proguard!
     */
    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }

    public static void LOGD(final String tag, String message) {
//        if (Config.DEBUG/* Log.isLoggable(tag, Log.DEBUG) */) {
            Log.d(tag, message);
//        }
    }

    public static void LOGD(final String tag, String message, Throwable cause) {
//        if (Config.DEBUG/* Log.isLoggable(tag, Log.DEBUG) */) {
            Log.d(tag, message, cause);
//        }
    }

    public static void LOGV(final String tag, String message) {
        // noinspection PointlessBooleanExpression,ConstantConditions
//        if (Config.DEBUG/* && Log.isLoggable(tag, Log.VERBOSE) */) {
            Log.v(tag, message);
//        }
    }

    public static void LOGV(final String tag, String message, Throwable cause) {
        // noinspection PointlessBooleanExpression,ConstantConditions
//        if (Config.DEBUG /* && Log.isLoggable(tag, Log.VERBOSE) */) {
            Log.v(tag, message, cause);
//        }
    }

    public static void LOGI(final String tag, String message) {
        Log.i(tag, message);
    }

    public static void LOGI(final String tag, String message, Throwable cause) {
        Log.i(tag, message, cause);
    }

    public static void LOGW(final String tag, String message) {
        Log.w(tag, message);
    }

    public static void LOGW(final String tag, String message, Throwable cause) {
        Log.w(tag, message, cause);
    }

    public static void LOGE(final String tag, String message) {
        Log.e(tag, message);
    }

    public static void LOGE(final String tag, String message, Throwable cause) {
        Log.e(tag, message, cause);
    }

    private LogUtils() {
    }

    public static String dumpIntent(Intent i) {
        StringBuilder sb = new StringBuilder();
        if (i == null) {
            sb.append(i);
        } else {
            sb.append("Dumping Intent start");
            sb.append("\nData:" + i.getData());
            sb.append("\nAction:" + i.getAction());
            sb.append("\n");
            Bundle bundle = i.getExtras();
            if (bundle != null) {
                Set<String> keys = bundle.keySet();
                Iterator<String> it = keys.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    sb.append("[" + key + "=" + bundle.get(key) + "]");
                    sb.append("\n");
                }
            }
            sb.append("Dumping Intent end");
        }
        return sb.toString();

    }

    public static String getMethodName() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();

        StackTraceElement e = stacktrace[3];
        String methodName = e.getMethodName();
        return methodName;
    }

    public static String dumpCameraSupportedInfo(Camera.Parameters p) {
        StringBuilder b = new StringBuilder();
        b.append("Supported Antibanding List:").append("\n");
        for (Object o : p.getSupportedAntibanding()) {
            b.append(o).append("\n");
        }
        b.append("Supported ColorEffects List:").append("\n");
        for (Object o : p.getSupportedColorEffects()) {
            b.append(o).append("\n");
        }
        b.append("Supported FlashModes List:").append("\n");
        for (Object o : p.getSupportedFlashModes()) {
            b.append(o).append("\n");
        }
        b.append("Supported FocusModes List:").append("\n");
        for (Object o : p.getSupportedFocusModes()) {
            b.append(o).append("\n");
        }
        b.append("Supported JpegThumbnailSizes List:").append("\n");
        for (Object o : p.getSupportedJpegThumbnailSizes()) {
            b.append(o).append("\n");
        }
        b.append("Supported PictureFormats List:").append("\n");
        for (Object o : p.getSupportedPictureFormats()) {
            b.append(o).append("\n");
        }
        b.append("Supported PictureSizes List:").append("\n");
        for (Object o : p.getSupportedPictureSizes()) {
            b.append(o).append("\n");
        }
        b.append("Supported PreviewFormats List:").append("\n");
        for (Object o : p.getSupportedPreviewFormats()) {
            b.append(o).append("\n");
        }
        b.append("Supported PreviewFpsRange List:").append("\n");
        for (Object o : p.getSupportedPreviewFpsRange()) {
            b.append(o).append("\n");
        }
        b.append("Supported PreviewFrameRates List:").append("\n");
        for (Object o : p.getSupportedPreviewFrameRates()) {
            b.append(o).append("\n");
        }
        b.append("Supported PreviewFpsRange List:").append("\n");
        for (Object o : p.getSupportedPreviewFpsRange()) {
            b.append(o).append("\n");
        }
        b.append("Supported PreviewSizes List:").append("\n");
        for (Object o : p.getSupportedPreviewSizes()) {
            b.append(o).append("\n");
        }
        b.append("Supported SceneModes List:").append("\n");
        for (Object o : p.getSupportedSceneModes()) {
            b.append(o).append("\n");
        }
        b.append("Supported VideoSizes List:").append("\n");
        for (Object o : p.getSupportedVideoSizes()) {
            b.append(o).append("\n");
        }
        b.append("Supported WhiteBalance List:").append("\n");
        for (Object o : p.getSupportedWhiteBalance()) {
            b.append(o).append("\n");
        }

        b.append("Zoom Ratios:" + p.getZoomRatios().size()).append("\n");
        for (Object o : p.getZoomRatios()) {
            b.append(o).append(",");
        }
        b.append("\n");


        b.append("MaxExposureCompensation: ").append(p.getMaxExposureCompensation() + "\n");
        b.append("MaxNumDetectedFaces: ").append(p.getMaxNumDetectedFaces() + "\n");
        b.append("MaxNumFocusAreas: ").append(p.getMaxNumFocusAreas() + "\n");
        b.append("MaxNumMeteringAreas: ").append(p.getMaxNumMeteringAreas() + "\n");
        b.append("MaxZoom: ").append(p.getMaxZoom() + "\n");


        return b.toString();
    }
}
