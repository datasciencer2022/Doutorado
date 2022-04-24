<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Projeto Doutorado - Isaque Katahira</title>
<style type="text/css">
td {
		font-style: italic;
		font-weight: bold;
		font-size: 15px;
		font-family: arial, sans-serif;
	}
</style>
</head>
<body>
	
	<form name="frmDataSciencer01" action="./datasciencer.do" method="POST"
            	enctype="multipart/form-data">
            	<input type="hidden" name = "action" value="similaridade">
                	<center>
								
	                                    
                                <table class="text1" style="border-collapse:separate;border-spacing:1em;">
                                    <tr>
                                    <td align="center" colspan="2"><img src="./images/unesp-logo-11.png" width="20%" height="20%"></td> 
                                       
                                    </tr>
                                
                                    <tr>
                                       <td align="center" colspan = "2"><div class="title2">Redução de Dimensionalidade</div></td> 
                                    </tr>
                                    <tr>	                                            
							   			  <td>	                                                   
                                              Porcentagem de similaridade:
                                           </td>
                                           <td>
                                                   <input type="number" name="porcentagem" value="90">
                                           </td>
                                    </tr>
                                    <tr>	                                            
							   			  <td>	                                                   
                                              Arquivo a ser otimizado:
                                           </td>
                                           <td>
                                                   <input type="file" name="arquivoReduz" required>
                                           </td>
                                    </tr>
                                    <tr>	                                            
							   			  <td colspan="2">	                                                   
                                              Limite máximo: 2.000 linhas
                                           </td>
                                    </tr>
                                    <tr>
                                        <td align="center" colspan="2">
                                           <input type="submit" value="Otimizar">
                                       </td>
                                    </tr>
                                </table>
               		 </center>
               	</form>
</body>
</html>