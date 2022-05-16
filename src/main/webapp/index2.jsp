<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Projeto Doutorado - Isaque Katahira</title>
<style type="text/css">

input[type=button], input[type=submit], input[type=reset] {
  background-color: white;
  color: black;
  font-weight: bold;
  font-size: 30px;
  border: 2px solid #FF0000;
}



td {
		font-style: italic;
		font-weight: bold;
		font-size: 15px;
		font-family: arial, sans-serif;
	}
</style>
</head>
<body>
	<center>
	</br>
	<table class="text1" style="border-collapse:separate;border-spacing:1em;">
                                    <tr>
                                    <td align="center" rowspan="7"><img src="./images/figuraSite.png" width="75%" height="75%"></td>
                                    <td align="center" colspan="2"><img src="./images/unesp-logo-11.png" width="20%" height="20%"></td> 
                                    </tr>
                                </table>
                                <table>
                                    <tr>
                                       <td align="center">
											<form name="frmDataSciencer00a" action="./datasciencer.do" method="GET">
												<input type="hidden" name = "action" value="callProjeto1">
												<input type="submit" value="Análise de Dados">
											</form>
                                       </td> 
                                    </tr>

									<tr>
                                    <td align="center">
										&nbsp;
									</td>
                                    </tr>

                                    <tr>
                                    <td align="center">
											<form name="frmDataSciencer00b" action="./datasciencer.do" method="GET">
												<input type="hidden" name = "action" value="callProjeto2">
												<input type="submit" value="Redução de Dimensionalidade">
											</form>
									</td>
                                    </tr>
	</table>	
</center>
	
</body>
</html>