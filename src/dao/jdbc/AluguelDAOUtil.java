package dao.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import entidades.Aluguel;
import entidades.Cliente;
import entidades.Filme;

public class AluguelDAOUtil {
	
	public void edit(Connection conn, Aluguel aluguel) throws Exception {
		
		PreparedStatement myStmt = conn.prepareStatement("update en_aluguel set id_cliente = (?), data_aluguel = (?), valor = (?) where id_aluguel = (?)");
		
		Integer idAluguel = aluguel.getIdAluguel();
		List<Filme> filmes = aluguel.getFilmes();
		Cliente cliente = aluguel.getCliente();
		Date dataAluguel = new Date(aluguel.getDataAluguel().getTime());
		Float valor = aluguel.getValor();
			
		myStmt.setInt(1, cliente.getIdCliente());
		myStmt.setDate(2, dataAluguel);
		myStmt.setFloat(3, valor);
		myStmt.setInt(4, idAluguel);
			
		myStmt.execute();
		conn.commit();
		
		edit(conn, aluguel, filmes);			
	}

    public void edit(Connection conn, Aluguel aluguel, List<Filme> novosFilmes) throws Exception {

    	PreparedStatement myStmtDel = conn.prepareStatement("delete from re_aluguel_filme where id_aluguel = (?)");

    	Integer idAluguel = aluguel.getIdAluguel();
    	
		myStmtDel.setInt(1, idAluguel);
	
		myStmtDel.execute();
		conn.commit();
			
		novosFilmes.forEach(item -> {
			try {
				PreparedStatement myStmtAdd = conn.prepareStatement("insert into re_aluguel_filme (id_filme, id_aluguel) values (?, ?)");
				myStmtAdd.setInt(1, item.getIdFilme());
				myStmtAdd.setInt(2, idAluguel);
					
				myStmtAdd.execute();
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		});
    }
    
    public void edit(Connection conn, Aluguel aluguel, Cliente novoCliente) throws Exception {

    	PreparedStatement myStmt = conn.prepareStatement("update en_aluguel set id_cliente = (?) where id_aluguel = (?)");
    	
    	Integer idAluguel = aluguel.getIdAluguel();

		myStmt.setInt(1, novoCliente.getIdCliente());
		myStmt.setInt(2, idAluguel);
		
		myStmt.execute();
    	conn.commit();
    }
    
    public void edit(Connection conn, Aluguel aluguel, Date novaDataAluguel) throws Exception {
    	PreparedStatement myStmt = conn.prepareStatement("update en_aluguel set data_aluguel = (?) where id_aluguel = (?)");

    	Integer idAluguel = aluguel.getIdAluguel();
    	Date dataAluguel = new Date(novaDataAluguel.getTime());
    	
		myStmt.setDate(1, dataAluguel);
		myStmt.setInt(2, idAluguel);
		
		myStmt.execute();
    	conn.commit();
    }
    
    public void edit(Connection conn, Aluguel aluguel, float novoValor) throws Exception {
    	PreparedStatement myStmt = conn.prepareStatement("update en_aluguel set valor = (?) where id_aluguel = (?)");

    	Integer idAluguel = aluguel.getIdAluguel();

        myStmt.setFloat(1, novoValor);
        myStmt.setInt(2, idAluguel);
            
        myStmt.execute();   
    	conn.commit();
    }
}
