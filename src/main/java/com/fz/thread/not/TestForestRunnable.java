/**
 * 
 */
package com.fz.thread.not;

import com.fz.util.HUtils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.classifier.df.mapreduce.TestForest;

import java.util.HashMap;
import java.util.Map;

/**
 * 读取testforest分类结果
 * 
 * @author fansy
 * @date 2015年8月12日
 */
public class TestForestRunnable implements INotMRJob {
	private String output;
	private String model;
	private String input;
	private String describe;

	@Override
	public void setArgs(String[] args) {
		this.input = args[0];
		this.output = args[1];
		this.describe=args[2];
		this.model=args[3];
	}

	@Override
	public Map<String, Object> runJob() {
		Map<String, Object> map = new HashMap<String, Object>();
		String txt = null;
		map.put("return_show", "testforest_return");
		try {
			TestForest tf = new TestForest();
			HUtils.getFs().delete(new Path(output), true);
			String [] args =new String[]{
					"-i",input,
					"-ds",describe,
					"-m",model,
					"-o",output,
					"-a"
			};
			int ret = ToolRunner.run(HUtils.getConf()	,tf	, args);
			if(ret==0){// 所有任务运行完成
				HUtils.setALLJOBSFINISHED(true);
			}
			txt = tf.getAnalyzerInfo();
			txt=txt.replaceAll("\n", "<br>");
			txt = "分类的结果是:<br>" + txt;
			map.put("flag", "true");

			map.put("return_txt", txt);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("flag", "false");
			map.put("monitor", "false");
			map.put("msg",   "testforest分类结果读取失败！");
		}
		return map;
	}

	
}
