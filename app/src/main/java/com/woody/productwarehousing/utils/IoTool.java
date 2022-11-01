package com.woody.productwarehousing.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class IoTool {

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

    private static String distinctFileName = "";

    /**
     * 設置TXT檔名稱(當使用者按下上傳按鈕，名稱都會更新一次)
     **/
    public static void setDistinctFileName() {
        //以下為TXT檔的名稱設置
        distinctFileName = simpleDateFormat.format(new Date()) + ".txt";
    }

    /**
     * @param json 紀錄上傳的JSON字串
     **/
    public static void writeUploadJson(String json) {
        //匯出位置
        File jsonFolderPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Log", "Json");
        if (!jsonFolderPath.exists()) {
            File logFolderPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Log");
            if (!logFolderPath.exists()) {
                logFolderPath.mkdirs();
            }
            jsonFolderPath.mkdirs();
        }
        //以下為TXT檔的名稱設置
        File file = new File(jsonFolderPath, distinctFileName);

        try {
            FileWriter fw = new FileWriter(file, true);
            fw.write("\"" + "【" + simpleDateFormat.format(new Date()) + "】" + "\"" + ":");
            fw.write(json + ",");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 刪除LIMIT_DAY以前，上傳的Log檔
     **/
    public static void deleteUploadJson(int limitTime) {
        try {
            File jsonFolderPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Log", "Json");
            if (jsonFolderPath.exists()) {
                File[] files = jsonFolderPath.listFiles();
                Date today = new Date();
                for (int i = 0; i < files.length; i++) {
                    long differenceDay = TimeUnit.MILLISECONDS.toDays(today.getTime() - simpleDateFormat2.parse(files[i].getName().substring(0, 10)).getTime());
                    if (differenceDay >= limitTime) {
                        File file = new File(jsonFolderPath, files[i].getName());
                        file.delete();
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param folderName 匯出檔案的資料夾名稱(註：資料夾將位在/storage/emulated/0/底下)
     * @param fileName 要匯出的檔案名稱(註：含副檔名，檔案位在Download底下)
     **/
    //匯出檔案
    public static void exportFile(String folderName, String fileName) throws IOException {
        //匯出位置
        File exportFolderPath = new File(Environment.getExternalStorageDirectory(), folderName);
        if (!exportFolderPath.exists()) {
            exportFolderPath.mkdirs();
        }

        try {
            //原始檔案(要匯出來的檔案)
            File oldDBFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
            //匯出來的新檔案(放到exportFolder路徑下)
            File newDBFile = new File(exportFolderPath, oldDBFile.getName());
            //建立新檔案
            newDBFile.createNewFile();
            //將原始檔案複製到新檔案
            fileCopy(oldDBFile, newDBFile);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    //檔案複製處理
    private static void fileCopy(File oldFile, File newFile) throws IOException {
//        Log.v("LINS","oldFile：" + oldFile.exists());
//        Log.v("LINS","newFile：" + newFile.exists());
        FileChannel inChannel = new FileInputStream(oldFile).getChannel();
        FileChannel outChannel = new FileOutputStream(newFile).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    /**
     * @param sourceFolderPath 要包成zip檔的資料夾的路徑
     * @param exportZipPath 匯出zip檔的路徑
     **/
    //製作zip檔案
    public static void createZip(String sourceFolderPath, String exportZipPath) throws IOException {
        try {
            FileOutputStream fos = new FileOutputStream(exportZipPath);
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            File fileToZip = new File(sourceFolderPath);
            zipFile(fileToZip, fileToZip.getName(), zipOut);
            zipOut.close();
            fos.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //zip檔的資料處理
    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
            }
            zipOut.closeEntry();

            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
}
