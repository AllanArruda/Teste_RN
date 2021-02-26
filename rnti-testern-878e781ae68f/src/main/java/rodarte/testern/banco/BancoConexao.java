package rodarte.testern.banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class BancoConexao {

	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	
	private static final String USUARIO = "user";
	private static final String SENHA = "senha";
	
	public static final String NOME_BANCO_DADOS_PRINCIPAL = "O NOME DO SEU BANCO DE DADOS :D"; //TODO: alterar para o nome do banco de dados do projeto
	public static final String NOME_BANCO_DADOS_GERAL = "nome_bd";
	
	private BancoConexao() {
	}
	
	/**
	 * Abre uma conexão com o banco de dados informado.
	 * @param banco - o nome do banco de dados a ser conectado
	 * @return a conexão criada
	 * @throws Exception caso ocorra algum erro ao criar a conexão
	 */
	public static Connection abrir(final String banco) throws Exception {
		Connection conexao = null;
		String url = "jdbc:mysql://localhost/" +banco+ "?rewriteBatchedStatements=true"
				+ "&useSSL=false&useTimezone=true&serverTimezone=UTC";
		
		try {
			Class.forName(DRIVER);
			conexao = DriverManager.getConnection(url, USUARIO, SENHA);

			return conexao;
		}catch(Exception ex) {
			throw new Exception("Problema ao realizar a conexão com o banco de dados. Erro: " +ex);
		}
	}
	
	/**
	 * Fecha a {@linkplain Connection}, o {@linkplain ResultSet} e/ou o {@linkplain PreparedStatement}.
	 * @param conexao - a {@linkplain Connection} que será fechada. <code>null</code> caso não exista
	 * @param pstm - o {@linkplain PreparedStatement} que será fechado. <code>null</code> caso não exista
	 * @param rs - o {@linkplain ResultSet} que será fechado. <code>null</code> caso não exista
	 * @throws SQLException caso ocorra algum erro ao fechar alguma das estruturas
	 */
	public static void fechar(Connection conexao, PreparedStatement pstm, ResultSet rs) throws SQLException {
		try {
			if (conexao != null)
				conexao.close();

			if (pstm != null)
				pstm.close();
			
			if (rs != null)
				rs.close();
		}catch(SQLException ex) {
			throw new SQLException("Problema ao fechar a conexão. Erro: " +ex);
		}
	}
	
}
