/**
 * 
 */
package com.fz.thread.not;

import com.fz.util.HUtils;
import org.apache.mahout.classifier.df.tools.Describe;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fansy
 * @date 2015年8月5日
 */
public class DescribeTest implements INotMRJob {

	private String input;
	private String output;
	private String description;
	private String regress;
	
	@Override
	public void setArgs(String[] args) {
		this.input=args[0];
		this.output=args[1];
		this.description=args[2];
		this.regress=args[3];
	}

	@Override
	public Map<String, Object> runJob() {
		Map<String,Object> map = new HashMap<String,Object>();
		String[] args=null;
		String[] descriptions = description.split(" ");
		if("true".equals(regress)){
			args=new String[5+descriptions.length+1];
			initialPre(args,new String[]{input,output},true);
			initialLast(args,descriptions);
			
		}else{
			args=new String[4+descriptions.length+1];
			initialPre(args,new String[]{input,output},false);
			initialLast(args,descriptions);
		}
		map.put("return_show", "describe_return");
		try{
			HUtils.getFs().delete(HUtils.getPath(output),true);
			Describe.main(args);
			map.put("flag", "true");
			map.put("return_txt", "描述文件生成在："+output);
		}catch(Exception e){
			e.printStackTrace();
			map.put("flag", "false");
			map.put("monitor", "false");
			map.put("msg", e.getMessage());
		}
		return map;
	}

	/**
	 * @param args
	 * @param descriptions
	 */
	private void initialLast(String[] args, String[] descriptions) {
		int aLength= args.length;
		int dLength = descriptions.length;
		for(int i=aLength-dLength,j=0;i<aLength;i++,j++){
			args[i]=descriptions[j];
		}
	}

	/**
	 * 
	 * @param args
	 * @param strings ：需要添加全路径
	 * @param regress: 是否添加-r属性
	 */
	private void initialPre(String[] args, String[] strings, boolean regress) {
		int i=0;
		args[i++]="-p";
		args[i++]=HUtils.getFullHDFS(strings[0]);
		args[i++]="-f";
		args[i++]=HUtils.getFullHDFS(strings[1]);
		if(regress){
			args[i++]="-r";
		}	
		args[i++]="-d";
	}

}
