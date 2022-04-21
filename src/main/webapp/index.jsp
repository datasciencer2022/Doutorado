<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Projeto Doutorado do Isaque</title>
</head>
<body>
	<h1>DataSciencer sucess!!</h1>
	<form name="frmDataSciencer01" action="/Doutorado/datasciencer.do" method="POST"
            	enctype="multipart/form-data">
                	<center>
	                    <table>
	                        <tr>
	                            <td align="center">
	                                    <input type="hidden" name="action" value="parteUpload" />
	                                    
	                                    <table>
	                                        <tr>
	                                           <td align="center"><div class="title2">Upload dos Arquivos de Entrada</div></td> 
	                                        </tr>
	                                        <tr>
	                                            <td align="center">
	                                            <table class="text1" style="border-collapse:separate;border-spacing:1em;">
	                                            
												
	                                            <tr>	                                            
												   <td>	                                                   
	                                                  Arquivo #1 para incluir em resultados:
	                                               </td>
	                                               <td>
	                                                       <input type="file" name="arquivo1" required>
	                                               </td>
	                                            </tr>
	                                            
	                                            <tr>	                                            
												   <td>	                                                   
	                                                  Arquivo #2 para incluir em resultados:
	                                               </td>
	                                               <td>
	                                                       <input type="file" name="arquivo2" required>
	                                               </td>
	                                            </tr>
	                                            
	                                            <tr>	                                            
												   <td>	                                                   
	                                                  Arquivo  para excluir em resultados:
	                                               </td>
	                                               <td>
	                                                       <input type="file" name="arquivoExclui" required>
	                                               </td>
	                                            </tr>
	                                            	                                            </table>
	                                           </td>
	                                        </tr>
	                                        
	                                        <tr>
	                                            <td align="center">
	                                               <input type="submit" id="idUpDownStream" value=" Search" name="Enter"/>
	                                           </td>
	                                        </tr>
	                                        
	                                    </table>
	                            </td>
	                        </tr>

	                    </table>
               		 </center>
               	</form>
</body>
</html>