/**
 * 
 */
package com.fz.thread;

import com.fz.util.HUtils;
import com.fz.util.Utils;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.math.hadoop.similarity.VectorDistanceSimilarityJob;

/**
 * @author fansy
 * @date 2015-8-10
 */
public class VecDistRunnable implements RunnableWithArgs {

	private String input;
	private String output;
	private String seed;
	private String dm;
	private String ot;
	private String mx;
	@Override
	public void run() {
		double tmp=0;
		
		try{
			tmp = Double.parseDouble(mx);
			
		}catch(Exception e){
			tmp=0;
			mx= String.valueOf(Double.MAX_VALUE);
		}
		if(tmp==-1.0){
			mx= String.valueOf(Double.MAX_VALUE);
		}
		
		String[] args=new String[]{
				"-i",input,
				"-o",output,
				"-dm",dm,
				"-s",seed,
				"-ot",ot,
				"-mx",mx,"-ow"
		};
		Utils.printStringArr(args);
		try {
			ToolRunner.run(HUtils.getConf()	,new VectorDistanceSimilarityJob()	, args);
		} catch (Exception e) {
			e.printStackTrace();
			// 任务中，报错，需要在任务监控界面体现出来
			HUtils.setRUNNINGJOBERROR(true);
			Utils.simpleLog("VectorDistanceSimilarityJob任务错误！");
		}
	}

	@Override
	public void setArgs(String[] args) {
		this.input=args[0];
		this.output=args[1];
		this.seed=args[2];
		this.dm=args[3];
		this.ot=args[4];
		this.mx=args[5];
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
