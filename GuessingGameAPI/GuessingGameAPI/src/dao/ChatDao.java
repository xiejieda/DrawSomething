package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.ChatBean;

import util.DBUtils;

public class ChatDao {
	//���������Ϣ
	public Integer setChat(int table_id,String name, String time,String data) {
		Connection conn = DBUtils.getConnection();
		try {
			PreparedStatement preparedStatement = conn.prepareStatement("insert into chat_info(table_id,name,time,data) values(?,?,?,?)");
			preparedStatement.setInt(1, table_id);
			preparedStatement.setString(2, name);
			preparedStatement.setString(3, time);
			preparedStatement.setString(4, data);
			// ִ��SQL��ѯ���
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(conn);
		}
		return null;
	}
	
	// ����������Ϣ
	public ArrayList<ChatBean> getChat_Info(int table_id) {
		ArrayList<ChatBean> allinfo = new ArrayList<ChatBean>();
		Connection conn = DBUtils.getConnection();
		try {
			PreparedStatement statement = conn.prepareStatement("select * from chat_info where table_id=?");
			statement.setInt(1,table_id);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				ChatBean chatBean = new ChatBean();
				chatBean.setName(resultSet.getString("name"));
				chatBean.setTime(resultSet.getString("time"));
				chatBean.setData(resultSet.getString("data"));
				allinfo.add(chatBean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(conn);
		}
		return allinfo;
	}
	
	//ɾ������������Ϣ
	public Integer deleteChat(int table_id) {
		Connection conn = DBUtils.getConnection();
		try {
			PreparedStatement preparedStatement = conn.prepareStatement("delete from chat_info where table_id=?");
			preparedStatement.setInt(1, table_id);
			// ִ��SQL��ѯ���
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtils.closeQuietly(conn);
		}
		return null;
	}
}
