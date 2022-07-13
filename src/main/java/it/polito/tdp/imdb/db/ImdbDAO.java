package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Arco;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(Map<Integer, Actor> idMap){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				idMap.put(actor.getId(), actor);
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
		
		public List<String> popolaCmbGeneri(){
			String sql="SELECT DISTINCT mg.genre AS g "
					+ "FROM movies_genres mg "
					+ "ORDER BY g";
			
			List<String> result = new ArrayList<String>();
			Connection conn = DBConnect.getConnection();

			try {
				PreparedStatement st = conn.prepareStatement(sql);
				ResultSet res = st.executeQuery();
				while (res.next()) {

					result.add(res.getString("g"));
				}
				conn.close();
				return result;
				
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
		}
	}
	
	
		
		public List<Actor> getVertici(String genere, Map<Integer, Actor> idMap){
			String sql="SELECT DISTINCT r.actor_id AS id "
					+ "FROM movies_genres mg, roles r "
					+ "WHERE r.movie_id=mg.movie_id AND mg.genre=?";
			
			List<Actor> result = new ArrayList<Actor>();
			Connection conn = DBConnect.getConnection();

			try {
				PreparedStatement st = conn.prepareStatement(sql);
				st.setString(1, genere);
				ResultSet res = st.executeQuery();
				while (res.next()) {
					Integer id1= res.getInt("id");
					
					result.add(idMap.get(id1));
				}
				conn.close();
				return result;
				
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
		}
					
		}
	
		public List<Arco> getArco(String genere, Map<Integer, Actor> idMap){
			String sql="SELECT r1.actor_id AS id1, r2.actor_id AS id2, COUNT(DISTINCT(r1.movie_id)) AS peso "
					+ "FROM roles r1, roles r2 "
					+ "WHERE r1.movie_id=r2.movie_id AND r1.movie_id IN (SELECT mg.movie_id "
					+ "FROM movies_genres mg "
					+ "WHERE mg.genre=?) AND r1.actor_id>r2.actor_id "
					+ "GROUP BY r1.actor_id, r2.actor_id";
			
			List<Arco> result = new ArrayList<Arco>();
			Connection conn = DBConnect.getConnection();

			try {
				PreparedStatement st = conn.prepareStatement(sql);
				st.setString(1, genere);
				ResultSet res = st.executeQuery();
				while (res.next()) {
					Actor a1= idMap.get(res.getInt("id1"));
					Actor a2= idMap.get(res.getInt("id2"));
					result.add(new Arco(a1, a2, res.getInt("peso")));
				}
				conn.close();
				return result;
				
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
		}
		
		}
	
	
	
	
}
