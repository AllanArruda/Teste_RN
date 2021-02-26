package rodarte.testern.viewmodel;

import java.io.File;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Filedownload;

public class VmArquivos {

	@Command
	public void baixarArquivoBaseImportacao() throws Exception {
		
		Filedownload.save(new File(Executions.getCurrent().getSession().getWebApp().getRealPath("arquivos/Base Importação Teste RN.xlsx")), "xlsx");
		
	}
	
}
