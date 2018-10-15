import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Zip文件工具类
 * @author Luxh
 */
public class ZipUtil {

    static int bufferSize = 8192;


    public static void doCompress(File srcFile, File destFile) throws IOException {

        if (srcFile.isDirectory() && srcFile.list().length > 0) {

            List<File> allFile = getAllFile(srcFile);
            long allFileSize = 0;
            long alreadyFileSize = 0;   // 已压缩文件的大小
            for (File file : allFile) {
                allFileSize += file.length();
            }

            String path = srcFile.getAbsolutePath();

            ZipArchiveOutputStream out = new ZipArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(destFile), bufferSize));

            for (File file : allFile) {
                if (file.isFile()) {
                    InputStream is = new BufferedInputStream(new FileInputStream(file), bufferSize);
                    String relative = file.getAbsolutePath().substring(path.length() + 1);
                    ZipArchiveEntry entry = new ZipArchiveEntry(relative);
                    entry.setSize(file.length());
                    out.putArchiveEntry(entry);
                    IOUtils.copy(is, out);
                    is.close();
                    out.closeArchiveEntry();
                } else {
                    // 空文件夹
                    String relative = file.getAbsolutePath().substring(path.length() + 1);
                    ZipArchiveEntry entry = new ZipArchiveEntry(file, relative);
                    entry.setSize(file.length());
                    out.putArchiveEntry(entry);
                    out.closeArchiveEntry();
                }

                alreadyFileSize += file.length();
                System.out.println("文件" + file.getName() + "已经被压缩，当前进度:" + alreadyFileSize + "/" + allFileSize);
            }

            IOUtils.closeQuietly(out);
            System.out.println("完成压缩");
        }
    }


    private static List<File> getAllFile(File srcFile) {

        File[] files = srcFile.listFiles();
        List<File> allFile = new ArrayList<File>();

        for (File file : files) {
            if (file.isFile()) {
                allFile.add(file);
            } else {
                if (file.list().length == 0) {
                    allFile.add(file);   // 空文件
                } else {
                    allFile.addAll(getAllFile(file));
                }
            }
        }

        return allFile;
    }


    public static void doDecompress(File srcFile, File destDir) throws IOException {
        ZipArchiveInputStream is = null;
        try {
            is = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(srcFile), bufferSize));
            ZipArchiveEntry entry = null;
            while ((entry = is.getNextZipEntry()) != null) {
                if (entry.isDirectory()) {
                    File directory = new File(destDir, entry.getName());
                    directory.mkdirs();
                } else {
                    OutputStream os = null;
                    try {
                        os = new BufferedOutputStream(new FileOutputStream(new File(destDir, entry.getName())), bufferSize);
                        IOUtils.copy(is, os);
                    } finally {
                        IOUtils.closeQuietly(os);
                    }
                }
            }
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

}
