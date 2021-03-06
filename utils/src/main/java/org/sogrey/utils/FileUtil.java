/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sogrey.utils;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.apache.http.Header;
//import org.apache.http.HttpResponse;

/**
 * © 2012 amsoft.cn 名称：AbFileUtil.java 描述：文件操作类.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-01-18 下午11:52:13
 */
@SuppressLint("DefaultLocale")
public class FileUtil {

    private static final String TAG = "AbFileUtil";
    /**
     * 默认下载文件地址.
     */
    public static String DOWNLOAD_ROOT_DIR = "app";

    /**
     * 默认下载图片文件地址.
     */
    public static String DOWNLOAD_IMAGE_DIR = "images";

    /**
     * 默认下载文件地址.
     */
    public static String DOWNLOAD_FILE_DIR = "files";

    /**
     * APP缓存目录.
     */
    public static String CACHE_DIR = "cache";

    /**
     * DB目录.
     */
    public static String DB_DIR = "db";

    /**
     * 日志目录.
     */
    public static String LOG_DIR = "log";
    /**
     * 默认APP根目录.
     */
    private static String downloadRootDir = null;

    /**
     * 默认下载图片文件目录.
     */
    private static String imageDownloadDir = null;

    /**
     * 默认下载文件目录.
     */
    private static String fileDownloadDir = null;

    /**
     * 默认缓存目录.
     */
    private static String cacheDownloadDir = null;

    /**
     * 默认下载数据库文件的目录.
     */
    private static String dbDownloadDir = null;

    /**
     * 默认日志的目录.
     */
    private static String logDir = null;

    /**
     * 剩余空间大于200M才使用SD缓存.
     */
    private static int freeSdSpaceNeededToCache = 200 * 1024 * 1024;

