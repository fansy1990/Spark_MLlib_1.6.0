/**
 * 
 */
package com.fz.thread.not;

import org.apache.mahout.clustering.streaming.tools.ResplitSequenceFiles;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据分割
 * @author fansy
 * @date 2015年8月10日
 */
public class ResplitSeq implements INotMRJob {

	private String input;
	private String output;
	private String files;
	@Override
	public void setArgs(String[] args) {
		this.input=args[0];
		this.output=args[1];
		this.files=args[2];
	}

	@Override
	public Map<String, Object> runJob() {
		Map<String ,Object> map = new HashMap<String,Object>();
		String txt =null;
		map.put("return_show", "resplitseq_return");
		try{
			String[] args=new String[] {
					"-i",input,
					"-o",output,
					"-ns",files
			};
			ResplitSequenceFiles.main(args);
			txt ="文件:"+input+" 分割到HDFS文件："+output+"-i 成功!";
			map.put("flag", "true");
			
			map.put("return_txt", txt);
		}catch(Exception e){
			e.printStackTrace();
			map.put("flag", "false");
			map.put("monitor", "false");
			map.put("msg", input+"分割失败！");
		}
		return map;
	}

}
