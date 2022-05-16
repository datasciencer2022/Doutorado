<!DOCTYPE html>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="pt-br"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="pt-br"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="pt-br"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class=" js flexbox flexboxlegacy canvas canvastext webgl no-touch geolocation postmessage no-websqldatabase indexeddb hashchange history draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow textshadow opacity cssanimations csscolumns cssgradients no-cssreflections csstransforms csstransforms3d csstransitions fontface generatedcontent video audio localstorage sessionstorage webworkers applicationcache svg inlinesvg smil svgclippaths" style="" lang="pt-br"><!--<![endif]--><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="text/html; charset=UTF-8" http-equiv="Content-Type">
<meta charset="UTF-8">
<meta content="IE=edge" http-equiv="X-UA-Compatible">
<meta content="width=device-width,initial-scale=1" name="viewport">
<link rel="shortcut icon" href="https://repositorio.unesp.br/themes/Mirage2/images/favicon.ico">
<link rel="apple-touch-icon" href="https://repositorio.unesp.br/themes/Mirage2/images/apple-touch-icon.png">
<meta name="Generator" content="DSpace 5.3">
<link href="About%20Reposit%C3%B3rio%20Institucional%20UNESP_files/main.css" rel="stylesheet">
<link type="application/opensearchdescription+xml" rel="search" href="https://repositorio.unesp.br/description.xml" title="DSpace">
<script async="" src="About%20Reposit%C3%B3rio%20Institucional%20UNESP_files/analytics.js"></script><script type="text/javascript" async="" src="About%20Reposit%C3%B3rio%20Institucional%20UNESP_files/recaptcha__en.js" crossorigin="anonymous" integrity="sha384-u58lmaqnNN/mK+75qtMhq1v5qJ4VVlBXBCJoUO+w4/lUH+f065BssRD9I8gNvIhn"></script><script>
                //Clear default text of emty text areas on focus
                function tFocus(element)
                {
                if (element.value == ' '){element.value='';}
                }
                //Clear default text of emty text areas on submit
                function tSubmit(form)
                {
                var defaultedElements = document.getElementsByTagName("textarea");
                for (var i=0; i != defaultedElements.length; i++){
                if (defaultedElements[i].value == ' '){
                defaultedElements[i].value='';}}
                }
                //Disable pressing 'enter' key to submit a form (otherwise pressing 'enter' causes a submission to start over)
                function disableEnterKey(e)
                {
                var key;

                if(window.event)
                key = window.event.keyCode;     //Internet Explorer
                else
                key = e.which;     //Firefox and Netscape

                if(key == 13)  //if "Enter" pressed, then disable!
                return false;
                else
                return true;
                }
			</script><!--[if lt IE 9]>
                <script src="/themes/Mirage2/vendor/html5shiv/dist/html5shiv.js">Â </script>
                <script src="/themes/Mirage2/vendor/respond/respond.min.js">Â </script>
                <![endif]--><script src="About%20Reposit%C3%B3rio%20Institucional%20UNESP_files/modernizr.js">/*Modernizr enables HTML5 elements & feature detects*/</script>
<title>Projeto Doutorado Isaque Katahira</title>
<script src="About%20Reposit%C3%B3rio%20Institucional%20UNESP_files/api.js">/*Auto imported Script*/</script>
<script>
function voltar() {
    window.history.back()
}
</script>
</head><body>
<header>
<div role="navigation" class="navbar navbar-default navbar-static-top">
<div class="container">
<div class="navbar-header">
<table>
  <tr>
  	<td><a class="navbar-brand" href="https://repositorio.unesp.br/"><img src="About%20Reposit%C3%B3rio%20Institucional%20UNESP_files/logo-repositorio.png" alt="Repositório Institucional UNESP"></a>
	</td>
	
	<td>
		<h3><font color = "blue">RILogUser</font></h3>
	</td>
  </tr>
</table>

