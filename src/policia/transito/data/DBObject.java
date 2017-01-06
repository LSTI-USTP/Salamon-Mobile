package policia.transito.data;

public interface DBObject 
{
	
	
	public String T_ADMIN = "t_admin",
			ADM_ID = "adm_id",
			ADM_USERNAME = "adm_username",
			ADM_PWD = "adm_pwd",
			
			ADM_HOSTSERVER = "adm_host",
			ADM_CODREGISTER = "adm_correg",
			ADM_DTREG = "adm_dtreg",
			ADM_STATE = "adm_state",
			ADM_DIVICENAME = "adm_divicename",
			T_LOGIN="t_login",
			ID_LOGIN="ID LOGIN",
			ID_ALOCACAO="ID ALOCACAO",
			TIPO_OPERACAO="TIPO OPERACAO",
			NIVEL_ATUACAO="NIVEL ATUACAO",
			NOME_ACESSO="ACCESS NAME",
			NOME="NAME",
			APELIDO="APELIDO",
			ESTADO="STATE",
			DB_NAME ="fisacalizacao";
	
	
	public String T_INFRACAO ="t_infracao",
			INFRACAO_ID ="infracao_id",
			INFRACAO_ARTIGO = "infracao_artigo",
			INFRACAO_CATEGORIA = "infracao_categoria",
			INFRACAO_DESCRICAO ="infracao_descricao",
			INFRACAO_LINHA ="infracao_linha",
			INFRACAO_MODO_COIMA ="infracao_modo_coima",
			INFRACAO_NOME ="infracao_nome",
			INFRACAO_TIPO ="infracao_tipo";
	
	
	public String T_DRIVER_STATE ="t_driver_state",
		   DRIVER_STATE_ID ="driver_state_id",
		   DRIVER_STATE_DESC="driver_state_desc";
	
	public String T_CATEGORIA_VEICULO="t_categoria_veiculo",
			CATEGORIA_VEICULO_ID="categoria_veiculo_id",
			CATEORIA_VEICULO_DESC="categoria_veiculo_descricao";
	
	public String T_FISCALIZACAO ="t_fiscalizacao",
			FISCA_ESTADO_CONDUTOR= "fisca_estado_condutor",
			FISCA_EXISTENCIA_CARTA="fisca_existencia_carta",
			FISCA_INCOMPATIBILIDADE_CARTA="fisca_incompatibilidade_carta",
			FISCA_NUMERO_CARTA="fisca_numero_carta",
			FISCA_LIVRETE="fisca_livrete",
			FISCA_CATEGORIA_VEICULO="fisca_categoria_veiculo",
			FISCA_NUMERO_MATRICULA="fisca_numero_matricula",
			FISCA_NOME_CONDUTOR ="fisca_nome_condutor",
			/**
			 * ESTADO = 2 EM EDITACAO
			 * ESTADO = 1 EM ENVIO
			 * ESTADO = 0 ENVIADO
			 */
			FISCA_ESTADO="fisca_estado",
			FISCA_ID="fisca_id",
			FISCA_DESC_CATEGORIA_VEICULO="fisca_descricao_veiculo",
			FISCA_DESC_ESTADO_CONDUTOR="fisca_desc_estado_condutor",
			FISCA_DATA_REGISTRO="fisca_data_registro",
			FISCA_LATITUDE="fisca_latitude",
			FISCA_LONGITUDE="fisca_longitude",
			FISCA_LOCAL="fisca_local";
	
	
	//Esses atibutos sao das tivites
	
	
	public String T_SEQUENCIA ="t_sequencia",
			SEQUENCIA_CODIGO ="sequencia_codigo",
			SEQUENCIA_NUMERO="sequencia_numero";
	
	public String CAMPO_ID_ESTADO_CONDUTOR="ID";
	public String CAMPO_DESCRICAO_ESTADO_CONDUTOR="DESCRICAO";
	
	public String CAMPO_ID_CATEGORIA_VEICULO="ID";
	public String CAMPO_DESCRICAO_CATEGORIA_VEICULO="DESCRICAO";
	
	public String T_AGENTE ="t_agente",
			AGENTE_ID="agente_id",
			AGENTE_ID_LOGIN="agente_id_login",
			AGENTE_ID_ALOCACAO="agente_id_alocacao",
			AGENTE_TIPO_OPERACAO="agente_tipo_operacao",
			AGENTE_NIVEL_ATUACAO="agente_nivel_atuacao",
			AGENTE_NOME_ACESSO="agente_nome_acesso",
			AGENTE_NOME="agente_nome",
			AGENTE_APELIDO="agente_apelido",
			AGENTE_ESTADO="agente_estado",
			AGENTE_ID_TIPO_OPERACAO="agente_id_tipo_operacao",
			AGENTE_OPERACAO="agente_operacao",
			AGENTE_AUTENTICACAO="agente_autenticacao";

	public String T_INFRACAO_SELECIONADA ="t_infracao_selecionada",
			INFRACAO_SELECIONADA_ID="infracao_selecionada_id",
			INFRACAO_SELECIONADA_GRAVIDADE="infracao_selecionada_gravidade";
	
	public String T_REGISTO_FISCALIZACAO ="t_registo_fiscalizacao",
			ID_FISCA ="fisca_id",
			FISCALIZACAO_ID="fiscalizacao_id",
			FISCALIZACAO_INFRACAO_ID ="fiscalizacao_infracao_id",
			FISCALIZACAO_GRAVIDADE ="fiscalizacao_gravidade",
			/**
			 * Estado de registro
			 * 1- A espera de ser enviado para o servidor
			 * 2- Inválido
			 */
			FISCALIZACAO_REGISTRO_ESTADO="fiscalizacao_registro_estado";
			
	public String ID_FISCALIZACAO ="FISCALIZACAO_ID";
	

}
