<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<html>
<head>
	<title>宝易互通示例——商户发送</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body >
	<h1>宝易互通示例——商户发送</h1>         
	<form name="ExampleForm" action="https://www.umbpay.com/pay2_1_/paymentImplAction.do" method="post" target="_blank">
	
	<input  name="merchantid"  type="text" value=""/>
	<input  name="merorderid"  type="text" value="">
	<input  name="amountsum"  type="text" value="">
	<input  name="subject"  type="text" value="">
	<input  name="currencytype"  type="text" value="">
	<input  name="autojump"  type="text" value="">
	<input  name="waittime"  type="text" value="">
	<input  name="merurl"  type="text" value="">
	<input  name="informmer"  type="text" value="">
	<input  name="informurl"  type="text" value="">
	<input  name="confirm"  type="text" value="">
	<input  name="merbank"  type="text" value="">
	<input  name="tradetype"  type="text" value="">
	<input  name="bankInput"  type="text" value="">
	<input  name="interface"  type="text" value="">
	<input  name="bankcardtype"  type="text" value="">
	<input  name="pdtdetailurl"  type="text" value="">
	<input  name="mac"  type="text" value="">
	<input  name="remark"  type="text" value="">
	<input  name="pdtdnm"  type="text" value="">
	<input name="submit" type="submit" value=" 到宝易互通支付 " >
	</form>
	
	<!-- <form action="recive_xml_mess_api" method="post">
	
	<input  name="merchantid"  type="text" value="1162"/>
	<input  name="merorderid"  type="text" value="123456"/>
	<input  name="amountsum"  type="text" value="0.1"/>
	<input  name="currencytype"  type="text" value="01"/>
	<input  name="subject"  type="text" value="1162000"/>
	<input  name="state"  type="text" value="1"/>
	<input  name="paybank"  type="text" value="ICBC"/>
	<input  name="banksendtime"  type="text" value="2015-04-29"/>
	<input  name="merrecvtime"  type="text" value="2015-04-29"/>
	<input  name="interface"  type="text" value="5.00"/>
	<input  name="mac"  type="text" value="vzvavdfbvsdvaqvrgngfsbvrn"/>
	
	
	<input name="submit" type="submit" value=" 到宝易互通支付 " >
	</form> -->
		
	<!--</form>
	
--><!--<script type="text/javascript" language="javascript">

    function submitform()
    {
        document.ExampleForm.submit();
    }
    window.onload = submitform;
</script>
	
--></body>
</html>
