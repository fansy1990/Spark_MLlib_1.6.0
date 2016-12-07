/**
 * 
 */
package com.fz.thread;

import com.fz.util.HUtils;
import com.fz.util.Utils;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.clustering.conversion.InputDriver;

/**
 * @author fansy
 * @date 2015-8-4
 */
public class InputDriverRunnable implements RunnableWithArgs {

	private String input;
	private String output;
	private String select_value;
	@Override
	public void run() {
		
		String[] args=new String[]{
				input,
				output,
				select_value
		};
		Utils.printStringArr(args);
		try {
			HUtils.delete(output);
			ToolRunner.run(HUtils.getConf()	,new InputDriver()	, args);
//			InputDriver.main(args);
		} catch (Exception e) {
			e.printStackTrace();
			// 任务中，报错，需要在任务监控界面体现出来
			HUtils.setRUNNINGJOBERROR(true);
			Utils.simpleLog("InputDriver任务错误！");
		}
	}

	@Override
	public void setArgs(String[] args) {
		this.input=args[0];
		this.output=args[1];
		this.select_value=args[2];
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
}
