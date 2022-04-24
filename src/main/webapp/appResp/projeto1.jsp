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
                                                   <input type="file" name="logs" required>
                                           </td>
                                        </tr>
                                        
                                        <tr>	                                            
							   			   <td>	                                                   
                                              Carregar palavras-chave:
                                           </td>
                                           <td>
                                                   <input type="file" name="palavrasChave" required>
                                           </td>
                                        </tr>
                                        
                                        <tr>	                                            
							   				<td>	                                                   
                                              Carregar termos Tesauro:
                                           </td>
                                           <td>
                                                   <input type="file" name="tesauro" required>
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
                         <input type="submit" value="Análise de Dados">
               		 </center>
               	</form>
       <p>Os Repositórios Institucionais (RI) têm papel fundamental na divulgação do conhecimento, especialmente das publicações acadêmicas. Executados em diversas plataformas de software, com grande diversidade de sistemas de instalação, configuração e suporte, os RI remodelaram as formas de armazenamento, organização e recuperação de materiais, trazendo maior agilidade para todos esses processos.  No entanto, disponibilizar recursos on-line para recuperação direta dos usuários é desafiador, na medida em que se faz necessário combinar o processo técnico realizado internamente nas bibliotecas com as necessidades dos usuários externos.  Nessa perspectiva, a falta de articulação entre os vocabulários utilizados durante a indexação e os vocabulários utilizados pelos usuários tende a provocar ruídos e fracassos nos processos de busca e recuperação. A fim de que a recuperação de documentos ocorra de modo efetivo, faz-se necessário padronizar a indexação, minimizar os ruídos intrínsecos à linguagem natural, como ambiguidades, sinonímias, inadequações de registros e ampliar a convergência entre a linguagem natural dos usuários e os vocabulários controlados disponibilizados no arquivamento e na organização dos repositórios. Objetivo: analisar os logs de buscas de usuários do Repositório Institucional Unesp para propor atualização do vocabulário controlado Tesauro Unesp. Metodologia: abordagem mista, qualitativa e quantitativa, classificada quanto aos objetivos como exploratória e descritiva. O corpus é formado pela lista de termos de busca utilizados pelos usuários comparados à listagem de termos pertencentes ao Tesauro Unesp e às palavras-chave atribuídas pelos autores aos materiais indexados no RI Unesp. O método proposto foi implementado a partir do modelo teórico proposto por Jansen (2008) em três etapas: coleta, preparação e análise dos logs. Resultados e Discussão: a análise da lista de termos oriundos dos logs de busca dos usuários comparada a lista de palavras-chave atribuídas pelos autores aos documentos indexados no RI Unesp viabilizou a obtenção de uma lista de expressões de busca contendo palavras simples, palavras compostas e frases as quais compõem lista de 760 termos candidatos à atualização do vocabulário controlado Tesauro Unesp. Considerações Finais: a análise de logs se apresentou como fonte confiável de informações, as quais gerenciadas por estratégias computacionais têm amplo potencial para otimizar instrumentos de representação, a fim de beneficiar os processos de busca e, consequentemente, de recuperação de informações. A metodologia apresentada se mostrou eficaz por viabilizar uma grande depuração de expressões de busca de usuários e palavras-chave dos autores que produziu um corpus terminológico consistente proveniente diretamente da linguagem utilizada no RI.</p>
</body>
</html>