/**
 * 
 */
package com.fz.thread;

import com.fz.util.HUtils;
import com.fz.util.Utils;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.clustering.fuzzykmeans.FuzzyKMeansDriver;

/**
 * @author fansy
 * @date 2015-8-4
 */
public class FuzzyKMeansDriverRunnable implements RunnableWithArgs {

	private String input;
	private String output;
	private String clusters;
	private String k;
	private String convergenceDelta;
	private String maxIter;
	private String clustering;
	private String  distanceMeasure;
	
	private String m;
	private String emitMostLikely;
	private String threshold;
	@Override
	public void run() {
		
		String[] args=null;
		if("true".equals(clustering)){
			args=new String[23];
			args[22]="-cl";
		}else{
			args= new String[22];
		}
		args[0]="-i";
		args[1]=input;
		args[2]="-o";
		args[3]=output;
		args[4]="-c";
		args[5]=clusters;
		args[6]="-k";
		args[7]=k;
		args[8]="-cd";
		args[9]=convergenceDelta;
		args[10]="-x";
		args[11]=maxIter;
		args[12]="-dm";
		args[13]=distanceMeasure;
		args[14]="--tempDir";
		args[15]="temp";
		args[16]="-m";
		args[17]=m;
		args[18]="-e";
		args[19]=emitMostLikely;
		args[20]="-t";
		args[21]=threshold;
		
		Utils.printStringArr(args);
		try {
			HUtils.delete(output);
			HUtils.delete("temp");
			HUtils.delete(clusters);

			int ret = ToolRunner.run(HUtils.getConf()	,new FuzzyKMeansDriver()	, args);
			if(ret==0){// 所有任务运行完成
				HUtils.setALLJOBSFINISHED(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 任务中，报错，需要在任务监控界面体现出来
			HUtils.setRUNNINGJOBERROR(true);
			Utils.simpleLog("KMeansDriver任务错误！");
		}
	}

	@Override
	public void setArgs(String[] args) {
		this.input=args[0];
		this.output=args[1];
		this.clusters=args[2];
		this.k=args[3];
		this.convergenceDelta=args[4];
		this.maxIter=args[5];
		this.clustering=args[6];
		this.distanceMeasure=args[7];
		this.m=args[8];
		this.emitMostLikely=args[9];
		this.threshold=args[10];
		
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
