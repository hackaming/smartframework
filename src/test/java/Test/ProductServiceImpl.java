package Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductServiceImpl implements ProductService{
	private static final String UPDATE_PRODUCT_SQL = "update product set price = ? where id = ?";
	private static final String INSERT_LOG_SQL = "insert into log(created,description) values (?,?)";
	
	@Override
	public void updateProductPrice(long productId, int price) throws Throwable {
		try{
			Connection conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			updateProduct(conn,UPDATE_PRODUCT_SQL,productId,price);
			insertLog(conn,	INSERT_LOG_SQL,"Create product.");
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection();
		}
	}
	
	private void updateProduct(Connection conn,String updateProductSql, long productId,int productPrice) throws Throwable{
		PreparedStatement pstmt = conn.prepareStatement(updateProductSql);
		pstmt.setLong(1, productId);
		pstmt.setInt(2, productPrice);
		int rows =  pstmt.executeUpdate();
		if (0 != rows){
			System.out.println("Update product success!");
		}
	}
	
	private void insertLog(Connection conn, String insertLogSql,String logDescription)throws Exception{
		PreparedStatement pstmt = conn.prepareStatement(insertLogSql);
		pstmt.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date()));
		pstmt.setString(2, logDescription);
		int rows = pstmt.executeUpdate();
		if (0 != rows){
			System.out.println("Insert log success!");
		}
	}
	
	public static void main(String[] argv) throws Throwable{
		ProductService ProductService = new ProductServiceImpl();
		ProductService.updateProductPrice(1, 3000);
	}
}
