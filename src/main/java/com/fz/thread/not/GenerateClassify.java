/**
 * 
 */
package com.fz.thread.not;

import com.fz.util.HUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.Text;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
//import com.com.fz.util.Utils;

/**
 * @author fansy
 * @date 2015年8月10日
 */
public class GenerateClassify implements INotMRJob {

	private String input;
	private String output;
	private String labelindex;
	
	@Override
	public void setArgs(String[] args) {
//		this.input=Utils.getRootPathBasedPath(args[0]);// 获取绝对路径
		this.input=args[0];
		this.output=args[1];
		this.labelindex=args[2];
	}

	@Override
	public Map<String, Object> runJob() {
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("return_show", "generate_classify_return");
		InputStream in = null;
		Writer writer = null;
		try{
			Path path = new Path(input);
			String inputFilename = path.getName();
			Path output_= new Path(output);
			writer = SequenceFile.createWriter(HUtils.getConf(), Writer.file(output_),
					Writer.keyClass(Text.class),
					Writer.valueClass(VectorWritable.class));
			FileSystem fs = HUtils.getFs();
			in = fs.open(path);
			BufferedReader read = new BufferedReader(new InputStreamReader(in));
			String line = null;
			String[] lines=null;
			Text key = new Text();
			VectorWritable vw = new VectorWritable();
			while ((line = read.readLine()) != null) {
				lines = line.split(",");
				if("start".equals(labelindex)){// 第一个
					key.set(inputFilename+"/"+lines[0]);// 需要注意 这里需要构造"文件名+label"的组合
					vw.set(getVector(lines,1,lines.length-1));
				}else{
					key.set(inputFilename+"/"+lines[lines.length-1]);
					vw.set(getVector(lines,0,lines.length-2));
				}
				writer.append(key, vw);
			}
			map.put("flag", "true");
			map.put("return_txt", "序列文件生成在："+output);
		}catch(Exception e){
			e.printStackTrace();
			map.put("flag", "false");
			map.put("monitor", "false");
			map.put("msg", e.getMessage());
		} finally {
			try {
				in.close();
				IOUtils.closeStream(writer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * @param lines
	 * @param start
	 * @param end
	 * @return
	 */
	private Vector getVector(String[] lines, int start, int end) {
	
		DenseVector vector=new DenseVector(end-start+1);
		int k=0;
		for(int i=start;i<=end;i++){
			vector.set(k++, Double.parseDouble(lines[i]));
		}
		return vector;
	}



}
