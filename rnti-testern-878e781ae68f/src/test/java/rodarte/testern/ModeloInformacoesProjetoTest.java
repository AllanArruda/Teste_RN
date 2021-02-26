package rodarte.testern;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import rodarte.testern.ModeloInformacoesProjeto;

public class ModeloInformacoesProjetoTest {

	private static final ModeloInformacoesProjeto INFORMACOES_DO_PROJETO = new ModeloInformacoesProjeto();
	
	
	
	
	@Test
	public void testarExistenciaVersao() {
		assertThat(INFORMACOES_DO_PROJETO.getVersao())
			.isNotEmpty();
	}
	
	@Test
	public void testarExistenciaNome() {
		assertThat(INFORMACOES_DO_PROJETO.getNome()).isNotEmpty();
	}

	@Test
	public void testarFormatoDataVersao() {
		assertThat(INFORMACOES_DO_PROJETO.getDataVersao())
			.matches("\\d{1,2}\\/\\d{1,2}\\/\\d{4}"); // se corresponde ao formato dd/mm/aaaa
	}
	
	@Test
	public void testarExistenciaCaminhoDeAcessoAplicacao() {
		assertThat(INFORMACOES_DO_PROJETO.getCaminhoDeAcesso()).isNotBlank();
	}
	
}
