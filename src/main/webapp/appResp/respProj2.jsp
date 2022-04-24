<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>

<!DOCTYPE html>
<%
List<String> generatedFiles = new ArrayList<String>();
if (request.getAttribute("generatedFiles") != null){
	generatedFiles = (List<String>) request.getAttribute("generatedFiles");
}
%>
<html>
<head>
<meta charset="UTF-8">
<script>
function voltar() {
    window.history.back()
}
</script>
<title>Resultados</title>
</head>
<body>
<center>
<h1>Página de resultados - Redução de Dimensionalidade</h1>
<hr>
&nbsp;
</br>
<button onClick="voltar()">Voltar</button>
&nbsp;
</br>
</br>
<%
	if (!generatedFiles.isEmpty()){
		for(String arquivo: generatedFiles){
		%>
		&nbsp;
		</br>
		<form action="./datasciencer.do" method="POST"
			enctype="multipart/form-data">
			<input type="hidden" name = "action" value="download">
			<input type="hidden" name = "fileName" value="<%=arquivo %>">
			<input type="submit" value="Download: <%=arquivo.substring(45)%>">
		
		</form>
		</br>
		&nbsp;
		<%
		}
	}

%>
</center>
</body>
</html>