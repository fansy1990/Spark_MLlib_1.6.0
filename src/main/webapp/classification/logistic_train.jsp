<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
 
<body>
	<div style="padding-left: 30px;font-size: 20px;padding-top:10px;">逻辑回归模型训练任务算法调用</div>
	<br>
	
	<div style="padding-left: 30px;font-size: 20px;padding-top:10px;">  
	 
		<table>
			<tr>
				<td><label >HDFS输入路径:</label>
				</td>
				<td><input class="easyui-validatebox" type="text"
				 value="/user/algorithm/input/logistic.dat"
					id="logistic_train_input" data-options="required:true" style="width:300px" />
				</td>
			</tr>
			
			<tr>
				<td><label >HDFS输出路径:</label>
				</td>
				<td><input class="easyui-validatebox" type="text" 
				 value="/user/algorithm/model/logistic/output00"
					id="logistic_train_output" data-options="required:true" style="width:300px" />
				</td>
			</tr>
			
			<tr>
				<td><label >平滑参数:</label>
				</td>
				<td><input class="easyui-validatebox" type="text" value="1.0"
					id="trainnb_alphaI" data-options="required:true" style="width:300px" />
				</td>
			</tr>
			<!--  此参数暂未调通，不使用 
			<tr>
				<td><label for="name">使用额外的训练:</label>
				</td>
				<td>
				<select id="trainnb_trainComplementary" class="easyui-combobox" 
					style="width:200px;">
						<option value="false">否</option>
						<option value="true">是</option>

				</select>
				</td>
			</tr>
			-->
			<tr>
				<td><label >HDFS类别存储路径:</label>
				</td>
				<td><input class="easyui-validatebox" type="text" 
				value="/user/root/classification/trainnb/labelIndex"
					id="trainnb_labelIndex" data-options="required:true" style="width:300px" />
				</td>
			</tr>
			
			<tr>
				<td><a id="trainnb_submit" class="easyui-linkbutton"
					data-options="iconCls:'icon-door_in'">提交</a></td>
			</tr>
			
		</table>
	
	</div> 
	<script type="text/javascript" src="js/mr_classification.js"></script>  

</body>

