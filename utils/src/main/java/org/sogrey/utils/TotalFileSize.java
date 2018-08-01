package org.sogrey.utils;

import java.io.File;

public class TotalFileSize {
    public static String fileName = "C:\\Users\\Administrator\\Desktop\\1_11";

    // 递归方式 计算文件的大小
    public static long getTotalSizeOfFilesInDir(final File file) {

        long total = 0;
        if (file.isFile()) return file.length();
        if (file.isDirectory()) {
            final File[] children = file.listFiles();

            if (children != null) for (final File child : children){
                total += getTotalSizeOfFilesInDir(child);
            }

        }
//        LogUtil.D(total+" "+file.getAbsolutePath());
        return total;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        final long start = System.nanoTime();
        final long total = getTotalSizeOfFilesInDir(new File(fileName));
        final long end = System.nanoTime();
        System.out.println("Total Size: " + total);
        System.out.println("Time taken: " + (end - start) / 1.0e9);
    }

}