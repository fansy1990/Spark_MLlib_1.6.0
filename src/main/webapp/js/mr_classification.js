$(function(){
	// logistic regression ---
	$('#logistic_train_submit').bind('click', function(){
		// get parameters
		var input=$('#logistic_train_input').val();//
		var output=$('#logistic_train_output').val();//
		var targetIndex=$('#logistic_train_targetIndex').numberbox('getValue');//
		var numClasses=$('#logistic_train_numClasses').numberbox('getValue');//
		var splitter=$('#logistic_train_splitter').val();
		var method=$('#logistic_train_method').combobox("getValue");
		var hasIntercept=$('#logistic_train_hasIntercept').combobox("getValue");


		// 弹出进度框 ,不同用户提交没有影响，这里不是interval，而是实实在在的进度
		popupProgressbar('回归','逻辑回归任务初始化中...',1000);
		// ajax 异步提交任务
		console.info("input:"+input+",output:"+output+",targetIndex:"+targetIndex+
		",numClasses:"+numClasses+",splitter:"+splitter+",method:"+method+",hasIntercept:"+hasIntercept);
		// 直接一个进度条显示即可，把进度条拉长点，然后修改器状态，而不是interval设置
		// 状态有：任务初始化，任务提交完成；任务运行进度（初始化，accept，running，finished）
//		callByAJax('cloud/cloud_submitIterMR.action',{algorithm:"TrainNaiveBayesJobRunnable",jobnums:jobnums_,
//			arg1:input,arg2:output,arg3:alphaI,arg4:labelIndex
//			,arg5:trainComplementary
//			});
		
	});
	
	// ------trainnb
	
	
	// testnb---
	$('#testnb_submit').bind('click', function(){
		
		// 检查是否有“MR监控页面”，如果有，则退出，并提示关闭
		if(exitsMRmonitor()){
			return ;
		}	
		var input=$('#testnb_input').val();//
		var output=$('#testnb_output').val();//
		var model=$('#testnb_model').val();//
		var labelIndex=$('#testnb_labelIndex').val();
		
		var jobnums_='1'; // 一共的MR个数
		// 弹出进度框
		popupProgressbar('分类MR','testnb任务提交中...',1000);
		// ajax 异步提交任务
		
		callByAJax('cloud/cloud_submitIterMR.action',{algorithm:"TestNaiveBayesDriverRunnable",jobnums:jobnums_,
			arg1:input,arg2:output,arg3:model,arg4:labelIndex	});
		
	});
//	==================testnb
	
	
	// buildforest---
	$('#buildforest_submit').bind('click', function(){
		
		// 检查是否有“MR监控页面”，如果有，则退出，并提示关闭
		if(exitsMRmonitor()){
			return ;
		}	
		var input=$('#buildforest_input').val();//
		var output=$('#buildforest_output').val();//
		var describe=$('#buildforest_describe').val();//
		var selection=$('#buildforest_selection').val();//
		var minsplit=$('#buildforest_minsplit').val();
		var minprop=$('#buildforest_minprop').val();
		var nbtrees=$('#buildforest_nbtrees').val();
		var complete=$('#buildforest_complete').combobox("getValue");
		var jobnums_='1'; // 一共的MR个数
		// 弹出进度框
		popupProgressbar('分类MR','buildforest任务提交中...',1000);
		// ajax 异步提交任务
		
		callByAJax('cloud/cloud_submitJob.action',{algorithm:"BuildForestRunnable",jobnums:jobnums_,
			arg1:input,arg2:output,arg3:describe,arg4:selection,arg5:minsplit,arg6:minprop,
			arg7:nbtrees,arg8:complete});
		
	});
//	==================buildforest
	
	
	
	// testforest---
	$('#testforest_submit').bind('click', function(){
		
		// 检查是否有“MR监控页面”，如果有，则退出，并提示关闭
		if(exitsMRmonitor()){
			return ;
		}	
		var input=$('#testforest_input').val();//
		var output=$('#testforest_output').val();//
		var describe=$('#testforest_describe').val();//
		var model=$('#testforest_model').val();
		// 弹出进度框
		popupProgressbar('分类MR','testforest任务提交中...',1000);
		// ajax 异步提交任务
		
		callByAJax('cloud/cloud_submitJobNotMR.action',{algorithm:"TestForestRunnable",
			arg1:input,arg2:output,arg3:describe,arg4:model});
		
	});
//	==================testforest
	

	
	
});
