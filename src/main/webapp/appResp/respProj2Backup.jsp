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
<!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">

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
<button  class="btn btn-primary" onClick="voltar()">Voltar</button>
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
			<input type="submit"  class="btn btn-outline-primary" value="Download: <%=arquivo.substring(45)%>">
		
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