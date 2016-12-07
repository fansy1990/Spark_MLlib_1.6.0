/**
 * 
 */
package com.fz.thread;

import com.fz.util.HUtils;
import com.fz.util.Utils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.classifier.df.mapreduce.BuildForest;

/**
 * @author fansy
 * @date 2015-8-12
 */
public class BuildForestRunnable implements RunnableWithArgs {

	private String input;
	private String output;
	private String describe;
	private String selection;
	private String minsplit;
	private String minprop;
	private String nbtrees;
	private String complete;
	@Override
	public void run() {
		
		String[] args=null;
		if("true".equals(complete)){
			if("-1".equals(selection)){
				args= new String[13];
			}else{
				args=new String[15];
				args[13]="-sl";
				args[14]=selection;
			}
			
		}else{
			if("-1".equals(selection)){
				args= new String[14];
				args[13]="-nc";
			}else{
				args=new String[16];
				args[13]="-sl";
				args[14]=selection;
				args[15]="-nc";
			}
		}
		args[0]="-d";
		args[1]=input;
		args[2]="-o";
		args[3]=output;
		args[4]="-ds";
		args[5]=describe;
		args[6]="-ms";
		args[7]=minsplit;
		args[8]="-mp";
		args[9]=minprop;
		args[10]="-t";
		args[11]=nbtrees;
		args[12]="-p";
		
		Utils.printStringArr(args);
		try {
			String tmpPath = new Path(output).getName();
			HUtils.getFs().delete(new Path(HUtils.getFs().getWorkingDirectory(),tmpPath),
					true);
			HUtils.getFs().delete(new Path(output), true);
			int ret = ToolRunner.run(HUtils.getConf()	,new BuildForest()	, args);
			if(ret==0){// 所有任务运行完成
				HUtils.setALLJOBSFINISHED(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 任务中，报错，需要在任务监控界面体现出来
			HUtils.setRUNNINGJOBERROR(true);
			Utils.simpleLog("BuildForest任务错误！");
		}
	}

	@Override
	public void setArgs(String[] args) {
		this.input=args[0];
		this.output=args[1];
		this.describe=args[2];
		this.selection=args[3];
		this.minsplit=args[4];
		this.minprop=args[5];
		this.nbtrees=args[6];
		this.complete=args[7];
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
