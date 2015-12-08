/**
 * 上传文件
 */
function initUploadPlugin() {
	var options_info = {
		url : "FileUploadServlet?token=84e285c0a7735c44b61fd7fd2f337026&code=3&imgtype=2&goodId=&colorName=",
		dataType : null,
		success : function(data) {
			alert("导入成功");
		},
		error : function() {
			alert("导入失败");
		}
	};
	$('#infoExcelFile').on(
			"change",
			function() {
				if ($("#infoExcelFile").val() == "") {
					return;
				} else {
					var fileAllowExt = $("#infoExcelFile").val().split(".");
					if ($.inArray(fileAllowExt[fileAllowExt.length - 1]
							.toLocaleLowerCase(),
							[ "xls", "xlsx", "jpg", "png" ]) == -1) {
						alert("上传文件只支持"
								+ [ "xls", "xlsx", "jpg", "png" ].join("、")
								+ "格式");
						return;
					}
				}
				$("form#upload4info").ajaxSubmit(options_info);
				return;
			});
}