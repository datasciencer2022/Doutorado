package modelo;

import java.util.HashSet;
import java.util.Set;

public class Arquivo {
	private String nome;
	private Set<String> palavrasLinha = new HashSet<String>();
	private String linhaToda = "";
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Set<String> getPalavrasLinha() {
		return palavrasLinha;
	}
	public void setPalavrasLinha(Set<String> palavrasLinha) {
		this.palavrasLinha = palavrasLinha;
	}
	public String getLinhaToda() {
		return linhaToda;
	}
	public void setLinhaToda(String linhaToda) {
		this.linhaToda = linhaToda;
	}
	
	
}