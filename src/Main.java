import dao.ClienteDAO;
import dao.jdbc.ClienteDAOImpl;
import entidades.Cliente;

import dao.FilmeDAO;
import dao.jdbc.FilmeDAOImpl;
import dao.jdbc.FilmeDAOUtil;
import entidades.Filme;

import dao.AluguelDAO;
import dao.jdbc.AluguelDAOImpl;
import dao.jdbc.AluguelDAOUtil;
import entidades.Aluguel;


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Main {

    public static void main(String[] args) {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testeestagio", "postgres", "D@tabas3!");
            System.out.println("Conexão realizada com sucesso!!!");
            conn.setAutoCommit(false);
            
            // Demonstrar o funcionamento aqui
            
            // CLIENTES
            ClienteDAO clienteDAO = new ClienteDAOImpl();
            
            // insert
            Cliente cliente1 = new entidades.Cliente(null, "Joana");
            // clienteDAO.insert(conn, cliente1);
            
            // edit/update
            Cliente clienteEdit = new entidades.Cliente(1, "Ana Maria");
            // clienteDAO.edit(conn, clienteEdit);
            
            // delete 
            // clienteDAO.delete(conn, 2);
            
            // list 
            Collection<Cliente> clientes = clienteDAO.list(conn);
            // clientes.forEach(item -> System.out.println(item.getNome()));
            
            // find
            Cliente searchedCliente = clienteDAO.find(conn, 1);
            // System.out.println("Cliente: " + searchedCliente.getNome());
            
            
            // FILMES
            FilmeDAO filmeDAO = new FilmeDAOImpl();
            FilmeDAOUtil filmeDAOUtil = new FilmeDAOUtil();
            
            // insert
            Filme meuFilme = new entidades.Filme(null, Date.valueOf("2001-03-20"), "Harry Potter", "E a pedra filosofal");
            // filmeDAO.insert(conn, meuFilme);
            
            // edit/update
            	// editando todos os campos
            Filme editFilme1 = new Filme(1, Date.valueOf("2000-08-28"), "X-Men", "O Homem de ferro");
            // filmeDAOUtil.edit(conn, editFilme1);
            
            	// editando apenas um campo
            Filme editFilme2 = new Filme();
            editFilme2.setIdFilme(2);
            // filmeDAOUtil.edit(conn, editFilme2, Date.valueOf("2023-05-01"));
              
            // delete
            // filmeDAO.delete(conn, 5);
            
            // list
            Collection<Filme> filmes = filmeDAO.list(conn);
            // filmes.forEach(item -> System.out.println(item.toString()));
            
            // find
            Filme searchedFilme = filmeDAO.find(conn, 2);
            // System.out.println(searchedFilme.toString());
            
            
            // ALUGUEIS
            AluguelDAO aluguelDAO = new AluguelDAOImpl();
            AluguelDAOUtil aluguelDAOUtil = new AluguelDAOUtil();
            
            // insert
            List<Filme> listaFilmesAluguel = new ArrayList<>();
            listaFilmesAluguel.add(new Filme().setIdFilme(1));
            listaFilmesAluguel.add(new Filme().setIdFilme(3));
            Cliente clienteAluguel = new Cliente().setIdCliente(3);
            
            Aluguel meuAluguel = new entidades.Aluguel(null, listaFilmesAluguel, clienteAluguel, null, 15);
            // aluguelDAO.insert(conn, meuAluguel);
            
            // edit/update
            	// editando todos os campos
            Aluguel editAluguel1 = new Aluguel(2, listaFilmesAluguel, clienteAluguel, Date.valueOf("2020-03-01"), 9);
            // aluguelDAOUtil.edit(conn, editAluguel1);
            
            	// editando somente um campo
            Aluguel editAluguel2 = new entidades.Aluguel();
            editAluguel2.setIdAluguel(3);
            // aluguelDAOUtil.edit(conn, editAluguel2, Date.valueOf("2000-12-31"));
            
            // delete
            Aluguel apagarAluguel = new entidades.Aluguel().setIdAluguel(5);
            // aluguelDAO.delete(conn, apagarAluguel);
            
            // list
            Collection<Aluguel> alugueis = aluguelDAO.list(conn);
            // alugueis.forEach(item -> System.out.println(item.toString()));
            
            // find
            Aluguel searchedAluguel = aluguelDAO.find(conn, 2);
            // System.out.println(searchedAluguel.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Fim do teste.");
    }
}
