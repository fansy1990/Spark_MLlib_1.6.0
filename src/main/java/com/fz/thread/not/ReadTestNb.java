/**
 * 
 */
package com.fz.thread.not;

import com.fz.util.HUtils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.mahout.classifier.ClassifierResult;
import org.apache.mahout.classifier.ResultAnalyzer;
import org.apache.mahout.classifier.naivebayes.BayesUtils;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.iterator.sequencefile.PathFilters;
import org.apache.mahout.common.iterator.sequencefile.PathType;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileDirIterable;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import java.util.HashMap;
import java.util.Map;

/**
 * 读取testnb分类结果
 * 
 * @author fansy
 * @date 2015年8月11日
 */
public class ReadTestNb implements INotMRJob {
	private String output;
	private String label;

	@Override
	public void setArgs(String[] args) {
		this.output = args[0];
		this.label = args[1];
	}

	@Override
	public Map<String, Object> runJob() {
		Map<String, Object> map = new HashMap<String, Object>();
		String txt = null;
		map.put("return_show", "readtestnb_return");
		try {
			// load the labels
			Map<Integer, String> labelMap = BayesUtils.readLabelIndex(
					HUtils.getConf(), new Path(label));

			// loop over the results and create the confusion matrix
			SequenceFileDirIterable<Text, VectorWritable> dirIterable = new SequenceFileDirIterable<>(
					new Path(output), PathType.LIST, PathFilters.partFilter(),
					HUtils.getConf());
			ResultAnalyzer analyzer = new ResultAnalyzer(labelMap.values(),
					"DEFAULT");
			analyzeResults(labelMap, dirIterable, analyzer);
//			txt = analyzer.getConfusionMatrix().toString();
			txt = analyzer.toString();
			txt=txt.replaceAll("\n", "<br>");
			txt = "分类的结果是:<br>" + txt;
			map.put("flag", "true");

			map.put("return_txt", txt);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("flag", "false");
			map.put("monitor", "false");
			map.put("msg",   "testnb分类结果读取失败！");
		}
		return map;
	}

	private static void analyzeResults(Map<Integer, String> labelMap,
			SequenceFileDirIterable<Text, VectorWritable> dirIterable,
			ResultAnalyzer analyzer) {
		for (Pair<Text, VectorWritable> pair : dirIterable) {
			int bestIdx = Integer.MIN_VALUE;
			double bestScore = Long.MIN_VALUE;
			for (Vector.Element element : pair.getSecond().get().all()) {
				if (element.get() > bestScore) {
					bestScore = element.get();
					bestIdx = element.index();
				}
			}
			if (bestIdx != Integer.MIN_VALUE) {
				ClassifierResult classifierResult = new ClassifierResult(
						labelMap.get(bestIdx), bestScore);
				analyzer.addInstance(pair.getFirst().toString(),
						classifierResult);
			}
		}
	}

}
