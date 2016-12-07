/**
 * 
 */
package com.fz.thread;

import com.fz.util.HUtils;
import com.fz.util.Utils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.cf.taste.hadoop.als.RecommenderJob;

/**
 *  als recommend
 * @author fansy
 * @date 2015-8-15
 */
public class RecommendFactorizedRunnable implements RunnableWithArgs {
//	arg1:input,arg2:output,arg3:userFeatures,arg4:itemFeatures,arg5:num,
//	arg6:maxRating,arg7:numThreads
	private String input;
	private String output;
	private String userFeatures;
	private String itemFeatures;
	private String num;
	private String maxRating;
	private String numThreads;
	@Override
	public void run() {
		
		String[] args=new String[]{
				"-i",input,
				"-o",output,
				"--userFeatures",userFeatures,
				"--itemFeatures",itemFeatures,
				"--numRecommendations",num,
				"--maxRating",maxRating,// 最大的评分值
				"--numThreads",numThreads,
////				"--usesLongIDs","?",
////				"--userIDIndex","?",
//				"--itemIDIndex","?",// 
				 "--tempDir","temp"
		};
		
		Utils.printStringArr(args);
		try {
			HUtils.getFs().delete(new Path("temp"), true);
			HUtils.getFs().delete(new Path(output), true);
			
			int ret = ToolRunner.run(HUtils.getConf()	,new RecommenderJob()	, args);
			if(ret==0){// 所有任务运行完成
				HUtils.setALLJOBSFINISHED(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 任务中，报错，需要在任务监控界面体现出来
			HUtils.setRUNNINGJOBERROR(true);
			Utils.simpleLog("ALS RecommenderJob任务错误！");
		}
	}

	@Override
	public void setArgs(String[] args) {
//		arg1:input,arg2:output,arg3:userFeatures,arg4:itemFeatures,arg5:num,
//		arg6:maxRating,arg7:numThreads
		this.input=args[0];
		this.output=args[1];
		this.userFeatures=args[2];
		this.itemFeatures=args[3];
		this.num=args[4];
		this.maxRating=args[5];
		this.numThreads=args[6];
	}

}
