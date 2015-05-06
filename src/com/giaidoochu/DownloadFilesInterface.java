package com.giaidoochu;

public interface DownloadFilesInterface {
    public void onDownloadTaskStarted();
    public void onDownloadUpdateProgressStatus(String status);
    public void onDownloadTaskCompleted(boolean completed, int progress, String errorMessage);
}
