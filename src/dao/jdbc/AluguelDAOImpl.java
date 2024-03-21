package dao.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dao.AluguelDAO;
import dao.ClienteDAO;
import dao.FilmeDAO;
import entidades.Aluguel;
import entidades.Cliente;
import entidades.Filme;

public class AluguelDAOImpl implements AluguelDAO {

	@Override
	public void insert(Connection conn, Aluguel aluguel) throws Exception {
		
		PreparedStatement myStmt = conn.prepareStatement("insert into en_aluguel (id_aluguel, id_cliente, data_aluguel, valor) values (?, ?, ?, ?)");
		
		Integer idAluguel = this.getNextId(conn);
		
		myStmt.setInt(1, idAluguel);
		myStmt.setInt(2, aluguel.getCliente().getIdCliente());
		myStmt.setDate(3, new Date(new java.util.Date().getTime()));
		myStmt.setFloat(4, aluguel.getValor());
		
		myStmt.execute();
		conn.commit();
		
		PreparedStatement myStmtFilmes = conn.prepareStatement("insert into re_aluguel_filme (id_filme, id_aluguel) values (?, ?)");
	
		myStmtFilmes.setInt(2, idAluguel);
		
		aluguel.getFilmes().forEach(item -> {
			try {
				myStmtFilmes.setInt(1, item.getIdFilme());
				
				myStmtFilmes.execute();
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		
		aluguel.setIdAluguel(idAluguel);
	}

	@Override
	public Integer getNextId(Connection conn) throws Exception {
		
        PreparedStatement myStmt = conn.prepareStatement("select nextval('seq_en_aluguel')");
        
        ResultSet rs = myStmt.executeQuery();
        rs.next();
        
        return rs.getInt(1);
	}

	@Override
	public void edit(Connection conn, Aluguel aluguel) throws Exception {
		
	    AluguelDAOUtil aluguelDAOUtil = new AluguelDAOUtil();
	    
		aluguelDAOUtil.edit(conn, aluguel);
	}

	@Override
	public void delete(Connection conn, Aluguel aluguel) throws Exception {
		PreparedStatement myStmtRelFilmes = conn.prepareStatement("delete from re_aluguel_filme where id_aluguel=(?)");
		
		myStmtRelFilmes.setInt(1, aluguel.getIdAluguel());
		
		myStmtRelFilmes.execute();
		conn.commit();
		
		PreparedStatement myStmtAluguel = conn.prepareStatement("delete from en_aluguel where id_aluguel=(?)");
		
		myStmtAluguel.setInt(1, aluguel.getIdAluguel());
		
		myStmtAluguel.execute();
		conn.commit();
	}

	@Override
	public Aluguel find(Connection conn, Integer idAluguel) throws Exception {
		
		PreparedStatement myStmt = conn.prepareStatement("select * from en_aluguel where id_aluguel = (?)");
		
		myStmt.setInt(1, idAluguel);
		
        ResultSet myRs = myStmt.executeQuery();

        if (!myRs.next()) {
            return null;
        }
        
        List<Filme> filmes = listFilmes(conn, idAluguel);
        Cliente cliente = getCliente(conn, myRs.getInt("id_cliente"));
        Date dataAluguel = myRs.getDate("data_aluguel");
        float valor = myRs.getFloat("valor");
        
        return new Aluguel(idAluguel, filmes, cliente, dataAluguel, valor);
	}

	@Override
	public Collection<Aluguel> list(Connection conn) throws Exception {
		
		PreparedStatement myStmt = conn.prepareStatement("select * from en_aluguel order by id_aluguel");
		
        ResultSet myRs = myStmt.executeQuery();

        Collection<Aluguel> items = new ArrayList<>();

        while (myRs.next()) {
            Integer idAluguel = myRs.getInt("id_aluguel");
          	List<Filme> filmes = listFilmes(conn, idAluguel);
          	Cliente cliente = getCliente(conn, myRs.getInt("id_cliente"));
            Date dataAluguel = myRs.getDate("data_aluguel");
            float valor = myRs.getFloat("valor");

            items.add(new Aluguel(idAluguel, filmes, cliente, dataAluguel, valor));
        }

        return items;
	}
	
	private List<Filme> listFilmes(Connection conn, Integer idAluguel) throws Exception {
		
		PreparedStatement myStmtFilmes = conn.prepareStatement("select * from re_aluguel_filme where id_aluguel = (?)");
		
        myStmtFilmes.setInt(1, idAluguel);
         
       	ResultSet myRsFilmes = myStmtFilmes.executeQuery();
       	
       	List<Filme> filmes = new ArrayList<>();
       	FilmeDAO filmeDAO = new FilmeDAOImpl();
       	
       	while (myRsFilmes.next()) {
       		Integer idFilme = myRsFilmes.getInt("id_filme");

            try {
            	Filme searchedFilme = filmeDAO.find(conn, idFilme);
 				filmes.add(new Filme(idFilme, searchedFilme.getDataLancamento(), searchedFilme.getNome(), searchedFilme.getDescricao()));
 			} catch (Exception e) {
 				e.printStackTrace();
 			}
       	}
       	
       	return filmes;
	};
	
	private Cliente getCliente(Connection conn, Integer idCliente) throws Exception {
		
		ClienteDAO clienteDAO = new ClienteDAOImpl();
		
      	Cliente searchedCliente = clienteDAO.find(conn, idCliente);
      	
      	return new Cliente(idCliente, searchedCliente.getNome());
	};
	
}
