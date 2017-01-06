package policia.transito.model;

import java.io.Serializable;

import policia.transito.data.BaseFiscalizacao;

import android.app.Activity;
import android.content.Context;

public class Agente implements Serializable
{
	private static final long serialVersionUID = 1L;
    private String nome;
    private String apelido;
    private String senha;
    private String nivelAtuacao;
    private String estado;
    private String codigoAcesso;
	private int idAlocacao;
	private int idLogin;
	private String tipoOperacao;
	private int id;
	private int idTipoOperacao;
	private String operacao;
	private int autenticacao;
	
	
	public Agente()
	{		
	}
	
	
	public String getOperacao() {
		return operacao;
	}


	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}


	public int getId()
	{
		return this.id;
	}
	
	
	
	public int getAutenticacao() {
		return autenticacao;
	}


	public void setAutenticacao(int autenticacao) {
		this.autenticacao = autenticacao;
	}


	public String getTipoOperacao() {
		return tipoOperacao;
	}
	public void setTipoOperacao(String tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}
	public int getIdAlocacao() {
		return idAlocacao;
	}
	public void setIdAlocacao(int idAlocacao) {
		this.idAlocacao = idAlocacao;
	}
	
	
	public int getIdTipoOperacao() {
		return idTipoOperacao;
	}

	public void setIdTipoOperacao(int idTipoOperacao) {
		this.idTipoOperacao = idTipoOperacao;
	}

	public int getIdLogin() {
		return idLogin;
	}
	public void setIdLogin(int idLogin) {
		this.idLogin = idLogin;
	}
	public int getidAlocacao()
	{
		return this.idAlocacao;
	}
	

	public static Agente getUserLogado(Context context) 
	{
		 BaseFiscalizacao baseFiscalizacao = new BaseFiscalizacao(context);
		return baseFiscalizacao.getAgente();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNivelAtuacao() {
		return nivelAtuacao;
	}

	public void setNivelAtuacao(String nivelAtuacao) {
		this.nivelAtuacao = nivelAtuacao;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCodigoAcesso() {
		return codigoAcesso;
	}

	public void setCodigoAcesso(String codigoAcesso) {
		this.codigoAcesso = codigoAcesso;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public static String getSender(Activity activity,Context context)
	{
		Device device = new Device(activity);
		return device.getMac()+"@"+getUserLogado(context).getCodigoAcesso();
	}

		
}
