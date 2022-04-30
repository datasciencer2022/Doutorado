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

p {
		font-style: italic;
		font-weight: bold;
		font-size: 15px;
		font-family: Courier, monospace;
		text-align: justify;
	}

</style>

<!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">

</head>
<body>
	
	<form name="frmDataSciencer01" action="./datasciencer.do" method="POST"
            	enctype="multipart/form-data">
            	<input type="hidden" name = "action" value="parteUpload">
                	<center>
								
	                                    
                                <table class="text1" style="border-collapse:separate;border-spacing:1em;">
                                    <tr>
                                    <td align="center" rowspan="7"><img src="./images/figuraSite.png" width="92%" height="92%"></td>
                                    <td align="center" colspan="2"><img src="./images/unesp-logo-11.png" width="20%" height="20%"></td> 
                                       
                                    </tr>
                                
                                    <tr>
                                       <td align="center" colspan = "2"><div class="title2">Upload dos Arquivos de Entrada</div></td> 
                                    </tr>
                                    
                                    <tr>	                                            
							   			  <td>	                                                   
                                              Carregar logs:
                                           </td>
                                           <td>
                                                   <input type="file" name="logs" class="btn btn-outline-primary" required>
                                           </td>
                                        </tr>
                                        
                                        <tr>	                                            
							   			   <td>	                                                   
                                              Carregar palavras-chave:
                                           </td>
                                           <td>
                                                   <input type="file" name="palavrasChave"  class="btn btn-outline-primary" required>
                                           </td>
                                        </tr>
                                        
                                        <tr>	                                            
							   				<td>	                                                   
                                              Carregar termos Tesauro:
                                           </td>
                                           <td>
                                                   <input type="file" name="tesauro"  class="btn btn-outline-primary" required>
                                           </td>
                                        </tr>
                                    
                                        <tr>	                                            
							   				<td>	                                                   
                                              Tipo de Resposta:
                                           </td>
                                           <td>
                                           		<table>
                                           		   <tr><td>
                                                   <input type="radio" name="typeResult" value="P1" required checked></td><td> Intersecção de todos (P1)</br>
                                                   </td></tr>
                                                   <tr><td>
                                                   <input type="radio" name="typeResult" value="P2" ></td><td> P2</br>
                                                   </td></tr>
                                                   <tr><td>
                                                   <input type="radio" name="typeResult" value="P3" ></td><td> P3</br>
                                                   </td></tr>
                                                   <tr><td>
                                                   <input type="radio" name="typeResult" value="P4" ></td><td> P4</br>
                                                   </td></tr>
                                                  </table>
                                           </td>
                                  </tr>
                                  <tr>	                                            
							   			  <td colspan="2">	                                                   
                                              Limite máximo: 300 linhas
                                           </td>
                                    </tr>
                                </table>
                         <input type="submit"  class="btn btn-primary" value="Análise de Dados">
               		 </center>
               	</form>
               	</br></br>
       
</body>
</html>