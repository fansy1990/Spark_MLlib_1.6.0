/**
 * 
 */
package com.fz.thread;

import com.fz.util.HUtils;
import com.fz.util.Utils;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.classifier.naivebayes.training.TrainNaiveBayesJob;

/**
 * @author fansy
 * @date 2015-8-11
 */
public class TrainNaiveBayesJobRunnable implements RunnableWithArgs {

	private String input;
	private String output;
	private String alphaI;
	private String labelIndex;
//	private String complementary;
	@Override
	public void run() {
		
		String[] args=null;
//		if("true".equals(complementary)){
//			args=new String[12];
//			args[10]="-c";
//		}else{
			args= new String[11];
//		}
		args[0]="-i";
		args[1]=input;
		args[2]="-o";
		args[3]=output;
		args[4]="-a";
		args[5]=alphaI;
		args[6]="-li";
		args[7]=labelIndex;
		args[8]="--tempDir";
		args[9]="temp";
		args[10]="-ow";
		
		Utils.printStringArr(args);
		try {
			int ret = ToolRunner.run(HUtils.getConf()	,new TrainNaiveBayesJob()	, args);
			if(ret==0){// 所有任务运行完成
				HUtils.setALLJOBSFINISHED(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 任务中，报错，需要在任务监控界面体现出来
			HUtils.setRUNNINGJOBERROR(true);
			Utils.simpleLog("TrainNaiveBayesJob任务错误！");
		}
	}

	@Override
	public void setArgs(String[] args) {
		this.input=args[0];
		this.output=args[1];
		this.alphaI=args[2];
		this.labelIndex=args[3];
//		this.complementary=args[4];		
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
