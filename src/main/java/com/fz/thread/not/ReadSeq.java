/**
 * 
 */
package com.fz.thread.not;

import org.apache.mahout.utils.SequenceFileDumper;

import java.util.HashMap;
import java.util.Map;

/**
 * 读取HDFS seq文件
 * @author fansy
 * @date 2015年8月10日
 */
public class ReadSeq implements INotMRJob {
	private String input;
	private String lines;
	

	@Override
	public void setArgs(String[] args) {
		this.input=args[0];
		this.lines=args[1];
	}

	@Override
	public Map<String, Object> runJob() {
		Map<String ,Object> map = new HashMap<String,Object>();
		String txt =null;
		map.put("return_show", "readseq_return");
		try{
			String[] args=new String[]{
					"-i",input,
					"-n",lines,
					"-sp","<br>"
			};
			SequenceFileDumper sf = new SequenceFileDumper();
			sf.run(args);
			txt = sf.getRetStr();
			txt ="序列文件的信息是:<br>"+txt;
			map.put("flag", "true");
			
			map.put("return_txt", txt);
		}catch(Exception e){
			e.printStackTrace();
			map.put("flag", "false");
			map.put("monitor", "false");
			map.put("msg", input+"读取失败！");
		}
		return map;
	}

}
