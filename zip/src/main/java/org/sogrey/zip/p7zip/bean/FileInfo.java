package org.sogrey.zip.p7zip.bean;

/**
 * Created by Sogrey on 2018/8/8.
 */

public class FileInfo {
    private String fileName;
    private String filePath;
    private boolean isFolder;
    private FileType fileType;
    private int subCount;
    private long fileLength;

    public FileInfo(String fileName, String filePath, boolean isFolder, FileType fileType, int subCount, long fileLength) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.isFolder = isFolder;
        this.fileType = fileType;
        this.subCount = subCount;
        this.fileLength = fileLength;
    }

    public FileInfo() {

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public int getSubCount() {
        return subCount;
    }

    public void setSubCount(int subCount) {
        this.subCount = subCount;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }
}
