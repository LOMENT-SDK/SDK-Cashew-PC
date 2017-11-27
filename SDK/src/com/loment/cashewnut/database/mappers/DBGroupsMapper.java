package com.loment.cashewnut.database.mappers;

import java.util.ArrayList;

import org.json.JSONArray;
import com.loment.cashewnut.database.helper.DBConnection;
import com.loment.cashewnut.database.helper.DatabaseHandler;
import com.loment.cashewnut.model.GroupModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.JSONException;

public class DBGroupsMapper {

    private static DBGroupsMapper instance;
    private Connection connection;

    private DBGroupsMapper() throws ClassNotFoundException {
        try {
            connection = DBConnection.getInstance().getConnection(
                    DatabaseHandler.DATABASE_NAME);
            createTableIfNotExists();
        } catch (Exception ex) {
        }
    }

    public static DBGroupsMapper getInstance() {
        if (instance == null) {
            try {
                instance = new DBGroupsMapper();
            } catch (Exception ex) {
            }
        }
        return instance;
    }

    private void createTableIfNotExists() {
        PreparedStatement pst;
        try {
            pst = connection
                    .prepareStatement(DatabaseHandler.CREATE_TABLE_GROUPS);
            pst.execute();
        } catch (SQLException e) {
        }
    }

    public long insert(GroupModel groupModel) {

        try {
            JSONArray groupMembers = new JSONArray();
            if (groupModel.getMembers() != null) {
                groupMembers = groupModel.getMembers();
            }

            createTableIfNotExists();
            PreparedStatement insertStmt = connection.prepareStatement(
                    DatabaseHandler.INSERT_GROUP_DATA_QUERY,
                    Statement.RETURN_GENERATED_KEYS);

            insertStmt.setString(2, groupModel.getServerGroupId());
            insertStmt.setString(3, groupModel.getGroupName());
            insertStmt.setString(4, groupModel.getOwner());
            insertStmt.setInt(5, 0);
            insertStmt.setString(6, groupMembers.toString());
            insertStmt.executeUpdate();
            //System.out.println("Data Inserted successfully into Message Table");

            ResultSet rs = insertStmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
        } finally {
            DBConnection.getInstance().closeLock();
        }
        return -1;

    }

    public GroupModel getGroup(String groupID, boolean isServerId) {
        GroupModel groupModel = new GroupModel();
        ResultSet cursor = null;
        try {
            String selectQuery = "SELECT  * FROM  "
                    + DatabaseHandler.TABLE_GROUPS + " where ";

            if (isServerId) {
                selectQuery += DatabaseHandler.KEY_SERVER_GROUP_ID + "='" + groupID + "'";
            } else {
                selectQuery += DatabaseHandler.KEY_GROUP_ID + "='" + groupID + "'";
            }

            cursor = connection.createStatement().executeQuery(selectQuery);
            if (cursor.next()) {
                do {
                    groupModel.setGroupId(cursor.getInt(1));
                    groupModel.setServerGroupId(cursor.getString(2));
                    groupModel.setGroupName(cursor.getString(3));
                    groupModel.setOwner(cursor.getString(4));
                    JSONArray members = new JSONArray(cursor.getString(6));
                    groupModel.setMembers(members);

                } while (cursor.next());
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (SQLException ex) {
                }
            }
        }
        return groupModel;
    }

    public ArrayList<GroupModel> getGroupsList() {
        String MakeSelect = "select * from  "
                + DatabaseHandler.TABLE_GROUPS + "";
        ArrayList<GroupModel> groupList = new ArrayList<GroupModel>();

        ResultSet cursor = null;
        try {
            cursor = connection.createStatement().executeQuery(MakeSelect);
            if (cursor.next()) {
                do {
                    GroupModel groupModel = new GroupModel();
                    groupModel.setGroupId(cursor.getInt(1));
                    groupModel.setServerGroupId(cursor.getString(2));
                    groupModel.setGroupName(cursor.getString(3));
                    groupModel.setOwner(cursor.getString(4));

                    JSONArray members = new JSONArray(cursor.getString(6));
                    groupModel.setMembers(members);
                    groupList.add(groupModel);

                } while (cursor.next());
            }
            cursor.close();
        } catch (JSONException e) {
        }catch( Exception e){
  
        }
        return groupList;
    }

    public int updateGroup(GroupModel groupModel, boolean isServerId) {

        int status = 0;
        String updateQuery = "";

        if (isServerId) {
            updateQuery = "UPDATE  " + DatabaseHandler.TABLE_GROUPS
                    + "  SET  " + DatabaseHandler.KEY_SERVER_GROUP_ID + " = "
                    + "?," + DatabaseHandler.KEY_GROUP_NAME + " = " + "?,"
                    + DatabaseHandler.KEY_GROUP_CREATED_BY + " = " + "?,"
                    + DatabaseHandler.KEY_GROUP_MEMBERS + " = " + "?"
                    + "  WHERE " + DatabaseHandler.KEY_SERVER_GROUP_ID + " =  '" 
                    + groupModel.getServerGroupId() + "'";
                    
        } else {
            updateQuery = "UPDATE  " + DatabaseHandler.TABLE_GROUPS
                    + "  SET  " + DatabaseHandler.KEY_SERVER_GROUP_ID + " = "
                    + "?," + DatabaseHandler.KEY_GROUP_NAME + " = " + "?,"
                    + DatabaseHandler.KEY_GROUP_CREATED_BY + " = " + "?,"
                    + DatabaseHandler.KEY_GROUP_MEMBERS + " = " + "?"
                    + "  WHERE " + DatabaseHandler.KEY_GROUP_ID + " =  '" + groupModel.getGroupId()
                    + "'";
        }

        try {

            PreparedStatement insertStmt = connection
                    .prepareStatement(updateQuery);

                String members = groupModel.getMembers().toString();
                insertStmt.setString(1, groupModel.getServerGroupId());
                insertStmt.setString(2, groupModel.getGroupName());
                insertStmt.setString(3, groupModel.getOwner());
                insertStmt.setString(4, members);

            if (insertStmt.execute()) {
                status = 1;
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return status;
    }

    public int deleteAll() {
        return 0;
        //return wdb.delete(DatabaseHandler.TABLE_GROUPS, null, null);
    }
}
