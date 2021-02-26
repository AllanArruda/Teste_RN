package rodarte.testern.viewmodel;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;

import rodarte.testern.ModeloInformacoesProjeto;

public class VmIndex {

	private final ModeloInformacoesProjeto informacoesDoProjeto = new ModeloInformacoesProjeto(); 
	
	@Command
	public void abrirTelaArquivos() {
		
		Executions.createComponents("pageArquivos.zul", null, null);
		
	}
	
	public ModeloInformacoesProjeto getInformacoesDoProjeto() {
		
		return this.informacoesDoProjeto;
		
	}
	
}
