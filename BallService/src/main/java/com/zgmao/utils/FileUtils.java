package com.zgmao.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * 
 * @project AppAssistWebservice
 * @author mzg
 * @date 2016年6月21日
 * @company 巨蚁网络科技有限公司
 * @desc 文件操作工具类
 */
public class FileUtils {
	/**
	 * 文件存放根目录
	 */
	public static String ROOT_PATH = "file";
	/**
	 * 文件存放根目录
	 */
	public static String LOG_DIR = "logs";
	/**
	 * 音频文件存放路径
	 */
	public static String SOUND_DIR = "sound";

	/**
	 * 判断文件是否存在，不存在则新建
	 *
	 * @param dirName
	 */
	private static void mkDirs(String dirName) {
		File dir = new File(dirName);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	/**
	 * 初始化文件夹，如果没有，新建
	 */
	public static void initDir() {
		mkDirs(ROOT_PATH);
	}

	/**
	 * 如果文件不存在，新建
	 *
	 * @param fileName
	 * @return 创建文件成功，返回true
	 */
	private static boolean createNewFile(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
				return true;
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 向文件里面写文本
	 *
	 * @param text
	 *            文本内容
	 * @param fileName
	 *            文件绝对路径
	 */
	public static void writeTextToFile(String text, String fileName) {
		createNewFile(fileName);
		File file = new File(fileName);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file, true);
			fileWriter.write(text);
			fileWriter.write("\n");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			// 关闭流
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * 记录崩溃的log日志
	 *
	 * @param text
	 */
	public static void writeLog(String text) {
		// crash log文件名
		String logDir = ROOT_PATH + File.separator + LOG_DIR;
		mkDirs(logDir);
		String logFileName = logDir + File.separator + "log_"
				+ DateUtils.getDateByFormat("yyyyMMdd") + ".log";
		writeTextToFile(text, logFileName);
	}

	/**
	 * 记录崩溃的log日志
	 *
	 * @param text
	 */
	public static void writeCrashLog(String text) {
		// crash log文件名
		String logDir = ROOT_PATH + File.separator + LOG_DIR;
		mkDirs(logDir);
		String logFileName = logDir + File.separator + "error_"
				+ DateUtils.getDateByFormat("yyyyMMddHHmmss") + ".log";
		writeTextToFile(text, logFileName);
	}

	/**
	 * 复制文件
	 * 
	 * @param file
	 * @return 返回文件地址
	 */
	public static String copyFile(HttpServletRequest request,
			CommonsMultipartFile file) {
		if (file == null) {
			return "";
		}
		if (file.isEmpty()) {
			return "";
		}
		// 得到webapp
		String url = request.getSession().getServletContext().getRealPath("/");
		System.out.println("url:" + url);
		// 获取文件名
		String name = file.getOriginalFilename();
		// 重命名
		// name = DateUtils.getDateByFormat("yyyyMMddHHmmss") + "_" + name;
		name = StringUtils.randomUid();
		String path = url + ROOT_PATH + File.separator + SOUND_DIR;
		mkDirs(ROOT_PATH);
		mkDirs(path);
		InputStream is = null;
		OutputStream os = null;
		try {
			is = file.getInputStream();
			os = new FileOutputStream(new File(path, name));
			int length = 0;
			byte[] buffer = new byte[1024];
			while ((length = is.read(buffer)) != -1) {
				os.write(buffer, 0, length);
			}
			return ROOT_PATH + "/" + SOUND_DIR + "/" + name;
		} catch (IOException e) {
			e.printStackTrace();
			Lg.writeError(e.fillInStackTrace().getMessage());
		} finally {
			// 关闭流
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}


	/**
	 * 删除文件
	 * 
	 * @param path
	 */
	public static void deleteFile(HttpServletRequest request, String path) {
		// 得到webapp
		String url = request.getSession().getServletContext().getRealPath("/");
		// 获取文件名
		String filePath = url + path;
		File file = new File(filePath);
		System.out.println(file.getAbsolutePath());
		if (!file.exists()) {
			System.out.println("文件不存在");
			return;
		}
		file.delete();
	}

	public static void main(String[] args) {
		System.out.println(FileUtils.class.getResource(""));
	}
}