<div class="navbar-header pull-right visible-xs hidden-sm hidden-md hidden-lg">
<ul class="nav nav-pills pull-left ">
<li class="dropdown" id="ds-language-selection-xs">
<button title="menu" data-toggle="dropdown" class="dropdown-toggle navbar-toggle navbar-link" role="button" id="language-dropdown-toggle-xs"><span aria-hidden="true" class="visible-xs glyphicon glyphicon-globe"></span></button>
<ul data-no-collapse="true" aria-labelledby="language-dropdown-toggle-xs" role="menu" class="dropdown-menu pull-right">
<li role="presentation" class="disabled">
<a href="https://repositorio.unesp.br/page/about?locale-attribute=pt_BR">português (Brasil)</a>
</li>
<li role="presentation">
<a href="https://repositorio.unesp.br/page/about?locale-attribute=en">English</a>
</li>
<li role="presentation">
<a href="https://repositorio.unesp.br/page/about?locale-attribute=es">espanhol</a>
</li>
</ul>
</li>
<li>
<form method="get" action="/ldap-login" style="display: inline">
<button type="submit" title="usuÃ¡rio" class="navbar-toggle navbar-link"><span aria-hidden="true" class="visible-xs glyphicon glyphicon-user"></span></button>
</form>
</li>
<li>
<form method="get" action="/chat" style="display: inline">
<button type="submit" title="Chat" class="navbar-toggle navbar-link"><span aria-hidden="true" class="visible-xs glyphicon glyphicon-comment"></span></button>
</form>
</li>
<li>
<form method="get" action="/page/about" style="display: inline">
<button type="submit" class="navbar-toggle navbar-link" title="Sobre"><span aria-hidden="true" class="visible-xs glyphicon glyphicon-info-sign"></span></button>
</form>
</li>
</ul>
</div>
</div>

</div>
</div>
</header>
<div class="trail-wrapper hidden-print">
<div class="container">
<div class="row">
<div class="col-xs-12">
<div class="breadcrumb dropdown visible-xs">
<a data-toggle="dropdown" class="dropdown-toggle" role="button" href="#" id="trail-dropdown-toggle">About This Repository&nbsp;<span class="caret"></span></a>
<ul aria-labelledby="trail-dropdown-toggle" role="menu" class="dropdown-menu">

<li role="presentation" class="disabled">
<a href="#" role="menuitem">ANÁLISE DE LOGS DE BUSCA COMO FONTE INFORMACIONAL PARA ATUALIZAÇÃO DE VOCABULÁRIO CONTROLADO EM REPOSITÓRIOS INSTITUCIONAIS</a>
</li>
</ul>
</div>
<ul class="breadcrumb hidden-xs">

<li class="active">ANÁLISE DE LOGS DE BUSCA COMO FONTE INFORMACIONAL PARA ATUALIZAÇÃO DE VOCABULÁRIO CONTROLADO EM REPOSITÓRIOS INSTITUCIONAIS</li>
</ul>
</div>
</div>
</div>
</div>
<div class="hidden" id="no-js-warning-wrapper">
<div id="no-js-warning">
<div class="notice failure">JavaScript is disabled for your browser. Some features of this site may not work without it.</div>
</div>
</div>
<div class="container" id="main-container">
<div class="row row-offcanvas row-offcanvas-right">
<div class="horizontal-slider clearfix">
<div role="navigation" id="sidebar" class="col-xs-6 col-sm-3 sidebar-offcanvas">
<div class="word-break hidden-print" id="ds-options">
<div class="ds-option-set" id="ds-search-option">
<form method="post" class="" id="ds-search-form" action="/discover">
<fieldset>
</fieldset>
</form>
</div>
<div id="aspect_viewArtifacts_Navigation_list_browse" class="list-group">
<a class="list-group-item active" href="./datasciencer.do?action=callProjeto2"><span class="h5 list-group-item-heading  h5">Preparação de dados</span></a>
<a class="list-group-item inactive" href="#"><span class="h5 list-group-item-heading  h5">&nbsp;&nbsp;&nbsp;</span></a>
<a class="list-group-item active" href="./datasciencer.do?action=disponibilizaAll"><span class="h5 list-group-item-heading  h5">Dados Completos Analisados</span></a>
<a class="list-group-item inactive" href="#"><span class="h5 list-group-item-heading  h5">&nbsp;&nbsp;&nbsp;</span></a>
<a class="list-group-item active" href="javascript:voltar()"><span class="h5 list-group-item-heading  h5">Voltar</span></a>
</div>

<div>

<div class="ds-option-set list-group">
&nbsp;
</div>
</div>
</div>
<div align="justify">
<p>Os produtos das comparações entre as listas viabilizam a identificação de conjuntos compostos por elementos comuns, nomeados no diagrama: P1, P2, P3 e P4.
</p>
<img src = "./images/figuraSite.png" width="120%" heigth="120%">
<p>Onde P1 corresponde à compatibilização dos três conjuntos; P2 corresponde aos termos comuns apenas ao Tesauro e ao vocabulário dos usuários; P3 aos termos comuns apenas ao Tesauro e às palavras-chave dos autores e P4 aos termos comuns apenas às palavras-chave dos autores e aos termos de busca dos usuários.</p>
</div>


