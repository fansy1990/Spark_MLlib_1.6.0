/**
 * 
 */
package com.fz.action;

import com.alibaba.fastjson.JSON;
import com.fz.model.CurrentJobInfo;
import com.fz.thread.RunnableWithArgs;
import com.fz.thread.not.INotMRJob;
import com.fz.util.HUtils;
import com.fz.util.Utils;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author fansy
 * @date 2015-8-4
 */
@Component("cloudAction")
public class CloudAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String algorithm; // MR或非MR算法, 实例化的类名
	private String jobnums; // MR算法Job的个数
	
	
	// 算法页面参数,每个页面不超过11个参数,如果超过，则需要修改
	private String arg1; 
	private String arg2;
	private String arg3;
	private String arg4;
	private String arg5;
	private String arg6;
	private String arg7;
	private String arg8;
	private String arg9;
	private String arg10;
	private String arg11;
		
	/**
	 * 提交MR任务
	 * 算法具体参数意思对照jsp页面理解，每个实体类会把arg1~arg11 转换为实际的意思
	 */
	public void submitJob(){	
		Map<String ,Object> map = new HashMap<String,Object>();
		try {
			//提交一个Hadoop MR任务的基本流程
			// 1. 设置提交时间阈值,并设置这组job的个数
			//使用当前时间即可,当前时间往前10s，以防服务器和云平台时间相差
			HUtils.setJobStartTime(System.currentTimeMillis()-10000);// 
			HUtils.setRUNNINGJOBERROR(false); // 每次运行 ，需要设置运行任务错误标识为false
			HUtils.JOBNUM=Integer.parseInt(jobnums);
			// 2. 使用Thread的方式启动一组MR任务
			// 2.1 生成Runnable接口
			RunnableWithArgs runJob = (RunnableWithArgs) Utils.getClassByName(
					Utils.THREADPACKAGES+algorithm);
			// 2.2 设置参数
			runJob.setArgs(new String[]{arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8,arg9,arg10,arg11});
			// 2.3 启动Thread
			new Thread(runJob).start();
			
			// 3. 启动成功后，直接返回到监控，同时监控定时向后台获取数据，并在前台展示；
			
			map.put("flag", "true");
			map.put("monitor", "true");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("flag", "false");
			map.put("monitor", "false");
			map.put("msg", algorithm+"任务启动失败！");
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;
	}

	/**
	 * 提交非MR的任务
	 * 算法具体参数意思对照jsp页面理解，每个实体类会把arg1~arg11 转换为实际的意思
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public void submitJobNotMR() throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		
		Map<String ,Object> map = new HashMap<String,Object>();
		INotMRJob runJob = (INotMRJob) Utils.getClassByName(
				Utils.THREADNOTPACKAGES+algorithm);
		// 2.2 设置参数
		runJob.setArgs(new String[]{arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8,arg9,arg10,arg11});
		map= runJob.runJob();
		
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return ;
	}
	
	/**
	 * 提交变jobnum的任务，暂未添加
	 * 
	 */
	public void submitIterMR(){
		Map<String ,Object> map = new HashMap<String,Object>();
		try {
			//提交一个Hadoop MR任务的基本流程
			// 1. 设置提交时间阈值,并设置这组job的个数
			//使用当前时间即可,当前时间往前10s，以防服务器和云平台时间相差
			HUtils.setJobStartTime(System.currentTimeMillis()-10000);// 
			// 由于不知道循环多少次完成，所以这里设置为最大值，
			// 当所有MR完成的时候，在监控代码处重新设置JOBNUM；
			HUtils.setALLJOBSFINISHED(false);
			HUtils.JOBNUM=Integer.parseInt(jobnums);
			
			// 2. 使用Thread的方式启动一组MR任务
			// 2.1 生成Runnable接口
			RunnableWithArgs runJob = (RunnableWithArgs) Utils.getClassByName(
					Utils.THREADPACKAGES+algorithm);
			// 2.2 设置参数
			runJob.setArgs(new String[]{arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8,arg9,arg10,arg11});
			// 2.3 启动Thread
			new Thread(runJob).start();
			// 3. 启动成功后，直接返回到监控，同时监控定时向后台获取数据，并在前台展示；
			
			map.put("flag", "true");
			map.put("monitor", "true");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("flag", "false");
			map.put("monitor", "false");
			map.put("msg", "任务启动失败！");
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
	}

	/**
	 * 任务监控
	 * @throws java.io.IOException
	 */
	public void monitorone() throws IOException{
    	Map<String ,Object> jsonMap = new HashMap<String,Object>();
    	List<CurrentJobInfo> currJobList =null;
    	if(HUtils.isRUNNINGJOBERROR()){// 任务提交阶段运行失败
    		jsonMap.put("finished", "error");
			HUtils.setJobStartTime(System.currentTimeMillis());
			Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
			return ;
    	}
    	try{
    		currJobList= HUtils.getJobs();
    		jsonMap.put("jobnums", HUtils.JOBNUM);
    		// 任务完成的标识是获取的任务个数必须等于jobNum，同时最后一个job完成
    		// true 所有任务完成
    		// false 任务正在运行
    		// error 某一个任务运行失败，则不再监控
    		
    		// 由Runnable设置任务是否运行完成，如果任务运行完成，那么就设置标志位，然后再次设置JOBNUM
    		if(HUtils.isALLJOBSFINISHED()){
    			HUtils.JOBNUM=currJobList.size();
    		}
    		
    		if(currJobList.size()>=HUtils.JOBNUM){// 如果返回的list有JOBNUM个，那么才可能完成任务
    			if("success".equals(HUtils.hasFinished(currJobList.get(currJobList.size()-1)))){
    				jsonMap.put("finished", "true");
    				// 运行完成，初始化时间点
    				HUtils.setJobStartTime(System.currentTimeMillis());
    			}else if("running".equals(HUtils.hasFinished(currJobList.get(currJobList.size()-1)))){
    				jsonMap.put("finished", "false");
    			}else{// fail 或者kill则设置为error
    				jsonMap.put("finished", "error");
    				HUtils.setJobStartTime(System.currentTimeMillis());
    			}
    		}else if(currJobList.size()>0){
    			if("fail".equals(HUtils.hasFinished(currJobList.get(currJobList.size()-1)))||
    					"kill".equals(HUtils.hasFinished(currJobList.get(currJobList.size()-1)))){
    				jsonMap.put("finished", "error");
    				HUtils.setJobStartTime(System.currentTimeMillis());
    			}else{
    				jsonMap.put("finished", "false");
    			}
        	}
    		
    		
    		if(currJobList.size()==0){
    			jsonMap.put("finished", "false");
//    			return ;
    		}else{
    			if(jsonMap.get("finished").equals("error")){
    				CurrentJobInfo cj =currJobList.get(currJobList.size()-1);
    				cj.setRunState("Error!");
    				jsonMap.put("rows", cj);
    			}else{
    				jsonMap.put("rows", currJobList.get(currJobList.size()-1));
    			}
    		}
    		jsonMap.put("currjob", currJobList.size());
    	}catch(Exception e){
    		e.printStackTrace();
    		jsonMap.put("finished", "error");
    		HUtils.setJobStartTime(System.currentTimeMillis());
    	}
    	Utils.simpleLog(JSON.toJSONString(jsonMap));
    	
    	Utils.write2PrintWriter(JSON.toJSONString(jsonMap));// 使用JSON数据传输
    	
    	return ;
    }


	public String getAlgorithm() {
		return algorithm;
	}


	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}


	public String getJobnums() {
		return jobnums;
	}


	public void setJobnums(String jobnums) {
		this.jobnums = jobnums;
	}


	public String getArg1() {
		return arg1;
	}


	public void setArg1(String arg1) {
		this.arg1 = arg1;
	}


	public String getArg2() {
		return arg2;
	}


	public void setArg2(String arg2) {
		this.arg2 = arg2;
	}


	public String getArg3() {
		return arg3;
	}


	public void setArg3(String arg3) {
		this.arg3 = arg3;
	}


	public String getArg4() {
		return arg4;
	}


	public void setArg4(String arg4) {
		this.arg4 = arg4;
	}


	public String getArg5() {
		return arg5;
	}


	public void setArg5(String arg5) {
		this.arg5 = arg5;
	}


	public String getArg6() {
		return arg6;
	}


	public void setArg6(String arg6) {
		this.arg6 = arg6;
	}


	public String getArg7() {
		return arg7;
	}


	public void setArg7(String arg7) {
		this.arg7 = arg7;
	}


	public String getArg8() {
		return arg8;
	}


	public void setArg8(String arg8) {
		this.arg8 = arg8;
	}


	public String getArg9() {
		return arg9;
	}


	public void setArg9(String arg9) {
		this.arg9 = arg9;
	}


	public String getArg10() {
		return arg10;
	}


	public void setArg10(String arg10) {
		this.arg10 = arg10;
	}


	public String getArg11() {
		return arg11;
	}


	public void setArg11(String arg11) {
		this.arg11 = arg11;
	}

}
