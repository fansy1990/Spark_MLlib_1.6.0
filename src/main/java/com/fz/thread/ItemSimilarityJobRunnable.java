/**
 * 
 */
package com.fz.thread;

import com.fz.util.HUtils;
import com.fz.util.Utils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.cf.taste.hadoop.similarity.item.ItemSimilarityJob;

/**
 * itemsimilarity
 * @author fansy
 * @date 2015-8-17
 */
public class ItemSimilarityJobRunnable implements RunnableWithArgs {
//	arg1:input,arg2:output,arg3:b,arg4:mp,arg5:m,
//	arg6:mppu,arg7:s
	private String input;
	private String output;
	private String b;
	private String mp;
	private String m;
	private String mppu;
	private String s;
	@Override
	public void run() {
		
		String[] args=null;
		if("true".equals(b)){// 包含评分值
			args= new String[14];
		}else{
			args= new String[15];
			args[20]="-b";
		}
		args[0]="-i";
		args[1]=input;
		args[2]="-o";
		args[3]=output;
		args[4]="-s";
		args[5]=s;
		args[6]="-m";
		args[7]=m;
		args[8]="-mp";
		args[9]=mp;
		args[10]="-mppu";
		args[11]=mppu;
		args[12]="--tempDir";
		args[13]="temp";
		
		Utils.printStringArr(args);
		try {
			HUtils.getFs().delete(new Path("temp"), true);
			HUtils.getFs().delete(new Path(output), true);
			
			int ret = ToolRunner.run(HUtils.getConf()	,new ItemSimilarityJob()	, args);
			if(ret==0){// 所有任务运行完成
				HUtils.setALLJOBSFINISHED(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 任务中，报错，需要在任务监控界面体现出来
			HUtils.setRUNNINGJOBERROR(true);
			Utils.simpleLog("ItemSimilarityJob任务错误！");
		}
	}

	@Override
	public void setArgs(String[] args) {
//		arg1:input,arg2:output,arg3:b,arg4:mp,arg5:m,
//		arg6:mppu,arg7:s
		this.input=args[0];
		this.output=args[1];
		this.b=args[2];
		this.mp=args[3];
		this.m=args[4];
		this.mppu=args[5];
		this.s=args[6];
	}

}
