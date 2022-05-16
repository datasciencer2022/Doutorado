package pacote;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.regex.*;
import java.util.stream.Collectors;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.net.*;
import java.awt.Color;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.io.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import modelo.Arquivo;

import org.apache.commons.collections4.list.TreeList;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class DataSciencer
 */
@WebServlet("/datasciencer.do")
public class DataSciencer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private int maximumLinhas = 300;
	private int maximumLinhasProj2 = 5000;

	private String arqCaracteresInvalid = null;
	private String arqPalavrasInvalid = null;
	private String logs = null;
	private String palavrasChave = null;
	private String tesauro = null;
	private String arquivoReduz = null;

	private File diretorio;

	private Set<String> inclusive1 = new HashSet<String>();
	private Set<String> inclusive2 = new HashSet<String>();
	private Set<String> exclusive = new HashSet<String>();
	private Set<String> p1Result = new HashSet<String>();
	private Set<String> termosExclusivos = new HashSet<String>();
	private Set<String> coincidem = new HashSet<String>();
	private Map<String, String> myList = new HashMap<String, String>();
	private Map<Integer, String> mapaText = new HashMap<Integer, String>();
	private HashMap<Integer, String> mapaTextCompare = new HashMap<Integer, String>();
	private Map<Integer, Integer> mapaNum = new HashMap<Integer, Integer>();

	private static Set<String> cidades = new HashSet<String>();

	private String tipoPlanilha = "";

	private List<String> myListTexto = new ArrayList<String>();
	private List<String> myListTextCompare = new ArrayList<String>();
	private List<String> myListRefinamentoIncluso = new ArrayList<String>();
	private List<String> resultadosRefinamentoResto = new ArrayList<String>();
	private List<Double> myListFreq = new ArrayList<Double>();
	private List<Double> myListNum = new ArrayList<Double>();

	private List<String[]> resultadosTotais = new ArrayList<String[]>();
	private List<String[]> resultadosRepetidos = new ArrayList<String[]>();
	private List<String[]> resultadosRefinamento = new ArrayList<String[]>();
	private List<String> generatedFiles = new ArrayList<String>();

	private String retiradaCaracteres = null;
	private String retiradaTermos = null;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String path = "/home/riloguser/riloguser.uh-app.com.br/temp/";
		diretorio = new File(path);
		diretorio.mkdirs();
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DataSciencer() {
		super();
		// TODO Auto-generated constructor stub
	}

	private void processUploadedFile(FileItem item) throws Exception {
		String fileName = item.getName();
		System.out.println(item.getFieldName() + "\t" + item.getName());
		File uploadedFile = new File("/home/riloguser/riloguser.uh-app.com.br/temp/" + fileName);

		if (uploadedFile.exists()) {
			uploadedFile.delete();
		}

		item.write(uploadedFile);

		if (item.getFieldName().equals("logs")) {
			logs = uploadedFile.getAbsolutePath();
			System.out.println("adicionou: " + uploadedFile.getAbsolutePath());
		} else if (item.getFieldName().equals("palavrasChave")) {
			palavrasChave = uploadedFile.getAbsolutePath();
			System.out.println("adicionou: " + uploadedFile.getAbsolutePath());
		} else if (item.getFieldName().equals("tesauro")) {
			tesauro = uploadedFile.getAbsolutePath();
			System.out.println("adicionou: " + uploadedFile.getAbsolutePath());
		} else if (item.getFieldName().equals("arquivoReduz")) {
			arquivoReduz = uploadedFile.getAbsolutePath();
			System.out.println("adicionou: " + uploadedFile.getAbsolutePath());
		} else if (item.getFieldName().equals("arqCaracteresInvalid")) {
			arqCaracteresInvalid = uploadedFile.getAbsolutePath();
			System.out.println("leu arquivo caracteres invalidos: " + uploadedFile.getAbsolutePath());
		} else if (item.getFieldName().equals("arqPalavrasInvalid")) {
			arqPalavrasInvalid = uploadedFile.getAbsolutePath();
			System.out.println("leu arquivo palavras invalidas: " + uploadedFile.getAbsolutePath());
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = null;

		String action = request.getParameter("action");

		if (action.equals("callProjeto1")) {
			rd = request.getRequestDispatcher("/appResp/projeto1.jsp");
		} else if (action.equals("callProjeto2")) {
			rd = request.getRequestDispatcher("/appResp/projeto2.jsp");
		} else if (action.equals("disponibilizaAll")) {
			rd = request.getRequestDispatcher("/appResp/disponibilizaAll.jsp");
		}
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		File file = new File(diretorio + "/municipios-brasil.csv"); // creates a new file instance
		FileReader fr = new FileReader(file); // reads the file
		BufferedReader br = new BufferedReader(fr); // creates a buffering character input stream
		// StringBuffer sb=new StringBuffer(); //constructs a string buffer with no
		// characters
		String line;
		Arquivo registro = new Arquivo();
		while ((line = br.readLine()) != null) {
			String[] tmp = line.split(";");
			cidades.add(tmp[4]);
		}

		br.close();
		fr.close();

		logs = null;
		palavrasChave = null;
		tesauro = null;
		arquivoReduz = null;

		arqCaracteresInvalid = null;
		arqPalavrasInvalid = null;

		inclusive1 = new HashSet<String>();
		inclusive2 = new HashSet<String>();
		exclusive = new HashSet<String>();
		p1Result = new HashSet<String>();
		coincidem = new HashSet<String>();
		termosExclusivos = new HashSet<String>();
		myList = new HashMap<String, String>();
		tipoPlanilha = "";
		generatedFiles = new ArrayList<String>();

		retiradaCaracteres = new String();
		retiradaTermos = new String();

		RequestDispatcher rd = null;
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");

		String fileName = "";

		// PrintWriter out = response.getWriter();

		List<Arquivo> registro1 = new ArrayList<Arquivo>();
		List<Arquivo> registro2 = new ArrayList<Arquivo>();
		List<Arquivo> registro3 = new ArrayList<Arquivo>();

		Set<String> resposta = new HashSet<String>();

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			return;
		}

		String action = "";
		String typeResult = "";
		int porcentagem = 90;

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(diretorio);

		ServletFileUpload upload = new ServletFileUpload(factory);

		try {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {
				if (!item.isFormField()) {
					System.out.println("item= " + item.getName());
					if (!item.getName().isEmpty()) {
						processUploadedFile(item);
					}
				} else {
					// para inputs que nao sao 'file', isFormField() é verdadeiro
					String nomeDoCampo = item.getFieldName();
					String valorDoCampo = item.getString();

					if (nomeDoCampo.equals("action")) {
						action = valorDoCampo;
					}

					if (nomeDoCampo.equals("typeResult")) {
						typeResult = valorDoCampo;
					}

					if (nomeDoCampo.equals("fileName")) {
						fileName = valorDoCampo;
					}

					if (nomeDoCampo.equals("porcentagem")) {
						porcentagem = Integer.parseInt(valorDoCampo);
					}

					if (nomeDoCampo.equals("retiradaCaracteres")) {
						retiradaCaracteres = valorDoCampo;
					}

					if (nomeDoCampo.equals("retiradaTermos")) {
						retiradaTermos = Normalizer.normalize(valorDoCampo, Normalizer.Form.NFD)
								.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase();
					}
				}
			}
		} catch (Exception e) {
			// out.println(e.getMessage());
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());

		}

		// Systen.out.println("typeResult="+typeResult);
		// Systen.out.println("action="+action);

		if (action.equals("download")) {
			response.reset();
			File downloadFile = new File(fileName);
			FileInputStream inStream = new FileInputStream(downloadFile);

			// if you want to use a relative path to context root:
			String relativePath = getServletContext().getRealPath("");
			// Systen.out.println("relativePath = " + relativePath);

			// obtains ServletContext
			ServletContext context = getServletContext();

			// gets MIME type of the file
			String mimeType = context.getMimeType(fileName);
			if (mimeType == null) {
				// set to binary type if MIME mapping not found
				mimeType = "application/octet-stream";
			}
			// Systen.out.println("MIME type: " + mimeType);

			// modifies response
			response.setContentType(mimeType);
			response.setContentLength((int) downloadFile.length());

			// forces download
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
			response.setHeader(headerKey, headerValue);

			// obtains response's output stream
			OutputStream outStream = response.getOutputStream();

			byte[] buffer = new byte[4096];
			int bytesRead = -1;

			while ((bytesRead = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			inStream.close();
			outStream.close();

		}

		if (action.equals("similaridade")) {
			if (arqCaracteresInvalid != null) {
				retiradaCaracteres = lerRetirada(arqCaracteresInvalid);
				// System.out.println ("entrou file retiradaCaracteres: ");
			}

			if (arqPalavrasInvalid != null) {
				retiradaTermos = lerRetirada(arqPalavrasInvalid);
				// System.out.println ("entrou file retiradaTermos: ");
			}

			// System.out.println ("retiradaCaracteres: "+ retiradaCaracteres);
			// System.out.println ("retiradaTermos: "+ retiradaTermos);
			myListTexto = new ArrayList<String>();
			myListTextCompare = new ArrayList<String>();
			myListRefinamentoIncluso = new ArrayList<String>();
			resultadosRefinamentoResto = new ArrayList<String>();
			myListFreq = new ArrayList<Double>();
			myListNum = new ArrayList<Double>();

			resultadosTotais = new ArrayList<String[]>();
			resultadosRepetidos = new ArrayList<String[]>();
			resultadosRefinamento = new ArrayList<String[]>();
			generatedFiles = new ArrayList<String>();

			lerArqRefinamento(arquivoReduz);

			int numDuplicados = 0;
			int qtdMaior = 0;
			int qtdMenor = 999999;
			double extensaoMedia = 0.0;

			double somaPalavrasPorLinha = 0;
			int numPalavrasLinha = 0;
			gerarRefinacoes(porcentagem);

			// Geração de estatísticas

			List<String> frasesOrdenadas = myListTexto.stream().sorted().collect(Collectors.toList());
			String last = "";
			for (String frase : frasesOrdenadas) {
				if (last.equals(frase)) {
					numDuplicados++;
				}
				if (frase.contains(" ")) {
					String[] palavras = frase.split(" ");
					somaPalavrasPorLinha += palavras.length;
					numPalavrasLinha = palavras.length;
				} else {
					somaPalavrasPorLinha++;
					numPalavrasLinha = 1;
				}

				if (numPalavrasLinha > qtdMaior)
					qtdMaior = numPalavrasLinha;
				if (numPalavrasLinha < qtdMenor)
					qtdMenor = numPalavrasLinha;

				last = frase;
			}

			extensaoMedia = somaPalavrasPorLinha / Double.parseDouble(frasesOrdenadas.size() + "");

			if (!generatedFiles.isEmpty()) {
				request.setAttribute("generatedFiles", generatedFiles);
				request.setAttribute("numDuplicados", numDuplicados);
				request.setAttribute("extensaoMedia", extensaoMedia);
				request.setAttribute("qtdMenor", qtdMenor);
				request.setAttribute("qtdMaior", qtdMaior);
			}
			if (!response.isCommitted()) {
				rd = request.getRequestDispatcher("/appResp/respProj2.jsp");
				rd.forward(request, response);
			}
		}

		else if (action.equals("parteUpload")) {
			String titulo1 = "", titulo2 = "", titulo3 = "";
			if (typeResult.equals("P1") || typeResult.equals("P4")) {
				registro1 = lerArq(logs);
				titulo1 = logs.substring(45);
				registro2 = lerArq(palavrasChave);
				titulo2 = palavrasChave.substring(45);
				registro3 = lerArq(tesauro);
				titulo3 = tesauro.substring(45);

				inclusive1 = lerArqFraseByFrase(logs);
				inclusive2 = lerArqFraseByFrase(palavrasChave);
				exclusive = lerArqFraseByFrase(tesauro);
			} else if (typeResult.equals("P2")) {

				registro1 = lerArq(logs);
				titulo1 = logs.substring(45);
				registro2 = lerArq(tesauro);
				titulo2 = tesauro.substring(45);
				registro3 = lerArq(palavrasChave);
				titulo3 = palavrasChave.substring(45);

				inclusive1 = lerArqFraseByFrase(logs);
				inclusive2 = lerArqFraseByFrase(tesauro);
				exclusive = lerArqFraseByFrase(palavrasChave);
			} else if (typeResult.equals("P3")) {
				registro1 = lerArq(tesauro);
				titulo1 = tesauro.substring(45);
				registro2 = lerArq(palavrasChave);
				titulo2 = palavrasChave.substring(45);
				registro3 = lerArq(logs);
				titulo3 = logs.substring(45);

				inclusive1 = lerArqFraseByFrase(tesauro);
				inclusive2 = lerArqFraseByFrase(palavrasChave);
				exclusive = lerArqFraseByFrase(logs);
			} else if (typeResult.equals("exclusiveLogs")) {
				// aqui nessa seção o incluive1 é o termo Logs e os demais são os outros
				// System.out.println("entrou no exclusiveLogs");
				titulo1 = logs.substring(45);
				inclusive1 = lerArqFraseByFrase(logs);
				inclusive2 = lerArqFraseByFrase(tesauro);
				exclusive = lerArqFraseByFrase(palavrasChave);
				registro3 = lerArq(tesauro);
			} else if (typeResult.equals("exclusivePalavrasChave")) {
				// aqui nessa seção o incluive1 é o termo Palavras Chave e os demais são os
				// outros
				titulo1 = palavrasChave.substring(45);
				inclusive1 = lerArqFraseByFrase(palavrasChave);
				inclusive2 = lerArqFraseByFrase(tesauro);
				exclusive = lerArqFraseByFrase(logs);
				registro3 = lerArq(tesauro);
			} else if (typeResult.equals("exclusiveTesauro")) {
				// aqui nessa seção o incluive1 é o termo Tesauro e os demais são os outros
				titulo1 = tesauro.substring(45);
				inclusive1 = lerArqFraseByFrase(tesauro);
				inclusive2 = lerArqFraseByFrase(palavrasChave);
				exclusive = lerArqFraseByFrase(logs);
			}

			if (typeResult.startsWith("exclusive")) {
				// System.out.println("entrou na seleção dos termos exclusivos");
				for (String cada : inclusive1) {
					if (!(inclusive2
							.contains(Normalizer.normalize(cada, Normalizer.Form.NFD)
									.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase())
							|| exclusive.contains(Normalizer.normalize(cada, Normalizer.Form.NFD)
									.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase()))) {

						// exclusive:"+myList.get(cada));

						termosExclusivos.add(myList.get(cada));
					}
				}
				// System.out.println("número de itens: "+termosExclusivos.size());
			} else if (!typeResult.equals("P1")) {
				// construção dos coincidem
				for (String cada : inclusive1) {

					if (inclusive2
							.contains(Normalizer.normalize(cada, Normalizer.Form.NFD)
									.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase())
							&& !exclusive.contains(Normalizer.normalize(cada, Normalizer.Form.NFD)
									.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase())) {

						// exclusive:"+myList.get(cada));

						coincidem.add(myList.get(cada));
					}
				}
			} else {
				// construção dos todos juntos
				for (String cada : inclusive1) {

					if (inclusive2
							.contains(Normalizer.normalize(cada, Normalizer.Form.NFD)
									.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase())

							&& exclusive.contains(Normalizer.normalize(cada, Normalizer.Form.NFD)
									.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase())) {
						// System.out.println("tem no inclusive 1 e inclusive2 e também no
						// exclusive:"+myList.get(cada));

						p1Result.add(myList.get(cada));
					}
				}
			}

			File Fcoincidem = null;

			Integer rand = (int) (10000000 + Math.random() * 90000000);

			if (typeResult.startsWith("P")) {
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheetResult = workbook.createSheet("Resultados");

				int rownum = 0;
				Row row = sheetResult.createRow(rownum++);
				int cellnum = 0;

				CellStyle headerStyle = workbook.createCellStyle();
				headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				headerStyle.setAlignment(HorizontalAlignment.CENTER);
				headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

				CellStyle lineStyle1 = workbook.createCellStyle();
				lineStyle1.setFillForegroundColor(IndexedColors.GOLD.getIndex());
				lineStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				CellStyle lineStyle2 = workbook.createCellStyle();
				lineStyle1.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
				lineStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				if (typeResult.equals("P1")) {
					Cell cellTitulo1 = row.createCell(cellnum++);
					cellTitulo1.setCellStyle(headerStyle);
					cellTitulo1.setCellValue(titulo1);
					sheetResult.autoSizeColumn(0);
					Cell cellTitulo2 = row.createCell(cellnum++);
					cellTitulo2.setCellStyle(headerStyle);
					cellTitulo2.setCellValue(titulo2);
					sheetResult.autoSizeColumn(1);
					Cell cellTitulo3 = row.createCell(cellnum++);
					cellTitulo3.setCellStyle(headerStyle);
					cellTitulo3.setCellValue(titulo3);
					sheetResult.autoSizeColumn(2);

					resposta = joinAll(registro1, registro2, registro3);

					int numLinha = 0;

					CellStyle estilo = null;
					for (String linha : resposta) {
						// Systen.out.println("linha #:"+rownum+"\t"+linha);
						row = sheetResult.createRow(rownum++);
						if (numLinha % 2 == 0) {
							estilo = lineStyle1;
						} else {
							estilo = lineStyle2;
						}
						cellnum = 0;
						String[] temp = linha.split("\t");
						Cell cell1 = row.createCell(cellnum++);
						cell1.setCellValue(temp[0]);
						cell1.setCellStyle(estilo);
						Cell cell2 = row.createCell(cellnum++);
						cell2.setCellValue(temp[1]);
						cell2.setCellStyle(estilo);
						if (temp.length == 3) {
							Cell cell3 = row.createCell(cellnum++);
							cell3.setCellValue(temp[2]);
							cell3.setCellStyle(estilo);
						}
						numLinha++;
					}

				} else {
					Cell cellTitulo1 = row.createCell(cellnum++);
					cellTitulo1.setCellStyle(headerStyle);
					cellTitulo1.setCellValue(titulo1);
					sheetResult.autoSizeColumn(0);
					Cell cellTitulo2 = row.createCell(cellnum++);
					cellTitulo2.setCellStyle(headerStyle);
					cellTitulo2.setCellValue(titulo2);
					sheetResult.autoSizeColumn(1);
					resposta = montarDados(registro1, registro2, exclusive);
					int numLinha = 0;

					CellStyle estilo = null;
					for (String linha : resposta) {
						// Systen.out.println("linha #:"+rownum+"\t"+linha);
						row = sheetResult.createRow(rownum++);
						if (numLinha % 2 == 0) {
							estilo = lineStyle1;
						} else {
							estilo = lineStyle2;
						}
						cellnum = 0;
						String[] temp = linha.split("\t");
						Cell cell1 = row.createCell(cellnum++);
						cell1.setCellValue(temp[0]);
						cell1.setCellStyle(estilo);
						Cell cell2 = row.createCell(cellnum++);
						cell2.setCellValue(temp[1]);
						cell2.setCellStyle(estilo);
						if (temp.length == 3) {
							Cell cell3 = row.createCell(cellnum++);
							cell3.setCellValue(temp[2]);
							cell3.setCellStyle(estilo);
						}
						numLinha++;
					}

				}

				try {
					rand = (int) (10000000 + Math.random() * 90000000);
					fileName = diretorio + "/" + typeResult + "_" + rand + ".xlsx";
					FileOutputStream out = new FileOutputStream(new File(fileName));
					workbook.write(out);
					out.close();
					generatedFiles.add(fileName);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.out.println("Arquivo não encontrado!");
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Erro na edição do arquivo!");
				}
				// System.out.println("arquivo: "+typeResult+ " gravado");

			}

			List<String> coincidemOrdenados = new ArrayList<String>();

			if (!coincidem.isEmpty()) {
				// System.out.println("entrou no coincidem que tem nº elementos:
				// "+coincidem.size());

				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheetResult = workbook.createSheet("Coincidem as frases");
				CellStyle headerStyle = workbook.createCellStyle();
				headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				headerStyle.setAlignment(HorizontalAlignment.CENTER);
				headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

				CellStyle lineStyle1 = workbook.createCellStyle();
				lineStyle1.setFillForegroundColor(IndexedColors.GOLD.getIndex());
				lineStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				CellStyle lineStyle2 = workbook.createCellStyle();
				lineStyle1.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
				lineStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				int rownum = 0;
				Row row = sheetResult.createRow(rownum++);

				Cell cellTitulo1 = row.createCell(0);
				cellTitulo1.setCellStyle(headerStyle);
				cellTitulo1.setCellValue("Coincidem as frases");
				sheetResult.autoSizeColumn(0);

				if (typeResult.equals("P4")) {
					Cell cellTitulo2 = row.createCell(1);
					cellTitulo2.setCellStyle(headerStyle);
					cellTitulo2.setCellValue("Termos Relacionados ao Tesauro");
					sheetResult.autoSizeColumn(1);
				}
				int numLinha = 0;
				CellStyle estilo = null;

				// ordena os coincidem

				coincidemOrdenados = coincidem.stream().sorted().collect(Collectors.toList());

				for (String linha : coincidemOrdenados) {
					if (typeResult.equals("P4")) {
						StringBuilder str = new StringBuilder();
						Set<String> frasesCorrespondentes = new HashSet<String>();
						if (linha.contains(" ")) {
							String[] palavras = linha.split(" ");
							for (String p : palavras) {

								for (Arquivo tesauro : registro3) {

									boolean pular = false;
									if (tesauro.getPalavrasLinha().size() < 2) {
										String palavraCompare = null;
										palavraCompare = gerarStringP4(tesauro.getPalavrasLinha().toString());
										pular = getStopWords(palavraCompare);
										if (pular)
											continue;
										if (palavraCompare.equals(gerarStringP4(p))) {
											frasesCorrespondentes.add(tesauro.getLinhaToda());
											// System.out.print ("gerarString(p): "+ gerarString(p));
											// System.out.print(" é igual ");
											// System.out.println(" a palavraCompare: "+ palavraCompare);
										}
									} else {
										for (String palavraCompare2 : tesauro.getPalavrasLinha()) {
											String palavraCompare = null;
											palavraCompare = gerarStringP4(palavraCompare2);
											/*
											 * if (palavraCompare.startsWith("CALCI")){
											 * System.out.println("palavraCompare = "+ palavraCompare);
											 * System.out.println(" e p = "+ p); }
											 */
											pular = getStopWords(palavraCompare);
											if (pular)
												continue;
											if (palavraCompare.equals(gerarStringP4(p))) {
												// System.out.print ("gerarString(p): "+ gerarString(p));
												frasesCorrespondentes.add(tesauro.getLinhaToda());
												// System.out.print(" é igual ");
												// System.out.println(" a palavraCompare: "+ palavraCompare);
											}
										}
									}
								}
							}
							for (String frase : frasesCorrespondentes) {
								str.append(frase + " / ");
							}
						} else {

							for (Arquivo tesauro : registro3) {
								boolean pular = false;
								if (tesauro.getPalavrasLinha().size() < 2) {
									String palavraCompare = null;
									palavraCompare = gerarStringP4(tesauro.getPalavrasLinha().toString());
									pular = getStopWords(palavraCompare);
									if (pular)
										continue;
									if (palavraCompare.equals(gerarStringP4(linha))) {
										frasesCorrespondentes.add(tesauro.getLinhaToda());
										// System.out.println("achou correspondente de palavra única: "+ linha+ "\t as
										// frases: "+tesauro.getLinhaToda());
									}
								} else {
									for (String palavraCompare2 : tesauro.getPalavrasLinha()) {
										String palavraCompare = null;
										palavraCompare = gerarStringP4(palavraCompare2);
										pular = getStopWords(palavraCompare);
										if (pular)
											continue;
										if (palavraCompare.equals(gerarStringP4(linha))) {
											frasesCorrespondentes.add(tesauro.getLinhaToda());
											// System.out.println("achou correspondente de palavra única: "+ linha+ "\t
											// as frases: "+tesauro.getLinhaToda());
										}
									}
								}
							}

							for (String frase : frasesCorrespondentes) {
								str.append(frase + " / ");
							}
						}
						row = sheetResult.createRow(rownum++);
						int cellnum = 0;
						Cell cellLinha = row.createCell(cellnum);
						if (numLinha % 2 == 0) {
							estilo = lineStyle1;
						} else {
							estilo = lineStyle2;
						}
						cellLinha.setCellValue(linha);
						cellLinha.setCellStyle(estilo);
						if (str.length() > 3) {
							cellnum = 1;
							Cell cellLinha2 = row.createCell(cellnum);
							if (numLinha % 2 == 0) {
								estilo = lineStyle1;
							} else {
								estilo = lineStyle2;
							}
							cellLinha2.setCellValue(str.toString().substring(0, str.lastIndexOf("/")));
							cellLinha2.setCellStyle(estilo);
						}

						numLinha++;

					} else {
						row = sheetResult.createRow(rownum++);
						int cellnum = 0;
						Cell cellLinha = row.createCell(cellnum);
						if (numLinha % 2 == 0) {
							estilo = lineStyle1;
						} else {
							estilo = lineStyle2;
						}
						cellLinha.setCellValue(linha);
						cellLinha.setCellStyle(estilo);
						numLinha++;
					}

				}

				try {
					rand = (int) (10000000 + Math.random() * 90000000);
					fileName = diretorio + "/" + typeResult + "_coincidem_" + rand + ".xlsx";
					FileOutputStream out = new FileOutputStream(new File(fileName));
					workbook.write(out);
					out.close();
					generatedFiles.add(fileName);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.out.println("Arquivo não encontrado!");
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Erro na edição do arquivo!");
				}
				// Systen.out.println("arquivo: "+typeResult+ " gravado");

			}

			List<String> p1ResultOrdenados = new ArrayList<String>();
			if (!p1Result.isEmpty() && typeResult.equals("P1")) {
				// System.out.println("entrou no p1Result que tem nº elementos:
				// "+p1Result.size());
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheetResult = workbook.createSheet("Frases comuns a todos os conjuntos");
				CellStyle headerStyle = workbook.createCellStyle();
				headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				headerStyle.setAlignment(HorizontalAlignment.CENTER);
				headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

				CellStyle lineStyle1 = workbook.createCellStyle();
				lineStyle1.setFillForegroundColor(IndexedColors.GOLD.getIndex());
				lineStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				CellStyle lineStyle2 = workbook.createCellStyle();
				lineStyle1.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
				lineStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				int rownum = 0;
				Row row = sheetResult.createRow(rownum++);

				Cell cellTitulo1 = row.createCell(0);
				cellTitulo1.setCellStyle(headerStyle);
				cellTitulo1.setCellValue("Coincidem as frases");
				sheetResult.autoSizeColumn(0);

				int numLinha = 0;
				CellStyle estilo = null;
				// System.out.println("p1Result tem "+p1Result.size());

				// ordena os p1Result
				p1ResultOrdenados = p1Result.stream().sorted().collect(Collectors.toList());

				for (String linha : p1ResultOrdenados) {
					row = sheetResult.createRow(rownum++);
					int cellnum = 0;
					Cell cellLinha = row.createCell(cellnum);
					if (numLinha % 2 == 0) {
						estilo = lineStyle1;
					} else {
						estilo = lineStyle2;
					}
					cellLinha.setCellValue(linha);
					cellLinha.setCellStyle(estilo);
					numLinha++;
				}

				try {
					rand = (int) (10000000 + Math.random() * 90000000);
					fileName = diretorio + "/" + typeResult + "_comuns_a_todos_" + rand + ".xlsx";
					FileOutputStream out = new FileOutputStream(new File(fileName));
					workbook.write(out);
					out.close();
					generatedFiles.add(fileName);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.out.println("Arquivo não encontrado!");
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Erro na edição do arquivo!");
				}
				// Systen.out.println("arquivo: "+typeResult+ " gravado");

			}

			List<String> termosExclusivosOrdenados = new ArrayList<String>();

			if (!termosExclusivos.isEmpty() && !typeResult.startsWith("P")) {
				// System.out.println("entrou no termosExclusivos que tem nº elementos:
				// "+termosExclusivos.size());
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheetResult = workbook.createSheet("Termos exclusivos");
				CellStyle headerStyle = workbook.createCellStyle();
				headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				headerStyle.setAlignment(HorizontalAlignment.CENTER);
				headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

				CellStyle lineStyle1 = workbook.createCellStyle();
				lineStyle1.setFillForegroundColor(IndexedColors.GOLD.getIndex());
				lineStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				CellStyle lineStyle2 = workbook.createCellStyle();
				lineStyle1.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
				lineStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				int rownum = 0;
				Row row = sheetResult.createRow(rownum++);

				Cell cellTitulo1 = row.createCell(0);
				cellTitulo1.setCellStyle(headerStyle);
				if (typeResult.contains("esauro")) {
					cellTitulo1.setCellValue("Termos exclusivos");
				} else {
					cellTitulo1.setCellValue("Palavras exclusivas");
					Cell cellTitulo2 = row.createCell(1);
					cellTitulo2.setCellStyle(headerStyle);
					cellTitulo2.setCellValue("Parte das Palavras Relacionadas ao Tesauro Unesp");
				}

				sheetResult.autoSizeColumn(0);

				int numLinha = 0;
				CellStyle estilo = null;

				// ordena os termosExclusivos
				termosExclusivosOrdenados = termosExclusivos.stream().sorted().collect(Collectors.toList());

				/***************** adição da nova feature ************/

				for (String linha : termosExclusivosOrdenados) {
					if (!typeResult.contains("esauro")) {
						StringBuilder str = new StringBuilder();
						Set<String> frasesCorrespondentes = new HashSet<String>();
						if (linha.contains(" ")) {
							String[] palavras = linha.split(" ");
							for (String p : palavras) {

								for (Arquivo tesauro : registro3) {

									boolean pular = false;
									if (tesauro.getPalavrasLinha().size() < 2) {
										String palavraCompare = null;
										palavraCompare = gerarStringP4(tesauro.getPalavrasLinha().toString());
										pular = getStopWords(palavraCompare);
										if (pular)
											continue;
										if (palavraCompare.equals(gerarStringP4(p))) {
											frasesCorrespondentes.add(tesauro.getLinhaToda());
											// System.out.print ("gerarString(p): "+ gerarString(p));
											// System.out.print(" é igual ");
											// System.out.println(" a palavraCompare: "+ palavraCompare);
										}
									} else {
										for (String palavraCompare2 : tesauro.getPalavrasLinha()) {
											String palavraCompare = null;
											palavraCompare = gerarStringP4(palavraCompare2);
											/*
											 * if (palavraCompare.startsWith("CALCI")){
											 * System.out.println("palavraCompare = "+ palavraCompare);
											 * System.out.println(" e p = "+ p); }
											 */
											pular = getStopWords(palavraCompare);
											if (pular)
												continue;
											if (palavraCompare.equals(gerarStringP4(p))) {
												// System.out.print ("gerarString(p): "+ gerarString(p));
												frasesCorrespondentes.add(tesauro.getLinhaToda());
												// System.out.print(" é igual ");
												// System.out.println(" a palavraCompare: "+ palavraCompare);
											}
										}
									}
								}
							}
							for (String frase : frasesCorrespondentes) {
								str.append(frase + " / ");
							}
						} else {
							for (Arquivo tesauro : registro3) {

								boolean pular = false;
								if (tesauro.getPalavrasLinha().size() < 2) {
									String palavraCompare = null;
									palavraCompare = gerarStringP4(tesauro.getPalavrasLinha().toString());
									pular = getStopWords(palavraCompare);
									if (pular)
										continue;
									if (palavraCompare.equals(gerarStringP4(linha))) {
										frasesCorrespondentes.add(tesauro.getLinhaToda());
										// System.out.print ("gerarString(p): "+ gerarString(p));
										// System.out.print(" é igual ");
										// System.out.println(" a palavraCompare: "+ palavraCompare);
									}
								} else {
									for (String palavraCompare2 : tesauro.getPalavrasLinha()) {
										String palavraCompare = null;
										palavraCompare = gerarStringP4(palavraCompare2);
										/*
										 * if (palavraCompare.startsWith("CALCI")){
										 * System.out.println("palavraCompare = "+ palavraCompare);
										 * System.out.println(" e p = "+ p); }
										 */
										pular = getStopWords(palavraCompare);
										if (pular)
											continue;
										if (palavraCompare.equals(gerarStringP4(linha))) {
											// System.out.print ("gerarString(p): "+ gerarString(p));
											frasesCorrespondentes.add(tesauro.getLinhaToda());
											// System.out.print(" é igual ");
											// System.out.println(" a palavraCompare: "+ palavraCompare);
										}
									}
								}

							}
							for (String frase : frasesCorrespondentes) {
								str.append(frase + " / ");
							}
						}
						/*
						 * if (str.length() >=32767) { System.out.println(str); break; }
						 */
						row = sheetResult.createRow(rownum++);
						int cellnum = 0;
						Cell cellLinha = row.createCell(cellnum);
						if (numLinha % 2 == 0) {
							estilo = lineStyle1;
						} else {
							estilo = lineStyle2;
						}
						cellLinha.setCellValue(linha);
						if (rownum % 30 == 0)
							System.out.println("coluna 0 gravada:" + linha);
						cellLinha.setCellStyle(estilo);
						if (str.length() > 3) {
							cellnum = 1;
							Cell cellLinha2 = row.createCell(cellnum);
							if (numLinha % 2 == 0) {
								estilo = lineStyle1;
							} else {
								estilo = lineStyle2;
							}
							if (str.toString().length() >= 32762) {
								String strTemp = str.toString().substring(0, 32762);
								cellLinha2.setCellValue(strTemp.substring(0, str.lastIndexOf("/")));
							} else {
								cellLinha2.setCellValue(str.toString().substring(0, str.lastIndexOf("/")));
							}

							if (rownum % 30 == 0)
								System.out.println(
										"coluna 1 gravada:" + str.toString().substring(0, str.lastIndexOf("/")));
							cellLinha2.setCellStyle(estilo);
						} else {
							cellnum = 1;
							Cell cellLinha2 = row.createCell(cellnum);
							if (numLinha % 2 == 0) {
								estilo = lineStyle1;
							} else {
								estilo = lineStyle2;
							}
							cellLinha2.setCellValue("   ");
							cellLinha2.setCellStyle(estilo);
						}

						numLinha++;

					} else {
						row = sheetResult.createRow(rownum++);
						int cellnum = 0;
						Cell cellLinha = row.createCell(cellnum);
						if (numLinha % 2 == 0) {
							estilo = lineStyle1;
						} else {
							estilo = lineStyle2;
						}
						cellLinha.setCellValue(linha);
						cellLinha.setCellStyle(estilo);
						numLinha++;
					}

				}
				try {
					rand = (int) (10000000 + Math.random() * 90000000);
					fileName = diretorio + "/" + typeResult + "_finalmente" + rand + ".xlsx";
					FileOutputStream out = new FileOutputStream(new File(fileName));
					workbook.write(out);
					out.close();
					generatedFiles.add(fileName);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.out.println("Arquivo não encontrado!");
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Erro na edição do arquivo!");
				}
				System.out.println("arquivo: " + typeResult + " gravado");

			}

			if (!generatedFiles.isEmpty()) {
				// System.out.println("entrou no generatedFiles que tem nº elementos:
				// "+generatedFiles.size());
				request.setAttribute("generatedFiles", generatedFiles);
			}

			if (!coincidem.isEmpty()) {
				request.setAttribute("coincidentes", coincidemOrdenados);
			}
			if (!termosExclusivos.isEmpty()) {
				request.setAttribute("termosExclusivos", termosExclusivosOrdenados);
			}

			if (!p1Result.isEmpty()) {
				request.setAttribute("coincidentes", p1ResultOrdenados);
			}
			if (!response.isCommitted()) {
				rd = request.getRequestDispatcher("/appResp/respProj1.jsp");
				rd.forward(request, response);
			}
		}

	}

	private String lerRetirada(String arquivo) {
		String resposta = "";
		try {
			FileInputStream fis = new FileInputStream(new File(arquivo));

			int indice = 0;
			boolean primeiraLinha = true;
			// Systen.out.println(arquivo);

			XSSFWorkbook workbookX = null;
			HSSFWorkbook workbookH = null;

			XSSFSheet sheetX = null;
			HSSFSheet sheetH = null;
			try {
				workbookX = new XSSFWorkbook(fis);

				sheetX = workbookX.getSheetAt(0);

				tipoPlanilha = "X";
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			try {
				workbookH = new HSSFWorkbook(fis);

				sheetH = workbookH.getSheetAt(0);

				tipoPlanilha = "H";
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			Iterator<Row> rowIterator = null;

			if (sheetX == null) {
				rowIterator = sheetH.iterator();
			} else if (sheetH == null) {
				rowIterator = sheetX.iterator();
			}

			String finalTemp1 = "";
			Arquivo registro = null;

			boolean first = true;

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				registro = new Arquivo();
				Set<String> palavras = new HashSet<String>();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getCellType()) {
					case STRING:
						resposta = resposta + cell.getStringCellValue() + " ";
						break;
					}

				}

			}
			fis.close();
		} catch (IOException e) {
			System.out.println("Ocorreu exceção: " + e.getMessage());
		}
		return Normalizer.normalize(resposta, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
				.toUpperCase();

	}

	private boolean getStopWords(String palavraCompare) {
		boolean resp = false;
		switch (palavraCompare.toLowerCase()) {
		case "um":
		case "uns":
		case "ao":
		case "aos":
		case "a":
		case "as":
		case "o":
		case "os":
		case "de":
		case "of":
		case "the":
		case "por":
		case "by":
		case "do":
		case "dos":
		case "da":
		case "das":
		case "no":
		case "nos":
		case "na":
		case "nas":
		case "em":
		case "in":
		case "to":
		case "para":
		case ",":
		case "-":
		case "(":
		case ")":
		case "e":
		case "and":
		case "como":
		case "entre":
		case "com":
		case "for":

			resp = true;

		}
		return resp;
	}

	private List<Arquivo> lerArq(String arquivo) {
		List<Arquivo> registros = new ArrayList<Arquivo>();
		int cont = 0;
		try {
			FileInputStream fis = new FileInputStream(new File(arquivo));

			int indice = 0;
			boolean primeiraLinha = true;
			// Systen.out.println(arquivo);

			XSSFWorkbook workbookX = null;
			HSSFWorkbook workbookH = null;

			XSSFSheet sheetX = null;
			HSSFSheet sheetH = null;
			try {
				workbookX = new XSSFWorkbook(fis);

				sheetX = workbookX.getSheetAt(0);

				tipoPlanilha = "X";
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			try {
				workbookH = new HSSFWorkbook(fis);

				sheetH = workbookH.getSheetAt(0);

				tipoPlanilha = "H";
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			Iterator<Row> rowIterator = null;

			if (sheetX == null) {
				rowIterator = sheetH.iterator();
			} else if (sheetH == null) {
				rowIterator = sheetX.iterator();
			}

			String finalTemp1 = "";
			Arquivo registro = null;

			boolean first = true;

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				registro = new Arquivo();
				Set<String> palavras = new HashSet<String>();
				if (!first) {
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						switch (cell.getCellType()) {
						case STRING:
							if (cell.getStringCellValue().contains(",")) {
								finalTemp1 = cell.getStringCellValue().replace(',', ' ');
							} else {
								finalTemp1 = cell.getStringCellValue();
							}

							break;
						}

					}
					if (finalTemp1.contains(" ")) {
						String[] cadaP = finalTemp1.split(" ");
						for (String p : cadaP) {

							if (cidades.contains(p))
								continue;

							if (p.length() > 1) {
								palavras.add(Normalizer.normalize(p, Normalizer.Form.NFD)
										.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase());
								// System.out.println("adicionada palavra:"+ p.toUpperCase());
							}
						}
					} else {
						if (finalTemp1.length() > 1) {

							if (cidades.contains(finalTemp1))
								continue;

							palavras.add(Normalizer.normalize(finalTemp1, Normalizer.Form.NFD)
									.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase());
						}
					}
					registro.setNome(arquivo);
					registro.setPalavrasLinha(palavras);
					registro.setLinhaToda(finalTemp1);
				} else {
					first = false;
				}
				
				registros.add(registro);
				cont++;
				if (cont > maximumLinhas) break;
			}
			fis.close();
		} catch (IOException e) {
			System.out.println("Ocorreu exceção: " + e.getMessage());
		}
		return registros;
	}

	
	private Set<String> lerArqFraseByFrase(String arquivo) {
		int cont = 0;
		Set<String> resposta = new HashSet<String>();
		try {

			FileInputStream fis = new FileInputStream(new File(arquivo));

			int indice = 0;
			boolean primeiraLinha = true;
			// Systen.out.println(arquivo);

			XSSFWorkbook workbookX = null;
			HSSFWorkbook workbookH = null;

			XSSFSheet sheetX = null;
			HSSFSheet sheetH = null;
			try {
				workbookX = new XSSFWorkbook(fis);

				sheetX = workbookX.getSheetAt(0);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			try {
				workbookH = new HSSFWorkbook(fis);

				sheetH = workbookH.getSheetAt(0);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			Iterator<Row> rowIterator = null;

			if (sheetX == null) {
				rowIterator = sheetH.iterator();
			} else if (sheetH == null) {
				rowIterator = sheetX.iterator();
			}

			String frase = "";

			boolean first = true;

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();

				if (!first) {
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						switch (cell.getCellType()) {
						case STRING:
							frase = cell.getStringCellValue();
							break;
						}

					}

					try {
						if (!(frase.isEmpty() || frase == null)) {
							if (Character.isDigit(frase.charAt(0))) {
								// System.out.println("corrigir: "+ frase1);
								boolean corrigir = true;
								while (corrigir) {
									// System.out.println(frase);
									frase = frase.substring(1);
									if (frase.charAt(0) == '-') {
										frase = frase.substring(1);
									}
									corrigir = Character.isDigit(frase.charAt(0));
									// System.out.println("corrigindo: "+ frase1);
								}
								frase = frase.trim();
								// System.out.println("frase analisada: "+frase);
							}
						} else {
							continue;
						}

					} catch (Exception ex) {
						System.out.println("o erro é na leitura do xlsx " + ex.getMessage());
					}

					if (myList.isEmpty()) {

						myList.put(Normalizer.normalize(frase, Normalizer.Form.NFD)
								.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase(), frase);
					} else {
						if (!myList.containsKey(Normalizer.normalize(frase, Normalizer.Form.NFD)
								.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase())) {

							myList.put(Normalizer.normalize(frase, Normalizer.Form.NFD)
									.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase(), frase);
						}
					}

					resposta.add(Normalizer.normalize(frase, Normalizer.Form.NFD)
							.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase());
				} else {
					first = false;
				}
				cont++;
				if (cont > maximumLinhas) break;
			}
			fis.close();
		} catch (IOException e) {
			System.out.println("Ocorreu exceção: " + e.getMessage());
		}
		return resposta;
	}

	

	private static Set<String> joinAll(List<Arquivo> registro1, List<Arquivo> registro2, List<Arquivo> registro3) {
		Set<String> resposta = new HashSet<String>();
		String frase1;
		String frase2;
		String frase3;
		int cont = 1;

		boolean encerrar = false;
		try {
			for (Arquivo arq1 : registro1) {
				frase1 = null;
				frase1 = Normalizer.normalize(arq1.getLinhaToda(), Normalizer.Form.NFD)
						.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase();

				if (!(frase1.isEmpty())) {
					if (Character.isDigit(frase1.charAt(0))) {
						// System.out.println("corrigir: "+ frase1);
						boolean corrigir = true;
						while (corrigir) {
							frase1 = frase1.substring(1);
							if (frase1.charAt(0) == '-') {
								frase1 = frase1.substring(1);
							}
							corrigir = Character.isDigit(frase1.charAt(0));
							// System.out.println("corrigindo: "+ frase1);
						}
						frase1 = frase1.trim();

					}
					cont++;

					for (Arquivo arq2 : registro2) {
						if (encerrar) {
							encerrar = false;
							break;
						}

						frase2 = null;
						frase2 = Normalizer.normalize(arq2.getLinhaToda(), Normalizer.Form.NFD)
								.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase();
						if (!(frase2.isEmpty())) {
							if (Character.isDigit(frase2.charAt(0))) {
								// System.out.println("corrigir: "+ frase2);
								boolean corrigir = true;
								while (corrigir) {
									frase2 = frase2.substring(1);
									if (frase2.charAt(0) == '-') {
										frase2 = frase2.substring(1);
									}
									corrigir = Character.isDigit(frase2.charAt(0));
									// System.out.println("corrigindo: "+ frase2);
								}
								frase2 = frase2.trim();
							}

							// Systen.out.println("vai comparar frase 1 com frase
							// 2"+arq1.getLinhaToda()+"\t"+arq2.getLinhaToda());
							if (frase2.equals(frase1)) {
								// Systen.out.println("É IGUAL");
								if (frase1.startsWith("Ac"))
									System.out.print(frase2 + " frase 2 == frase1");
								for (Arquivo arq3 : registro3) {
									if (encerrar) {
										break;
									}
									frase3 = null;
									frase3 = Normalizer.normalize(arq3.getLinhaToda(), Normalizer.Form.NFD)
											.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase();
									if (!(frase3.isEmpty())) {
										if (Character.isDigit(frase3.charAt(0))) {
											// Systen.out.println("corrigir: "+ frase3);
											boolean corrigir = true;
											while (corrigir) {
												frase3 = frase3.substring(1);
												if (frase3.charAt(0) == '-') {
													frase3 = frase3.substring(1);
												}
												corrigir = Character.isDigit(frase3.charAt(0));
												// Systen.out.println("corrigindo: "+ frase3);
											}
											frase3 = frase3.trim();
										}
										// Systen.out.println("vai comparar frase 2 com frase
										// 3"+arq2.getLinhaToda()+"\t"+arq3.getLinhaToda());

										if (frase3.equals(frase2)) {
											// Systen.out.println("É IGUAL");
											resposta.add(arq1.getLinhaToda() + "\t" + arq2.getLinhaToda() + "\t"
													+ arq3.getLinhaToda());
											if (frase1.startsWith("Ac"))
												System.out.print(frase3 + " frase 3 == frase 2 ");
											// Systen.out.println("adicionado ao P1(joinAll):
											// "+arq1.getLinhaToda()+"\t"+arq2.getLinhaToda()+"\t"+arq3.getLinhaToda());
											encerrar = true;
										}
									}

								}
							}
						}

					}

				}

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return resposta;
	}

	private static Set<String> montarDados(List<Arquivo> registro1, List<Arquivo> registro2, Set<String> exclusive) {
		Set<String> resposta = new HashSet<String>();
		String frase1;
		String frase2;

		boolean encerrar = false;

		int cont = 1;
		int cont2 = 1;
		try {
			for (Arquivo arq1 : registro1) {
				frase1 = null;
				frase1 = Normalizer.normalize(arq1.getLinhaToda(), Normalizer.Form.NFD)
						.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase();
				if (!(frase1.isEmpty())) {
					if (Character.isDigit(frase1.charAt(0))) {
						// System.out.println("corrigir: "+ frase1);
						boolean corrigir = true;
						while (corrigir) {
							frase1 = frase1.substring(1);
							if (frase1.charAt(0) == '-') {
								frase1 = frase1.substring(1);
							}
							corrigir = Character.isDigit(frase1.charAt(0));
							// System.out.println("corrigindo: "+ frase1);
						}
						frase1 = frase1.trim();
					}

					if (exclusive.contains(frase1)) {
						// System.out.println("EXCLUIU : " + frase1);
						continue;
					}

					for (Arquivo arq2 : registro2) {
						if (encerrar) {
							encerrar = false;
							break;
						}
						frase2 = null;
						frase2 = Normalizer.normalize(arq2.getLinhaToda(), Normalizer.Form.NFD)
								.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase();

						if (!(frase2.isEmpty())) {
							if (Character.isDigit(frase2.charAt(0))) {
								// System.out.println("corrigir: "+ frase2);
								boolean corrigir = true;
								while (corrigir) {
									frase2 = frase2.substring(1);
									if (frase2.charAt(0) == '-') {
										frase2 = frase2.substring(1);
									}
									corrigir = Character.isDigit(frase2.charAt(0));
									// System.out.println("corrigindo: "+ frase2);
								}
								frase2 = frase2.trim();
							}

							if (exclusive.contains(frase2)) {
								// System.out.println("EXCLUIU : " + frase2);
								continue;
							}

							if (frase2.equals(frase1)) {
								// System.out.println("ADICIONOU : " + frase2);
								resposta.add(arq1.getLinhaToda() + "\t" + arq2.getLinhaToda());
								encerrar = true;
							}
						}

					}
				}

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return resposta;
	}

	private void lerArqRefinamento(String arquivo) {
		int cont = 0;
		String textCell = "";
		String[] textoAux = null;
		boolean first = true;
		double contFreq = 0;
		double numTemp = 0;
		try {
			FileInputStream fis = new FileInputStream(new File(arquivo));

			int indice = 0;
			boolean primeiraLinha = true;
			// Systen.out.println(arquivo);

			XSSFWorkbook workbookX = null;
			HSSFWorkbook workbookH = null;

			XSSFSheet sheetX = null;
			HSSFSheet sheetH = null;
			try {
				workbookX = new XSSFWorkbook(fis);

				sheetX = workbookX.getSheetAt(0);

				tipoPlanilha = "X";
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			try {
				workbookH = new HSSFWorkbook(fis);

				sheetH = workbookH.getSheetAt(0);

				tipoPlanilha = "H";
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			Iterator<Row> rowIterator = null;

			if (sheetX == null) {
				rowIterator = sheetH.iterator();
			} else if (sheetH == null) {
				rowIterator = sheetX.iterator();
			}

			try {
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					if (!first) {
						Iterator<Cell> cellIterator = row.cellIterator();

						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();
							switch (cell.getCellType()) {
							case STRING:
								textCell = tratamentoCelula(cell.getStringCellValue());
								mapaText.put(indice, textCell);
								myListTexto.add(textCell);

								String textCompare = gerarString(Normalizer.normalize(textCell, Normalizer.Form.NFD)
										.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase().trim());
								mapaTextCompare.put(indice, textCompare);
								myListTextCompare.add(textCompare);

								break;
							case NUMERIC:
								try {
									numTemp = cell.getNumericCellValue();
								} catch (Exception e) {
									System.out.println(e.getMessage());
									numTemp = 1;
								}
								mapaNum.put(indice, (int) numTemp);
								myListNum.add(numTemp);
								textoAux = gerarString(textCell).split(" ");
								contFreq = textoAux.length;
								myListFreq.add(numTemp / contFreq);

								break;
							}

						}

						cont++;
						if (cont > maximumLinhasProj2)
							break;
					} else {
						first = false;
					}
					indice++;
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			fis.close();
		} catch (IOException e) {
			System.out.println("Ocorreu exceção no lerArqRefinamento: " + e.getMessage());
		}

	}

	private String tratamentoCelula(String celula) {
		if (!(retiradaCaracteres == null || retiradaCaracteres.equals(""))) {
			String splitarray[];

			splitarray = retiradaCaracteres.split("\\s+");

			for (String c : splitarray) {
				// System.out.println("caracter a retirar :"+c +"\tcelula: "+celula);
				if (celula.contains(c)) {
					// System.out.println(celula);
					celula = celula.replace(c, "");
					// System.out.println(celula);
				}
			}
		}
		return celula;
	}

	private String gerarString(String termo) {
		boolean continuar = true;
		String parte = "";
		int pos = 0;
		int posAfter = 0;
		while (continuar) {
			parte = "";
			if (termo.contains("de"))
				parte = "de";
			if (termo.contains("do"))
				parte = "do";
			if (termo.contains("da"))
				parte = "da";
			if (termo.contains("no"))
				parte = "no";
			if (termo.contains("na"))
				parte = "na";
			if (termo.contains("em"))
				parte = "em";
			if (termo.contains(","))
				parte = ",";
			if (termo.contains(" "))
				parte = " ";
			if (termo.contains("-"))
				parte = "-";
			if (termo.contains("("))
				parte = "(";
			if (termo.contains(")"))
				parte = ")";
			if (!parte.equals("")) {
				pos = termo.indexOf(parte);
				posAfter = pos + parte.length();
				termo = termo.substring(0, pos) + termo.substring(posAfter);
			} else {
				continuar = false;
			}
		}
		return termo;
	}

	private String gerarStringP4(String termo) {
		boolean continuar = true;
		String parte = "";
		int pos = 0;
		int posAfter = 0;
		while (continuar) {
			parte = "";
			if (termo.contains("de"))
				parte = "de";
			if (termo.contains("do"))
				parte = "do";
			if (termo.contains("da"))
				parte = "da";
			if (termo.contains("no"))
				parte = "no";
			if (termo.contains("na"))
				parte = "na";
			if (termo.contains("em"))
				parte = "em";
			if (termo.contains(","))
				parte = ",";
			if (termo.contains(" "))
				parte = " ";
			if (termo.contains("-"))
				parte = "-";
			if (termo.contains("("))
				parte = "(";
			if (termo.contains(")"))
				parte = ")";
			if (!parte.equals("")) {
				pos = termo.indexOf(parte);
				posAfter = pos + parte.length();
				termo = termo.substring(0, pos) + termo.substring(posAfter);
			} else {
				continuar = false;
			}
		}
		return Normalizer.normalize(termo, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
				.toUpperCase();
	}

	public void gerarRefinacoes(double porcentagem) {
		// String fileName = "frasesUnicasTodas_oficial.csv";

		double resto = 100.0 - porcentagem;

		try {

			double cont = 0;
			double freq = 0;
			double soma = 0;
			double i = 0;
			double perc = 0;
			double qtd = 0;
			double tam = 0;
			double lastTam = 0;
			double lastNum = 0;
			double lastFreq = 0;
			double contEqual = 0;
			int rand = 0;
			String fileName = "";
			String last = "";
			String lastCompare = "";
			boolean first = true;

			Map<Integer, String> sortedSet = sortByValue(mapaTextCompare);

			for (Map.Entry<Integer, String> entrada : sortedSet.entrySet()) {
				// System.out.println(entrada.getKey());
				// System.out.println(entrada.getValue());

				// System.out.println(contInterno+ " de "+myListTextCompare.size());
				// if (contInterno == 15) break;
				if (!first) {
					tam = entrada.getValue().length();

					cont++;

					for (int j = 1; j < Math.min(tam, lastTam); j++) {
						if (lastCompare.substring(0, j).equals(entrada.getValue().substring(0, j))) {
							contEqual++;
						} else {
							break;
						}
					}
					if (!lastCompare.equals(entrada.getValue())) {
						perc = contEqual / Math.max(tam, lastTam);
					} else {
						perc = 1;
					}

					/*
					 * if (entrada.getValue().equalsIgnoreCase("fungos")) {
					 * 
					 * System.out.println("da vez: "+
					 * entrada.getValue()+"\t last: "+lastCompare+"\t perc= "+perc+"\t cont= "
					 * +cont+"\t soma= "+soma
					 * +"\tNum da vez= "+mapaNum.get(entrada.getKey())+"\tlastNum="+lastNum);
					 * System.out.println("tam=: "+ tam
					 * +"\tlastTam="+lastTam+"\tcontEqual="+contEqual);
					 * 
					 * }
					 */

					if (cont > 1) {
						soma = soma + lastNum;
						freq = freq + lastFreq;
					} else {
						soma = lastNum;
						freq = lastFreq;
					}

					if (!(perc >= (porcentagem / 100))) {
						if (myListNum.isEmpty()) {
							if (!contemPalavraInvalida(lastCompare)) {
								resultadosRefinamento.add(new String[] { last });
							}
						} else {
							if (!contemPalavraInvalida(lastCompare)) {
								resultadosRefinamento.add(new String[] { last, soma + "" });
							}
						}

						// Systen.out.println(last);
						// Systen.out.println(new DecimalFormat("0.00").format((freq/cont)));
						// Systen.out.println(soma+"");
						myListRefinamentoIncluso.add(lastCompare);

						// Systen.out.println("gravou "+last + "\tfreq: "+(freq/cont));
						// Systen.out.println("soma: "+soma);

						if (!lastCompare.equals(entrada.getValue())) {
							cont = 0;
							soma = 0;
							perc = 0;
							freq = 0;
						}

					}

					contEqual = 0;
				}

				last = mapaText.get(entrada.getKey());
				lastCompare = entrada.getValue();
				lastTam = entrada.getValue().length();
				if (!myListNum.isEmpty()) {
					// lastFreq = myListFreq.get(contInterno);
					lastNum = mapaNum.get(entrada.getKey());
				}

				if (first) {
					first = false;
				}
			}

			// do the last

			if (perc >= (porcentagem / 100)) {
				soma = soma + lastNum;
				freq = freq + lastFreq;
				if (myListNum.isEmpty()) {
					if (!contemPalavraInvalida(last)) {
						resultadosRefinamento.add(new String[] { last });
					}

				} else {
					if (!contemPalavraInvalida(last)) {
						resultadosRefinamento.add(new String[] { last, (soma == 0 ? lastNum : soma) + "" });
					}
				}

				myListRefinamentoIncluso.add(lastCompare);

			}

			// GRAVAR REFINAMENTOS TOTAIS

			if (tipoPlanilha.equals("H")) {
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet sheetResult = workbook.createSheet("Resultados");

				int rownum = 0;
				Row row = sheetResult.createRow(rownum++);
				int cellnum = 0;

				CellStyle headerStyle = workbook.createCellStyle();
				headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				headerStyle.setAlignment(HorizontalAlignment.CENTER);
				headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

				CellStyle lineStyle1 = workbook.createCellStyle();
				lineStyle1.setFillForegroundColor(IndexedColors.GOLD.getIndex());
				lineStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				CellStyle lineStyle2 = workbook.createCellStyle();
				lineStyle1.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
				lineStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				Cell cellTitulo1 = row.createCell(cellnum++);
				cellTitulo1.setCellStyle(headerStyle);
				cellTitulo1.setCellValue("FRASE/PALAVRA-CHAVE");
				sheetResult.autoSizeColumn(0);

				if (!resultadosRefinamento.isEmpty()) {
					if (resultadosRefinamento.get(0).length > 1) {
						Cell cellTitulo2 = row.createCell(cellnum++);
						cellTitulo2.setCellStyle(headerStyle);
						cellTitulo2.setCellValue("Nº DOCUMENTOS INDEXADOS NO R.I.");
						sheetResult.autoSizeColumn(1);
						/*
						 * Cell cellTitulo3 = row.createCell(cellnum++);
						 * cellTitulo3.setCellStyle(headerStyle);
						 * cellTitulo3.setCellValue("QUANTIDADE"); sheetResult.autoSizeColumn(2);
						 */
					}
				}

				int numLinha = 0;
				CellStyle estilo = null;
				for (String[] linha : resultadosRefinamento) {
					// Systen.out.println("linha #:"+numLinha+"\t"+linha[0]);
					row = sheetResult.createRow(rownum++);
					if (numLinha % 2 == 0) {
						estilo = lineStyle1;
					} else {
						estilo = lineStyle2;
					}
					cellnum = 0;

					Cell cell1 = row.createCell(cellnum++);
					cell1.setCellValue(linha[0]);
					cell1.setCellStyle(estilo);

					if (linha.length > 1) {
						Cell cell2 = row.createCell(cellnum++);
						cell2.setCellValue(linha[1]);
						cell2.setCellStyle(estilo);
						/*
						 * Cell cell3 = row.createCell(cellnum++); cell3.setCellValue(linha[2]);
						 * cell3.setCellStyle(estilo);
						 */
					}

					numLinha++;
				}

				try {
					rand = (int) (10000000 + Math.random() * 90000000);
					fileName = diretorio + "/RefinamentoTotal_" + porcentagem + "%_" + rand + ".xls";
					FileOutputStream out = new FileOutputStream(new File(fileName));
					workbook.write(out);
					out.close();
					generatedFiles.add(fileName);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.out.println("Arquivo não encontrado!");
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Erro na edição do arquivo!");
				}

			}

			else if (tipoPlanilha.equals("X")) {
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheetResult = workbook.createSheet("Resultados");

				int rownum = 0;
				Row row = sheetResult.createRow(rownum++);
				int cellnum = 0;

				CellStyle headerStyle = workbook.createCellStyle();
				headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				headerStyle.setAlignment(HorizontalAlignment.CENTER);
				headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

				CellStyle lineStyle1 = workbook.createCellStyle();
				lineStyle1.setFillForegroundColor(IndexedColors.GOLD.getIndex());
				lineStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				CellStyle lineStyle2 = workbook.createCellStyle();
				lineStyle1.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
				lineStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				Cell cellTitulo1 = row.createCell(cellnum++);
				cellTitulo1.setCellStyle(headerStyle);
				cellTitulo1.setCellValue("FRASE/PALAVRA-CHAVE");
				sheetResult.autoSizeColumn(0);
				if (!resultadosRefinamento.isEmpty()) {
					if (resultadosRefinamento.get(0).length > 1) {
						Cell cellTitulo2 = row.createCell(cellnum++);
						cellTitulo2.setCellStyle(headerStyle);
						cellTitulo2.setCellValue("Nº DOCUMENTOS INDEXADOS NO R.I.");
						sheetResult.autoSizeColumn(1);
						/*
						 * Cell cellTitulo3 = row.createCell(cellnum++);
						 * cellTitulo3.setCellStyle(headerStyle);
						 * cellTitulo3.setCellValue("QUANTIDADE"); sheetResult.autoSizeColumn(2);
						 */
					}
				}

				int numLinha = 0;
				CellStyle estilo = null;
				for (String[] linha : resultadosRefinamento) {
					// Systen.out.println("linha #:"+numLinha+"\t"+linha[0]);
					row = sheetResult.createRow(rownum++);
					if (numLinha % 2 == 0) {
						estilo = lineStyle1;
					} else {
						estilo = lineStyle2;
					}
					cellnum = 0;

					Cell cell1 = row.createCell(cellnum++);
					cell1.setCellValue(linha[0]);
					cell1.setCellStyle(estilo);

					if (linha.length > 1) {
						Cell cell2 = row.createCell(cellnum++);
						cell2.setCellValue(linha[1]);
						cell2.setCellStyle(estilo);
						/*
						 * Cell cell3 = row.createCell(cellnum++); cell3.setCellValue(linha[2]);
						 * cell3.setCellStyle(estilo);
						 */
					}

					numLinha++;
				}

				try {
					rand = (int) (10000000 + Math.random() * 90000000);
					fileName = diretorio + "/RefinamentoTotal_" + porcentagem + "%_" + rand + ".xlsx";
					FileOutputStream out = new FileOutputStream(new File(fileName));
					workbook.write(out);
					out.close();
					generatedFiles.add(fileName);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.out.println("Arquivo não encontrado!");
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Erro na edição do arquivo!");
				}

			}

			for (int z = 0; z < myListTexto.size(); z++) {
				if (!myListRefinamentoIncluso.contains(myListTextCompare.get(z))) {
					if (!contemPalavraInvalida(myListTexto.get(z))) {
						resultadosRefinamentoResto.add(myListTexto.get(z));
					}

				}
			}

			// GRAVAR REFINAMENTOS RESTO

			if (tipoPlanilha.equals("H")) {
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet sheetResult = workbook.createSheet("Resultados");

				int rownum = 0;
				Row row = sheetResult.createRow(rownum++);
				int cellnum = 0;

				CellStyle headerStyle = workbook.createCellStyle();
				headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				headerStyle.setAlignment(HorizontalAlignment.CENTER);
				headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

				CellStyle lineStyle1 = workbook.createCellStyle();
				lineStyle1.setFillForegroundColor(IndexedColors.GOLD.getIndex());
				lineStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				CellStyle lineStyle2 = workbook.createCellStyle();
				lineStyle1.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
				lineStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				Cell cellTitulo1 = row.createCell(cellnum++);
				cellTitulo1.setCellStyle(headerStyle);
				cellTitulo1.setCellValue("FRASE/PALAVRA-CHAVE");
				sheetResult.autoSizeColumn(0);

				int numLinha = 0;
				CellStyle estilo = null;
				for (String linha : resultadosRefinamentoResto) {
					// Systen.out.println("linha #:"+numLinha+"\t"+linha);
					row = sheetResult.createRow(rownum++);
					if (numLinha % 2 == 0) {
						estilo = lineStyle1;
					} else {
						estilo = lineStyle2;
					}
					cellnum = 0;

					Cell cell1 = row.createCell(cellnum++);
					cell1.setCellValue(linha);
					cell1.setCellStyle(estilo);

					numLinha++;
				}

				try {
					rand = (int) (10000000 + Math.random() * 90000000);

					fileName = diretorio + "/RefinamentoResto_" + resto + "%_" + rand + ".xls";
					FileOutputStream out = new FileOutputStream(new File(fileName));
					workbook.write(out);
					out.close();
					generatedFiles.add(fileName);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.out.println("Arquivo não encontrado!");
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Erro na edição do arquivo!");
				}

			}

			else if (tipoPlanilha.equals("X")) {
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheetResult = workbook.createSheet("Resultados");

				int rownum = 0;
				Row row = sheetResult.createRow(rownum++);
				int cellnum = 0;

				CellStyle headerStyle = workbook.createCellStyle();
				headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				headerStyle.setAlignment(HorizontalAlignment.CENTER);
				headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

				CellStyle lineStyle1 = workbook.createCellStyle();
				lineStyle1.setFillForegroundColor(IndexedColors.GOLD.getIndex());
				lineStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				CellStyle lineStyle2 = workbook.createCellStyle();
				lineStyle1.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
				lineStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				Cell cellTitulo1 = row.createCell(cellnum++);
				cellTitulo1.setCellStyle(headerStyle);
				cellTitulo1.setCellValue("FRASE/PALAVRA-CHAVE");
				sheetResult.autoSizeColumn(0);

				int numLinha = 0;
				CellStyle estilo = null;
				for (String linha : resultadosRefinamentoResto) {
					// System.out.println("linha #:"+rownum+"\t"+linha);
					row = sheetResult.createRow(rownum++);
					if (numLinha % 2 == 0) {
						estilo = lineStyle1;
					} else {
						estilo = lineStyle2;
					}
					cellnum = 0;

					Cell cell1 = row.createCell(cellnum++);
					cell1.setCellValue(linha);
					cell1.setCellStyle(estilo);

					numLinha++;
				}

				try {
					rand = (int) (10000000 + Math.random() * 90000000);
					fileName = diretorio + "/RefinamentoResto_" + resto + "%_" + rand + ".xlsx";
					FileOutputStream out = new FileOutputStream(new File(fileName));
					workbook.write(out);
					out.close();
					generatedFiles.add(fileName);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.out.println("Arquivo não encontrado!");
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Erro na edição do arquivo!");
				}

			}
		} catch (Exception e3) {
			System.out.println("problema no getRefinações: " + e3.getMessage());
		}

	}

	private boolean contemPalavraInvalida(String frase) {
		boolean resp = false;
		if (!(retiradaTermos == null || retiradaTermos.equals(""))) {
			String splitarray[];

			splitarray = retiradaTermos.split("\\s+");

			for (String c : splitarray) {
				if (frase.contains(c)) {
					resp = true;
				}
			}
		}
		return resp;
	}

	/*
	 * private Map<String, Integer> sortByValue(Map<String, Integer> unsortMap,
	 * final boolean order) { List<Entry<String, Integer>> list = new
	 * LinkedList<>(unsortMap.entrySet());
	 * 
	 * // Sorting the list based on values list.sort((o1, o2) -> order ?
	 * o1.getValue().compareTo(o2.getValue()) == 0 ?
	 * o1.getKey().compareTo(o2.getKey()) : o1.getValue().compareTo(o2.getValue()) :
	 * o2.getValue().compareTo(o1.getValue()) == 0 ?
	 * o2.getKey().compareTo(o1.getKey()) : o2.getValue().compareTo(o1.getValue()));
	 * return list.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue,
	 * (a, b) -> b, LinkedHashMap::new));
	 * 
	 * }
	 */
	public static HashMap<Integer, String> sortByValue(HashMap<Integer, String> hm) {
		// Create a list from elements of HashMap
		List<Map.Entry<Integer, String>> list = new LinkedList<Map.Entry<Integer, String>>(hm.entrySet());

		// Sort the list using lambda expression
		Collections.sort(list, (i1, i2) -> i1.getValue().compareTo(i2.getValue()));

		// put data from sorted list to hashmap
		HashMap<Integer, String> temp = new LinkedHashMap<Integer, String>();
		for (Map.Entry<Integer, String> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}
}