    /**
     * 描述：通过文件的网络地址从SD卡中读取图片，如果SD中没有则自动下载并保存.
     *
     * @param url           文件的网络地址
     * @param type          图片的处理类型（剪切或者缩放到指定大小，参考AbImageUtil类） 如果设置为原图，则后边参数无效，得到原图
     * @param desiredWidth  新图片的宽
     * @param desiredHeight 新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap getBitmapFromSD(String url, int type,
                                         int desiredWidth, int desiredHeight) {
        Bitmap bitmap = null;
        try {
            if (TextUtils.isEmpty(url)) {
                return null;
            }

            // SD卡不存在 或者剩余空间不足了就不缓存到SD卡了
            if (!isCanUseSD() || freeSdSpaceNeededToCache < freeSpaceOnSD()) {
                bitmap = getBitmapFromURL(url, type, desiredWidth,
                        desiredHeight);
                return bitmap;
            }
            // 下载文件，如果不存在就下载，存在直接返回地址
            String downFilePath = downloadFile(url, imageDownloadDir);
            if (downFilePath != null) {
                // 获取图片
                return getBitmapFromSD(new File(downFilePath), type,
                        desiredWidth, desiredHeight);
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 描述：通过文件的本地地址从SD卡读取图片.
     *
     * @param file          the file
     * @param type          图片的处理类型（剪切或者缩放到指定大小，参考AbConstant类） 如果设置为原图，则后边参数无效，得到原图
     * @param desiredWidth  新图片的宽
     * @param desiredHeight 新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap getBitmapFromSD(File file, int type, int desiredWidth,
                                         int desiredHeight) {
        Bitmap bitmap = null;
        try {
            // SD卡是否存在
            if (!isCanUseSD()) {
                return null;
            }

            // 文件是否存在
            if (!file.exists()) {
                return null;
            }

            // 文件存在
            if (type == AbImageUtil.CUTIMG) {
                bitmap = AbImageUtil.cutImg(file, desiredWidth, desiredHeight);
            } else if (type == AbImageUtil.SCALEIMG) {
                bitmap = AbImageUtil
                        .scaleImg(file, desiredWidth, desiredHeight);
            } else {
                bitmap = AbImageUtil.getBitmap(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 描述：通过文件的本地地址从SD卡读取图片.
     *
     * @param file the file
     * @return Bitmap 图片
     */
    public static Bitmap getBitmapFromSD(File file) {
        Bitmap bitmap = null;
        try {
            // SD卡是否存在
            if (!isCanUseSD()) {
                return null;
            }
            // 文件是否存在
            if (!file.exists()) {
                return null;
            }
            // 文件存在
            bitmap = AbImageUtil.getBitmap(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 描述：将图片的byte[]写入本地文件.
     *
     * @param imgByte       图片的byte[]形势
     * @param fileName      文件名称，需要包含后缀，如.jpg
     * @param type          图片的处理类型（剪切或者缩放到指定大小，参考AbConstant类）
     * @param desiredWidth  新图片的宽
     * @param desiredHeight 新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap getBitmapFromByte(byte[] imgByte, String fileName,
                                           int type, int desiredWidth, int desiredHeight) {
        FileOutputStream fos = null;
        DataInputStream dis = null;
        ByteArrayInputStream bis = null;
        Bitmap bitmap = null;
        File file = null;
        try {
            if (imgByte != null) {

                file = new File(imageDownloadDir + fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                int readLength = 0;
                bis = new ByteArrayInputStream(imgByte);
                dis = new DataInputStream(bis);
                byte[] buffer = new byte[1024];

                while ((readLength = dis.read(buffer)) != -1) {
                    fos.write(buffer, 0, readLength);
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                fos.flush();

                bitmap = getBitmapFromSD(file, type, desiredWidth,
                        desiredHeight);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * 描述：根据URL从互连网获取图片.
     *
     * @param url           要下载文件的网络地址
     * @param type          图片的处理类型（剪切或者缩放到指定大小，参考AbConstant类）
     * @param desiredWidth  新图片的宽
     * @param desiredHeight 新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap getBitmapFromURL(String url, int type,
                                          int desiredWidth, int desiredHeight) {
        Bitmap bit = null;
        try {
            bit = AbImageUtil.getBitmap(url, type, desiredWidth, desiredHeight);
        } catch (Exception e) {
            LogUtil.d(FileUtil.class, "下载图片异常：" + e.getMessage());
        }
        return bit;
    }

    /**
     * 描述：获取src中的图片资源.
     *
     * @param src 图片的src路径，如（“image/arrow.png”）
     * @return Bitmap 图片
     */
    public static Bitmap getBitmapFromSrc(String src) {
        Bitmap bit = null;
        try {
            bit = BitmapFactory.decodeStream(FileUtil.class
                    .getResourceAsStream(src));
        } catch (Exception e) {
            LogUtil.d(FileUtil.class, "获取图片异常：" + e.getMessage());
        }
        return bit;
    }

    /**
     * 根据Uri 获取真实路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 描述：获取Asset中的图片资源.
     *
     * @param context  the context
     * @param fileName the file name
     * @return Bitmap 图片
     */
    public static Bitmap getBitmapFromAsset(Context context, String fileName) {
        Bitmap bit = null;
        try {
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open(fileName);
            bit = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            LogUtil.d(FileUtil.class, "获取图片异常：" + e.getMessage());
        }
        return bit;
    }

    /**
     * 描述：获取Asset中的图片资源.
     *
     * @param context  the context
     * @param fileName the file name
     * @return Drawable 图片
     */
    public static Drawable getDrawableFromAsset(Context context, String fileName) {
        Drawable drawable = null;
        try {
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open(fileName);
            drawable = Drawable.createFromStream(is, null);
        } catch (Exception e) {
            LogUtil.d(FileUtil.class, "获取图片异常：" + e.getMessage());
        }
        return drawable;
    }

    /**
     * 下载网络文件到SD卡中.如果SD中存在同名文件将不再下载
     *
     * @param url     要下载文件的网络地址
     * @param dirPath the dir path
     * @return 下载好的本地文件地址
     */
    public static String downloadFile(String url, String dirPath) {
        InputStream in = null;
        FileOutputStream fileOutputStream = null;
        HttpURLConnection connection = null;
        String downFilePath = null;
        File file = null;
        try {
            if (!isCanUseSD()) {
                return null;
            }
            // 先判断SD卡中有没有这个文件，不比较后缀部分比较
            String fileNameNoMIME = getCacheFileNameFromUrl(url);
            File parentFile = new File(dirPath);
            File[] files = parentFile.listFiles();
            for (File file1 : files) {
                String fileName = file1.getName();
                String name = fileName.substring(0, fileName.lastIndexOf("."));
                if (name.equals(fileNameNoMIME)) {
                    // 文件已存在
                    return file1.getPath();
                }
            }

            URL mUrl = new URL(url);
            connection = (HttpURLConnection) mUrl.openConnection();
            connection.connect();
            // 获取文件名，下载文件
            String fileName = getCacheFileNameFromUrl(url, connection);

            file = new File(imageDownloadDir, fileName);
            downFilePath = file.getPath();
            if (!file.exists()) {
                file.createNewFile();
            } else {
                // 文件已存在
                return file.getPath();
            }
            in = connection.getInputStream();
            fileOutputStream = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int temp = 0;
            while ((temp = in.read(b)) != -1) {
                fileOutputStream.write(b, 0, temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(FileUtil.class, "有文件下载出错了,已删除");
            // 检查文件大小,如果文件为0B说明网络不好没有下载成功，要将建立的空文件删除
            if (file != null) {
                file.delete();
            }
            file = null;
            downFilePath = null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return downFilePath;
    }

    /**
     * 描述：获取网络文件的大小.
     *
     * @param Url 图片的网络路径
     * @return int 网络文件的大小
     */
    public static int getContentLengthFromUrl(String Url) {
        int mContentLength = 0;
        try {
            URL url = new URL(Url);
            HttpURLConnection mHttpURLConnection = (HttpURLConnection) url
                    .openConnection();
            mHttpURLConnection.setConnectTimeout(5 * 1000);
            mHttpURLConnection.setRequestMethod("GET");
            mHttpURLConnection
                    .setRequestProperty(
                            "Accept",
                            "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
            mHttpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
            mHttpURLConnection.setRequestProperty("Referer", Url);
            mHttpURLConnection.setRequestProperty("Charset", "UTF-8");
            mHttpURLConnection
                    .setRequestProperty(
                            "User-Agent",
                            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
            mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            mHttpURLConnection.connect();
            if (mHttpURLConnection.getResponseCode() == 200) {
                // 根据响应获取文件大小
                mContentLength = mHttpURLConnection.getContentLength();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d(FileUtil.class, "获取长度异常：" + e.getMessage());
        }
        return mContentLength;
    }

    /**
     * 获取文件名，通过网络获取.
     *
     * @param url 文件地址
     * @return 文件名
     */
    public static String getRealFileNameFromUrl(String url) {
        String name = null;
        try {
            if (TextUtils.isEmpty(url)) {
                return name;
            }

            URL mUrl = new URL(url);
            HttpURLConnection mHttpURLConnection = (HttpURLConnection) mUrl
                    .openConnection();
            mHttpURLConnection.setConnectTimeout(5 * 1000);
            mHttpURLConnection.setRequestMethod("GET");
            mHttpURLConnection
                    .setRequestProperty(
                            "Accept",
                            "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
            mHttpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
            mHttpURLConnection.setRequestProperty("Referer", url);
            mHttpURLConnection.setRequestProperty("Charset", "UTF-8");
            mHttpURLConnection.setRequestProperty("User-Agent", "");
            mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            mHttpURLConnection.connect();
            if (mHttpURLConnection.getResponseCode() == 200) {
                for (int i = 0; ; i++) {
                    String mine = mHttpURLConnection.getHeaderField(i);
                    if (mine == null) {
                        break;
                    }
                    if ("content-disposition".equals(mHttpURLConnection
                            .getHeaderFieldKey(i).toLowerCase())) {
                        Matcher m = Pattern.compile(".*filename=(.*)").matcher(
                                mine.toLowerCase());
                        if (m.find())
                            return m.group(1).replace("\"", "");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(FileUtil.class, "网络上获取文件名失败");
        }
        return name;
    }

    /**
     * 获取真实文件名（xx.后缀），通过网络获取.
     *
     * @param connection 连接
     * @return 文件名
     */
    public static String getRealFileName(HttpURLConnection connection) {
        String name = null;
        try {
            if (connection == null) {
                return name;
            }
            if (connection.getResponseCode() == 200) {
                for (int i = 0; ; i++) {
                    String mime = connection.getHeaderField(i);
                    if (mime == null) {
                        break;
                    }
                    // "Content-Disposition","attachment; filename=1.txt"
                    // Content-Length
                    if ("content-disposition".equals(connection
                            .getHeaderFieldKey(i).toLowerCase())) {
                        Matcher m = Pattern.compile(".*filename=(.*)").matcher(
                                mime.toLowerCase());
                        if (m.find()) {
                            return m.group(1).replace("\"", "");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(FileUtil.class, "网络上获取文件名失败");
        }
        return name;
    }

    /**
     * 获取文件名带后缀名（实际原理是取“/”最后一段）
     *
     * @param path 文件路径（本地或网络路径）
     * @return
     * @author Sogrey
     * @date 2015年6月30日
     */
    public static String getFileName(String path) {
        return new File(path.trim()).getName();
    }

    /**
     * 获取文件名（不含后缀）.
     *
     * @param url 文件地址
     * @return 文件名
     */
    public static String getCacheFileNameFromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String name = null;
        try {
            name = MD5.MD5(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 获取文件名（.后缀），外链模式和通过网络获取.
     *
     * @param url
     *            文件地址
     * @param response
     *            the response
     * @return 文件名
     */
//	public static String getCacheFileNameFromUrl(String url,
//			HttpResponse response) {
//		if (TextUtils.isEmpty(url)) {
//			return null;
//		}
//		String name = null;
//		try {
//			// 获取后缀
//			String suffix = getMIMEFromUrl(url, response);
//			if (TextUtils.isEmpty(suffix)) {
//				suffix = ".ab";
//			}
//			name = MD5.md5(url) + suffix;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return name;
//	}

    /**
     * 获取文件名（.后缀），外链模式和通过网络获取.
     *
     * @param url        文件地址
     * @param connection the connection
     * @return 文件名
     */
    public static String getCacheFileNameFromUrl(String url,
                                                 HttpURLConnection connection) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String name = null;
        try {
            // 获取后缀
            String suffix = getMIMEFromUrl(url, connection);
            if (TextUtils.isEmpty(suffix)) {
                suffix = ".ab";
            }
            name = MD5.MD5(url) + suffix;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 获取文件后缀，本地.
     *
     * @param url        文件地址
     * @param connection the connection
     * @return 文件后缀
     */
    public static String getMIMEFromUrl(String url, HttpURLConnection connection) {

        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String suffix = null;
        try {
            // 获取后缀
            if (url.lastIndexOf(".") != -1) {
                suffix = url.substring(url.lastIndexOf("."));
                if (suffix.contains("/") || suffix.contains("?")
                        || suffix.contains("&")) {
                    suffix = null;
                }
            }
            if (TextUtils.isEmpty(suffix)) {
                // 获取文件名 这个效率不高
                String fileName = getRealFileName(connection);
                if (fileName != null && fileName.lastIndexOf(".") != -1) {
                    suffix = fileName.substring(fileName.lastIndexOf("."));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return suffix;
    }

    /**
     * 获取文件后缀，本地和网络.
     *
     * @param url
     *            文件地址
     * @param response
     *            the response
     * @return 文件后缀
     */
//	public static String getMIMEFromUrl(String url, HttpResponse response) {
//
//		if (TextUtils.isEmpty(url)) {
//			return null;
//		}
//		String mime = null;
//		try {
//			// 获取后缀
//			if (url.lastIndexOf(".") != -1) {
//				mime = url.substring(url.lastIndexOf("."));
//				if (mime.indexOf("/") != -1 || mime.indexOf("?") != -1
//						|| mime.indexOf("&") != -1) {
//					mime = null;
//				}
//			}
//			if (TextUtils.isEmpty(mime)) {
//				// 获取文件名 这个效率不高
//				String fileName = getRealFileName(response);
//				if (fileName != null && fileName.lastIndexOf(".") != -1) {
//					mime = fileName.substring(fileName.lastIndexOf("."));
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return mime;
//	}

    /**
     * 描述：从sd卡中的文件读取到byte[].
     *
     * @param path sd卡中文件路径
     * @return byte[]
     */
    public static byte[] getByteArrayFromSD(String path) {
        byte[] bytes = null;
        ByteArrayOutputStream out = null;
        try {
            File file = new File(path);
            // SD卡是否存在
            if (!isCanUseSD()) {
                return null;
            }
            // 文件是否存在
            if (!file.exists()) {
                return null;
            }

            long fileSize = file.length();
            if (fileSize > Integer.MAX_VALUE) {
                return null;
            }

            FileInputStream in = new FileInputStream(path);
            out = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];
            int size = 0;
            while ((size = in.read(buffer)) != -1) {
                out.write(buffer, 0, size);
            }
            in.close();
            bytes = out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }

    /**
     * 描述：将byte数组写入文件.
     *
     * @param path    the path
     * @param content the content
     * @param create  the create
     */
    public static void writeByteArrayToSD(String path, byte[] content,
                                          boolean create) {

        FileOutputStream fos = null;
        try {
            File file = new File(path);
            // SD卡是否存在
            if (!isCanUseSD()) {
                return;
            }
            // 文件是否存在
            if (!file.exists()) {
                if (create) {
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                        file.createNewFile();
                    }
                } else {
                    return;
                }
            }
            fos = new FileOutputStream(path);
            fos.write(content);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 描述：SD卡是否能用.
     *
     * @return true 可用,false不可用
     */
    public static boolean isCanUseSD() {
        try {
            return Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 描述：初始化存储目录.
     *
     * @param context the context
     */
    public static void initFileDir(Context context) {

        PackageInfo info = AppUtil.getPackageInfo(context);
        DOWNLOAD_ROOT_DIR = "Android"+File.separator+"data"  + File.separator + info.packageName + File.separator;
        // 默认下载文件根目录.
        String downloadRootPath =DOWNLOAD_ROOT_DIR;
//        File.separator + DOWNLOAD_ROOT_DIR
//                + File.separator + info.packageName + File.separator;

        // 默认下载图片文件目录.
        String imageDownloadPath = downloadRootPath
                + DOWNLOAD_IMAGE_DIR + File.separator;

        // 默认下载文件目录.
        String fileDownloadPath = downloadRootPath
                + DOWNLOAD_FILE_DIR + File.separator;

        // 默认缓存目录.
        String cacheDownloadPath = downloadRootPath + CACHE_DIR
                + File.separator;

        // 默认DB目录.
        String dbDownloadPath = downloadRootPath + DB_DIR
                + File.separator;

        // 默认日志目录.
        String logPath = downloadRootPath + LOG_DIR + File.separator;

        try {
            if (!isCanUseSD()) {
                downloadRootDir = context.getFilesDir().getPath();
                cacheDownloadDir = context.getCacheDir().getPath();
                imageDownloadDir = downloadRootDir;
                fileDownloadDir = downloadRootDir;
                dbDownloadDir = downloadRootDir;
                logDir = downloadRootDir;
            } else {

                File root = Environment.getExternalStorageDirectory();
                File downloadDir = new File(root.getAbsolutePath()
                        + downloadRootPath);
                if (!downloadDir.exists()) {
                    downloadDir.mkdirs();
                }

                downloadRootDir = downloadDir.getPath();

                File cacheDownloadDirFile = new File(root.getAbsolutePath()
                        + cacheDownloadPath);
                if (!cacheDownloadDirFile.exists()) {
                    cacheDownloadDirFile.mkdirs();
                }

                cacheDownloadDir = cacheDownloadDirFile.getPath();

                File imageDownloadDirFile = new File(root.getAbsolutePath()
                        + imageDownloadPath);
                if (!imageDownloadDirFile.exists()) {
                    imageDownloadDirFile.mkdirs();
                }
                imageDownloadDir = imageDownloadDirFile.getPath();

                File fileDownloadDirFile = new File(root.getAbsolutePath()
                        + fileDownloadPath);
                if (!fileDownloadDirFile.exists()) {
                    fileDownloadDirFile.mkdirs();
                }
                fileDownloadDir = fileDownloadDirFile.getPath();

                File dbDownloadDirFile = new File(root.getAbsolutePath()
                        + dbDownloadPath);
                if (!dbDownloadDirFile.exists()) {
                    dbDownloadDirFile.mkdirs();
                }
                dbDownloadDir = dbDownloadDirFile.getPath();

                File logDirFile = new File(root.getAbsolutePath() + logPath);
                if (!logDirFile.exists()) {
                    logDirFile.mkdirs();
                }
                logDir = logDirFile.getPath();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算sdcard上的剩余空间.
     *
     * @return the int
     */
    @SuppressWarnings("deprecation")
    public static int freeSpaceOnSD() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
                .getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
                .getBlockSize()) / 1024 * 1024;
        return (int) sdFreeMB;
    }

    /**
     * 根据文件的最后修改时间进行排序.
     */
    public static class FileLastModifSort implements Comparator<File> {

        /*
         * (non-Javadoc)
         *
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    /**
     * 删除所有缓存文件.
     *
     * @return true, if successful
     */
    public static boolean clearDownloadFile() {

        try {
            if (!isCanUseSD()) {
                return false;
            }

            File path = Environment.getExternalStorageDirectory();
            File fileDirectory = new File(path.getAbsolutePath()
                    + downloadRootDir);
            File[] files = fileDirectory.listFiles();
            if (files == null) {
                return true;
            }
            for (File file : files) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 描述：读取Assets目录的文件内容.
     *
     * @param context  the context
     * @param name     the name
     * @param encoding the encoding
     * @return the string
     */
    public static String readAssetsByName(Context context, String name,
                                          String encoding) {
        String text = null;
        InputStreamReader inputReader = null;
        BufferedReader bufReader = null;
        try {
            inputReader = new InputStreamReader(context.getAssets().open(name));
            bufReader = new BufferedReader(inputReader);
            String line = null;
            StringBuilder buffer = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                buffer.append(line);
            }
            text = new String(buffer.toString().getBytes(), encoding);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufReader != null) {
                    bufReader.close();
                }
                if (inputReader != null) {
                    inputReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return text;
    }

    /**
     * 描述：读取Raw目录的文件内容.
     *
     * @param context  the context
     * @param id       the id
     * @param encoding the encoding
     * @return the string
     */
    public static String readRawByName(Context context, int id, String encoding) {
        String text = null;
        InputStreamReader inputReader = null;
        BufferedReader bufReader = null;
        try {
            inputReader = new InputStreamReader(context.getResources()
                    .openRawResource(id));
            bufReader = new BufferedReader(inputReader);
            String line = null;
            StringBuilder buffer = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                buffer.append(line);
            }
            text = new String(buffer.toString().getBytes(), encoding);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufReader != null) {
                    bufReader.close();
                }
                if (inputReader != null) {
                    inputReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return text;
    }

    /**
     * Gets the download root dir.
     *
     * @param context the context
     * @return the download root dir
     */
    public static String getDownloadRootDir(Context context) {
        if (downloadRootDir == null) {
            initFileDir(context);
        }
        return downloadRootDir;
    }

    /**
     * Gets the image download dir.
     *
     * @param context the context
     * @return the image download dir
     */
    public static String getImageDownloadDir(Context context) {
        if (downloadRootDir == null) {
            initFileDir(context);
        }
        return imageDownloadDir;
    }

    /**
     * Gets the file download dir.
     *
     * @param context the context
     * @return the file download dir
     */
    public static String getFileDownloadDir(Context context) {
        if (downloadRootDir == null) {
            initFileDir(context);
        }
        return fileDownloadDir;
    }

    /**
     * Gets the cache download dir.
     *
     * @param context the context
     * @return the cache download dir
     */
    public static String getCacheDownloadDir(Context context) {
        if (downloadRootDir == null) {
            initFileDir(context);
        }
        return cacheDownloadDir;
    }

    /**
     * Gets the db download dir.
     *
     * @param context the context
     * @return the db download dir
     */
    public static String getDbDownloadDir(Context context) {
        if (downloadRootDir == null) {
            initFileDir(context);
        }
        return dbDownloadDir;
    }

    /**
     * Gets the log dir.
     *
     * @param context the context
     * @return the log dir
     */
    public static String getLogDir(Context context) {
        if (logDir == null) {
            initFileDir(context);
        }
        return logDir;
    }

    /**
     * Gets the free sd space needed to cache.
     *
     * @return the free sd space needed to cache
     */
    public static int getFreeSdSpaceNeededToCache() {
        return freeSdSpaceNeededToCache;
    }

    /**
     * 根据文件路径 递归创建文件
     *
     * @param file
     */
    public static boolean createDipPath(String file) {
        String parentFile = file.substring(0, file.lastIndexOf("/"));
        File file1 = new File(file);
        File parent = new File(parentFile);
        if (!parent.exists()) {
            parent.mkdirs();
        }
        boolean isCreated = false;
        if (!file1.exists()) {
            try {
                isCreated = file1.createNewFile();
                LogUtil.i(TAG, "Create new file :" + file);
            } catch (IOException e) {
                LogUtil.e(TAG, e.getMessage());
            }
        }
        return isCreated;

    }

    /**
     * 删除文件
     *
     * @param path
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.exists() && file.delete();
    }

    /**
     * 将bitmap保存到本地
     *
     * @param bitmap
     * @param imagePath
     */
    @SuppressLint("NewApi")
    public static String saveBitmap(Bitmap bitmap, String imagePath, int s) {
        File file = new File(imagePath);
        if (createDipPath(imagePath)) return null;
        FileOutputStream fOut = null;

        try {
            fOut = new FileOutputStream(file);
            if (imagePath.toLowerCase().endsWith(".png")) {
                bitmap.compress(Bitmap.CompressFormat.PNG, s, fOut);
            } else if (imagePath.toLowerCase().endsWith(".jpg") || imagePath.toLowerCase().endsWith(".jpeg")) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, s, fOut);
            } else {
                bitmap.compress(Bitmap.CompressFormat.WEBP, s, fOut);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fOut != null) {
                    fOut.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imagePath;
    }

    /**
     * 复制文件
     *
     * @param sourcePath
     * @param toPath
     * @author Sogrey
     * @date 2015年6月30日
     */
    public static void copyFile(String sourcePath, String toPath) {
        File sourceFile = new File(sourcePath);
        File targetFile = new File(toPath);
        createDipPath(toPath);
        try {
            BufferedInputStream inBuff = null;
            BufferedOutputStream outBuff = null;
            try {
                // 新建文件输入流并对它进行缓冲
                inBuff = new BufferedInputStream(
                        new FileInputStream(sourceFile));

                // 新建文件输出流并对它进行缓冲
                outBuff = new BufferedOutputStream(new FileOutputStream(
                        targetFile));

                // 缓冲数组
                byte[] b = new byte[1024 * 5];
                int len;
                while ((len = inBuff.read(b)) != -1) {
                    outBuff.write(b, 0, len);
                }
                // 刷新此缓冲的输出流
                outBuff.flush();
            } finally {
                // 关闭流
                if (inBuff != null)
                    inBuff.close();
                if (outBuff != null)
                    outBuff.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制文件
     *
     * @param sourceFile
     * @param targetFile
     * @author Sogrey
     * @date 2015年6月30日
     */
    public static void copyFile(File sourceFile, File targetFile) {

        try {
            BufferedInputStream inBuff = null;
            BufferedOutputStream outBuff = null;
            try {
                // 新建文件输入流并对它进行缓冲
                inBuff = new BufferedInputStream(
                        new FileInputStream(sourceFile));

                // 新建文件输出流并对它进行缓冲
                outBuff = new BufferedOutputStream(new FileOutputStream(
                        targetFile));

                // 缓冲数组
                byte[] b = new byte[1024 * 5];
                int len;
                while ((len = inBuff.read(b)) != -1) {
                    outBuff.write(b, 0, len);
                }
                // 刷新此缓冲的输出流
                outBuff.flush();
            } finally {
                // 关闭流
                if (inBuff != null)
                    inBuff.close();
                if (outBuff != null)
                    outBuff.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过路径生成Base64文件
     *
     * @param path
     * @return
     * @author Sogrey
     * @date 2015年7月23日
     */
    public static String getBase64FromPath(String path) {
        String base64 = "";
        try {
            File file = new File(path);
            byte[] buffer = new byte[(int) file.length() + 100];
            @SuppressWarnings("resource")
            int length = new FileInputStream(file).read(buffer);
            base64 = Base64.encodeToString(buffer, 0, length, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64;
    }

    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B{@link #SIZETYPE_B}、2为KB
     *                 {@link #SIZETYPE_KB}、3为MB{@link #SIZETYPE_MB}
     *                 、4为GB{@link #SIZETYPE_GB}
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("获取文件大小", filePath + ">>>获取失败!");
        }
        return FormetFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("获取文件大小", "获取失败!");
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            LogUtil.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (File aFlist : flist) {
            if (aFlist.isDirectory()) {
                size = size + getFileSizes(aFlist);
            } else {
                size = size + getFileSize(aFlist);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df
                        .format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public void DeleteFile(File file) {
        if (!file.exists()) {
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
                file.delete();
            }
        }
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param path 要删除的根目录
     */
    public void DeleteFile(String path) {
        DeleteFile(new File(path));
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath 是否删除根目录
     * @param filePath       根目录
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 打开app缓存根目录
     *
     * @param context
     * @author Sogrey
     * @date 2015-11-27下午1:56:55
     */
    public static void openFolder(Context context, String path) {
        // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // Uri uri = Uri.parse(path);
        // intent.setDataAndType(uri, "text/csv");
        // try {
        // context.startActivity(Intent.createChooser(intent, "Open folder"));
        // } catch (Exception e) {
        // }

        // File root = new File(path);
        // Uri uri = Uri.fromFile(root);
        // Intent intent = new Intent();
        // intent.setAction(android.content.Intent.ACTION_VIEW);
        // intent.setData(uri);
        // context.startActivity(intent);

        // Intent intent = new Intent();
        // String videoPath = path;// 指定路径
        // File file = new File(videoPath);
        // Uri uri = Uri.fromFile(file);
        // intent.setData(uri);
        // intent.setAction(android.content.Intent.ACTION_VIEW);// "com.xxx.xxx"
        // 为文件管理器包名
        // try {
        // context.startActivity(intent);
        // } catch (ActivityNotFoundException e) {
        // e.printStackTrace();
        // }

        // Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        // // i.setAction(android.content.Intent.ACTION_VIEW);
        // File file = new File(path);
        // i.setDataAndType(Uri.fromFile(file), "file/*");
        // try {
        // context.startActivity(i);
        // } catch (Exception e) {
        // }

        // Intent intent = new Intent();
        // File file = new File(path);
        // intent.setAction(android.content.Intent.ACTION_VIEW);
        // // intent.setData(Uri.fromFile(file));
        // intent.setDataAndType(Uri.fromFile(file), "file/*");
        // try {
        // context.startActivity(intent);
        // } catch (Exception e) {
        // }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // intent.setType("*/*");
        intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
        File file = new File(path);
        intent.setData(Uri.fromFile(file));
        // intent.setDataAndType(Uri.fromFile(file), "*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            ToastUtil.showToast(context, "请安装文件管理器");
        }

    }

    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    public static File getFileByPath(String filePath) {
        return (filePath == null || filePath.trim().length() == 0) ? null : new File(filePath);
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param dirPath 目录路径
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsDir(String dirPath) {
        return createOrExistsDir(getFileByPath(dirPath));
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsDir(File file) {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param filePath 文件路径
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsFile(String filePath) {
        return createOrExistsFile(getFileByPath(filePath));
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsFile(File file) {
        if (file == null) return false;
        // 如果存在，是文件则返回true，是目录则返回false
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断文件是否存在，存在则在创建之前删除
     *
     * @param filePath 文件路径
     * @return {@code true}: 创建成功<br>{@code false}: 创建失败
     */
    public static boolean createFileByDeleteOldFile(String filePath) {
        return createFileByDeleteOldFile(getFileByPath(filePath));
    }

    /**
     * 判断文件是否存在，存在则在创建之前删除
     *
     * @param file 文件
     * @return {@code true}: 创建成功<br>{@code false}: 创建失败
     */
    public static boolean createFileByDeleteOldFile(File file) {
        if (file == null) return false;
        // 文件存在并且删除失败返回false
        if (file.exists() && file.isFile() && !file.delete()) return false;
        // 创建目录失败返回false
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
