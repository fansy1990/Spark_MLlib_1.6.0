/**
 * 
 */
package com.fz.thread;

import com.fz.util.HUtils;
import com.fz.util.Utils;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.cf.taste.hadoop.als.DatasetSplitter;

/**
 * @author fansy
 * @date 2015-8-4
 */
public class DatasetSplitterRunnable implements RunnableWithArgs {

	private String input;
	private String output;
	private String trainPercent;
	private String testPercent;
	@Override
	public void run() {
		
		String[] args=new String[]{
				"-i",input,
				"-o",output,
				"-t",trainPercent,
				"-p",testPercent,
				"--tempDir","temp"
		};
		try {
			HUtils.delete(output);
			HUtils.delete("temp");
			ToolRunner.run(HUtils.getConf()	,new DatasetSplitter()	, args);
		} catch (Exception e) {
			e.printStackTrace();
			// 任务中，报错，需要在任务监控界面体现出来
			HUtils.setRUNNINGJOBERROR(true);
			Utils.simpleLog("DataSetSplitter任务错误！");
		}
	}

	@Override
	public void setArgs(String[] args) {
		this.input=args[0];
		this.output=args[1];
		this.trainPercent=args[2];
		this.testPercent=args[3];
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getTrainPercent() {
		return trainPercent;
	}

	public void setTrainPercent(String trainPercent) {
		this.trainPercent = trainPercent;
	}

	public String getTestPercent() {
		return testPercent;
	}

	public void setTestPercent(String testPercent) {
		this.testPercent = testPercent;
	}

}
