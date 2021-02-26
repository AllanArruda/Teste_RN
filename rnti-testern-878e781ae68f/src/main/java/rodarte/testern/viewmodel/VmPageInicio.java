package rodarte.testern.viewmodel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;


import jdk.jfr.Percentage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;
import org.apache.poi.ss.util.NumberToTextConverter;


public class VmPageInicio {

	private Media arquivoTemporario;
	
	private boolean visibilidadeBotaoLerEProcessar = true;
	
	@Command
	public void uploadArquivo(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws Exception {
		
		UploadEvent event = (UploadEvent) ctx.getTriggerEvent();

		arquivoTemporario = event.getMedia();
		
		BindUtils.postNotifyChange(null, null, VmPageInicio.this, "nomeArquivo");
		
	}
	
	@Command
	public void lerArquivoEProcessar() {
		
		try {
			InputStream arquivo = arquivoTemporario.getStreamData();

			// Conexão com o banco
			Class.forName("com.mysql.jdbc.Driver");
			Connection connect = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/testern?useTimezone=true&serverTimezone=UTC" ,
					"root" ,
					""
			);
			connect.setAutoCommit(false);

				// Criação da Planilha
				XSSFWorkbook workbook = new XSSFWorkbook(arquivo);
				XSSFSheet sheet = workbook.getSheetAt(0);
				Iterator<Row> rowIterator = sheet.iterator();

				// Query para inserir os dados no banco
				String sql = "INSERT INTO tabela_info (id_tabela, nome, sexo, data_nascimento, nota_1trimestre, nota_2trimestre, nota_3trimestre) VALUES (?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement statement = connect.prepareStatement(sql);

				rowIterator.next(); //Pula o cabeçalho

				// Percorre e lê o arquivo Excel, inserindo os dados nas variáveis que vão para o banco
				while (rowIterator.hasNext()) {
					Row nextRow = rowIterator.next();
					Iterator<Cell> cellIterator = nextRow.cellIterator();

					while (cellIterator.hasNext()) {
						Cell nextCell = cellIterator.next();

						int columnIndex = nextCell.getColumnIndex();

						switch (columnIndex) {
							 case 0:
								int id = (int) nextCell.getNumericCellValue();
								statement.setInt(1, id);
								break;
							case 1:
								String nome = nextCell.getStringCellValue();
								statement.setString(2, nome);
								break;
							case 2:
								String sexo = nextCell.getStringCellValue();
								statement.setString(3, sexo);
								break;
							 case 3:
								Date dataNascimento = nextCell.getDateCellValue();
								statement.setTimestamp(4, new Timestamp(dataNascimento.getTime()));
								break;
							 case 4:
								int nota_1trimestre = (int) nextCell.getNumericCellValue();
								statement.setInt(5, nota_1trimestre);
								break;
							case 5:
								int nota_2trimestre = (int) nextCell.getNumericCellValue();
								statement.setInt(6, nota_2trimestre);
								break;
							case 6:
								int nota_3trimestre = (int) nextCell.getNumericCellValue();
								statement.setInt(7, nota_3trimestre);
								break;
						}

					}
					statement.addBatch();
				}

				statement.executeBatch();
				workbook.close();
				connect.commit();
				connect.close();

			visibilidadeBotaoLerEProcessar = false;

			BindUtils.postNotifyChange(null, null, VmPageInicio.this, "visibilidadeBotaoLerEProcessar");

			Clients.showNotification("Processamento realizado com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null, 3500, true);

		} catch (Exception ex) {

			Messagebox.show(ex.getMessage(), "Erro ao Ler e Processar Arquivo", Messagebox.OK, Messagebox.ERROR);

		}
		
	}
	
	@Command
	public void exportarResultados() {

		try {

			File arquivo = new File("Resultados.xlsx").getCanonicalFile();
			OutputStream out = new FileOutputStream(arquivo.getAbsolutePath());

			//Conexão com o banco
			Class.forName("com.mysql.jdbc.Driver");
			Connection connect = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/testern?useTimezone=true&serverTimezone=UTC" ,
					"root" ,
					""
			);

			// Query para a planilha 1, ordenando os resultados pelo nome.
			Statement statement = connect.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from tabela_info ORDER BY nome");
			//Criação da planilha
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Resultado");
			// Gera os cabeçalhos
			XSSFRow row = sheet.createRow(1);
			XSSFCell cell;
			cell = row.createCell(1);
			cell.setCellValue("Identificação");
			cell = row.createCell(2);
			cell.setCellValue("Nome");
			cell = row.createCell(3);
			cell.setCellValue("Sexo");
			cell = row.createCell(4);
			cell.setCellValue("Data Nascimento");
			cell = row.createCell(5);
			cell.setCellValue("Nota 1º Trimestre");
			cell = row.createCell(6);
			cell.setCellValue("Nota 2º Trimestre");
			cell = row.createCell(7);
			cell.setCellValue("Nota 3º Trimestre");
			sheet.autoSizeColumn(1);
			int i = 2;
			// Percorre e insere os dados do banco nas linhas e colunas
			while(resultSet.next()) {
				row = sheet.createRow(i);
				cell = row.createCell(1);
				cell.setCellValue(resultSet.getInt("id_tabela"));
				cell = row.createCell(2);
				cell.setCellValue(resultSet.getString("nome"));
				cell = row.createCell(3);
				cell.setCellValue(resultSet.getString("sexo"));
				cell = row.createCell(4);
				cell.setCellValue(resultSet.getString("data_nascimento"));
				cell = row.createCell(5);
				cell.setCellValue(resultSet.getInt("nota_1trimestre"));
				cell = row.createCell(6);
				cell.setCellValue(resultSet.getInt("nota_2trimestre"));
				cell = row.createCell(7);
				cell.setCellValue(resultSet.getInt("nota_3trimestre"));
				sheet.autoSizeColumn(i);
				i++;
			}
			// Query para a planilha 2, calculando a idade e a média das notas
			Statement statement2 = connect.createStatement();
			ResultSet resultSet2 = statement2.executeQuery("SELECT ID_tabela, Nome, Sexo, Data_Nascimento, CURDATE(),TIMESTAMPDIFF(YEAR,Data_Nascimento,CURDATE()) AS idade, SUM(Nota_1Trimestre + Nota_2Trimestre + Nota_3Trimestre) / 3 AS media FROM tabela_info GROUP BY ID_tabela ORDER BY idade");
			XSSFSheet sheet2 = workbook.createSheet("Resultado2");

			XSSFRow row2 = sheet2.createRow(1);
			XSSFCell cell2;
			cell2 = row2.createCell(1);
			cell2.setCellValue("Identificação");
			cell2 = row2.createCell(2);
			cell2.setCellValue("Nome");
			cell2 = row2.createCell(3);
			cell2.setCellValue("Idade");
			cell2 = row2.createCell(4);
			cell2.setCellValue("Média das notas");
			sheet2.autoSizeColumn(1);
			int j = 2;

			while(resultSet2.next()) {
				row2 = sheet2.createRow(j);
				cell2 = row2.createCell(1);
				cell2.setCellValue(resultSet2.getInt("id_tabela"));
				cell2 = row2.createCell(2);
				cell2.setCellValue(resultSet2.getString("nome"));
				cell2 = row2.createCell(3);
				cell2.setCellValue(resultSet2.getInt("idade"));
				cell2 = row2.createCell(4);
				cell2.setCellValue(resultSet2.getDouble("media"));
				sheet2.autoSizeColumn(j);
				j++;
			}

			// Queries para a planilha 3, com as estatíscas requisitadas
			Statement statement3 = connect.createStatement();
			ResultSet resultSet3 = statement3.executeQuery("SELECT 100*SUM(CASE WHEN sexo='M' THEN 1 END)/count(*) masculino_porc, 100*SUM(CASE WHEN sexo = 'F' THEN 1 END)/count(*) feminino_porc FROM tabela_info");
			Statement statement4 = connect.createStatement();
			ResultSet resultSet4 = statement4.executeQuery("SELECT 100*SUM(CASE WHEN idade<30 THEN 1 END)/count(*) porc_menor_30 FROM (SELECT Data_Nascimento, CURDATE(),TIMESTAMPDIFF(YEAR,Data_Nascimento,CURDATE()) AS idade FROM tabela_info) AS X");
			Statement statement5 = connect.createStatement();
			ResultSet resultSet5 = statement5.executeQuery("SELECT 100*SUM(CASE WHEN nota > 60 THEN 1 END)/count(*) perc_aprovado FROM (SELECT SUM(Nota_1Trimestre + Nota_2Trimestre + Nota_3Trimestre) AS nota FROM tabela_info GROUP BY Nome) AS Z");
			Statement statement6 = connect.createStatement();
			ResultSet resultSet6 = statement6.executeQuery("SELECT AVG(media) AS media_30 FROM( SELECT ID_tabela, Nome, Sexo, Data_Nascimento, CURDATE(),TIMESTAMPDIFF(YEAR,Data_Nascimento,CURDATE()) AS idade, SUM(Nota_1Trimestre + Nota_2Trimestre + Nota_3Trimestre) / 3 AS media FROM tabela_info GROUP BY ID_tabela) as Z WHERE idade > 30");
			Statement statement7 = connect.createStatement();
			ResultSet resultSet7 = statement7.executeQuery("SELECT AVG(media) AS media_M FROM( SELECT ID_tabela, Nome, Sexo, SUM(Nota_1Trimestre + Nota_2Trimestre + Nota_3Trimestre) / 3 AS media FROM tabela_info GROUP BY ID_tabela) as Z WHERE sexo = 'M'");
			Statement statement8 = connect.createStatement();
			ResultSet resultSet8 = statement8.executeQuery("SELECT AVG(media) AS media_F FROM( SELECT ID_tabela, Nome, Sexo, SUM(Nota_1Trimestre + Nota_2Trimestre + Nota_3Trimestre) / 3 AS media FROM tabela_info GROUP BY ID_tabela) as Z WHERE sexo = 'F'");
			Statement statement9 = connect.createStatement();
			ResultSet resultSet9 = statement9.executeQuery("SELECT AVG(idade) AS media_idade FROM (SELECT ID_tabela, Nome, Sexo, Data_Nascimento, CURDATE(),TIMESTAMPDIFF(YEAR,Data_Nascimento,CURDATE()) AS idade FROM tabela_info GROUP BY ID_tabela) AS Z");
			XSSFSheet sheet3 = workbook.createSheet("Resultado3");

			XSSFRow row3 = sheet3.createRow(1);
			XSSFCell cell3;
			cell3 = row3.createCell(1);
			cell3.setCellValue("Estatística");
			int z = 2;

			// Percorre cada uma das queries, adicionando os valores na planilha
			while(resultSet3.next()) {
				row3 = sheet3.createRow(z);
				cell3 = row3.createCell(1);
				cell3.setCellValue("Percentual de alunos do sexo masculino");
				cell3 = row3.createCell(2);
				cell3.setCellValue(resultSet3.getDouble("masculino_porc"));
				row3 = sheet3.createRow(z+1);
				cell3 = row3.createCell(1);
				cell3.setCellValue("Percentual de alunos do sexo feminino");
				cell3 = row3.createCell(2);
				cell3.setCellValue(resultSet3.getDouble("feminino_porc"));
				sheet3.autoSizeColumn(z);
				z++;
			}

			while(resultSet4.next()) {
				row3 = sheet3.createRow(z+1);
				cell3 = row3.createCell(1);
				cell3.setCellValue("Percentual de alunos com menos de 30 anos");
				cell3 = row3.createCell(2);
				cell3.setCellValue(resultSet4.getDouble("porc_menor_30"));
				sheet3.autoSizeColumn(z);
				z++;
			}

			while(resultSet5.next()) {
				row3 = sheet3.createRow(z+1);
				cell3 = row3.createCell(1);
				cell3.setCellValue("Percentual de alunos aprovados");
				cell3 = row3.createCell(2);
				cell3.setCellValue(resultSet5.getDouble("perc_aprovado"));
				sheet3.autoSizeColumn(z);
				z++;
			}

			while(resultSet6.next()) {
				row3 = sheet3.createRow(z+1);
				cell3 = row3.createCell(1);
				cell3.setCellValue("Média de nota dos alunos com mais de 30 anos");
				cell3 = row3.createCell(2);
				cell3.setCellValue(resultSet6.getDouble("media_30"));
				sheet3.autoSizeColumn(z);
				z++;
			}

			while(resultSet7.next()) {
				row3 = sheet3.createRow(z+1);
				cell3 = row3.createCell(1);
				cell3.setCellValue("Média de nota dos alunos do sexo masculino");
				cell3 = row3.createCell(2);
				cell3.setCellValue(resultSet7.getDouble("media_M"));
				sheet3.autoSizeColumn(z);
				z++;
			}

			while(resultSet8.next()) {
				row3 = sheet3.createRow(z+1);
				cell3 = row3.createCell(1);
				cell3.setCellValue("Média de nota dos alunos do sexo feminino");
				cell3 = row3.createCell(2);
				cell3.setCellValue(resultSet8.getDouble("media_F"));
				sheet3.autoSizeColumn(z);
				z++;
			}

			while(resultSet9.next()) {
				row3 = sheet3.createRow(z+1);
				cell3 = row3.createCell(1);
				cell3.setCellValue("Média da idade dos participantes da base");
				cell3 = row3.createCell(2);
				cell3.setCellValue(resultSet9.getDouble("media_idade"));
				sheet3.autoSizeColumn(z);
				z++;
			}

				sheet3.autoSizeColumn(1);
				workbook.write(out);
				workbook.close();
				connect.close();

			Filedownload.save(arquivo, ".xlsx");

			Clients.showNotification("Arquivo exportado com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null, 3500, true);

		} catch (Exception ex) {

			Messagebox.show(ex.getMessage(), "Erro ao Exportar Resultados", Messagebox.OK, Messagebox.ERROR);

		}

	}
	
	@Command
	public void resetarImportacao() {
		
		arquivoTemporario = null;
		
		visibilidadeBotaoLerEProcessar = true;
		
		BindUtils.postNotifyChange(null, null, VmPageInicio.this, "visibilidadeBotaoLerEProcessar");
		BindUtils.postNotifyChange(null, null, VmPageInicio.this, "nomeArquivo");
		
		Clients.showNotification("Atenção quanto aos dados que foram persistidos no banco!", Clients.NOTIFICATION_TYPE_WARNING, null, null, 3500, true);
		
	}
	
	public String getNomeArquivo() {
		
		return arquivoTemporario == null ? "" : arquivoTemporario.getName();
		
	}
	
	public boolean getVisibilidadeBotaoLerEProcessar() {
		
		return this.visibilidadeBotaoLerEProcessar;
		
	}
	
}
