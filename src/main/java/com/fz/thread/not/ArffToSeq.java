/**
 * 
 */
package com.fz.thread.not;

import com.fz.util.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据转换
 * @author fansy
 * @date 2015年8月10日
 */
public class ArffToSeq implements INotMRJob {

	private String input;
	private String output;
	private String delimiter;
	private String dictionary;
	@Override
	public void setArgs(String[] args) {
		this.input=args[0];
		this.output=args[1];
		this.dictionary=args[2];
		this.delimiter=args[3];
	}

	@Override
	public Map<String, Object> runJob() {
		Map<String ,Object> map = new HashMap<String,Object>();
		String txt =null;
		map.put("return_show", "arff_return");
		try{
			String[] args=new String[] {
					"-d",input,
					"-o",output,
					"-t",dictionary,
					"-l",delimiter
			};
			org.apache.mahout.utils.vectors.arff.Driver.main(args);
//			outDir + '/' + file.getName() + ".mvc"
			txt ="文件:"+input+" 转换到HDFS文件："+output+"/"+Utils.getFileName(input)+".mvc 成功!";
			map.put("flag", "true");
			
			map.put("return_txt", txt);
		}catch(Exception e){
			e.printStackTrace();
			map.put("flag", "false");
			map.put("monitor", "false");
			map.put("msg", input+"转换失败！");
		}
		return map;
	}

}
