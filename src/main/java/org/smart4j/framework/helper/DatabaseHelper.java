package org.smart4j.framework.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.PropsUtil;

public final class DatabaseHelper {
	private static final Logger logger = LoggerFactory.getLogger(DatabaseHelper.class);
	private static final QueryRunner QUERY_RUNNER;
	private static final ThreadLocal<Connection> CONNECTION_HOLDER;
	private static final BasicDataSource Data_Source;
	private static Connection conn;
	static {
		logger.info("DBHelper initialized!");
		CONNECTION_HOLDER=new ThreadLocal<Connection>();
		QUERY_RUNNER = new QueryRunner();
		
		Properties prop = PropsUtil.loadProps("DB.properties");
		String DRIVER = prop.getProperty("jdbc.driver");
		String URL = prop.getProperty("jdbc.url");
		String USER = prop.getProperty("jdbc.user");
		String PASSWORD = prop.getProperty("jdbc.password");
		Data_Source= new BasicDataSource();
		Data_Source.setDriverClassName(DRIVER);
		Data_Source.setUrl(URL);
		Data_Source.setUsername(USER);
		Data_Source.setPassword(PASSWORD);
				
		/*try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			logger.error("JDBC Driver initializing failed");
		}*/
	}

	public static Connection getConnection() {
		Connection conn = CONNECTION_HOLDER.get();
		if (null == conn){
			try {
				conn = Data_Source.getConnection();
			} catch (SQLException e) {
				logger.error("Can't get connection " );
				throw new RuntimeException();
			}finally{
				CONNECTION_HOLDER.set(conn);				
			}
		}
		return conn;
	}

	public static void closeConnection() {
		//no need to close connection any more as we use the DBCP connection Pool
/*		conn = CONNECTION_HOLDER.get();
		if (null != conn) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("Can't close the connection");
				throw new RuntimeException();
			} finally{
				CONNECTION_HOLDER.remove();
			}
		}*/
	}
	public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
		List<T> entityList = null;
		try {
			conn = getConnection();
			entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
		} catch (SQLException e) {
			logger.error("SQL query error!");
			throw new RuntimeException();
		} finally {
			closeConnection();
		}
		return entityList;
	}
	public static <T> T queryEntity(Class<T> entityClass,String sql,Object...params){
		T entity;
		try{
			conn = getConnection();
			entity = QUERY_RUNNER.query(conn, sql,new BeanHandler<T>(entityClass),params);
		} catch(SQLException e){
			logger.error("Can't finish the query");
			throw new RuntimeException();
		}finally{
			closeConnection();
		}
		return entity;
	}
	public static List<Map<String,Object>> executeQuery(String sql,Object ... params){
		List<Map<String,Object>> result;
		try{
			conn = getConnection();
			result = QUERY_RUNNER.query(sql, new MapListHandler(), params);
		} catch (SQLException e){
			logger.error("execute query error");
			throw new RuntimeException();
		}finally{
			closeConnection();
		}
		return result;
	}
	public static int executeUpdate(String sql,Object...params){
		int rows=0;
		try {
			Connection conn = getConnection();
			rows = QUERY_RUNNER.update(sql,conn,params);
		}catch (SQLException e){
			logger.error("Error when executing update");
			throw new RuntimeException();
		} finally{
			closeConnection();
		}
		return rows;
	}
	public static <T> boolean insertEntity(Class<T>entityClass,Map<String,Object> fieldMap){
		if (CollectionUtil.isEmpty(fieldMap)){
			logger.error("Can't not insert entity:FieldMap is null");
		}
		String sql = "insert into" + getTableName(entityClass);
		StringBuilder columns = new StringBuilder("(");
		StringBuilder values = new StringBuilder(")");
		for (String fieldName:fieldMap.keySet()){
			columns.append(fieldName).append(",");
			values.append("?,");
		}
		columns.replace(columns.lastIndexOf(","), columns.length(), ")");
		values.replace(values.lastIndexOf(","), values.length(), ")");
		sql += columns+"values "+values;
		Object[] params = fieldMap.values().toArray();
		return executeUpdate(sql,params)==1;
	}
	public static <T> String getTableName(Class<T>entityClass){
		return entityClass.getSimpleName();
	}
	public static <T> boolean updateEntity(Class<T> entityClass,long id,Map<String,Object>fieldMap){
		if (CollectionUtil.isEmpty(fieldMap)){
			logger.error("Can't update entity:field is empty");
			return false;
		}
		String sql = "update "+ getTableName(entityClass) + "SET ";
		StringBuilder columns = new StringBuilder();
		for (String fieldName:fieldMap.keySet()){
			columns.append(fieldName).append("=?, ");
		}
		sql += columns.substring(0,columns.lastIndexOf(", "))+" where id =?";
		List<Object> paramList=new ArrayList<Object>();
		paramList.addAll(fieldMap.values());
		paramList.add(id);
		Object[] params = paramList.toArray();
		return executeUpdate(sql,params)==1;		
	}
	public static <T> boolean deleteEntity(Class<T> entityClass,long id){
		String sql = "delete from "+getTableName(entityClass)+" where id=?";
		return executeUpdate(sql,id)==1;
	}
	
	public static void beginTransaction(){
		Connection conn = getConnection();
		if (null != conn){
			try {
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				logger.error("begin transaction failure", e);
				throw new RuntimeException(e);
			} finally {
				CONNECTION_HOLDER.set(conn);
			}
		}
	}
	public static void commitTransaction(){
		Connection conn = getConnection();
		if (null != conn){
			try{
				conn.commit();
				conn.close();
			} catch (SQLException e){
				logger.error("Commit transaction failure",e);
				throw new RuntimeException(e);
			} finally {
				CONNECTION_HOLDER.remove();
			}
		}
	}
	
	public static void rollbackTransaction(){
		Connection conn = getConnection();
		if (null != conn){
			try{
				conn.rollback();
				conn.close();
			} catch (SQLException e) {
				logger.error("rollback transaction failure",e);
				throw new RuntimeException(e);
			} finally {
				CONNECTION_HOLDER.remove();
			}
		}
	}
}
