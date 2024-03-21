package dao.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import dao.FilmeDAO;
import entidades.Filme;

public class FilmeDAOImpl implements FilmeDAO {
	
	@Override
	public void insert(Connection conn, Filme filme) throws Exception {
		
		PreparedStatement myStmt = conn.prepareStatement("insert into en_filme (id_filme, data_lancamento, nome, descricao) values (?, ?, ?, ?)");
		
		Integer idFilme = this.getNextId(conn);
		Date sqlDate = new Date(filme.getDataLancamento().getTime());
		String nome = filme.getNome();
		String descricao = filme.getDescricao();
		
        myStmt.setInt(1, idFilme);
        myStmt.setDate(2, sqlDate);
        myStmt.setString(3, nome);
        myStmt.setString(4, descricao);

        myStmt.execute();
        conn.commit();

        filme.setIdFilme(idFilme);
	}

	@Override
	public Integer getNextId(Connection conn) throws Exception {
		
        PreparedStatement myStmt = conn.prepareStatement("select nextval('seq_en_filme')");
        
        ResultSet rs = myStmt.executeQuery();
        rs.next();
        
        return rs.getInt(1);
	}
	
	@Override
	public void edit(Connection conn, Filme filme) throws Exception {
		
		FilmeDAOUtil filmeDAOUtil = new FilmeDAOUtil();
		
		filmeDAOUtil.edit(conn, filme);
	}
	
	@Override
	public void delete(Connection conn, Integer idFilme) throws Exception {

		PreparedStatement myStmtRelAluguel = conn.prepareStatement("delete from re_aluguel_filme where id_filme = (?)");
		
		myStmtRelAluguel.setInt(1, idFilme);
		
		myStmtRelAluguel.execute();
		conn.commit();
		
		PreparedStatement myStmtFilme = conn.prepareStatement("delete from en_filme where id_filme = (?)");
		
		myStmtFilme.setInt(1, idFilme);
		
        myStmtFilme.execute();
        conn.commit();
	}
	
	@Override
	public Filme find(Connection conn, Integer idFilme) throws Exception {
		
		PreparedStatement myStmt = conn.prepareStatement("select * from en_filme where id_filme = (?)");
		
		myStmt.setInt(1, idFilme);
		
        ResultSet myRs = myStmt.executeQuery();

        if (!myRs.next()) {
            return null;
        }

        String nome = myRs.getString("nome");
        Date dataLancamento = myRs.getDate("data_lancamento");
        String descricao = myRs.getString("descricao");
        
        return new Filme(idFilme, dataLancamento, nome, descricao);
	}

	@Override
	public Collection<Filme> list(Connection conn) throws Exception {
		
        PreparedStatement myStmt = conn.prepareStatement("select * from en_filme order by nome");
        
        ResultSet myRs = myStmt.executeQuery();

        Collection<Filme> items = new ArrayList<>();

        while (myRs.next()) {
            Integer idFilme = myRs.getInt("id_filme");
            Date dataLancamento = myRs.getDate("data_lancamento");
            String nome = myRs.getString("nome");
            String descricao = myRs.getString("descricao");

            items.add(new Filme(idFilme, dataLancamento, nome, descricao));
        }

        return items;
	}
}
