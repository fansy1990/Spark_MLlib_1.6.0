/**
 * 
 */
package com.fz.thread;

import com.fz.util.HUtils;
import com.fz.util.Utils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.cf.taste.hadoop.als.FactorizationEvaluator;

/**
 * evaluateFactorization
 * @author fansy
 * @date 2015-8-17
 */
public class EvaluateFactorizationRunnable implements RunnableWithArgs {
//	arg1:input,arg2:output,arg3:userFeatures,arg4:itemFeatures
	private String input;
	private String output;
	private String userFeatures;
	private String itemFeatures;

	@Override
	public void run() {
		
		String[] args=new String[]{
				"-i",input,
				"-o",output,
				"--userFeatures",userFeatures,
				"--itemFeatures",itemFeatures,

////				"--usesLongIDs","?",
				 "--tempDir","temp"
		};
		
		Utils.printStringArr(args);
		try {
			HUtils.getFs().delete(new Path("temp"), true);
			HUtils.getFs().delete(new Path(output), true);
			
			int ret = ToolRunner.run(HUtils.getConf()	,new FactorizationEvaluator()	, args);
			if(ret==0){// 所有任务运行完成
				HUtils.setALLJOBSFINISHED(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 任务中，报错，需要在任务监控界面体现出来
			HUtils.setRUNNINGJOBERROR(true);
			Utils.simpleLog("FactorizationEvaluator任务错误！");
		}
	}

	@Override
	public void setArgs(String[] args) {
//		arg1:input,arg2:output,arg3:userFeatures,arg4:itemFeatures
		this.input=args[0];
		this.output=args[1];
		this.userFeatures=args[2];
		this.itemFeatures=args[3];

	}

}
