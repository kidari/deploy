
import java.io.*;
import java.util.*;

public class deploy {
	public final static String encode_GB2312 = "GB2312";
	public final static String encode_GBK = "GBK";
	public final static String encode_UTF8 = "UTF8";
	public final static String encode_ISO8859_1 = "8859_1";

	/**
	 * 私有属性描述：全局配置参数是否初始化
	 */
	private static boolean is_inited = false;

	/**
	 * 私有属性描述：系统全局配置参数
	 */
	private static HashMap deployHashMap = null;

	/**
	 * 公有属性描述：应用配置根目录
	 */
	public static String ConfigHome = "E:\\workspaces\\deploy\\src\\";// deploy.txt所在目录

	public deploy() {
		if (!is_inited) {
			is_inited = init();
		}
	}

	public static boolean init() {
		if (!is_inited) {
			try {
				deployHashMap = new HashMap();
				File file = new File(ConfigHome + "deploy.txt");
				if (file.exists()) {
					java.io.BufferedReader br = new BufferedReader(
							new FileReader(file));
					String rc = "";
					while ((rc = br.readLine()) != null) {
						if (!rc.trim().equals("") && rc.indexOf("=") > 0
								&& rc.trim().substring(0, 1) != "#") {
							String DataName = rc.substring(0, rc.indexOf("="));
							String DataValue = rc
									.substring(rc.indexOf("=") + 1);
							if (DataValue != null
									&& !DataValue.trim().equals("")) {
								deployHashMap.put(DataName, DataValue);
							}
						}
					}
				} else {
					throw new Exception(ConfigHome + "deploy.txt 配置文件不存在。");
				}
				System.out.println(deploy.getCurrTimestamp() + " 生成文件：");
			} catch (Exception ce) {
				System.out.println(getCurrTimestamp() + "--------------------");
				ce.printStackTrace();
				deployHashMap = null;
				return false;
			}
		}
		return true;
	}

	/**
	 * 功能描述：重新初始化
	 * 
	 * @return boolean
	 */
	public static boolean reinit() {
		is_inited = false;
		is_inited = init();
		return is_inited;
	}

	/**
	 * 功能描述：从deployHashMap中查询配置信息，静态调用
	 * 
	 * @param tagName
	 *            配置名称
	 * @return String 配置值
	 */
	public static String getData(String tagName) {
		if (deployHashMap == null) {
			if (init()) {
				return (String) deployHashMap.get(tagName);
			} else {
				System.out.println(getCurrTimestamp() + "配置初始化未成功。");
				return new String("");
			}
		} else {
			return (String) deployHashMap.get(tagName);
		}
	}

	/**
	 * 功能描述：获取环境配置
	 * 
	 * @return HashMap 环境配置
	 */
	public static HashMap getHashMap() {
		return deployHashMap;
	}

	/**
	 * 功能描述：获取对应格式时间
	 * 
	 * @return 2016年9月6日
	 */
	public static String getLimitTime() {

		Calendar now = Calendar.getInstance();
		return now.get(Calendar.YEAR) + "年" + (now.get(Calendar.MONTH) + 1)
				+ "月" + now.get(Calendar.DAY_OF_MONTH) + "日";

	}

	/**
	 * 功能描述：返回系统当前时间无分隔符的Date
	 * 
	 * @param SplitChar
	 *            日期的分隔 可以是- / 或空串"" 或"年月日"
	 * @return String 系统当前时间字符串
	 */
	public static String getCurrDate(String SplitChar) {
		java.text.SimpleDateFormat sdf = null;
		if (SplitChar.equals("年月日")) {
			sdf = new java.text.SimpleDateFormat("yyyy年MM月dd日");
		} else {
			sdf = new java.text.SimpleDateFormat("yyyy" + SplitChar + "MM"
					+ SplitChar + "dd");
		}
		java.util.Date d = new java.util.Date();
		d.setTime(System.currentTimeMillis());
		return sdf.format(d);
	}

	/**
	 * 功能描述：获取传入文本内容 遇/* 、//、空行跳过
	 * 
	 * @return 逐行存入ArrayList
	 */
	public static ArrayList<String> ReadTxt(String filePath, String encoding) {
		ArrayList<String> lineTxt = new ArrayList<String>();
		try {

			File file = new File(filePath);
			if (file.isFile() && file.exists()) {// 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 编码格式
				java.io.BufferedReader br = new BufferedReader(read);
				String rc = "";
				while ((rc = br.readLine()) != null) {
					if (!rc.trim().equals("")
							&& rc.trim().substring(0, 1) != "/") {
						String DataValue = rc;
						if (DataValue != null && !DataValue.trim().equals("")) {
							if (!lineTxt.contains(DataValue))
								lineTxt.add(DataValue);
						}
					}

				}

				System.out.println(filePath + "文件读取完毕");
			} else {
				System.out.println("找不到指定的文件");
			}

		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return lineTxt;

	}

	/**
	 * 功能描述：返回系统当前时间Timestamp,以java.sql.Timestamp类型为返回格式
	 * 
	 * @return String 系统当前时间字符串
	 */
	public static int copyFile(String oldPath, String newPath, boolean tran_flag) {
		int flag = 0;
		try {

			int bytesum = 0;
			int byteread = 0;

			File oldfile = new File(oldPath);
			if (oldfile.exists()) {// 文件存在时
				if (tran_flag) {
					InputStream inStream = new FileInputStream(oldPath);// 读取原文件
					FileOutputStream outStream = new FileOutputStream(newPath);
					byte[] buffer = new byte[1444];
					while ((byteread = inStream.read(buffer)) != -1) {
						bytesum += byteread;// 字节数 文件大小
						outStream.write(buffer, 0, byteread);
					}
					System.out.println(oldPath + "文件复制成功！字节数：" + bytesum);
					inStream.close();
				}
			} else {
				if (tran_flag) {
					System.out.println(oldPath + "*文件不存在");
				}
				flag = 1;
			}
		} catch (Exception e) {
			System.out.println("*复制文件出错");
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 功能描述：返回系统当前时间Timestamp,以java.sql.Timestamp类型为返回格式
	 * 
	 * @return String 系统当前时间字符串
	 */
	public static String getCurrTimestamp() {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SS");
		java.util.Date d = new java.util.Date();
		d.setTime(System.currentTimeMillis());
		String time = sdf.format(d);
		if (time.length() < 23) {
			for (int i = 0; i < (23 - time.length()); i++) {
				time += "0";
			}
		}
		return time;
	}

	public static void main(String[] args) throws Exception {
		deploy.init();

	}
}
