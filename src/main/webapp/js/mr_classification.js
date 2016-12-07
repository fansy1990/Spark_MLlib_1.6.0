$(function(){
	// trainnb---
	$('#trainnb_submit').bind('click', function(){
		
		// 检查是否有“MR监控页面”，如果有，则退出，并提示关闭
		if(exitsMRmonitor()){
			return ;
		}	
		var input=$('#trainnb_input').val();//
		var output=$('#trainnb_output').val();//
		var alphaI=$('#trainnb_alphaI').val();//
		var labelIndex=$('#trainnb_labelIndex').val();
//		var trainComplementary=$('#trainnb_trainComplementary').combobox("getValue");;
		
		var jobnums_='2'; // 一共的MR个数
		// 弹出进度框
		popupProgressbar('分类MR','trainnb任务提交中...',1000);
		// ajax 异步提交任务
		
		callByAJax('cloud/cloud_submitIterMR.action',{algorithm:"TrainNaiveBayesJobRunnable",jobnums:jobnums_,
			arg1:input,arg2:output,arg3:alphaI,arg4:labelIndex
//			,arg5:trainComplementary
			});
		
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
