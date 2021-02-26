package rodarte.testern;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ModeloInformacoesProjeto {

	private static final Properties PROPRIEDADES = new Properties();
	
	static {
		try {
			final InputStream arquivoPropriedades = Thread.currentThread()
					.getContextClassLoader()
					.getResourceAsStream("app.properties");
			
			if(arquivoPropriedades != null) {
				PROPRIEDADES.load(arquivoPropriedades);
			}
		}catch(IOException ex) {
			// não faz nada :)
		}
	}
	
	
	
	
	/**
	 * Retorna a versão atual do sistema. 
	 */
	public String getVersao() {
		return PROPRIEDADES.getProperty("versao", "");
	}

	/**
	 * Retorna uma a data de lançamento da versão formatada no seguinte padrão: 
	 * <b>dd/mm/aaaa</b>.
	 */
	public String getDataVersao() {
		return PROPRIEDADES.getProperty("data-da-versao", "");
	}
	
	/**
	 * Retorna o caminho de acesso à aplicação. 
	 */
	public String getCaminhoDeAcesso() {
		return PROPRIEDADES.getProperty("caminho-de-acesso", "");
	}
	
	/**
	 * Retorna o nome do projeto.
	 */
	public String getNome() {
		return PROPRIEDADES.getProperty("nome", "");
	}
	
	/**
	 * Retorna o nome do projeto concatenado a versão, seguindo o seguinte template: 
	 * <b>${NOME_DO_PROJETO} - versão ${VERSAO}</b>.
	 */
	public String getNomeConcatenadoComVersao() {
		return getNome().concat(" - versão ").concat(getVersao());
	}
	
}
