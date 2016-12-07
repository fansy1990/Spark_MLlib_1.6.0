/**
 * 
 */
package com.fz.thread;

import com.fz.util.HUtils;
import com.fz.util.Utils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.cf.taste.hadoop.item.RecommenderJob;

/**
 * itembased recommender
 * @author fansy
 * @date 2015-8-13
 */
public class RecommendItembasedRunnable implements RunnableWithArgs {
//	arg1:input,arg2:output,arg3:opfsm,arg4:num,arg5:b,
//	arg6:mxp,arg7:mp,arg8:m,arg9:mpiis,arg10:s,arg11:seq
	private String input;
	private String output;
	private String opfsm;
	private String num;
	private String b;
	private String mxp;
	private String mp;
	private String m;
	private String mpiis;
	private String s;
	private String seq;
	@Override
	public void run() {
		
		String[] args=null;
		if("true".equals(b)){// 包含评分值
			if("true".equals(seq)){// true 序列化
				args= new String[21];
				args[20]="--sequencefileOutput";
			}else{
				args=new String[20];
				// do nothing
			}
			
		}else{// 不包含评分值
			if("true".equals(seq)){// 序列化
				args= new String[22];
				args[21]="--sequencefileOutput";
			}else{// 输出不序列化
				args=new String[21];
			}
			args[20]="-b";// 不包含评分值
		}
		args[0]="-i";
		args[1]=input;
		args[2]="-o";
		args[3]=output;
		args[4]="-opfsm";
		args[5]=opfsm;
		args[6]="-n";
		args[7]=num;
		args[8]="-mp";
		args[9]=mp;
		args[10]="-m";
		args[11]=m;
		args[12]="-mpiis";
		args[13]=mpiis;
		args[14]="-mxp";
		args[15]=mxp;
		args[16]="-s";
		args[17]=s;
		args[18]="--tempDir";
		args[19]="temp";
		
		Utils.printStringArr(args);
		try {
			HUtils.getFs().delete(new Path("temp"), true);
			HUtils.getFs().delete(new Path(opfsm), true);
			HUtils.getFs().delete(new Path(output), true);
			
			int ret = ToolRunner.run(HUtils.getConf()	,new RecommenderJob()	, args);
			if(ret==0){// 所有任务运行完成
				HUtils.setALLJOBSFINISHED(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 任务中，报错，需要在任务监控界面体现出来
			HUtils.setRUNNINGJOBERROR(true);
			Utils.simpleLog("RecommendItembased任务错误！");
		}
	}

	@Override
	public void setArgs(String[] args) {
//		arg1:input,arg2:output,arg3:opfsm,arg4:num,arg5:b,
//		arg6:mxp,arg7:mp,arg8:m,arg9:mpiis,arg10:s,arg11:seq
		this.input=args[0];
		this.output=args[1];
		this.opfsm=args[2];
		this.num=args[3];
		this.b=args[4];
		this.mxp=args[5];
		this.mp=args[6];
		this.m=args[7];
		this.mpiis=args[8];
		this.s=args[9];
		this.seq=args[10];
	}

}