</div>


<div class="col-xs-12 col-sm-12 col-md-9 main-content">
<div>
<center>
<h4>Análise de Dados</h4>
<hr>

<form name="frmDataSciencer01" action="./datasciencer.do" method="POST"
            	enctype="multipart/form-data">
            	<input type="hidden" name = "action" value="parteUpload">
                	<center>
								
	                                    
                                <table class="text1" style="border-collapse:separate;border-spacing:1em;">
                                                                    
                                    <tr>	                                            
							   			  <td>	                                                   
                                              Coleta de logs de buscas:
                                           </td>
                                           <td>
                                                   <input type="file" name="logs" class="btn btn-outline-primary" required>
                                           </td>
                                        </tr>
                                        
                                        <tr>	                                            
							   			   <td>	                                                   
                                              Coleta de palavras-chave de autores:
                                           </td>
                                           <td>
                                                   <input type="file" name="palavrasChave"  class="btn btn-outline-primary" required>
                                           </td>
                                        </tr>
                                        
                                        <tr>	                                            
							   				<td>	                                                   
                                              Coleta de termos do Tesauro:
                                           </td>
                                           <td>
                                                   <input type="file" name="tesauro"  class="btn btn-outline-primary" required>
                                           </td>
                                        </tr>
                                    
                                        <tr>	                                            
							   				<td>	                                                   
                                              Vocábulos criados:
                                           </td>
                                           <td>
                                           		<table>
                                           		   <tr><td>
                                                   <input type="radio" name="typeResult" value="P1" required checked></td><td> Intersecção de todos (P1)</br>
                                                   </td></tr>
                                                   <tr><td>
                                                   <input type="radio" name="typeResult" value="P2" ></td><td>Termos comuns: logs de busca e termos Tesauro (P2)</br>
                                                   </td></tr>
                                                   <tr><td>
                                                   <input type="radio" name="typeResult" value="P3" ></td><td>Termos comuns: palavras-chave e termos Tesauro (P3)</br>
                                                   </td></tr>
                                                   <tr><td>
                                                   <input type="radio" name="typeResult" value="P4" ></td><td> Termos comuns: logs de busca e palavras-chave (P4)</br>
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
                         <input type="submit"  class="btn btn-primary" value="Preparação e Análise de Dados">
               		 </center>
               	</form>
</div>
<div class="visible-xs visible-sm">

</div>
</div>
</div>
</div>
<div class="hidden-xs hidden-sm">

</div>
</div>
<script>if(!window.DSpace){window.DSpace={};}window.DSpace.context_path='';window.DSpace.theme_path='/themes/Mirage2/';</script><script src="About%20Reposit%C3%B3rio%20Institucional%20UNESP_files/theme.js" charset="UTF-8">/*Auto imported script*/</script><script>
                  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
                  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
                  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

                  ga('create', 'UA-10538459-6', 'repositorio.unesp.br');
                  ga('send', 'pageview');
				</script>

<div><div class="grecaptcha-badge" data-style="bottomright" style="width: 256px; height: 60px; display: block; transition: right 0.3s ease 0s; position: fixed; bottom: 14px; right: -186px; box-shadow: gray 0px 0px 5px; border-radius: 2px; overflow: hidden;"><div class="grecaptcha-logo"><iframe title="reCAPTCHA" src="About%20Reposit%C3%B3rio%20Institucional%20UNESP_files/anchor.html" role="presentation" name="a-arbz0zh4uqys" scrolling="no" sandbox="allow-forms allow-popups allow-same-origin allow-scripts allow-top-navigation allow-modals allow-popups-to-escape-sandbox allow-storage-access-by-user-activation" width="256" height="60" frameborder="0"></iframe></div><div class="grecaptcha-error"></div><textarea id="g-recaptcha-response-100000" name="g-recaptcha-response" class="g-recaptcha-response" style="width: 250px; height: 40px; border: 1px solid rgb(193, 193, 193); margin: 10px 25px; padding: 0px; resize: none; display: none;"></textarea></div><iframe style="display: none;"></iframe></div></body></html>