package dao.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import entidades.Filme;

public class FilmeDAOUtil {

	public void edit(Connection conn, Filme filme) throws Exception {
		
		PreparedStatement myStmt = conn.prepareStatement("update en_filme set data_lancamento = (?), nome = (?), descricao = (?) where id_filme = (?)");

		Integer idFilme = filme.getIdFilme();
		Date dataLancamento = new Date(filme.getDataLancamento().getTime());
		String nome = filme.getNome();
		String descricao = filme.getDescricao();
		
		myStmt.setDate(1, dataLancamento);
		myStmt.setString(2, nome);
		myStmt.setString(3, descricao);
		myStmt.setInt(4, idFilme);
		
		myStmt.execute();
		conn.commit();
	}
	
	public void edit(Connection conn, Filme filme, Date novaDataLancamento) throws Exception {

		PreparedStatement myStmt = conn.prepareStatement("update en_filme set data_lancamento = (?) where id_filme = (?)");
		
		Integer idFilme = filme.getIdFilme();
		Date dataLancamento = new Date(novaDataLancamento.getTime());

		myStmt.setDate(1, dataLancamento);
		myStmt.setInt(2, idFilme);
		
		myStmt.execute();
		conn.commit();
	}
		
	public void edit(Connection conn, Filme filme, String campoAtualizar, String novaString) throws SQLException	{
		
		Integer idFilme = filme.getIdFilme();
		
		if (campoAtualizar == "nome") {
			
	        PreparedStatement myStmt = conn.prepareStatement("update en_filme set nome = (?) where id_filme = (?)");
	    
	        myStmt.setString(1, novaString);
	        myStmt.setInt(2, idFilme);
		
	        myStmt.execute();
	        conn.commit(); 
	        
		} else if (campoAtualizar == "descricao") {
			
		    PreparedStatement myStmt = conn.prepareStatement("update en_filme set descricao = (?) where id_filme = (?)");
		    
	        myStmt.setString(1, novaString);
	        myStmt.setInt(2, idFilme);
		
	        myStmt.execute();
	        conn.commit(); 
	        
		} else {
	        throw new IllegalArgumentException("Campo inválido (deve ser 'nome' ou 'descricao'): " + campoAtualizar);
	    }
	}	
}
